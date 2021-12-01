package com.database;

public class Spare {

    public final String model;
    public final String name;
    public final int count;
    public final double price;
    public final double fullPrice;

    public Spare(String model, String name, int count, double price) {
        this.model = model;
        this.name = name;
        this.count = count;
        this.price = price;
        this.fullPrice = price + 0.2 * price;
    }
}
