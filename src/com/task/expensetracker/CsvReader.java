package com.task.expensetracker;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CsvReader {

    private static final List<DateTimeFormatter> SUPPORTED_DATE_FORMATS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yy"),
            DateTimeFormatter.ofPattern("d/M/yy")
    );

    public static List<Transaction> read(String filename) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine(); // To avoid first line, as it will be the header
            int lineNo = 1;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length < 4) {
                    throw new RuntimeException("An error occurred while parsing line no ["+lineNo+"] invalid columns count.");
                }
                LocalDate date = LocalDate.parse(formatDate(parts[0]));
                String type = parts[1];
                String category = parts[2];
                double amount = Double.parseDouble(parts[3]);
                transactions.add(new Transaction(date, type, category, amount));
                lineNo++;
            }
        }
        return transactions;
    }

    public static String formatDate(String input) {
        for (DateTimeFormatter formatter : SUPPORTED_DATE_FORMATS) {
            try {
                LocalDate date = LocalDate.parse(input.trim(), formatter);
                return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                // Keeping empty to avoid any disruptions or logs
            }
        }
        throw new IllegalArgumentException("Unsupported date format: " + input);
    }
}