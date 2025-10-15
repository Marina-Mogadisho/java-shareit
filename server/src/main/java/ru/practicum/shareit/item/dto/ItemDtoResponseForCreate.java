package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.RequestDtoForItemCreate;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
public class ItemDtoResponseForCreate {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    String name;

    String description;

    Boolean available; // статус о том, доступна или нет вещь для аренды;

    Long ownerUserId; // id владельца вещи

    RequestDtoForItemCreate requestDtoForItemCreate;
}