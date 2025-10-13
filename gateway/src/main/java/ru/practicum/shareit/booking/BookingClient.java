package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingFilterEnum;
import ru.practicum.shareit.client.BaseClient;

import java.util.Collections;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired //Автоматически вводим зависимости — serverUrl и builder.
    //Конструктор инициализирует новый экземпляр класса BookingClient, наследуя при этом параметры и поведение от BaseClient.
    //Spring использует механизм @Value для привязки указанного значения к переменной serverUrl.
    //builder: объект RestTemplateBuilder создаётся Spring-контейнером и
    // внедряется в конструктор вашего класса BookingClient благодаря аннотации @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {

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

    public ResponseEntity<Object> getBookings(long userId, BookingFilterEnum state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()

        );
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsByOwnerId(long userId, BookingFilterEnum state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("/owner?state={state}", userId, parameters);

    }

    public ResponseEntity<Object> bookItem(long userId, BookingDtoCreate requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> updateBookingStatus(long userId, Long bookingId, boolean approved) {
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId,
                "approved", approved
        );
        String path = "/" + bookingId + "?approved=" + approved;
        return patch(path, userId, Collections.emptyMap());
    }
}