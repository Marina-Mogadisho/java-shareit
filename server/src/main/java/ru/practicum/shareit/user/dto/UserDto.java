package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String name;
    String email;
}
/*
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    При десериализации JSON в объект данное поле будет игнорироваться,
     и его значение не будет изменено, даже если в JSON оно присутствует.
     При сериализации объекта в JSON поле будет включено, как обычно.
     */