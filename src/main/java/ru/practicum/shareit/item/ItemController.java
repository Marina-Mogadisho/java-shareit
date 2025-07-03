package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/items")
public class ItemController {

    @Autowired
    ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemDtoRequest item) {
        log.info("Получен запрос на создание элемента с идентификатором пользователя: {}", userId);
        return itemService.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody @Valid ItemDtoRequestUpdate newItem) {
        return itemService.updateItem(userId, itemId, newItem);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    /*
    Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них.
    Эндпоинт GET /items.
     */
    @GetMapping
    public List<ItemDTONameDescription> getItemsFromUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getListItemsFromUserId(userId);
    }

    /*
    Поиск вещи потенциальным арендатором.
    Пример запроса:  GET /items/search?text=шляп
    @GetMapping("/search")
     */
    @GetMapping("/search")
    public List<ItemDtoResponse> getItemsBySearch(@RequestParam(name = "text") String text) {
        return itemService.getItemsBySearch(text);
    }
}
