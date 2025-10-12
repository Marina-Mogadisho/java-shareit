package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingFilterEnumTest {

    @Test
    void testValues() {
        assertEquals(BookingFilterEnum.ALL, BookingFilterEnum.valueOf("ALL"));
        assertEquals(BookingFilterEnum.CURRENT, BookingFilterEnum.valueOf("CURRENT"));
        assertEquals(BookingFilterEnum.PAST, BookingFilterEnum.valueOf("PAST"));
        assertEquals(BookingFilterEnum.FUTURE, BookingFilterEnum.valueOf("FUTURE"));
        assertEquals(BookingFilterEnum.WAITING, BookingFilterEnum.valueOf("WAITING"));
        assertEquals(BookingFilterEnum.REJECTED, BookingFilterEnum.valueOf("REJECTED"));
    }

    @Test
    void testToString() {
        assertEquals("ALL", BookingFilterEnum.ALL.toString());
        assertEquals("CURRENT", BookingFilterEnum.CURRENT.toString());
        assertEquals("PAST", BookingFilterEnum.PAST.toString());
        assertEquals("FUTURE", BookingFilterEnum.FUTURE.toString());
        assertEquals("WAITING", BookingFilterEnum.WAITING.toString());
        assertEquals("REJECTED", BookingFilterEnum.REJECTED.toString());
    }
}
