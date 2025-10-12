package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
public class ItemDtoResponse {
    String name;
    String description;
    Boolean available; // статус о том, доступна или нет вещь для аренды;
}
