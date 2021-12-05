package com.elen.dbmanager.manager;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.System.out;

public class DatabaseManager {

    private static final DatabaseManager INSTANCE = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

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
        File file = fileSubject.getValue().orElse(null);
        if (file == null) return;
        try {
            Scanner sc = new Scanner(file).useDelimiter("(\r)?\n");
            List<List<String>> data = new ArrayList<>();
            int targetRowSize = -1;
            while (sc.hasNext()) {
                String[] rowElements = sc.next().split(",");
                int rowSize = rowElements.length;
                if (targetRowSize != -1) {
                    if (rowSize > targetRowSize) {
                        throw new IllegalArgumentException("Строка '" + Arrays.toString(rowElements) +
                                "' содержит больше значений, чем количество столбцов!");
                    } else if (rowSize < targetRowSize) {
                        throw new IllegalArgumentException("Строка '" + Arrays.toString(rowElements) +
                                "' содержит меньше значений, чем количество столбцов!");
                    }
                } else {
                    targetRowSize = rowSize;
                }

                data.add(List.of(rowElements));
            }
            dataSubject.onNext(data);
        } catch (FileNotFoundException e) {
            out.println("Файл " + file.getAbsolutePath() + " не найден!");
            e.printStackTrace();
        }

    }

    public Observable<List<List<String>>> getDataStream() {
        return dataSubject;
    }

    public void deleteDocument() {
        File file = fileSubject.getValue().orElse(null);
        if (file != null) {
            file.delete();
            dataSubject.onNext(List.of());
            fileSubject.onNext(Optional.empty());
        }
    }

    public String getDocumentPath() {
        File file = fileSubject.getValue().orElse(null);
        return file == null ? "None" : file.getAbsolutePath();
    }

    public void saveData(List<List<String>> data) throws IOException {
        File file = fileSubject.getValue().orElse(null);
        String stringData = data.stream()
                .map(rows -> rows.stream().reduce((s1, s2) -> s1 + "," + s2).orElse(""))
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
}
