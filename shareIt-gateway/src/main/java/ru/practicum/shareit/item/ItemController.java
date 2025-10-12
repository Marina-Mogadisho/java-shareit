package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.Collections;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> save(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @RequestBody @Valid ItemDtoCreate item) {
        log.info("Получен запрос на создание элемента с идентификатором пользователя: {}", userId);
        ResponseEntity<Object> itemSave = itemClient.saveItem(userId, item);
        log.info("Item успешно создана (gateway -ItemController) : {}", itemSave);
        return itemSave;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable @Positive Long itemId,
            @RequestBody @Valid ItemDtoUpdate newItem) {
        return itemClient.update(userId, itemId, newItem);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                       @PathVariable @Positive Long itemId) {
        itemClient.delete(itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @PathVariable @Positive Long itemId) {
        return itemClient.findById(userId, itemId);
    }

    /**
     * Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них.
     * Эндпоинт GET /items.
     * Нужно, чтобы владелец видел даты последнего и ближайшего следующего бронирования для каждой вещи,
     */
    @GetMapping
    public ResponseEntity<Object> findItemsFromUserId(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemClient.findItemsFromUserId(userId);
    }

    /**
     * Пример запроса:  GET /items/search?text=шляп
     * Поиск вещи потенциальным арендатором.
     * required = true по умолчанию, если false то параметр необязательный + @Validated у класса
     */
    @GetMapping("/search")
    public ResponseEntity<Object> findItemsBySearch(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @RequestParam(name = "text", required = false) String text) {
        log.info("Поиск вещей с текстом: {}", text);
        if (text == null || text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return itemClient.findItemsBySearch(userId, text);
    }

    /**
     * POST /items/{itemId}/comment
     */
    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> saveComment(
            @RequestHeader("X-Sharer-User-Id")
            @Positive Long authorUserId,
            @PathVariable @Positive Long itemId,
            @RequestBody @Valid CommentRequestDto commentRequestDto) {
        log.info("Получен запрос на создание комментария (отзыва) на вещь с id: {}", itemId);
        return itemClient.saveComment(authorUserId, itemId, commentRequestDto);
    }
}

