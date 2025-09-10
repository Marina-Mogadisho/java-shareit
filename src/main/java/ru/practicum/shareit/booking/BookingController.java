package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.dto.BookingDtoPatchApproved;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * POST /bookings
     */
    @PostMapping
    public BookingDto save(@RequestBody @Valid BookingDtoCreate booking,
                           @RequestHeader("X-Sharer-User-Id") Long userBookerId) {
        log.info("Получен запрос на создание бронирования вещи с параметрами: {}", booking);
        return bookingService.save(booking, userBookerId);
    }

    /**
     * PATCH /bookings/{bookingId}?approved={approved}
     *
     * @param approved может принимать значения true или false.
     *                 пример: PATCH /bookings/123?approved=true
     */
    @PatchMapping("/{bookingId}")
    public BookingDtoPatchApproved updateBookingStatus(@PathVariable Long bookingId,  // id запроса бронирования
                                                       @RequestParam(value = "approved") boolean approved, // статус бронирования, при создании запроса
                                                       @RequestHeader("X-Sharer-User-Id") Long userOwnerId) { // id владельца вещи
        log.info("Получен запрос владельца вещи на изменение статуса бронирования.");
        return bookingService.updateBookingStatus(bookingId, approved, userOwnerId);
    }

    /**
     * GET /bookings/{bookingId}.
     * Получение данных о конкретном бронировании
     * Может быть выполнено либо автором бронирования, либо владельцем вещи
     *
     * @param bookerUserId id автора запроса на получение данных
     * @return BookingDtoGet
     */
    @GetMapping("{bookingId}")
    public BookingDtoGet getBookingById(@PathVariable Long bookingId,
                                        @RequestHeader("X-Sharer-User-Id") Long bookerUserId) {
        log.info("Получен запрос на получении данных о бронировании с id: {}", bookingId);
        return bookingService.getBookingById(bookingId, bookerUserId);
    }


    /**
     * GET /bookings?state={state}.
     * Получение списка всех бронирований текущего пользователя (букера, арендатора).
     * Фильтрация в соответствии с параметром state
     *
     * @param state  Фильтрация в соответствии с параметром state:
     *               ALL (англ. «все»).
     *               CURRENT (англ. «текущие»),
     *               PAST (англ. «завершённые»),
     *               FUTURE (англ. «будущие»),
     *               WAITING (англ. «ожидающие подтверждения»),
     *               REJECTED (англ. «отклонённые»)
     * @param userId (букер, арендатор)
     * @return list<BookingDtoGet>
     */
    @GetMapping
    public List<BookingDtoGet> getBookingsListByUserId(
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получении списка всех бронирований текущего пользователя с id: {}", userId);
        return bookingService.getBookingsListByUserId(userId, state);
    }

    /**
     * GET /bookings/owner?state={state}
     * предназначен для получения списка бронирований для всех вещей, принадлежащих владельцу вещи.
     * Фильтрация в соответствии с параметром state
     *
     * @param userId - владелец вещи
     */
    @GetMapping("/owner")
    public List<BookingDtoGet> getBookingsListByOwnerId(
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получении списка бронирований для всех вещей владельца с id: {}", userId);
        return bookingService.getBookingsListByOwnerUserId(userId, state);
    }
}
