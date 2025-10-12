package ru.practicum.shareit.user.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.id FROM User u")
    Set<Long> getAllIdUsers();
}