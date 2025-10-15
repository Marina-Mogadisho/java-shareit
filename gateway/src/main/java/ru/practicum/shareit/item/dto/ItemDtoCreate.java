package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
public class ItemDtoCreate {

    @NotBlank(message = "Название вещи не может быть пустым.")
    @NotNull
    String name;

    @NotNull
    String description;

    @NotNull
    Boolean available; // статус о том, доступна или нет вещь для аренды;

    Long requestId;
}

