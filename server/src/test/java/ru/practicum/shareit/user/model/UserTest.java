package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testEquals() {
        User user1 = new User(1L, "John Doe", "john.doe@example.com");
        User user2 = new User(1L, "John Doe", "john.doe@example.com");
        User user3 = new User(2L, "Jane Doe", "jane.doe@example.com");

        assertTrue(user1.equals(user2));
        assertFalse(user1.equals(user3));
    }

    @Test
    void testHashCode() {
        User user1 = new User(1L, "John Doe", "john.doe@example.com");
        User user2 = new User(1L, "John Doe", "john.doe@example.com");

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void getId() {
        User user = new User(1L, "John Doe", "john.doe@example.com");
        assertEquals(1L, user.getId());
    }

    @Test
    void getName() {
        User user = new User(1L, "John Doe", "john.doe@example.com");
        assertEquals("John Doe", user.getName());
    }

    @Test
    void getEmail() {
        User user = new User(1L, "John Doe", "john.doe@example.com");
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void setId() {
        User user = new User();
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    void setName() {
        User user = new User();
        user.setName("John Doe");
        assertEquals("John Doe", user.getName());
    }

    @Test
    void setEmail() {
        User user = new User();
        user.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void testToString() {
        User user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        String expected = "User(id=1, name=John Doe, email=john.doe@example.com)";
        assertEquals(expected, user.toString());
    }

    @Test
    void builder() {
        User user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }
}