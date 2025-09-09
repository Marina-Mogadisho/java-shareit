package ru.practicum.shareit.booking.model;


/*
Значения, которые описывают категории для фильтрации бронирований:
Необходимо разделять логику фильтрации (BookingFilterEnum) и состояния бронирований BookingStatusEnum.
 */
public enum BookingFilterEnum {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
}

/*
ALL (англ. «все»).
CURRENT (англ. «текущие»),
PAST (англ. «завершённые»),
FUTURE (англ. «будущие»),
WAITING (англ. «ожидающие подтверждения»),
REJECTED (англ. «отклонённые»)
 */