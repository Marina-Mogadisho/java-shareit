package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
public class ItemDtoItemIdAndNameAndOwnerId {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @NotBlank(message = "Название вещи не может быть пустым.")
    @NotNull
    String name;

    @NotBlank(message = "У вещи должен быть указан id владельца .")
    @NotNull
    Long ownerUserId; // id владельца вещи
}
