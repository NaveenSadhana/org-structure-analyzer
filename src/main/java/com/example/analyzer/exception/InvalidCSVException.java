package com.example.analyzer.exception;

public class InvalidCSVException extends RuntimeException {
    public InvalidCSVException(String message) {
        super(message);
    }
}
