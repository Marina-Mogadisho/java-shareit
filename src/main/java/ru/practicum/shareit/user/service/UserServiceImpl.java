package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoUpdate;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public List<UserDtoResponse> findAll() {
        List<User> listUsers = userRepository.findAll();
        //Преобразование объектов типа User в объекты типа UserDtoResponse.
        // Это делается с помощью метода toUserDtoResponse, который находится в классе UserMapper
        return listUsers
                .stream()
                .map(UserMapper::toUserDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto save(UserDtoCreate userDtoCreate) {
        User user = new User();

        List<User> users = userRepository.findAll();
        // Проверяем, что email ещё не используется
        if (users.stream().anyMatch(u -> u.getEmail().equals(userDtoCreate.getEmail()))) {
            throw new ConflictException("Пользователь с таким email уже существует.");
        }
        user.setName(userDtoCreate.getName());
        user.setEmail(userDtoCreate.getEmail());

        User createdUser = userRepository.save(user);
        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto update(Long userId, UserDtoUpdate userDto) {
        validationIdByUser(userId);

        // Сначала получаем существующего пользователя из базы данных
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с указанным ID не найден"));

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        List<User> users = userRepository.findAll();
        // Проверяем, что email ещё не используется
        if (users.stream().anyMatch(u -> u.getEmail().equals(userDto.getEmail()))) {
            throw new ConflictException("Пользователь с таким email уже существует.");
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
// Если объект уже существует (, то есть у него уже есть идентификатор, который присутствует в базе данных),
// то метод userRepository.save(user) выполнит обновление данных этого объекта.
        User updatedUser = userRepository.save(user);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void delete(Long userId) {
        validationIdByUser(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));
        userRepository.delete(user);
    }

    @Override
    public UserDto findById(Long userId) {
        validationIdByUser(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public Set<Long> getAllIdUsers() {
        return userRepository.getAllIdUsers();
    }

    public void validationIdByUser(Long userId) {
        if (userId == null || userId < 0) {
            throw new NotFoundException("Id пользователя должно быть указано.");
        }
    }
}