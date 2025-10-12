package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatusEnum;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
public class BookingDtoCreate {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long booker;
    private BookingStatusEnum status;
}