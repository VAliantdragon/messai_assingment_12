package org.example.enums;

public enum AccountType {
    SAVING("Savings Account"),CURRENT("Current Account");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String toString(){
        return displayName;
    }
}
