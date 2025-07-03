package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class User {
    Long id;

    @NotBlank(message = "Имя не может быть пустым.")
    String name;

    @NotBlank(message = "Емейл должен быть указан.")
    @Email(message = "Емейл должен быть указан.")
    String email;
}
