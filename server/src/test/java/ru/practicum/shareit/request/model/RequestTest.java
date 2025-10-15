package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Request request = new Request();
        request.setId(1L);
        request.setDescription("Test description");
        User user = new User();
        user.setId(42L);
        request.setRequestor(user);
        LocalDateTime created = LocalDateTime.now();
        request.setCreated(created);

        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setId(100L);
        items.add(item);
        request.setItems(items);

        assertEquals(1L, request.getId());
        assertEquals("Test description", request.getDescription());
        assertEquals(user, request.getRequestor());
        assertEquals(created, request.getCreated());
        assertEquals(items, request.getItems());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User();
        user.setId(5L);
        LocalDateTime created = LocalDateTime.of(2023, 1, 15, 10, 0);
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setId(20L);
        items.add(item);

        Request request = new Request(
                2L,
                "Request via all args",
                user,
                created,
                items
        );

        assertEquals(2L, request.getId());
        assertEquals("Request via all args", request.getDescription());
        assertEquals(user, request.getRequestor());
        assertEquals(created, request.getCreated());
        assertEquals(items, request.getItems());
    }

    @Test
    void testBuilder() {
        User user = new User();
        user.setId(10L);
        LocalDateTime created = LocalDateTime.of(2024, 6, 25, 15, 0);

        Request request = Request.builder()
                .id(3L)
                .description("Built request")
                .requestor(user)
                .created(created)
                .build();

        assertEquals(3L, request.getId());
        assertEquals("Built request", request.getDescription());
        assertEquals(user, request.getRequestor());
        assertEquals(created, request.getCreated());
//        assertTrue(request.getItems().isEmpty());
    }

    @Test
    void testToString_containsFields() {
        User user = new User();
        user.setId(7L);
        LocalDateTime created = LocalDateTime.now();

        Request request = Request.builder()
                .id(11L)
                .description("toString test")
                .requestor(user)
                .created(created)
                .build();

        String toString = request.toString();

        assertTrue(toString.contains("id=11"));
        assertTrue(toString.contains("description=toString test"));
        assertTrue(toString.contains("requestor=")); // Проверяем, что requestor включён, точное содержимое зависит от реализации User.toString()
        assertTrue(toString.contains("created="));
        //assertTrue(toString.contains("items=[]"));
    }

    @Test
    void testEqualsAndHashCode_sameId() {
        Request r1 = new Request();
        r1.setId(100L);
        Request r2 = new Request();
        r2.setId(100L);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testEquals_differentId() {
        Request r1 = new Request();
        r1.setId(1L);
        Request r2 = new Request();
        r2.setId(2L);

        assertNotEquals(r1, r2);
    }

    @Test
    void testEquals_nullAndOtherClass() {
        Request r = new Request();
        r.setId(1L);

        assertNotEquals(null, r);
        assertNotEquals("string", r);
    }

    @Test
    void testEquals_idNull() {
        Request r1 = new Request();
        Request r2 = new Request();

        assertNotEquals(r1, r2);
    }
}
