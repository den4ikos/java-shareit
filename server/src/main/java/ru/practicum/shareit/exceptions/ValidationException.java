package ru.practicum.shareit.exceptions;

public class ValidationException extends RuntimeException {
    private String message;

    public ValidationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
