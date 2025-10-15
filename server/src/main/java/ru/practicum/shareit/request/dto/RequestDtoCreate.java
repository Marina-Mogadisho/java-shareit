package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
public class RequestDtoCreate {
    private String description;
    //private Long userRequestor; //id пользователя, создавшего запрос
}

    /*
    в базе данных сохраняется именно идентификатор (id) сущности User,
     а не вся сущность целиком.
     */

