package com.elen.dbmanager.manager;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class DatabaseManager {

    private static final DatabaseManager INSTANCE = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

    private final BehaviorSubject<List<List<String>>> dataSubject = BehaviorSubject.createDefault(List.of());

    public void loadFromFile(File file) {
        readData(file);
    }

    private void readData(File file) {
        try {
            Scanner sc = new Scanner(file).useDelimiter("\n");
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

    public Observable<List<List<String>>> getData() {
        return dataSubject;
    }

}
