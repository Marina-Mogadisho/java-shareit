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
@Validated
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId, @RequestBody @Valid ItemDtoCreate item) {
        log.info("Получен запрос на создание элемента с идентификатором пользователя: {}", userId);
        return itemService.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable @Positive Long itemId,
            @RequestBody @Valid ItemDtoUpdate newItem) {
        return itemService.updateItem(userId, itemId, newItem);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                           @PathVariable @Positive Long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                               @PathVariable @Positive Long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    /*
    Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них.
    Эндпоинт GET /items.
     */
    @GetMapping
    public List<ItemDTONameDescription> getItemsFromUserId(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemService.getListItemsFromUserId(userId);
    }

    /*
    Поиск вещи потенциальным арендатором.
    Пример запроса:  GET /items/search?text=шляп
    @GetMapping("/search")
     */
    // required = true по умолчанию, если false то параметр необязательный + @Validated у класса
    @GetMapping("/search")
    public List<ItemDtoResponse> getItemsBySearch(@RequestParam(name = "text", required = false) String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList(); //По условию теста Postman необходимо вернуть пустой список. Не исключение.
        }
        return itemService.getItemsBySearch(text);
    }
}
