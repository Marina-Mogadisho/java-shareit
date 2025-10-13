package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ErrorResponse {
    private final String error; // общее сообщение об ошибке
    private Map<String, String> details; // детальные сообщения об ошибках

    public ErrorResponse(String error) {
        this.error = error;
    }

    // Конструктор для случая с деталями ошибок
    public ErrorResponse(String error, Map<String, String> details) {
        this.error = error;
        this.details = details;
    }
}

