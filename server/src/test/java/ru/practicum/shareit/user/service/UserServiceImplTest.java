package ru.practicum.shareit.user.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data.sql")
class UserServiceImplTest {

    private final UserService userService;

    UserDtoCreate userDto = new UserDtoCreate(
            "Пользователь №6",
            "mail6@mail.ru"
    );

    @Test
    void saveUser() {
        UserDto user = userService.save(userDto);
        UserDto getUser = userService.findById(user.getId());
        assertThat(user, equalTo(getUser));
    }

    @Test
    void update() {
        UserDtoUpdate userUpdate = new UserDtoUpdate("Пользователь №1 новый",
                "mail1new@mail.ru");
        UserDto userNew = userService.update(1L, userUpdate);
        assertThat(userNew.getName(), equalTo("Пользователь №1 новый"));
        assertThat(userNew.getEmail(), equalTo("mail1new@mail.ru"));
    }

    @Test
    void findById_whenUserFound_thenReturnedUser() {
        UserDto userTwo = userService.findById(2L);
        assertThat(userTwo.getName(), equalTo("Пользователь №2"));
        assertThat(userTwo.getEmail(), equalTo("mail2@mail.ru"));
    }

    @Test
    void findById_whenUserNotFound_thenUserNotFoundException() {
        NotFoundException notFoundException =
                assertThrows(NotFoundException.class, () -> userService.findById(150L));
    }

    @Test
    void deleteUser() {
        userService.delete(5L);
        assertThrows(NotFoundException.class, () -> userService.findById(5L));
    }
}
