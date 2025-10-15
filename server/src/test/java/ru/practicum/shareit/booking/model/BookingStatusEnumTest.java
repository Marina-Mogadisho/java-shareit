package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingStatusEnumTest {

    @Test
    void testValues() {
        assertEquals(BookingStatusEnum.WAITING, BookingStatusEnum.valueOf("WAITING"));
        assertEquals(BookingStatusEnum.APPROVED, BookingStatusEnum.valueOf("APPROVED"));
        assertEquals(BookingStatusEnum.REJECTED, BookingStatusEnum.valueOf("REJECTED"));
        assertEquals(BookingStatusEnum.CANCELED, BookingStatusEnum.valueOf("CANCELED"));
    }

    @Test
    void testToString() {
        assertEquals("WAITING", BookingStatusEnum.WAITING.toString());
        assertEquals("APPROVED", BookingStatusEnum.APPROVED.toString());
        assertEquals("REJECTED", BookingStatusEnum.REJECTED.toString());
        assertEquals("CANCELED", BookingStatusEnum.CANCELED.toString());
    }
}
