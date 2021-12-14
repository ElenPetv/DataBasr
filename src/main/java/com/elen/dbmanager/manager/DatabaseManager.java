package com.elen.dbmanager.manager;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class DatabaseManager {

    private static final DatabaseManager INSTANCE = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

    private final Pattern valuePattern = Pattern.compile(
            "(?m)(?<=^|,)(?:\"{2}|(?:)|[^,\"\r\n]+|\"(?:\"{2}|[^\"]+)+\")(?=,|$)"
    );

    private final BehaviorSubject<List<List<String>>> dataSubject = BehaviorSubject.createDefault(List.of());
    private final BehaviorSubject<Optional<File>> fileSubject = BehaviorSubject.createDefault(Optional.empty());

    public void loadFromFile(File file) {
        fileSubject.onNext(Optional.of(file));
        readData();
    }

    public Observable<Optional<File>> dbFile() {
        return fileSubject;
    }

    private void readData() {
        File file = fileSubject.getValue()
                .orElseThrow(() -> new NullPointerException("Can't read before open or create new file"));
        try {
            String fileContent = Files.readString(file.toPath());
            List<List<String>> data = new ArrayList<>();
            int targetRowSize = -1;
            List<String> rowElements = new ArrayList<>();
            Matcher matcher = valuePattern.matcher(fileContent);
            while (matcher.find()) {
                rowElements.add(decodeValue(matcher.group()));
                if (fileContent.length() == matcher.end() || fileContent.charAt(matcher.end()) == '\n' || fileContent.charAt(matcher.end()) == '\r') {

                    int rowSize = rowElements.size();
                    if (targetRowSize != -1) {
                        if (rowSize > targetRowSize) {
                            throw new IllegalArgumentException("Строка '" + rowElements +
                                    "' содержит больше значений, чем количество столбцов!");
                        } else if (rowSize < targetRowSize) {
                            throw new IllegalArgumentException("Строка '" + rowElements +
                                    "' содержит меньше значений, чем количество столбцов!");
                        }
                    } else {
                        targetRowSize = rowSize;
                    }

                    data.add(new ArrayList<>(rowElements));
                    rowElements.clear();
                }
            }
            if (data.isEmpty()) {
                data.add(new ArrayList<>());
            }
            dataSubject.onNext(data);
        } catch (IOException e) {
            out.println("Ошибка чтения файла " + file.getAbsolutePath());
            e.printStackTrace();
        }

    }

    public Observable<List<List<String>>> getDataStream() {
        return dataSubject;
    }

    public void deleteDocument() {
        File file = fileSubject.getValue()
                .orElseThrow(() -> new NullPointerException("Cant delete before open or create new file"));
        file.delete();
        dataSubject.onNext(List.of());
        fileSubject.onNext(Optional.empty());
    }

    public String getDocumentPath() {
        File file = fileSubject.getValue().orElse(null);
        return file == null ? "None" : file.getAbsolutePath();
    }

    public void saveData(List<List<String>> data) throws IOException {
        File file = fileSubject.getValue()
                .orElseThrow(() -> new NullPointerException("Cant call save before open or create new file"));

        String stringData = data.stream()
                .map(rows -> rows.stream()
                        .map(this::encodeValue)
                        .reduce((s1, s2) -> s1 + "," + s2)
                        .orElse(""))
                .reduce((s, row) -> s + "\n" + row)
                .orElse("");
        FileWriter writer = new FileWriter(file, false);
        writer.write(stringData);
        writer.close();
        dataSubject.onNext(data);
    }

    public List<List<String>> getData() {
        return dataSubject.getValue();
    }

    private String encodeValue(String value) {
        String escaped = value.replaceAll("(?s)\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\"\"")) {
            return "\"" + escaped + "\"";
        } else {
            return escaped;
        }
    }

    private String decodeValue(String value) {
        final String unescaped = value.length() > 2 ? value.replaceAll("(?s)\"\"", "\"") : value;
        if (unescaped.length() > 1 && unescaped.startsWith("\"") && unescaped.endsWith("\"")) {
            return unescaped.substring(1, unescaped.length() - 1);
        } else {
            return unescaped;
        }
    }
}
