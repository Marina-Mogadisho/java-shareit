package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@Rollback
public class ItemTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    @Sql(scripts = "/data.sql")
    void setUp() {
    }

    @Test
    void testEquals() {
        Item item1 = itemRepository.findById(1L).orElseThrow();
        Item item2 = itemRepository.findById(2L).orElseThrow();

        assertTrue(item1.equals(item1));
        assertFalse(item1.equals(null));
        assertFalse(item1.equals("string"));
        assertFalse(item1.equals(item2));
    }

    @Test
    void testHashCode() {
        Item item1 = itemRepository.findById(1L).orElseThrow();
        int hashCode = item1.hashCode();
        assertEquals(hashCode, item1.hashCode());
    }

    @Test
    void getId() {
        Item item = itemRepository.findById(1L).orElseThrow();
        assertEquals(1L, item.getId());
    }

    @Test
    void getName() {
        Item item = itemRepository.findById(1L).orElseThrow();
        assertEquals("Название вещи №1", item.getName());
    }

    @Test
    void getDescription() {
        Item item = itemRepository.findById(2L).orElseThrow();
        assertEquals("Описание вещи №2", item.getDescription());
    }

    @Test
    void getAvailable() {
        Item itemTrue = itemRepository.findById(3L).orElseThrow();
        Item itemFalse = itemRepository.findById(4L).orElseThrow();

        assertFalse(itemTrue.getAvailable());
        assertTrue(itemFalse.getAvailable());
    }

    @Test
    void getUser() {
        User user = new User(1L, "Name", "mail@mail.ru");
        Item item = new Item(1L, "Item Name", "Item Description",
                true, user, new Request());
        assertEquals(user, item.getUser());
    }


    @Test
    void getRequest() {
        Item item = itemRepository.findById(1L).orElseThrow();
        item.setAvailable(true);
        assertTrue(true, String.valueOf(item.getAvailable()));
    }

    @Test
    void setId() {
        Item item = new Item();
        item.setId(1L);
        assertEquals(1L, item.getId());
    }

    @Test
    void setName() {
        Item item = itemRepository.findById(1L).orElseThrow();
        item.setName("Новое название");
        assertEquals("Новое название", item.getName());
    }

    @Test
    void setDescription() {
        Item item = itemRepository.findById(2L).orElseThrow();
        item.setDescription("Новое описание");
        assertEquals("Новое описание", item.getDescription());
    }

    @Test
    void setAvailable() {
        Item item = new Item();
        item.setAvailable(true);
        assertTrue(item.getAvailable());
        item.setAvailable(false);
        assertFalse(item.getAvailable());
    }

    @Test
    void setUser() {
        User user = new User();
        // Настройка пользователя
        Item item = itemRepository.findById(1L).orElseThrow();
        item.setUser(user);
        assertEquals(user, item.getUser());
    }

    @Test
    void setRequest() {
        Request request = new Request();
        // Настройка запроса
        Item item = itemRepository.findById(1L).orElseThrow();
        item.setRequest(request);
        assertEquals(request, item.getRequest());
    }
}