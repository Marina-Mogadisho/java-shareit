package ru.practicum.shareit.item.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final UserRepository userRepository;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item createItem(Long userId, Item item) {
        checkingUserExists(userId); // проверяем существует ли пользователь
        item.setId(getNextId()); // создали и добавили item id
        item.setOwnerUserId(userId); // добавили id владельца вещи

        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Long userId, Item newItem) {
        checkingUserExists(userId);// проверяем существует ли пользователь
        checkingItemExists(userId, newItem.getId());// проверяем что вещь существует в Map и пользователь владелец вещи

        Item itemOld = items.get(newItem.getId());

        if (newItem.getName() != null) {
            if (!itemOld.getName().equals(newItem.getName())) {
                itemOld.setName(newItem.getName());
            }
        }
        if (newItem.getDescription() != null) {
            if (!itemOld.getDescription().equals(newItem.getDescription())) {
                itemOld.setDescription(newItem.getDescription());
            }
        }
        if (newItem.getAvailable() != null) {
            if (!itemOld.getAvailable().equals(newItem.getAvailable())) {
                itemOld.setAvailable(newItem.getAvailable());// обновили статус о том, доступна или нет вещь для аренды;
            }
        }
        //Изменения, внесённые в userOld, автоматически отражаются в мапе users,
        // поскольку userOld — это ссылка на объект в коллекции.
        return itemOld;
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        checkingUserExists(userId);// проверяем существует ли пользователь в Map
        checkingItemExists(userId, itemId);//проверяем что вещь существует в Map и пользователь владелец вещи
        items.remove(itemId);
    }

    @Override
    public Item getItemById(Long userId, Long itemId) {
        checkingUserExists(userId);// проверяем существует ли пользователь в Map
        checkingItemExists(userId, itemId);// проверяем что вещь существует в Map и пользователь владелец вещи
        return items.get(itemId);
    }

    @Override
    public Set<Long> getAllIdItems() {
        return items.keySet();
    }

    /*
    Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них.
     Эндпоинт GET /items.
     */
    @Override
    public List<Item> getListItemsFromUserId(Long userId) {
        checkingUserExists(userId);  // проверяем существует ли пользователь
        return items
                .values()
                .stream()
                .filter(item -> item.getOwnerUserId().equals(userId))
                .toList();
    }

    /*
    Поиск вещи потенциальным арендатором.
    Пример запроса:  GET /items/search?text=Шляпа летняя
    @GetMapping("/search")
     */
    @Override
    public List<Item> getItemsBySearch(String text) {
        List<Item> itemsList = getAllItems();
        return itemsList
                .stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable()) //возвращает только доступные для аренды вещи = true.
                .toList();
    }

    public void checkingUserExists(Long userId) {
        // проверяем существует ли пользователь
        if (!userRepository.getAllIdUsers().contains(userId)) { // Проверка наличия userId в Map
            throw new NotFoundException("Пользователя с указанным id " + userId + " не существует.");
        }
    }

    public void checkingItemExists(Long userId, Long itemId) {
        if (!items.containsKey(itemId)) { // Проверка наличия itemId в Map
            throw new NotFoundException("Item с указанным id " + itemId + " не существует.");
        }

        Item item = items.get(itemId);
        Long userIdByItem = item.getOwnerUserId();
        if (!userIdByItem.equals(userId)) { // проверяем является ли пользователь владельцем вещи
            throw new ValidationException("Эту операцию с item может совершить только ее владелец.");
        }
    }

    // вспомогательный метод для генерации идентификатора нового item
    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}