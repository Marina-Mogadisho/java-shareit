package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingFilterEnum;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid BookingDtoCreate booking,
                                       @RequestHeader("X-Sharer-User-Id")
                                       @Positive Long userBookerId) {
        log.info("Получен запрос на создание бронирования вещи с параметрами: " +
                "booking {}, userBookerId {}", booking, userBookerId);
        return bookingClient.bookItem(userBookerId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userOwnerId,// id владельца вещи
            @PathVariable @Positive Long bookingId,  // id запроса бронирования
            @RequestParam(value = "approved") boolean approved) { // статус бронирования, при создании запроса

        log.info("Получен запрос владельца вещи на изменение статуса бронирования.");
        return bookingClient.updateBookingStatus(userOwnerId, bookingId, approved);
    }


    @GetMapping("{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable @Positive Long bookingId,
                                                 @RequestHeader("X-Sharer-User-Id") @Positive Long bookerUserId) {
        log.info("Получен запрос на получении данных о бронировании с id: {}", bookingId);
        return bookingClient.getBooking(bookerUserId, bookingId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getBookingsListByUserId(
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        BookingFilterEnum stateValid = BookingFilterEnum.from(state)
                .orElseThrow(() -> new IllegalArgumentException("У бронирований нет фильтрации с категорией: " + state));
        log.info("Получен запрос на получении списка всех бронирований текущего пользователя с id: {}", userId);
        return bookingClient.getBookings(userId, stateValid);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsListByOwnerId(
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        BookingFilterEnum stateValid = BookingFilterEnum.from(state)
                .orElseThrow(() -> new IllegalArgumentException("У бронирований нет фильтрации с категорией: " + state));
        log.info("Получен запрос на получении списка бронирований для всех вещей владельца с id: {}", userId);
        return bookingClient.getBookingsByOwnerId(userId, stateValid);
    }
}
