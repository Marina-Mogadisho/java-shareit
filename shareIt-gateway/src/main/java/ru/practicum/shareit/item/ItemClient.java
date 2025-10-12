package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.Map;

@Slf4j
@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired //Автоматически вводим зависимости — serverUrl и builder.
    //Конструктор инициализирует новый экземпляр класса BookingClient, наследуя при этом параметры и поведение от BaseClient.
    //Spring использует механизм @Value для привязки указанного значения к переменной serverUrl.
    //builder: объект RestTemplateBuilder создаётся Spring-контейнером и
    // внедряется в конструктор вашего класса BookingClient благодаря аннотации @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {

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

    public ResponseEntity<Object> saveItem(Long userId, ItemDtoCreate item) {
        ResponseEntity<Object> ret = post("", userId, item);
        log.info("ResponseEntity:" + ret);
        return ret;
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemDtoUpdate newItem) {
        return patch("/" + itemId, userId, newItem);
    }

    public void delete(Long userId, Long itemId) {
        delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> findById(Long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> findItemsFromUserId(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findItemsBySearch(Long userId, String text) {
        Map<String, Object> params = Map.of("text", text);
        return get("/search?text={text}", userId, params);
    }


    public ResponseEntity<Object> saveComment(Long authorUserId, Long itemId, CommentRequestDto commentRequestDto) {
        return post("/" + itemId + "/comment", authorUserId, commentRequestDto);
    }
}













