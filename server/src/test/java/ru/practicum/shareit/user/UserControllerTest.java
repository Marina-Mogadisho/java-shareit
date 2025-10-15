package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoUpdate;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import(UserControllerTest.TestConfig.class)
class UserControllerTest {


    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserService userService;

    private final UserDtoResponse userDtoResponse = new UserDtoResponse(
            "Пользователь №1",
            "mail@mail.ru"
    );
    private final List<UserDtoResponse> userDtoResponses = List.of(
            new UserDtoResponse("Пользователь №1", "mail1@mail.ru"),
            new UserDtoResponse("Пользователь №2", "mail2@mail.ru")
    );
    private final UserDtoCreate userDtoCreate = new UserDtoCreate(
            "Пользователь №1",
            "mail@mail.ru"
    );
    private final UserDtoUpdate userDtoUpdate = new UserDtoUpdate(
            "Пользователь №1 обновлен",
            "mail@mail.ru"
    );
    private final UserDto userDto = new UserDto(
            1L,
            "Пользователь №1 обновлен",
            "mail@mail.ru"
    );


    /**
     * @Test void save() {
     * }
     */
    @Test
    void createUser() throws Exception {
        when(userService.save(userDtoCreate))
                .thenReturn(userDto);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDtoCreate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    /**
     * @Test void update() {
     * }
     */
    @Test
    void updateUser() throws Exception {
        when(userService.update(1L, userDtoUpdate))
                .thenReturn(userDto);
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDtoUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    /**
     * @Test void delete() {
     * }
     */
    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    /**
     * @Test void findById() {
     * }
     */
    @Test
    void getUser() throws Exception {
        when(userService.findById(1L))
                .thenReturn(userDto);
        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    /**
     * @Test void findAll() {
     * }
     */
    @Test
    void getAllUser() throws Exception {
        when(userService.findAll())
                .thenReturn(userDtoResponses);

        mvc.perform(get("/users")  // предполагаем, что GET /users возвращает всех пользователей
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Проверяем, что возвращается массив с нужным размером
                .andExpect(jsonPath("$.length()", is(userDtoResponses.size())))
                // Проверяем имя первого пользователя
                .andExpect(jsonPath("$[0].name", is(userDtoResponses.get(0).getName())))
                // Проверяем email второго пользователя
                .andExpect(jsonPath("$[1].email", is(userDtoResponses.get(1).getEmail())));
    }
}