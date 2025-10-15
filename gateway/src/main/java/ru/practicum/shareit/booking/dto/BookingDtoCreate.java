package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
@Builder
public class BookingDtoCreate {
    @FutureOrPresent(message = "Дата и время начала бронирования должна быть позже или равна текущей дате")
    private LocalDateTime start;
    @Future(message = "Дата и время окончания бронирования должна быть позже текущей даты")
    private LocalDateTime end;
    @NotNull(message = "Необходимо указать идентификатор вещи")
    @Positive(message = "Идентификатор вещи должен быть больше нуля")
    private Long itemId;
    @Positive
    private Long bookerUserId;
    private BookingStatusEnum status;
}
