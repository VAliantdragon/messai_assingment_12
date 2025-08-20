package org.example.entity;

import org.example.enums.AccountType;
import org.example.exception.InvalidDepositValueException;
import org.example.exception.InvalidTransferAmountException;
import org.example.exception.InvalidWithdrawAmountException;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Account {
    private String accountNumber;
    private String customerId;
    private AccountType accountType;
    private BigDecimal balance;

    public Account(String accountNumber, String customerId, AccountType accountType, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
    }

    public Account() {}

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public synchronized void deposit(BigDecimal amount) throws InvalidDepositValueException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDepositValueException("Deposit value must be greater than 0.");
        }
        this.balance = this.balance.add(amount);
    }

    public synchronized void withdraw(BigDecimal amount) throws InvalidWithdrawAmountException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidWithdrawAmountException("Withdrawal amount must be greater than zero.");
        }
        if (amount.compareTo(this.balance) > 0) {
            throw new InvalidWithdrawAmountException("Insufficient funds. Current balance: " + this.balance);
        }
        this.balance = this.balance.subtract(amount);
    }

    public synchronized void transfer(Account destinationAccount, BigDecimal amount)
            throws InvalidTransferAmountException, InvalidWithdrawAmountException, InvalidDepositValueException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferAmountException("Transfer amount must be greater than zero.");
        }
        if (destinationAccount == null) {
            throw new IllegalArgumentException("Destination account cannot be null.");
        }
        this.withdraw(amount);
        destinationAccount.deposit(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return String.format("Account Type: %s\nAccount Number: %s\nCustomer ID: %s\nBalance: %.2f",
                this.accountType.getDisplayName(), this.accountNumber, this.customerId, this.balance);
    }

    public abstract BigDecimal getInterestRate();
    public abstract BigDecimal getMinimumBalance();

    public BigDecimal calculateInterest() {
        return balance.multiply(getInterestRate()).divide(new BigDecimal("100"));
    }
}