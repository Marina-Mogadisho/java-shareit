package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * Класс, отвечающий за запрос вещи
 */
@Data
public class ItemRequest {
    Long id;

    @NotBlank(message = "Текст запроса не может быть пустым.")
    String description;

    @NotBlank(message = "У запроса на аренду вещи должен быть владелец.")
    Long requestorUserId; //id пользователя, создавшего запрос

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    LocalDate requestCreationDate;  //дата и время создания запроса
}
