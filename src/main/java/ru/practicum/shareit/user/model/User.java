package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity //привязать модель к базе данных — превратить их в сущности.
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Имя не может быть пустым.")
    String name;

    @Column(name = "email", nullable = false)
    @NotBlank(message = "Емейл должен быть указан.")
    @Email(message = "Емейл должен быть указан.")
    String email;
}
