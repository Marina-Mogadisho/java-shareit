package ru.practicum.shareit.item.dal;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

public interface ItemRepository {
    public List<Item> getAllItems();

    public Item createItem(Long userId, Item item);

    public Item updateItem(Long userId, Item newItem);

    public void deleteItem(Long userId, Long itemId);

    public Item getItemById(Long userId, Long itemId);

    public Set<Long> getAllIdItems();

    public List<Item> getListItemsFromUserId(Long userId);

    public List<Item> getItemsBySearch(String text);
}
