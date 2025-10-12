package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Бронирование вещи.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity //привязать модель к базе данных — превратить их в сущности.
@Table(name = "bookings")
public class Booking {
    @Id
    //Благодаря @GeneratedValue(strategy = GenerationType.IDENTITY)
    // при сохранении объекта Booking в базе данных, система сама присвоит ему уникальный идентификатор id.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;

    //В базе данных сохраняется именно идентификатор (id) сущности Item, а не вся сущность целиком.
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item; // id вещи, которую пользователь бронирует;

    /*
   в базе данных сохраняется именно идентификатор (id) сущности User,
    а не вся сущность целиком.
 */
    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker; // id арендатора, пользователя, который осуществляет бронирование

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum status; //Cтатус бронирования


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o)
                .getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Booking booking = (Booking) o;
        return getId() != null && Objects.equals(getId(), booking.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this)
                .getHibernateLazyInitializer()
                .getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
