package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

/**
 * Класс, отвечающий за запрос вещи
 */
@Entity //привязать модель к базе данных — превратить их в сущности.
@Table(name = "requests")
@Data
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Текст запроса не может быть пустым.")
    @Column(name = "description", nullable = false)
    String description;

    /*
    в базе данных сохраняется именно идентификатор (id) сущности User,
     а не вся сущность целиком.
     */
    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    @NotNull(message = "У запроса на аренду вещи должен быть владелец.")
    User requestor; //id пользователя, создавшего запрос
}

