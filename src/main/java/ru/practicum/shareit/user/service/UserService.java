package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.util.List;
import java.util.Set;

public interface UserService {
    public List<UserDtoResponse> getAllUsers();

    public UserDto createUser(UserDtoCreate userDto);

    public UserDto updateUser(Long userId, UserDtoUpdate newUserDto);

    public void deleteUser(Long userId);

    public UserDto getUserById(Long id);

    public Set<Long> getAllIdUsers();
}
