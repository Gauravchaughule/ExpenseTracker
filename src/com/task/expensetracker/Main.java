package com.task.expensetracker;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		ExpenseTracker tracker = new ExpenseTracker();

		while (true) {
			System.out.println("\n1. Add Income\n2. Add Expense\n3. Load from file\n4. Save to file\n5. Monthly Summary\n6. Exit");
			int choice = 0;
			try {
				System.out.print("Enter your choice: ");
				choice = Integer.parseInt(sc.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number between 1 and 6.");
				continue;
			}

			switch (choice) {
				case 1, 2 -> {
					String type = choice == 1 ? "Income" : "Expense";
					System.out.println("Enter category (e.g. Salary, Rent, Travel):");
					String category = sc.nextLine();

					double amount = 0.0;
					while (true) {
						System.out.println("Enter amount:");
						try {
							String input = sc.nextLine().trim();
							amount = Double.parseDouble(input);
							if (amount < 0) {
								System.out.println("Amount cannot be negative. Please enter again.");
								continue;
							}
							break;
						} catch (NumberFormatException e) {
							System.out.println("Invalid amount. Please enter a valid number.");
						}
					}
					tracker.addTransaction(new Transaction(LocalDate.now(), type, category, amount));
					System.out.println(type + " added.");
				}

				case 3 -> {
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println();
					System.out.println("Make sure your file should contain columns ordered as date, type, category, amount");
					System.out.println("Date should be in 'YYYY-MM-DD' format.");
					System.out.println("Only CSV file format acceptable.");
					System.out.println();
					System.out.println("----------------------------------------------------------------------------------");

					int templateChoice = 0;

					while (true) {
						System.out.println("\nWould you like to generate a sample template file?\n1. Yes\n2. No\n\nEnter your choice: ");
						String input = sc.nextLine().trim();
						try {
							templateChoice = Integer.parseInt(input);
							if (templateChoice == 1 || templateChoice == 2) {
								break;
							} else {
								System.out.println("Invalid choice. Please enter 1 for Yes or 2 for No.");
							}
						} catch (NumberFormatException e) {
							System.out.println("Invalid input. Please enter a numeric value (1 or 2).");
						}
					}

					if (templateChoice == 1) {
						System.out.println("Enter filename for the template (e.g., template.csv):");
						String templateFile = sc.nextLine();
						try {
							List<String> lines = List.of("Date,Type,Category,Amount");
							java.nio.file.Files.write(java.nio.file.Paths.get(templateFile), lines);
							System.out.println("Template created successfully: " + templateFile);
						} catch (Exception e) {
							System.out.println("Error creating template: " + e.getMessage());
						}
					}

					System.out.println("Enter filepath to load :");
					String file = sc.nextLine();
					try {
						List<Transaction> loaded;
						if (file.endsWith(".csv")) {
							loaded = CsvReader.read(file);
						} else {
							System.out.println("Unsupported file type.");
							return;
						}
						loaded.forEach(tracker::addTransaction);
						System.out.println("Data loaded successfully.");
					} catch (Exception e) {
						System.out.println("Error loading file: " + e.getMessage());
					}
				}

				case 4 -> {
					System.out.println("Enter filename to save:");
					String file = sc.nextLine();
					try {
						tracker.saveToFile(file);
						System.out.println("Data saved.");
					} catch (Exception e) {
						System.out.println("Error saving file: " + e.getMessage());
					}
				}

				case 5 -> {
					System.out.println("Enter month and year (YYYY-MM):");
					String input = sc.nextLine();
					YearMonth month = YearMonth.parse(input);
					tracker.printMonthlySummary(month);
				}

				case 6 -> {
					System.out.println("Exiting.");
					return;
				}

				default -> System.out.println("Invalid choice.");
			}
		}
	}

}
