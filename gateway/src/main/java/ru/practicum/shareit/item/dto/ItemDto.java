package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
public class ItemDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @NotBlank(message = "Название вещи не может быть пустым.")
    @NotNull
    String name;

    String description;

    Boolean available; // статус о том, доступна или нет вещь для аренды;

    @NotBlank(message = "У вещи должен быть владелец.")
    Long ownerUserId; // id владельца вещи

    Long requestId;
}

/*
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    При десериализации JSON в объект данное поле будет игнорироваться,
     и его значение не будет изменено, даже если в JSON оно присутствует.
     При сериализации объекта в JSON поле будет включено, как обычно.
     */