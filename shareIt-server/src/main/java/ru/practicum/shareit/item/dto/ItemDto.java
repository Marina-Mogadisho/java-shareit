package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
public class ItemDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    String name;

    String description;

    Boolean available; // статус о том, доступна или нет вещь для аренды;

    Long ownerUserId; // id владельца вещи

    Long requestId;
}

/*
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    При десериализации JSON в объект данное поле будет игнорироваться,
     и его значение не будет изменено, даже если в JSON оно присутствует.
     При сериализации объекта в JSON поле будет включено, как обычно.
     */