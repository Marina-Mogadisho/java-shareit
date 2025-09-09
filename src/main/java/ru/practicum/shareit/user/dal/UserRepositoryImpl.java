package ru.practicum.shareit.user.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        // Проверяем, что email ещё не используется
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ConflictException("Пользователь с таким email уже существует.");
        }
        user.setId(getNextId());
        // сохраняем новую публикацию в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        containsIdByUser(newUser.getId());// Проверка наличия userId в Map
        User userOld = users.get(newUser.getId());
        if (newUser.getName() != null) {
            userOld.setName(newUser.getName());
        }
        if (newUser.getEmail() != null) {
            // Проверяем, что email ещё не используется другими пользователями
            boolean emailExists = users.values().stream()
                    .filter(user -> !user.getId().equals(newUser.getId())) // Исключаем текущего пользователя
                    .anyMatch(user -> user.getEmail().equals(newUser.getEmail()));

            if (emailExists) {
                throw new ConflictException("Email уже используется другим пользователем.");
            }
            userOld.setEmail(newUser.getEmail());
        }
        //Изменения, внесённые в userOld, автоматически отражаются в мапе users,
        // поскольку userOld — это ссылка на объект в коллекции.
        return userOld;
    }

    @Override
    public void deleteUser(Long userId) {
        containsIdByUser(userId);// Проверка наличия userId в Map
        users.remove(userId); // Удаление пользователя из Map
    }

    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public Set<Long> getAllIdUsers() {
        return users.keySet();
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void containsIdByUser(Long userId) {
        if (!users.containsKey(userId)) { // Проверка наличия userId в Map
            throw new ValidationException("Пользователя с указанным id " + userId + " не существует.");
        }
    }
}
