package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserDtoCreate userDtoCreate;
    private UserDtoUpdate userDtoUpdate;
    private UserDto userDtoResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        userDtoCreate = new UserDtoCreate("Пользователь №1", "mail@mail.ru");
        userDtoUpdate = new UserDtoUpdate("Пользователь №1 новый", "mail@mail.ru");
        userDtoResponse = new UserDto(1L, "Пользователь №1 новый", "mail@mail.ru");
    }

    @Test
    void findAll_shouldReturnUsers() throws Exception {
        when(userClient.findAll()).thenReturn(ResponseEntity.ok(List.of(userDtoResponse)));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Пользователь №1 новый"))
                .andExpect(jsonPath("$[0].email").value("mail@mail.ru"));

        verify(userClient, times(1)).findAll();
    }

    @Test
    void save_shouldReturnCreatedUser() throws Exception {
        when(userClient.save(any(UserDtoCreate.class)))
                .thenReturn(ResponseEntity.ok(userDtoResponse));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Пользователь №1 новый"))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"));

        verify(userClient, times(1)).save(any(UserDtoCreate.class));
    }

    @Test
    void update_shouldReturnUpdatedUser() throws Exception {
        long userId = 1L;
        when(userClient.update(eq(userId), any(UserDtoUpdate.class)))
                .thenReturn(ResponseEntity.ok(userDtoResponse));

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Пользователь №1 новый"))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"));

        verify(userClient, times(1)).update(eq(userId), any(UserDtoUpdate.class));
    }

    @Test
    void delete_shouldCallClient() throws Exception {
        long userId = 1L;
        when(userClient.delete(userId)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().is2xxSuccessful());
    }
}