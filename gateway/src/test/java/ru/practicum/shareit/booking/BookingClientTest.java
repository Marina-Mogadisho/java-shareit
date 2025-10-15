package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingFilterEnum;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BookingClientTest {

    @Mock
    private RestTemplateBuilder builder;

    @Mock
    private RestTemplate restTemplate;

    private BookingClient bookingClient;

    private final String baseUrl = "http://localhost:8080";

    private final long userId = 1L;
    private final Long bookingId = 2L;

    @BeforeEach
    void setUp() throws Exception {
        // Подготовка RestTemplateBuilder для возврата мокнутого RestTemplate
        when(builder.uriTemplateHandler(any(DefaultUriBuilderFactory.class))).thenReturn(builder);
        when(builder.requestFactory((Supplier<ClientHttpRequestFactory>) any())).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);

        bookingClient = new BookingClient(baseUrl, builder);

        // Через рефлексию подменяем поле "rest" из BaseClient на mock RestTemplate
        var restField = BaseClient.class.getDeclaredField("rest");
        restField.setAccessible(true);
        restField.set(bookingClient, restTemplate);
    }

    @Test
    void getBookings_ShouldCallGetWithParams() {
        BookingFilterEnum state = BookingFilterEnum.ALL;
        Map<String, Object> params = Map.of("state", state.name());

        ResponseEntity<Object> expected = ResponseEntity.ok("response");

        when(restTemplate.exchange(
                eq("?state={state}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(params)
        )).thenReturn(expected);

        ResponseEntity<Object> response = bookingClient.getBookings(userId, state);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(restTemplate).exchange(
                eq("?state={state}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(params)
        );
    }

    @Test
    void getBookingsByOwnerId_ShouldCallGetWithParams() {
        BookingFilterEnum state = BookingFilterEnum.ALL;
        Map<String, Object> params = Map.of("state", state.name());

        ResponseEntity<Object> expected = ResponseEntity.ok("responseOwner");

        when(restTemplate.exchange(
                eq("/owner?state={state}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(params)
        )).thenReturn(expected);

        ResponseEntity<Object> response = bookingClient.getBookingsByOwnerId(userId, state);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(restTemplate).exchange(
                eq("/owner?state={state}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(params)
        );
    }

    @Test
    void bookItem_ShouldCallPost() {
        BookingDtoCreate dto = new BookingDtoCreate(
                null, null, 1L, null, null
        );

        ResponseEntity<Object> expected = ResponseEntity.ok("created");

        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expected);

        ResponseEntity<Object> response = bookingClient.bookItem(userId, dto);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(restTemplate).exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void getBooking_ShouldCallGet() {
        ResponseEntity<Object> expected = ResponseEntity.ok("booking");

        when(restTemplate.exchange(
                eq("/" + bookingId),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expected);

        ResponseEntity<Object> response = bookingClient.getBooking(userId, bookingId);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(restTemplate).exchange(
                eq("/" + bookingId),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void updateBookingStatus_ShouldCallPatch() {
        boolean approved = true;
        String path = "/" + bookingId + "?approved=" + approved;

        ResponseEntity<Object> expected = ResponseEntity.ok("patched");

        when(restTemplate.exchange(
                eq(path),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expected);

        ResponseEntity<Object> response = bookingClient.updateBookingStatus(userId, bookingId, approved);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(restTemplate).exchange(
                eq(path),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }
}
