package ru.practicum.shareit.booking.dto;

import java.util.Optional;

/**
 * Состояние бронирования
 * Необходимо разделять логику фильтрации (BookingFilterEnum) и состояния бронирований BookingStatusEnum.
 */
public enum BookingStatusEnum {
    WAITING, APPROVED, REJECTED, CANCELED;

    public static Optional<BookingStatusEnum> from(String stringState) {
        for (BookingStatusEnum state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}

/*
WAITING — новое бронирование, ожидает одобрения,
APPROVED — бронирование подтверждено владельцем,
REJECTED — бронирование отклонено владельцем,
CANCELED — бронирование отменено создателем
 */



