package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    // во всех обработчиках замените формат ответа на ErrorResponse
    // добавьте коды ошибок
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код ошибки 400
    public ErrorResponse validationException(final ValidationException e) {
        log.error("Error validation." + e);
        Map<String, String> details = new HashMap<>();
        details.put("message", e.getMessage());
        details.put("exception", e.getClass().getName());
        return new ErrorResponse("BAD_REQUEST", details);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)// код ошибки 404
    public ErrorResponse notFoundException(final NotFoundException e) {
        log.error("Not found.", e);
        Map<String, String> details = new HashMap<>();
        details.put("message", e.getMessage());
        details.put("exception", e.getClass().getName());
        return new ErrorResponse("Resource not found", details);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // код ошибки 500
    public ErrorResponse enternalException(final EnternalException e) {
        log.error("Unexpected error." + e);
        return new ErrorResponse("Unexpected error.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)//409-Конфликт при обновлении данных
    public ErrorResponse conflictException(final ConflictException e) {
        log.error("Conflict of changes." + e);
        return new ErrorResponse("Conflict of changes.");
    }
}