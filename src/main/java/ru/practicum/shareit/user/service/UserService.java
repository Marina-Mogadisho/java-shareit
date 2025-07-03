package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.util.List;
import java.util.Set;

public interface UserService {
    public List<UserDtoResponse> getAllUsers();

    public UserDto createUser(UserDtoRequest userDto);

    public UserDto updateUser(Long userId, UserDtoRequestUpdate newUserDto);

    public void deleteUser(Long userId);

    public UserDto getUserById(Long id);

    public Set<Long> getAllIdUsers();
}
