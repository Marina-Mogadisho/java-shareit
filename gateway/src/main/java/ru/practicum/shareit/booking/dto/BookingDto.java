package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
public class BookingDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @FutureOrPresent(message = "Дата и время начала бронирования должна быть позже или равна текущей дате")
    private LocalDateTime start;
    @Future(message = "Дата и время окончания бронирования должна быть позже текущей даты")
    private LocalDateTime end;
    @NotNull(message = "Необходимо указать идентификатор вещи")
    @Positive(message = "Идентификатор вещи должен быть больше нуля")
    private Long itemId;
    @Positive
    private Long bookerId;
    private BookingStatusEnum status;
}
