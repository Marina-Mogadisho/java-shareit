package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

/**
 * Бронирование вещи.
 */
@Data
public class Booking {
    Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    LocalDate dateStart; //дата и время начала бронирования;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    LocalDate dateEnd; //дата и время окончания бронирования;

    Long itemId; // id вещи, которую пользователь бронирует;

    Long userIdBooker; // id арендатора, пользователя, который осуществляет бронирование

    BookingStatusEnum status; //Cтатус бронирования
}
