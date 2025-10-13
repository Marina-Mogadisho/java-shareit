package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.RequestDtoCreate;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired //Автоматически вводим зависимости — serverUrl и builder.
    //Конструктор инициализирует новый экземпляр класса BookingClient, наследуя при этом параметры и поведение от BaseClient.
    //Spring использует механизм @Value для привязки указанного значения к переменной serverUrl.
    //builder: объект RestTemplateBuilder создаётся Spring-контейнером и
    // внедряется в конструктор вашего класса BookingClient благодаря аннотации @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {

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

    public ResponseEntity<Object> save(Long userRequestor, RequestDtoCreate requestDtoCreate) {
        return post("", userRequestor, requestDtoCreate);  // POST для создания
    }

    public ResponseEntity<Object> getRequestsListByOwnerId(Long userRequestor) {
        return get("", userRequestor);  // GET для получения своих запросов
    }

    public ResponseEntity<Object> getRequests(Long userId) {
        return get("/all", userId);  // GET для получения всех запросов
    }

    public ResponseEntity<Object> getRequest(Long requestId, Long userId) {
        return get("/" + requestId, userId);  // GET для получения одного запроса
    }
}
