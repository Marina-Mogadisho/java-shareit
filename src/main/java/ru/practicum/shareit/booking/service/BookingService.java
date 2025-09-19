package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.dto.BookingDtoPatchApproved;

import java.util.List;

public interface BookingService {

    BookingDto save(BookingDtoCreate booking, Long userBookerId);

    BookingDtoPatchApproved updateBookingStatus(Long bookingId, boolean approved, Long userOwnerId);

    BookingDtoGet getBookingById(Long bookingId, Long userId);

    List<BookingDtoGet> getBookingsListByUserId(Long userId, String state);

    List<BookingDtoGet> getBookingsListByOwnerUserId(Long ownerId, String state);
}



