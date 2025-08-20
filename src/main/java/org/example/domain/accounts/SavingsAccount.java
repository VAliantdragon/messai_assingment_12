package org.example.domain;

import org.example.enums.AccountType;
import org.example.exception.InvalidWithdrawAmountException;

import java.math.BigDecimal;

public class SavingsAccount extends Account {
    // CORRECTED: Rates as per assignment description
    public static final BigDecimal INTEREST_RATE = new BigDecimal("4.5");
    public static final BigDecimal MINIMUM_BALANCE = new BigDecimal("1000.00");

    public SavingsAccount(String accountNumber, String customerId, BigDecimal balance) {
        super(accountNumber, customerId, AccountType.SAVING, balance);
    }

    @Override
    public BigDecimal getInterestRate() {
        return INTEREST_RATE;
    }

    @Override
    public BigDecimal getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    @Override
    public synchronized void withdraw(BigDecimal amount) throws InvalidWithdrawAmountException {
        // Check if the balance after withdrawal would be below the minimum
        if (getBalance().subtract(amount).compareTo(MINIMUM_BALANCE) < 0) {
            throw new InvalidWithdrawAmountException(
                    "Withdrawal failed. Balance cannot fall below the minimum of " + MINIMUM_BALANCE
            );
        }
        // If check passes, call the parent withdraw method
        super.withdraw(amount);
    }
}