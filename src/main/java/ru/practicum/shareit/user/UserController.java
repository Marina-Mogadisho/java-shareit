package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDtoResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDtoRequest userDto) {
        return userService.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") long userId,
                              @RequestBody @Valid UserDtoRequestUpdate newUserDto) {
        return userService.updateUser(userId, newUserDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") long userId) {
        userService.deleteUser(userId);
    }
}
