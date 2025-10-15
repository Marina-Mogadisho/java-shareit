package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import java.util.List;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserClientTest {

    @Mock
    private RestTemplateBuilder builder;
    @Mock
    private RestTemplate restTemplate;
    private UserClient userClient;

    private final UserDtoResponse userDtoResponse = new UserDtoResponse(
            "Пользователь №1",
            "mail@mail.ru"
    );

    private final UserDto userDto = new UserDto(
            1L,
            "Пользователь №1",
            "mail@mail.ru"
    );
    private final UserDtoCreate userDtoCreate = new UserDtoCreate(
            "Пользователь №1",
            "mail@mail.ru"
    );
    private final UserDtoUpdate userDtoUpdate = new UserDtoUpdate(
            "Пользователь №1 обновлен",
            "mail@mail.ru"
    );

    @BeforeEach
    void setUp() {
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);
        userClient = new UserClient("", builder);
    }

    @Test
    void findAll() {
        Mockito
                .when(restTemplate.exchange("", HttpMethod.GET, new HttpEntity<>(null,
                        getHeaders(null)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = userClient.findAll();
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));

    }

    @Test
    void getUser() {
        Mockito
                .when(restTemplate.exchange("/1", HttpMethod.GET, new HttpEntity<>(null,
                        getHeaders(null)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = userClient.findById(1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }


    @Test
    void saveUser() {
        Mockito
                .when(restTemplate.exchange("", HttpMethod.POST, new HttpEntity<>(userDtoCreate,
                        getHeaders(null)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = userClient.save(userDtoCreate);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void updateUser() {
        Mockito
                .when(restTemplate.exchange("/1", HttpMethod.PATCH, new HttpEntity<>(userDtoUpdate,
                        getHeaders(null)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = userClient.update(1L, userDtoUpdate);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void deleteUser() {
        Mockito
                .when(restTemplate.exchange("/1", HttpMethod.DELETE, new HttpEntity<>(null,
                        getHeaders(null)), Object.class))
                .thenReturn(ResponseEntity.ok().build());
        ResponseEntity<Object> response = userClient.delete(1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    private HttpHeaders getHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null)
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        return headers;
    }
}