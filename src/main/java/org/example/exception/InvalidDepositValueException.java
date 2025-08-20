package org.example.exception;

public class InvalidDepositValueException extends Exception {
    public InvalidDepositValueException(String message) {
        super(message);
    }
}