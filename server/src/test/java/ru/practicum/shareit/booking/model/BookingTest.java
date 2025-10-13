package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BookingTest {

    @Test
    void testNoArgsConstructor_andSettersGetters() {
        Booking booking = new Booking();
        booking.setId(10L);

        LocalDateTime now = LocalDateTime.now();
        booking.setStart(now);
        booking.setEnd(now.plusHours(2));

        Item item = new Item();
        booking.setItem(item);

        User user = new User();
        booking.setBooker(user);

        booking.setStatus(BookingStatusEnum.APPROVED);

        assertEquals(10L, booking.getId());
        assertEquals(now, booking.getStart());
        assertEquals(now.plusHours(2), booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(user, booking.getBooker());
        assertEquals(BookingStatusEnum.APPROVED, booking.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 1, 14, 0);
        Item item = new Item();
        User booker = new User();
        BookingStatusEnum status = BookingStatusEnum.REJECTED;

        Booking booking = new Booking(
                1L,
                start,
                end,
                item,
                booker,
                status
        );

        assertEquals(1L, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(status, booking.getStatus());
    }

    @Test
    void testBuilder() {
        LocalDateTime start = LocalDateTime.of(2024, 5, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 5, 10, 12, 0);
        Item item = new Item();
        User booker = new User();

        Booking booking = Booking.builder()
                .id(100L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(BookingStatusEnum.WAITING)
                .build();

        assertEquals(100L, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(BookingStatusEnum.WAITING, booking.getStatus());
    }

    @Test
    void testEqualsAndHashCode_sameId() {
        Booking b1 = new Booking();
        b1.setId(1L);

        Booking b2 = new Booking();
        b2.setId(1L);

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    void testEquals_differentId() {
        Booking b1 = new Booking();
        b1.setId(1L);
        Booking b2 = new Booking();
        b2.setId(2L);

        assertNotEquals(b1, b2);
    }

    @Test
    void testEquals_nullAndOtherClass() {
        Booking b = new Booking();
        b.setId(1L);

        assertNotEquals(null, b);
        assertNotEquals("string", b);
    }

    @Test
    void testEquals_idNull() {
        Booking b1 = new Booking();
        Booking b2 = new Booking();

        assertNotEquals(b1, b2);
    }
}