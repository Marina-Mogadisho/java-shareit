package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoCreate;
import ru.practicum.shareit.request.dto.RequestResponseDtoCreated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestClientTest {
    @Mock
    private RestTemplateBuilder builder;
    @Mock
    private RestTemplate restTemplate;
    private RequestClient requestClient;

    private RequestDtoCreate requestDtoCreate = new RequestDtoCreate("Нужна вещь №1");
    private RequestResponseDtoCreated responseDto = new RequestResponseDtoCreated(
            1L, "Нужна вещь №1", 1L, LocalDateTime.now());
    private RequestDto requestDto = new RequestDto("Нужна вещь №1", LocalDateTime.now(), null);


    @BeforeEach
    void setUp() {
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);
        requestClient = new RequestClient("", builder);
    }

    @Test
    void saveRequest() {
        Mockito
                .when(restTemplate.exchange("", HttpMethod.POST, new HttpEntity<>(requestDtoCreate,
                        getHeaders(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(responseDto));
        ResponseEntity<Object> response = requestClient.save(1L, requestDtoCreate);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(responseDto));
    }


    @Test
    void getRequestsListByOwnerId() {
        List<RequestDto> requests = List.of(requestDto);
        Mockito
                .when(restTemplate.exchange("", HttpMethod.GET, new HttpEntity<>(null,
                        getHeaders(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(requests));
        ResponseEntity<Object> response = requestClient.getRequestsListByOwnerId(1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(requests));
    }

    @Test
    void getRequests() {
        Mockito.when(restTemplate.exchange(
                        eq("/all"),
                        eq(HttpMethod.GET),
                        any(HttpEntity.class),
                        eq(Object.class)))
                .thenReturn(ResponseEntity.ok(responseDto));

        ResponseEntity<Object> response = requestClient.getRequests(1L);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(responseDto));
    }

    @Test
    void getRequest() {
        Mockito
                .when(restTemplate.exchange("/1", HttpMethod.GET, new HttpEntity<>(null,
                        getHeaders(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(requestDtoCreate));
        ResponseEntity<Object> response = requestClient.getRequest(1L, 1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(requestDtoCreate));
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