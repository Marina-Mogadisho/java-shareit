package ru.practicum.shareit.booking.dto;


import java.util.Optional;

/**
 * Значения, которые описывают категории для фильтрации бронирований:
 * Необходимо разделять логику фильтрации (BookingFilterEnum) и состояния бронирований BookingStatusEnum.
 */
public enum BookingFilterEnum {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static Optional<BookingFilterEnum> from(String stringState) {
        for (BookingFilterEnum state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}

/*
ALL (англ. «все»).
CURRENT (англ. «текущие»),
PAST (англ. «завершённые»),
FUTURE (англ. «будущие»),
WAITING (англ. «ожидающие подтверждения»),
REJECTED (англ. «отклонённые»)
 */