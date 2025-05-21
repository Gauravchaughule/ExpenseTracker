package com.task.expensetracker;

import java.io.IOException;
import java.nio.file.*;
import java.time.YearMonth;
import java.util.*;

public class ExpenseTracker {
    private final List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction tx) {
        transactions.add(tx);
    }

//    public void loadFromFile(String filename) throws IOException {
//        List<String> lines = Files.readAllLines(Path.of(filename));
//        for (String line : lines) {
//            transactions.add(Transaction.fromString(line));
//        }
//    }

    public void saveToFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("Date,Type,Category,Amount");
        for (Transaction t : transactions) {
            lines.add(t.toString());
        }
        Files.write(Path.of(filename), lines);
    }

    public void printMonthlySummary(YearMonth month) {
        double income = 0;
        double expense = 0;
        Map<String, Double> incomeCategories = new HashMap<>();
        Map<String, Double> expenseCategories = new HashMap<>();

        for (Transaction transaction : transactions) {
            if (YearMonth.from(transaction.getDate()).equals(month)) {
                if (transaction.getType().equalsIgnoreCase("Income")) {
                    income += transaction.getAmount();
                    incomeCategories.merge(transaction.getCategory(), transaction.getAmount(), Double::sum);
                } else {
                    expense += transaction.getAmount();
                    expenseCategories.merge(transaction.getCategory(), transaction.getAmount(), Double::sum);
                }
            }
        }

        System.out.println("\nSummary for " + month);
        System.out.println("Total Income: " + income);
        incomeCategories.forEach((k, v) -> System.out.println("  " + k + ": " + v));
        System.out.println("Total Expense: " + expense);
        expenseCategories.forEach((k, v) -> System.out.println("  " + k + ": " + v));
        System.out.println("Net Savings: " + (income - expense));
    }
}