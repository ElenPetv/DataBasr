package com.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

import static java.lang.System.out;

public class DataManager {
    private static final String FILE_NAME = "input.txt";
    private static final String FIELD_SEPARATOR = "[\\n\\-]";
    private final DecimalFormat priceFormat = ((DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH));
    private final Map<String, Spare> spareStoreMap = new TreeMap<>();

    {
        priceFormat.applyPattern(".00");
    }

    public DataManager() {
        readData();

    }

    private void readData() {
        try {
            Scanner sc = new Scanner(new File(FILE_NAME)).useDelimiter(FIELD_SEPARATOR);
            while (sc.hasNext()) {

                String name = sc.next();
                String model = sc.next();
                int count = Integer.parseInt(sc.next());
                Number number = priceFormat.parse(sc.next());
                double price = number.doubleValue();
                Spare spare = new Spare(name, model, count, price);

                spareStoreMap.put(spare.name, spare);
            }
        } catch (FileNotFoundException e) {
            out.println("Файл " + FILE_NAME + " не найден!");
            e.printStackTrace();
        } catch (ParseException e) {
            out.println("Неправильный формат цены: " + e.getMessage());
        }
    }

    public Spare getSpare(String name) {
        return spareStoreMap.get(name);
    }

    public void putSpare(Spare spare) {
        try {
            FileWriter writer = new FileWriter(FILE_NAME, true);
            writer.write(String.format("\n%s-%s-%d-%s", spare.model, spare.name,
                    spare.count, priceFormat.format(spare.price)));
            writer.close();
            spareStoreMap.put(spare.name, spare);
        } catch (IOException e) {
            out.println("Файл " + FILE_NAME + " не найден!");
        }
    }


}

