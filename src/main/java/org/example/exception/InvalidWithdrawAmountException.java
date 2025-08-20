package org.example.exception;

public class InvalidWithdrawAmountException extends RuntimeException {
  public InvalidWithdrawAmountException(String message) {
    super(message);
  }
}
