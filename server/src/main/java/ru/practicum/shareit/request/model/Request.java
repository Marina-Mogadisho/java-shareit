package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс, отвечающий за запрос вещи
 */
@Entity //привязать модель к базе данных — превратить их в сущности.
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    /*
    в базе данных сохраняется именно идентификатор (id) сущности User,
     а не вся сущность целиком.
     */
    @ManyToOne
    @JoinColumn(name = "requestor", nullable = false)
    private User requestor; //id пользователя, создавшего запрос

    @Column(name = "created_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created; //дата создания комментария

    /*
    Для каждого запроса может быть указан ответ, если он есть,
     в виде списка в формате: id вещи, название, id владельца.
     JPA сохранит объект request в БД, записав в таблицу соответствующие поля (id, description, requestor и created).
     Коллекция items не затрагивается, так как она не владельческая сторона (владелец — Item через поле request).
     Пока вы не создадите и не сохраните Item с указанием этого request, связанной записи для items в БД нет.
     */
    @OneToMany(mappedBy = "request")
    private List<Item> items = new ArrayList<>(); // Связь с Item — много вещей к одному запросу

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o)
                .getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Request request = (Request) o;
        return getId() != null && Objects.equals(getId(), request.getId());
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

