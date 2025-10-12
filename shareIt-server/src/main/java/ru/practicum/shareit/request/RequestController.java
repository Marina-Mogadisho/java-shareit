package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoCreate;
import ru.practicum.shareit.request.dto.RequestDtoForGetRequest;
import ru.practicum.shareit.request.dto.ResponseDtoCreated;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService requestService;


    /**
     * POST /requests — добавить новый запрос вещи.
     * Основная часть запроса — текст запроса, в котором пользователь описывает, какая именно вещь ему нужна.
     *
     * @param userRequestor    - автор (пользователь) запроса
     * @param requestDtoCreate - объект класса dto в котором только текст запроса
     */
    @PostMapping
    public ResponseDtoCreated save(@RequestHeader("X-Sharer-User-Id") Long userRequestor,
                                   @RequestBody RequestDtoCreate requestDtoCreate) {
        log.info("Получен запрос пользователя на создание запроса вещи, идентификатор пользователя: {}", userRequestor);

        return requestService.save(userRequestor, requestDtoCreate);
    }

    /**
     * GET /requests — получить список своих запросов вместе с данными об ответах на них.
     * Для каждого запроса должны быть указаны описание, дата и время создания,
     * а также список ответов в формате: id вещи, название, id владельца.
     * В дальнейшем, используя указанные id вещей, можно будет получить подробную информацию о каждой из них.
     * Запросы должны возвращаться отсортированными от более новых к более старым.
     *
     * @param userRequestor - автор (пользователь) запроса
     * @return List<RequestDtoList>:
     * String description
     * LocalDateTime created; //дата создания комментария
     * List<ItemDtoItemIdAndNameAndOwnerId> items;
     */
    @GetMapping
    public List<RequestDto> getRequestsListByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long userRequestor) {
        log.info("Получен запрос на получение своих запросов на вещь вместе с данными об ответах на них, идентификатор пользователя: {}", userRequestor);

        return requestService.getRequestsListByOwnerId(userRequestor);
    }

    /**
     * GET /requests/all — получить список запросов, созданных другими пользователями.
     * С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить.
     * Запросы сортируются по дате создания от более новых к более старым.
     *
     * @return
     */
    @GetMapping("/all")
    public List<RequestDto> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение всех запросов на вещи");
        return requestService.getRequests(userId);
    }

    /**
     * GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах на него
     * в том же формате, что и в эндпоинте GET /requests.
     * Посмотреть данные об отдельном запросе может любой пользователь
     */
    @GetMapping("{requestId}")
    public RequestDtoForGetRequest getRequest(@PathVariable Long requestId,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получении данных по запросу с id: {}", requestId);
        return requestService.getRequest(requestId, userId);
    }
}
