package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = "/data.sql") // Можно явно указать скрипт, если не загружается автоматически
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindItemsFromUserId_shouldReturnCorrectItems() {
        // В файле user с id=1 есть, у него вещь с id=1
        Long userId = 1L;

        List<Item> items = itemRepository.findItemsFromUserId(userId);

        assertFalse(items.isEmpty());
        assertTrue(items.stream().allMatch(i -> i.getUser().getId().equals(userId)));

        Item item = items.get(0);
        assertEquals("Название вещи №1", item.getName());
        assertEquals("Описание вещи №1", item.getDescription());
        assertTrue(item.getAvailable());
    }

    @Test
    void testFindAllIdItems_shouldReturnAllIds() {
        Set<Long> ids = itemRepository.findAllIdItems();

        assertNotNull(ids);
        assertTrue(ids.containsAll(Set.of(1L, 2L, 3L, 4L, 5L)));
    }

    @Test
    void testFindItemsBySearch_shouldReturnMatchingItemsIgnoringCase() {
        String searchText = "вещи №1"; // ищем по части названия описания

        List<Item> items = itemRepository.findItemsBySearch(searchText);

        assertFalse(items.isEmpty());
        assertTrue(items.stream().anyMatch(i ->
                i.getName().toLowerCase().contains("вещи №1") ||
                        i.getDescription().toLowerCase().contains("вещи №1")
        ));
    }
}
