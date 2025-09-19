package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;
import java.util.Set;

public interface ItemService {

    List<ItemDtoResponse> findAll();

    ItemDto save(Long userId, ItemDtoCreate item);

    ItemDto update(Long userId, Long itemId, ItemDtoUpdate newItem);

    void delete(Long userId, Long itemId);

    ItemDtoComments findById(Long userId, Long itemId);

    Set<Long> findAllIdItems();

    List<ItemDtoList> findItemsFromUserId(Long userId);

    List<ItemDtoResponse> findItemsBySearch(String text);

    CommentDto saveComment(Long authorUserId, Long itemId, CommentRequestDto commentRequestDto);
}
