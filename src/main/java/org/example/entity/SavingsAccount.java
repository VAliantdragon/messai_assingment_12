package org.example.entity;

import org.example.enums.AccountType;
import org.example.exception.InvalidWithdrawAmountException;

import java.math.BigDecimal;

public class SavingsAccount extends Account {
    // CORRECTED: Defines its OWN constants as per the menu description (6% and 1000)
    public static final BigDecimal INTEREST_RATE = new BigDecimal("6.0");
    public static final BigDecimal MINIMUM_BALANCE = new BigDecimal("1000.00");

    public SavingsAccount() {
        super();
        setAccountType(AccountType.SAVING);
    }

    public SavingsAccount(String accountNumber, String customerId, BigDecimal balance) {
        super(accountNumber, customerId, AccountType.SAVING, balance);
    }

    @Override
    public BigDecimal getInterestRate() {
        // CORRECTED: Returns its OWN interest rate.
        return INTEREST_RATE;
    }

    @Override
    public BigDecimal getMinimumBalance() {
        // CORRECTED: Returns its OWN minimum balance.
        return MINIMUM_BALANCE;
    }

    @Override
    public synchronized void withdraw(BigDecimal amount) throws InvalidWithdrawAmountException {
        // First check if the withdrawal is valid based on the abstract class logic (e.g., sufficient funds)
        super.withdraw(amount);

        // Then, apply the specific rule for SavingsAccount
        BigDecimal newBalance = getBalance(); // The balance is already updated by super.withdraw()
        if (newBalance.compareTo(getMinimumBalance()) < 0) {
            // This is a failsafe; the initial check in super.withdraw should prevent this.
            // However, it's good practice to enforce the rule specific to this class.
            throw new InvalidWithdrawAmountException("Withdrawal would bring balance below the minimum required amount of " + getMinimumBalance() + ".");
        }
    }
}