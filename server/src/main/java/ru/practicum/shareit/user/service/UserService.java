package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<UserDtoResponse> findAll();

    UserDto save(UserDtoCreate userDto);

    UserDto update(Long userId, UserDtoUpdate newUserDto);

    void delete(Long userId);

    UserDto findById(Long id);

    Set<Long> getAllIdUsers();
}
