package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BaseClientTest {

    static class TestBaseClient extends BaseClient {
        public TestBaseClient(RestTemplate rest) {
            super(rest);
        }
    }

    @Mock
    RestTemplate rest;

    @InjectMocks
    TestBaseClient baseClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void put_withoutParameters_callsRestExchangeWithoutParams() {
        String path = "/test";
        long userId = 1L;
        String body = "body";

        ResponseEntity<Object> responseEntity = ResponseEntity.ok("response");

        // Настраиваем мок RestTemplate на вызов без параметров
        when(rest.exchange(eq(path), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = baseClient.put(path, userId, body);

        assertEquals(responseEntity, response);
        verify(rest).exchange(eq(path), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Object.class));
    }

    @Test
    void put_withParameters_callsRestExchangeWithParams() {
        String path = "/test/{id}";
        long userId = 2L;
        String body = "body";
        Map<String, Object> params = Map.of("id", 123);

        ResponseEntity<Object> responseEntity = ResponseEntity.ok("resp");

        when(rest.exchange(eq(path), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Object.class), eq(params)))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = baseClient.put(path, userId, params, body);

        assertEquals(responseEntity, response);
        verify(rest).exchange(eq(path), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Object.class), eq(params));
    }

    @Test
    void patch_body_callsRestExchangeWithoutParams() {
        String path = "/patchtest";
        String body = "patch body";

        ResponseEntity<Object> responseEntity = ResponseEntity.ok("patch response");

        when(rest.exchange(eq(path), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = baseClient.patch(path, body);

        assertEquals(responseEntity, response);
        verify(rest).exchange(eq(path), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class));
    }

    @Test
    void patch_userId_callsRestExchangeWithoutParams() {
        String path = "/patchtest2";
        long userId = 5L;
        // реального body нет, поэтому body = null

        ResponseEntity<Object> responseEntity = ResponseEntity.ok("patchresp");

        when(rest.exchange(eq(path), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = baseClient.patch(path, userId);

        assertEquals(responseEntity, response);
        verify(rest).exchange(eq(path), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class));
    }

    @Test
    void makeAndSendRequest_whenHttpStatusCodeException_returnsResponseEntityWithError() {
        String path = "/error";
        long userId = 1L;
        String body = "body";
        HttpStatusCodeException httpException = mock(HttpStatusCodeException.class);
        when(httpException.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(httpException.getResponseBodyAsByteArray()).thenReturn("error body".getBytes());

        // сетап мока rest.exchange -> кинет исключение
        when(rest.exchange(eq(path), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Object.class)))
                .thenThrow(httpException);

        ResponseEntity<Object> response = baseClient.put(path, userId, body);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertArrayEquals("error body".getBytes(), (byte[]) response.getBody());
    }
}
