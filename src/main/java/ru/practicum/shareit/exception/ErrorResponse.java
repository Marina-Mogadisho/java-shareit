package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    String error;// название ошибки

    public ErrorResponse(String error) {
        this.error = error;
    }
}
