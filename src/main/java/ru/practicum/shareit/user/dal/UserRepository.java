package ru.practicum.shareit.user.dal;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Set;

public interface UserRepository {

    public List<User> getAllUsers();

    public User createUser(User user);

    public User updateUser(User newUser);

    public void deleteUser(Long userId);

    public User getUserById(Long id);

    public Set<Long> getAllIdUsers();
}
