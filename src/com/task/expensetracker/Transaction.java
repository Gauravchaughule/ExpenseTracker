package com.task.expensetracker;

import java.time.LocalDate;

public class Transaction {
    private LocalDate date;
    private String type;
    private String category;
    private double amount;

    public Transaction(LocalDate date, String type, String category, double amount) {
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
    }

    public LocalDate getDate() { return date; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return date + "," + type + "," + category + "," + amount;
    }
    

    public static Transaction fromString(String line) {
        String[] parts = line.split(",");
        return new Transaction(LocalDate.parse(parts[0]), parts[1], parts[2], Double.parseDouble(parts[3]));
    }
}
