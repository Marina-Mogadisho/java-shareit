package ru.practicum.shareit.item.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dal.RequestRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RequestRepository requestRepository;

    private User user;
    private Request request;
    private Item item;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .name("Имя пользователя")
                .email("mail@mail.ru")
                .build());

        request = Request.builder()
                .description("Описание запроса")
                .requestor(user)
                .created(LocalDateTime.parse("2026-10-16T12:12:00"))
                .build();

        request = requestRepository.save(request);   // *Сохраняем Request*

        item = Item.builder()
                .name("Имя вещи")
                .description("Описание вещи")
                .available(true)
                .user(user)
                .request(request)  // теперь request сохранен
                .build();

        itemRepository.save(item);
    }


    @Test
    void findAllIdItems_shouldReturnAllItemIds() {
        Set<Long> ids = itemRepository.findAllIdItems();

        assertNotNull(ids);
        assertTrue(ids.contains(item.getId()), "Set должен содержать id сохраненного Item");
    }

    @Test
    void findItemsFromUserId_shouldReturnItemsOfGivenUser() {
        List<Item> items = itemRepository.findItemsFromUserId(user.getId());

        assertNotNull(items);
        assertFalse(items.isEmpty(), "Список не должен быть пустым");
        assertTrue(items.stream().allMatch(i -> i.getUser().getId().equals(user.getId())),
                "Все вещи должны принадлежать пользователю");
    }

    @Test
    void findItemsBySearch_shouldReturnItemsMatchingNameOrDescription() {
        List<Item> itemsByName = itemRepository.findItemsBySearch("имя");
        List<Item> itemsByDescription = itemRepository.findItemsBySearch("описание");
        List<Item> itemsNoMatch = itemRepository.findItemsBySearch("не_существующий_текст");

        assertFalse(itemsByName.isEmpty(), "Должны найти вещи по имени");
        assertTrue(itemsByName.stream()
                        .anyMatch(i -> i.getName().toLowerCase().contains("имя")),
                "В каждом предмете имя должно содержать 'имя'");

        assertFalse(itemsByDescription.isEmpty(), "Должны найти вещи по описанию");
        assertTrue(itemsByDescription.stream()
                        .anyMatch(i -> i.getDescription().toLowerCase().contains("описание")),
                "В каждом предмете описание должно содержать 'описание'");

        assertTrue(itemsNoMatch.isEmpty(), "Не должно быть вещей под несуществующий текст");
    }
}
