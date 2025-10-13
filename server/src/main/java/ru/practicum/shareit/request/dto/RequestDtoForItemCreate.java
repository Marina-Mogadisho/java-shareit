package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
public class RequestDtoForItemCreate {
    @NotBlank(message = "Описание вещи не может быть пустым")
    String description;
    LocalDateTime created;
}
