package ru.practicum.shareit.client;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<Object> get(String path) {
        return get(path, null, null);
    }

    protected ResponseEntity<Object> get(String path, long userId) {
        return get(path, userId, null);
    }

    protected ResponseEntity<Object> get(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, long userId, T body) {
        return post(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, Long userId, @Nullable Map<String, Object> parameters,
                                              T body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
    }

    protected <T> ResponseEntity<Object> put(String path, long userId, T body) {
        return put(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> put(String path, long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PUT, path, userId, parameters, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, T body) {
        return patch(path, null, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId) {
        return patch(path, userId, null, null);
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId, T body) {
        return patch(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, userId, parameters, body);
    }

    protected ResponseEntity<Object> delete(String path) {
        return delete(path, null, null);
    }

    protected ResponseEntity<Object> delete(String path, long userId) {
        return delete(path, userId, null);
    }

    protected ResponseEntity<Object> delete(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.DELETE, path, userId, parameters, null);
    }

    /**
     * Метод makeAndSendRequest предназначен для отправки HTTP-запросов и обработки ответов от сервера
     *
     * @param method     -HttpMethod method: метод HTTP (например, GET, POST, PUT и т. д.).
     * @param path       -String path: путь к ресурсу на сервере.
     * @param userId     -Long userId: идентификатор пользователя, который используется для настройки заголовков запроса.
     * @param parameters - параметры запроса, которые могут быть переданы в URL
     * @param body       - @Nullable T body: тело запроса
     * @param <T>
     * @return
     */
    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long userId,
                                                          @Nullable Map<String, Object> parameters, @Nullable T body) {
        //Создаётся объект HttpEntity<T> с телом запроса и заголовками, которые возвращаются методом defaultHeaders(userId)
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));

        ResponseEntity<Object> shareitServerResponse;
        /*
        Отправка запроса. Используется метод rest.exchange для отправки запроса на сервер.
        Если параметры parameters не равны null, они передаются в метод exchange.
         */
        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
            /*
            В случае возникновения исключения HttpStatusCodeException,
            метод возвращает ответ с кодом состояния и телом ошибки.
             */
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        /*
        Ответ сервера обрабатывается методом prepareGatewayResponse,
        который проверяет статус кода ответа и при необходимости подготавливает тело ответа.
         */
        return prepareGatewayResponse(shareitServerResponse);
    }

    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }

    /**
     * Метод prepareGatewayResponse проверяет, является ли статус ответа успешным (код 2xx),
     * и в зависимости от этого возвращает ответ или строит новый ответ с соответствующим статусом и телом.
     *
     * @param response -объект ответа, который был получен от сервера.
     * @return
     */
    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        /*
        Метод проверяет, является ли статус ответа успешным (код 2xx).
        Если да, то метод просто возвращает полученный ответ response.
         */
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
//Подготовка нового ответа при ошибке:
        //создаётся новый объект ResponseEntity.BodyBuilder с текущим статусом ответа (код ошибки).
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
/*
Затем проверяется, есть ли у ответа тело (response.hasBody()).
Если есть, то создаётся новый ответ, но с тем же статусом и телом, что и исходный, начальный ответ response.
 */
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
//Если тела нет, возвращается ответ, созданный только с использованием статуса ошибки.
        return responseBuilder.build();
    }
}
