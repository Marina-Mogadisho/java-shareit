package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatusEnum;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
public class BookingDtoCreate {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private User booker;
    private BookingStatusEnum status;
}
