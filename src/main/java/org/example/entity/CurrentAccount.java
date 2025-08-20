package org.example.entity;

import org.example.enums.AccountType;

import java.math.BigDecimal;

public class CurrentAccount extends Account {
    // Correctly defined constants as per menu description (4%)
    public static final BigDecimal INTEREST_RATE = new BigDecimal("4.0");
    public static final BigDecimal MINIMUM_BALANCE = new BigDecimal("0.00");

    public CurrentAccount() {
        super();
        setAccountType(AccountType.CURRENT);
    }

    public CurrentAccount(String accountNumber, String customerId, BigDecimal balance) {
        super(accountNumber, customerId, AccountType.CURRENT, balance);
    }

    @Override
    public BigDecimal getInterestRate() {
        // CORRECTED: Now returns the class's specific interest rate.
        return INTEREST_RATE;
    }

    @Override
    public BigDecimal getMinimumBalance() {
        // CORRECTED: Now returns the class's specific minimum balance.
        return MINIMUM_BALANCE;
    }

    // The default toString() from the abstract Account class is sufficient.
    // No need to override it here unless you want different formatting.
}