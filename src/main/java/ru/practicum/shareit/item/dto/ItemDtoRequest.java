package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
public class ItemDtoRequest {
    @NotBlank(message = "Название вещи не может быть пустым.")
    String name;
    String description;
    Boolean available; // статус о том, доступна или нет вещь для аренды;
}

