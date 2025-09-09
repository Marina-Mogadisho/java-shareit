package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    private final UserRepository userRepository;

    @Override
    public List<UserDtoResponse> getAllUsers() {
        List<User> listUsers = userRepository.getAllUsers();
        //Преобразование объектов типа User в объекты типа UserDtoResponse.
        // Это делается с помощью метода toUserDtoResponse, который находится в классе UserMapper
        return listUsers
                .stream()
                .map(UserMapper::toUserDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDtoCreate userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        User createdUser = userRepository.createUser(user);
        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDtoUpdate userDto) {
        validationIdByUser(userId);
        User user = new User();
        user.setId(userId);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        User updatedUser = userRepository.updateUser(user);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        validationIdByUser(userId);
        userRepository.deleteUser(userId);
    }

    @Override
    public UserDto getUserById(Long userId) {
        validationIdByUser(userId);
        User user = userRepository.getUserById(userId);
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
