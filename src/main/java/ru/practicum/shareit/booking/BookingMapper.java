package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.dto.BookingDtoPatchApproved;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoIdAndName;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static BookingDtoCreate toBookingDtoCreate(Booking booking) {
        return new BookingDtoCreate(
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static BookingDtoGet toBookingDtoGet(Booking booking) {
        return new BookingDtoGet(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static BookingDtoPatchApproved toBookingDtoPatch(Booking booking) {
        ItemDtoIdAndName item = new ItemDtoIdAndName(booking.getItem().getId(), booking.getItem().getName());
        return new BookingDtoPatchApproved(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker(),
                item
        );
    }
}
