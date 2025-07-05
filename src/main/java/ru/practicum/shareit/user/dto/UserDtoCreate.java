package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDtoCreate {
    @NotBlank(message = "Имя не может быть пустым.")
    String name;

    @NotBlank(message = "Емейл не может быть пустым.")
    @Email(message = "Емейл должен быть указан.")
    String email;
}
