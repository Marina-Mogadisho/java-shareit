package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatusEnum;
import ru.practicum.shareit.item.dto.ItemDtoIdAndName;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
public class BookingDtoPatchApproved {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatusEnum status;
    private User booker;
    private ItemDtoIdAndName item;
}
