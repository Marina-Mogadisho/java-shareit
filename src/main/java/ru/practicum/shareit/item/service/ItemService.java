package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;
import java.util.Set;

public interface ItemService {
    public List<ItemDtoResponse> getAllItems();

    public ItemDto createItem(Long userId, ItemDtoRequest item);

    public ItemDto updateItem(Long userId, Long itemId, ItemDtoRequestUpdate newItem);

    public void deleteItem(Long userId, Long itemId);

    public ItemDto getItemById(Long userId, Long itemId);

    public Set<Long> getAllIdItems();

    public List<ItemDTONameDescription> getListItemsFromUserId(Long userId);

    public List<ItemDtoResponse> getItemsBySearch(String text);
}
