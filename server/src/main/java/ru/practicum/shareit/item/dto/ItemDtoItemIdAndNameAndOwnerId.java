package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
public class ItemDtoItemIdAndNameAndOwnerId {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String name;
    Long ownerUserId; // id владельца вещи
}
