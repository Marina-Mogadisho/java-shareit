package ru.practicum.shareit.booking.model;

public enum BookingStatusEnum {
    WAITING, APPROVED, REJECTED, CANCELED
}

/*
WAITING — новое бронирование, ожидает одобрения,
APPROVED — бронирование подтверждено владельцем,
REJECTED — бронирование отклонено владельцем,
CANCELED — бронирование отменено создателем
 */