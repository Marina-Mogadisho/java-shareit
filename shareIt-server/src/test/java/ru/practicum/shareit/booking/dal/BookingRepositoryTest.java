package ru.practicum.shareit.booking.dal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = "/data.sql")
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void getBookingsListByItem_returnsBookingsForGivenItem() {
        Item item = new Item();
        item.setId(1L);

        List<Booking> bookings = bookingRepository.getBookingsListByItem(item);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
        assertTrue(bookings.stream().allMatch(b -> b.getItem().getId().equals(1L)));
    }

    @Test
    void getBookingsListByOwnerUserId_returnsBookingsForBookerId() {
        Long bookerId = 1L;

        List<Booking> bookings = bookingRepository.getBookingsListByOwnerUserId(bookerId);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
        assertTrue(bookings.stream().allMatch(b -> b.getBooker().getId().equals(bookerId)));
    }

    @Test
    void getBookingsListByBookerUserId_returnsBookingsForBookerId() {
        Long bookerId = 2L;

        List<Booking> bookings = bookingRepository.getBookingsListByBookerUserId(bookerId);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
        assertTrue(bookings.stream().allMatch(b -> b.getBooker().getId().equals(bookerId)));
    }
}
