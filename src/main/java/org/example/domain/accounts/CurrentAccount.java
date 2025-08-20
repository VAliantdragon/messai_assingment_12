package org.example.domain;

import org.example.enums.AccountType;

import java.math.BigDecimal;

public class CurrentAccount extends Account {
    // CORRECTED: Rates as per assignment description
    public static final BigDecimal INTEREST_RATE = new BigDecimal("0.0");
    public static final BigDecimal MINIMUM_BALANCE = new BigDecimal("0.00");

    public CurrentAccount(String accountNumber, String customerId, BigDecimal balance) {
        super(accountNumber, customerId, AccountType.CURRENT, balance);
    }

    @Override
    public BigDecimal getInterestRate() {
        return INTEREST_RATE;
    }

    @Override
    public BigDecimal getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}