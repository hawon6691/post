package com.example.board.exception;

/**
 * Exception thrown when invalid input is provided.
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}
