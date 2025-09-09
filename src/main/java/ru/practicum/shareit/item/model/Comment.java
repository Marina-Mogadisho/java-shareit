package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Entity //привязать модель к базе данных — превратить их в сущности.
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "text", nullable = false)
    String text; // содержимое комментария;

    /*
    в базе данных сохраняется именно идентификатор (id) сущности Item,
     а не вся сущность целиком.
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    Item item; // id вещи, на которую пользователь оставляет комментарий

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull(message = "У вещи должен быть владелец.")
    User author; //автор комментария;

    @Column(name = "created_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created; //дата создания комментария
}

