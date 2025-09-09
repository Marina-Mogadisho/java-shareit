package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Бронирование вещи.
 */
@Data
@Entity //привязать модель к базе данных — превратить их в сущности.
@Table(name = "bookings")
public class Booking {
    @Id
    //Благодаря @GeneratedValue(strategy = GenerationType.IDENTITY)
    // при сохранении объекта Booking в базе данных, система сама присвоит ему уникальный идентификатор id.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "start_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;


    /*
    в базе данных сохраняется именно идентификатор (id) сущности Item,
     а не вся сущность целиком.
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    Item item; // id вещи, которую пользователь бронирует;

    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    User booker; // id арендатора, пользователя, который осуществляет бронирование

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    BookingStatusEnum status; //Cтатус бронирования
}
