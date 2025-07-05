package BudgetTracker;

import java.util.*;
import java.io.*;
import java.time.LocalDate;

enum Type {
    INCOME, EXPENSE
}

class Transaction {
    String description;
    double amount;
    String category;
    Type type;
    String user;
    LocalDate date;

    public Transaction(String user, String desc, double amt, String cat, Type type, LocalDate date) {
        this.user = user;
        this.description = desc;
        this.amount = amt;
        this.category = cat;
        this.type = type;
        this.date = date;
    }

    public double getAmount() { return amount; }
    public Type getType() { return type; }
    public String getCategory() { return category; }
    public String getUser() { return user; }
    public LocalDate getDate() { return date; }

    public void display() {
        System.out.printf("%-10s | %-10s | %-10s | %-15s | ₹%-10.2f | %s\n",
                type, user, category, description, amount, date);
    }

    @Override
    public String toString() {
        return user + "," + type + "," + category + "," + description + "," + amount + "," + date;
    }
}

class BudgetManager {
    private ArrayList<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public void displayAll() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to show.");
            return;
        }
        System.out.printf("\n%-10s | %-10s | %-10s | %-15s | %-10s | %s\n",
                "Type", "User", "Category", "Description", "Amount", "Date");
        System.out.println("--------------------------------------------------------------------------");
        for (Transaction t : transactions) {
            t.display();
        }
    }

    public void showSummary() {
        double totalIncome = 0, totalExpense = 0;
        for (Transaction t : transactions) {
            if (t.getType() == Type.INCOME)
                totalIncome += t.getAmount();
            else
                totalExpense += t.getAmount();
        }
        double balance = totalIncome - totalExpense;
        System.out.println("\n==== Summary ====");
        System.out.printf("Total Income  : ₹%.2f\n", totalIncome);
        System.out.printf("Total Expenses: ₹%.2f\n", totalExpense);
        System.out.printf("Balance       : ₹%.2f\n", balance);
    }

    public void showUserSummary(String user) {
        double totalIncome = 0, totalExpense = 0;
        for (Transaction t : transactions) {
            if (t.getUser().equalsIgnoreCase(user)) {
                if (t.getType() == Type.INCOME)
                    totalIncome += t.getAmount();
                else
                    totalExpense += t.getAmount();
            }
        }
        double balance = totalIncome - totalExpense;
        System.out.println("\n==== Summary for " + user + " ====");
        System.out.printf("Total Income  : ₹%.2f\n", totalIncome);
        System.out.printf("Total Expenses: ₹%.2f\n", totalExpense);
        System.out.printf("Balance       : ₹%.2f\n", balance);
    }

    public void updateIncome(String user, String desc, double newAmount) {
        for (Transaction t : transactions) {
            if (t.getType() == Type.INCOME &&
                    t.getUser().equalsIgnoreCase(user) &&
                    t.description.equalsIgnoreCase(desc)) {
                t.amount = newAmount;
                System.out.println("Income updated successfully.");
                return;
            }
        }
        System.out.println("Income not found for user: " + user);
    }

    public void showMonthlySpending(int month, int year) {
        double totalIncome = 0, totalExpense = 0;
        for (Transaction t : transactions) {
            if (t.getDate().getMonthValue() == month && t.getDate().getYear() == year) {
                if (t.getType() == Type.INCOME)
                    totalIncome += t.getAmount();
                else
                    totalExpense += t.getAmount();
            }
        }
        System.out.println("\n==== Summary for " + month + "/" + year + " ====");
        System.out.printf("Income  : ₹%.2f\n", totalIncome);
        System.out.printf("Expense : ₹%.2f\n", totalExpense);
        System.out.printf("Balance : ₹%.2f\n", totalIncome - totalExpense);
    }

    public void showYearlySpending(int year) {
        double totalIncome = 0, totalExpense = 0;
        for (Transaction t : transactions) {
            if (t.getDate().getYear() == year) {
                if (t.getType() == Type.INCOME)
                    totalIncome += t.getAmount();
                else
                    totalExpense += t.getAmount();
            }
        }
        System.out.println("\n==== Summary for year " + year + " ====");
        System.out.printf("Income  : ₹%.2f\n", totalIncome);
        System.out.printf("Expense : ₹%.2f\n", totalExpense);
        System.out.printf("Balance : ₹%.2f\n", totalIncome - totalExpense);
    }

    public void displaySingleMonthGraph(int month, int year) {
        double totalIncome = 0, totalExpense = 0;

        for (Transaction t : transactions) {
            if (t.getDate().getYear() == year && t.getDate().getMonthValue() == month) {
                if (t.getType() == Type.INCOME)
                    totalIncome += t.getAmount();
                else
                    totalExpense += t.getAmount();
            }
        }

        System.out.println("\n==== Graph for " + String.format("%02d", month) + "/" + year + " ====");
        System.out.printf("Income  (₹%.2f): %s\n", totalIncome, generateBar(totalIncome));
        System.out.println();
        System.out.printf("Expense (₹%.2f): %s\n", totalExpense, generateBar(totalExpense));
    }

    public void displayYearlyBarGraph(int year) {
        double[] income = new double[12];
        double[] expense = new double[12];

        for (Transaction t : transactions) {
            if (t.getDate().getYear() == year) {
                int month = t.getDate().getMonthValue() - 1;
                if (t.getType() == Type.INCOME)
                    income[month] += t.getAmount();
                else
                    expense[month] += t.getAmount();
            }
        }

        System.out.println("\n==== Yearly Transaction Graph (" + year + ") ====");
        for (int i = 0; i < 12; i++) {
            String month = String.format("%02d", i + 1);
            System.out.printf("%s | Income:  ₹%-8.2f %s\n", month, income[i], generateBar(income[i]));
            System.out.println();
            System.out.printf("   | Expense: ₹%-8.2f %s\n", expense[i], generateBar(expense[i]));
            System.out.println();
        }
    }

    private String generateBar(double value) {
        int barLength = (int) (value / 10000);
        return "█ ".repeat(Math.max(0, barLength));
    }

    public void viewRawFileContent(String filename) {
        System.out.println("\n==== Raw File Content ====");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void saveToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Transaction t : transactions) {
                bw.write(t.toString());
                bw.newLine();
            }
            System.out.println("Transactions saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            transactions.clear();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String user = parts[0];
                Type type = Type.valueOf(parts[1]);
                String cat = parts[2];
                String desc = parts[3];
                double amt = Double.parseDouble(parts[4]);
                LocalDate date = LocalDate.parse(parts[5]);
                transactions.add(new Transaction(user, desc, amt, cat, type, date));
            }
            System.out.println("Transactions loaded from " + filename);
        } catch (IOException e) {
            System.out.println("No saved file found. Starting fresh.");
        }
    }
}

