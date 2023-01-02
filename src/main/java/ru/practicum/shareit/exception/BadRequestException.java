package ru.practicum.shareit.exception;

@SuppressWarnings("checkstyle:WhitespaceAround")
public class BadRequestException extends RuntimeException{
    private String message;
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    public BadRequestException(String message) {
        this.message = message;
    }
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    @Override
    public String getMessage() {
        return message;
    }
}
