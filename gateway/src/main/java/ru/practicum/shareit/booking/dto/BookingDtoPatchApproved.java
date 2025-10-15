package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoIdAndName;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
public class BookingDtoPatchApproved {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatusEnum status;
    private Long bookerId;
    private ItemDtoIdAndName item;
}