public class PersonalBudgetTracker {
    private static final Scanner sc = new Scanner(System.in);
    private static final BudgetManager manager = new BudgetManager();
    private static final String FILENAME = "transactions.txt";

    public static void main(String[] args) {
        manager.loadFromFile(FILENAME);
        int choice;
        do {
            showMenu();
            choice = getInt("Enter your choice: ");
            switch (choice) {
                case 1 -> addTransaction(Type.INCOME);
                case 2 -> addTransaction(Type.EXPENSE);
                case 3 -> manager.displayAll();
                case 4 -> manager.showSummary();
                case 5 -> manager.saveToFile(FILENAME);
                case 6 -> {
                    sc.nextLine();
                    System.out.print("Enter user name: ");
                    String user = sc.nextLine();
                    System.out.print("Enter income description to update: ");
                    String desc = sc.nextLine();
                    double newAmt = getDouble("Enter new amount (₹): ");
                    manager.updateIncome(user, desc, newAmt);
                }
                case 7 -> {
                    sc.nextLine();
                    System.out.print("Enter user name: ");
                    String user = sc.nextLine();
                    manager.showUserSummary(user);
                }
                case 8 -> {
                    int month = getInt("Enter month (1-12): ");
                    int year = getInt("Enter year (e.g. 2025): ");
                    manager.showMonthlySpending(month, year);
                }
                case 9 -> {
                    int year = getInt("Enter year (e.g. 2025): ");
                    manager.showYearlySpending(year);
                }
                case 10 -> {
                    int month = getInt("Enter month (1-12): ");
                    int year = getInt("Enter year (e.g. 2025): ");
                    manager.displaySingleMonthGraph(month, year);
                }
                case 11 -> {
                    int year = getInt("Enter year (e.g. 2025): ");
                    manager.displayYearlyBarGraph(year);
                }
                case 12 -> manager.viewRawFileContent(FILENAME);
                case 13 -> {
                    manager.saveToFile(FILENAME);
                    System.out.println("Exiting. Data saved. Thank you!");
                }
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 13); // ✅ Fixed exit condition
    }

    private static void showMenu() {
        System.out.println("\n==== Personal Budget Tracker ====");
        System.out.println("1. Add Income");
        System.out.println("2. Add Expense");
        System.out.println("3. View All Transactions");
        System.out.println("4. Show Summary");
        System.out.println("5. Save Transactions");
        System.out.println("6. Update Income for a User");
        System.out.println("7. Show Summary for a User");
        System.out.println("8. Show Monthly Summary");
        System.out.println("9. Show Yearly Summary");
        System.out.println("10. Show Monthly Graph");
        System.out.println("11. Show Yearly Graph");
        System.out.println("12. View Raw File Content");
        System.out.println("13. Exit");
    }

    private static void addTransaction(Type type) {
        sc.nextLine(); // clear buffer

        System.out.print("Enter user name: ");
        String user = sc.nextLine().trim();

        System.out.print("Enter description: ");
        String desc = sc.nextLine().trim();

        double amt = getDouble("Enter amount (₹): ");
        sc.nextLine(); // clear buffer before category

        System.out.print("Enter category (e.g. Food, Rent, Travel): ");
        String cat = sc.nextLine().trim();

        LocalDate date = LocalDate.now();
        Transaction t = new Transaction(user, desc, amt, cat, type, date);
        manager.addTransaction(t);

        System.out.println(type + " added successfully.");
    }

    private static int getInt(String msg) {
        System.out.print(msg);
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input. " + msg);
            sc.next();
        }
        return sc.nextInt();
    }

    private static double getDouble(String msg) {
        System.out.print(msg);
        while (!sc.hasNextDouble()) {
            System.out.print("Invalid input. " + msg);
            sc.next();
        }
        return sc.nextDouble();
    }
}
