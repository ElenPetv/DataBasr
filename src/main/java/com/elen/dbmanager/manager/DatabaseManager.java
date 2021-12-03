package com.elen.dbmanager.manager;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class DatabaseManager {
    private static final Pattern ENTRIES_SEPARATOR_PATTERN = Pattern.compile(",");
    private final File file;
    private final BehaviorSubject<List<List<String>>> dataSubject = BehaviorSubject.createDefault(List.of());

    public DatabaseManager(File file) {
        this.file = file;
        readData();
    }

    private void readData() {
        try {
            Scanner sc = new Scanner(file).useDelimiter("\n");
            List<List<String>> data = new ArrayList<>();
            int targetRowSize = -1;
            while (sc.hasNext()) {
                int rowSize = 0;
                String row = sc.next();
                List<String> rowElements = new ArrayList<>();
                Matcher matcher = ENTRIES_SEPARATOR_PATTERN.matcher(row);
                while (matcher.find()) {
                    rowSize++;
                    if (targetRowSize != -1 && rowSize > targetRowSize) {
                        throw new IllegalArgumentException("Строка '" + row + "' содержит больше значений, чем количество столбцов!");
                    }
                    rowElements.add(matcher.group());
                }
                if (targetRowSize == -1) {
                    targetRowSize = rowSize;
                }
                if (rowSize < targetRowSize) {
                    throw new IllegalArgumentException("Строка '" + row + "' содержит меньше значений, чем количество столбцов!");
                }
                data.add(rowElements);
            }
            dataSubject.onNext(data);
        } catch (FileNotFoundException e) {
            out.println("Файл " + file.getAbsolutePath() + " не найден!");
            e.printStackTrace();
        }

    }

    Observable<List<List<String>>> getData() {
        return dataSubject;
    }

}
