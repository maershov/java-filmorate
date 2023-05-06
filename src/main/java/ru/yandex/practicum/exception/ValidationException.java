package ru.yandex.practicum.exception;

import java.io.IOException;
import java.io.UncheckedIOException;

public class ValidationException extends UncheckedIOException {
    public ValidationException(String message, IOException cause) {
        super(message, cause);
    }
}