package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
public class ResponseDtoCreated {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    private String description;
    private Long userRequestor; //id пользователя, создавшего запрос
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime created; //дата создания комментария
}
