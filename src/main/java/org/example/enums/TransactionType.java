package org.example.enums;

public enum TransactionType {

    DEPOSIT("Deposit"),WITHDRAW("Withdraw"),TRANSFER("Transfer");

    public String getDisplayName() {
        return displayName;
    }

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String toString(){
        return displayName;
    }

}
