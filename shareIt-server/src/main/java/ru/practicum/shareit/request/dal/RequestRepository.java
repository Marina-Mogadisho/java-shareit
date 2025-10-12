package ru.practicum.shareit.request.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    //Просмотр владельцем списка всех его запросов вещи.
    @Query("select r from Request r left join fetch r.items i left join fetch i.user where r.requestor.id = :userId")
    List<Request> findRequestsFromUserId(@Param("userId") Long userId);

    //Просмотр владельцем списка всех его запросов вещи с указанием данных:
    // описания, даты создания для каждой из них и списка вещей, которые были созданы по конкретному запросу.
    @Query("SELECT r FROM Request r LEFT JOIN FETCH r.items WHERE r.requestor.id = :userRequestor")
    List<Request> findRequestsFromUserIdWithData(Long userRequestor);

    @Query("select r from Request r " +
            "left join fetch r.items i " +
            "left join fetch i.user " +
            "where r.id = :id")
    Optional<Request> findByIdWithItemsAndUsers(@Param("id") Long id);
}
