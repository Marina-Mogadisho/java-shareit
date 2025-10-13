package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired //Автоматически вводим зависимости — serverUrl и builder.
    //Конструктор инициализирует новый экземпляр класса BookingClient, наследуя при этом параметры и поведение от BaseClient.
    //Spring использует механизм @Value для привязки указанного значения к переменной serverUrl.
    //builder: объект RestTemplateBuilder создаётся Spring-контейнером и
    // внедряется в конструктор вашего класса BookingClient благодаря аннотации @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {

        // создаём экземпляр RestTemplate, который используется для выполнения HTTP-запросов в Spring
        super(
                builder
                        //настраивает обработчик URI для RestTemplate.
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        //настраивается фабрика запросов, которая создаёт HttpComponentsClientHttpRequest для выполнения HTTP-запросов
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        //метод завершает настройку и создаёт готовый к использованию экземпляр RestTemplate с заданными настройками.
                        .build()
        );
    }

    public ResponseEntity<Object> findAll() {
        return get("");
    }

    public ResponseEntity<Object> findById(Long id) {
        return get("/" + id);
    }


    public ResponseEntity<Object> save(UserDtoCreate userDto) {
        return post("", userDto);
    }

    public ResponseEntity<Object> update(Long userId, UserDtoUpdate newUserDto) {
        return patch("/" + userId, newUserDto);
    }

    public  ResponseEntity<Object> delete(Long userId) {
        return  delete("/" + userId);
    }

}

