package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto save(
            @RequestHeader("X-Sharer-User-Id")
            @Positive Long userId,
            @RequestBody @Valid ItemDtoCreate item) {
        log.info("Получен запрос на создание элемента с идентификатором пользователя: {}", userId);
        return itemService.save(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable @Positive Long itemId,
            @RequestBody @Valid ItemDtoUpdate newItem) {
        return itemService.update(userId, itemId, newItem);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                       @PathVariable @Positive Long itemId) {
        itemService.delete(userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoComments findById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                    @PathVariable @Positive Long itemId) {
        return itemService.findById(userId, itemId);
    }

    /**
    Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них.
    Эндпоинт GET /items.
    Нужно, чтобы владелец видел даты последнего и ближайшего следующего бронирования для каждой вещи,
     */
    @GetMapping
    public List<ItemDtoList> getItemsFromUserId(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemService.findItemsFromUserId(userId);
    }

    /**
     *  Пример запроса:  GET /items/search?text=шляп
     *  Поиск вещи потенциальным арендатором.
     *required = true по умолчанию, если false то параметр необязательный + @Validated у класса
     */
    @GetMapping("/search")
    public List<ItemDtoResponse> getItemsBySearch(@RequestParam(name = "text", required = false) String text) {
        log.info("Получен запрос на поиск вещи, в названии и описании которой есть текст: {}", text);
        if (text == null || text.isEmpty()) {
            return Collections.emptyList(); //По условию теста Postman необходимо вернуть пустой список. Не исключение.
        }
        return itemService.findItemsBySearch(text);
    }


    /**
     * POST /items/{itemId}/comment
     * @param authorUserId автор комментария
     * @param itemId id вещи на которую автор оставляет комментарий
     * @param commentRequestDto текст комментария
     * @return CommentDto
     */
    @PostMapping("{itemId}/comment")
    public CommentDto saveComment(
            @RequestHeader("X-Sharer-User-Id")
            @Positive Long authorUserId,
            @PathVariable @Positive Long itemId,
            @RequestBody @Valid CommentRequestDto commentRequestDto) {
        log.info("Получен запрос на создание комментария (отзыва) на вещь с id: {}", itemId);
        return itemService.saveComment(authorUserId, itemId, commentRequestDto);
    }
}
