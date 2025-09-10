package ru.practicum.shareit.booking.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    //Метод, получающий бронирования для определённой вещи.
    List<Booking> getBookingsListByItem(Item item);

    // список бронирований на все вещи владельца
    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId")
    List<Booking> getBookingsListByOwnerUserId(Long ownerId);

    // список бронирований пользователя (арендатора, букера), который делал запрос на бронирование
    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId")
    List<Booking> getBookingsListByBookerUserId(@Param("bookerId") Long bookerId);
}
