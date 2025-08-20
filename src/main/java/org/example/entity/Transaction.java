package org.example.entity;

import org.example.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private String transactionId;
    private BigDecimal amount;
    private String accountNumber;
    private LocalDateTime timestamp; // Renamed for clarity
    private TransactionType type;

    public Transaction(String transactionId, BigDecimal amount, String accountNumber, LocalDateTime timestamp, TransactionType type) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.timestamp = timestamp;
        this.type = type;
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public BigDecimal getAmount() { return amount; }
    public String getAccountNumber() { return accountNumber; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionType getType() { return type; }

    @Override
    public String toString() {
        return String.format("Transaction [ID: %s, Type: %s, Amount: %.2f, Date: %s]",
                transactionId, type, amount, timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}