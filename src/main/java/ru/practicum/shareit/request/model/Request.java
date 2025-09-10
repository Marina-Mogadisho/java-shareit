package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

/**
 * Класс, отвечающий за запрос вещи
 */
@Entity //привязать модель к базе данных — превратить их в сущности.
@Table(name = "requests")
@Getter
@Setter
@ToString
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "description", nullable = false)
    String description;

    /*
    в базе данных сохраняется именно идентификатор (id) сущности User,
     а не вся сущность целиком.
     */
    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    User requestor; //id пользователя, создавшего запрос

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        return id != null && id.equals(((Request) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

