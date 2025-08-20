package org.example;

import org.example.entity.*;
import org.example.enums.TransactionType;
import org.example.exception.InvalidDepositValueException;
import org.example.exception.InvalidTransferAmountException;
import org.example.exception.InvalidWithdrawAmountException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Map<String, Customer> customerMap = new HashMap<>();
    private static final Map<String, Account> accountMap = new HashMap<>(); // Renamed for clarity
    private static final List<Transaction> transactionHistory = new ArrayList<>(); // Renamed
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to HDFC Banking Application");
        boolean running = true;
        while (running) {
            try {
                running = showMainMenu();
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                // In a real app, you would log this e.printStackTrace();
            }
        }
        System.out.println("Thank you for using HDFC Banking Application. Goodbye!");
        sc.close();
    }

    private static boolean showMainMenu() {
        System.out.println("\n--------- Main Menu ---------");
        System.out.println("1. Register New Customer");
        System.out.println("2. Create Account");
        System.out.println("3. Perform Transaction");
        System.out.println("4. View Account Details");
        System.out.println("5. View Transaction History");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");

        int choice = choice();

        switch (choice) {
            case 1:
                registerCustomer();
                break;
            case 2:
                createAccount();
                break;
            case 3:
                performTransaction(); // CORRECTED
                break;
            case 4:
                viewAccountDetails(); // IMPLEMENTED
                break;
            case 5:
                viewTransactionHistory(); // IMPLEMENTED
                break;
            case 6:
                return false; // Exit the loop
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return true; // Continue running
    }

    public static void registerCustomer() {
        System.out.println("\n--- Register New Customer ---");
        System.out.print("Enter Customer ID: ");
        String customerId = sc.nextLine().trim();

        if (customerMap.containsKey(customerId)) {
            System.out.println("Error: Customer with this ID already exists.");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Enter Mobile No.: ");
        String phone = sc.nextLine().trim();

        System.out.print("Enter DOB in YYYY-MM-DD format: ");
        String dobStr = sc.nextLine().trim();

        LocalDate dateOfBirth;
        try {
            dateOfBirth = LocalDate.parse(dobStr, dateFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        System.out.print("Enter Password: ");
        String password = sc.nextLine().trim();

        // CORRECTED argument order
        Customer customer = new Customer(customerId, name, email, phone, password, dateOfBirth);
        customerMap.put(customerId, customer);

        System.out.println("Customer registered successfully!");
    }

    private static int choice() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a numerical choice: ");
            }
        }
    }

    private static void createAccount() {
        System.out.println("\n--- Create New Account ---");
        System.out.print("Please enter Customer ID: ");
        String customerID = sc.nextLine().trim();

        // CORRECTED null check
        if (!customerMap.containsKey(customerID)) {
            System.out.println("Error: Customer not found. Please register the customer first.");
            return;
        }

        System.out.println("Choose Account Type:");
        System.out.println("1. Savings Account (6% Interest rate and minimum balance of 1000)");
        System.out.println("2. Current Account (4% Interest rate and no minimum balance)");
        System.out.print("Select the type of account: ");

        int typeChoice = choice();
        Account newAccount; // Renamed

        System.out.print("Enter initial deposit amount: ");
        String balanceStr = sc.nextLine().trim(); // Renamed

        try {
            BigDecimal initialBalance = new BigDecimal(balanceStr);
            String accountNumber = generateAccountNumber();

            switch (typeChoice) {
                case 1:
                    if (initialBalance.compareTo(SavingsAccount.MINIMUM_BALANCE) < 0) {
                        System.out.println("Error: Initial deposit must be at least " + SavingsAccount.MINIMUM_BALANCE);
                        return;
                    }
                    newAccount = new SavingsAccount(accountNumber, customerID, initialBalance);
                    break;
                case 2:
                    newAccount = new CurrentAccount(accountNumber, customerID, initialBalance);
                    break;
                default:
                    System.out.println("Error: Invalid account type choice.");
                    return;
            }

            accountMap.put(accountNumber, newAccount);
            System.out.println("\nCongratulations! Account created successfully.");
            System.out.println("Your new account number is: " + accountNumber);

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid balance amount! Please enter a valid number.");
        }
    }

    private static String generateAccountNumber() {
        // Using UUID for a more unique account number
        return UUID.randomUUID().toString().substring(0, 13).replace("-", "").toUpperCase();
    }

    private static void performTransaction() {
        System.out.println("\n--- Perform Transaction ---");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer"); // IMPLEMENTED
        System.out.print("Please select transaction type: ");

        int transactionChoice = choice();

        switch (transactionChoice) {
            case 1:
                performDeposit();
                break;
            case 2:
                performWithdraw();
                break;
            case 3:
                performTransfer();
                break;
            default:
                System.out.println("Error: Invalid transaction type.");
        }
    }

    private static void performDeposit() {
        System.out.println("\n--- Perform Deposit ---");
        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine().trim();

        Account targetAccount = accountMap.get(accountNumber);
        if (targetAccount == null) {
            System.out.println("Error: Account not found.");
            return;
        }

        System.out.print("Enter deposit amount: ");
        String amountStr = sc.nextLine().trim();

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            targetAccount.deposit(amount);

            String transactionId = generateTransactionId();
            Transaction depositTransaction = new Transaction(
                    transactionId, amount, accountNumber, LocalDateTime.now(), TransactionType.DEPOSIT);
            transactionHistory.add(depositTransaction);

            System.out.println("Deposit successful. New balance is: " + targetAccount.getBalance());
            System.out.println("Transaction ID: " + transactionId);

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount format.");
        } catch (InvalidDepositValueException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String generateTransactionId() {
        return "HDFCT" + System.currentTimeMillis();
    }

    private static void performWithdraw() {
        System.out.println("\n--- Perform Withdraw ---");
        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine().trim();

        Account targetAccount = accountMap.get(accountNumber);
        if (targetAccount == null) {
            System.out.println("Error: Account not found.");
            return;
        }

        System.out.print("Enter withdrawal amount: ");
        String amountStr = sc.nextLine().trim();

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            targetAccount.withdraw(amount);

            String transactionId = generateTransactionId();
            Transaction withdrawTransaction = new Transaction(
                    transactionId, amount, accountNumber, LocalDateTime.now(), TransactionType.WITHDRAW);
            transactionHistory.add(withdrawTransaction);

            System.out.println("Withdrawal successful. New balance is: " + targetAccount.getBalance());
            System.out.println("Transaction ID: " + transactionId);

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount format.");
        } catch (InvalidWithdrawAmountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void performTransfer() {
        System.out.println("\n--- Perform Transfer ---");
        System.out.print("Enter source account number: ");
        String sourceAccountNo = sc.nextLine().trim();

        Account sourceAccount = accountMap.get(sourceAccountNo);
        if (sourceAccount == null) {
            System.out.println("Error: Source account not found.");
            return;
        }

        System.out.print("Enter destination account number: ");
        String destinationAccountNo = sc.nextLine().trim();

        Account destinationAccount = accountMap.get(destinationAccountNo);
        if (destinationAccount == null) {
            System.out.println("Error: Destination account not found.");
            return;
        }

        if (sourceAccount.equals(destinationAccount)) {
            System.out.println("Error: Source and destination accounts cannot be the same.");
            return;
        }

        System.out.print("Enter transfer amount: ");
        String amountStr = sc.nextLine().trim();

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            sourceAccount.transfer(destinationAccount, amount); // Using the transfer method

            String transactionId = generateTransactionId();
            // Log transfer for source account
            Transaction transferOut = new Transaction(transactionId, amount.negate(), sourceAccountNo, LocalDateTime.now(), TransactionType.TRANSFER);
            // Log transfer for destination account
            Transaction transferIn = new Transaction(transactionId, amount, destinationAccountNo, LocalDateTime.now(), TransactionType.TRANSFER);
            transactionHistory.add(transferOut);
            transactionHistory.add(transferIn);

            System.out.println("Transfer successful.");
            System.out.println("Source Account new balance: " + sourceAccount.getBalance());
            System.out.println("Destination Account new balance: " + destinationAccount.getBalance());
            System.out.println("Transaction ID: " + transactionId);

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount format.");
        } catch (InvalidTransferAmountException | InvalidWithdrawAmountException | InvalidDepositValueException e) {
            // CORRECTED: Show user-friendly message, don't crash
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    private static void viewAccountDetails() {
        System.out.println("\n--- View Account Details ---");
        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine().trim();

        Account account = accountMap.get(accountNumber);
        if (account == null) {
            System.out.println("Error: Account not found.");
            return;
        }
        // Using the overridden toString() method in Account
        System.out.println(account);
    }

    private static void viewTransactionHistory() {
        System.out.println("\n--- View Transaction History ---");
        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine().trim();

        if (!accountMap.containsKey(accountNumber)) {
            System.out.println("Error: Account not found.");
            return;
        }

        System.out.println("Transaction history for account: " + accountNumber);
        boolean found = false;
        for (Transaction tx : transactionHistory) {
            if (tx.getAccountNumber().equals(accountNumber)) {
                System.out.println(tx);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found for this account.");
        }
    }
}
