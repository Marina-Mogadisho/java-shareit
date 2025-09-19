package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
public class ItemDtoList {

    String name;
    String description;
    LocalDateTime start;
    LocalDateTime end;
}
