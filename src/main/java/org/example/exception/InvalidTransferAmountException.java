package org.example.exception;

public class InvalidTransferAmountException extends Exception {
    public InvalidTransferAmountException(String message) {
        super(message);
    }
}
