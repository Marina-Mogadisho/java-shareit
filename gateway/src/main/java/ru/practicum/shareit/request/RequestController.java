package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDtoCreate;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;


    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader("X-Sharer-User-Id") @Positive Long userRequestor,
                                       @RequestBody @Valid RequestDtoCreate requestDtoCreate) {
        log.info("Получен запрос пользователя на создание запроса вещи, идентификатор пользователя: {}", userRequestor);

        return requestClient.save(userRequestor, requestDtoCreate);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsListByOwnerId(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userRequestor) {
        log.info("Получен запрос на получение своих запросов на вещь вместе с данными об ответах на них, идентификатор пользователя: {}", userRequestor);
        return requestClient.getRequestsListByOwnerId(userRequestor);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Получен запрос на получение всех запросов на вещи");
        return requestClient.getRequests(userId);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable @Positive Long requestId,
                                             @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Получен запрос на получении данных по запросу с id: {}", requestId);
        return requestClient.getRequest(requestId, userId);
    }
}


