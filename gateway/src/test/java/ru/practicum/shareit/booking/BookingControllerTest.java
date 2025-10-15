package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingFilterEnum;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Slf4j
class BookingControllerTest {

    private BookingClient bookingClient;
    private BookingController bookingController;

    private final Long userBookerId = 10L;
    private final Long userOwnerId = 11L;
    private final Long bookingId = 100L;

    @BeforeEach
    void setUp() {
        bookingClient = mock(BookingClient.class);
        bookingController = new BookingController(bookingClient);
    }

    @Test
    void save_ShouldReturnResponseEntity() {
        BookingDtoCreate bookingDto = BookingDtoCreate.builder()
                .start(LocalDateTime.of(2025, 10, 1, 10, 0))
                .end(LocalDateTime.of(2025, 10, 12, 10, 0))
                .itemId(1L)
                .build();

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("created");

        when(bookingClient.bookItem(eq(userBookerId), eq(bookingDto))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.save(bookingDto, userBookerId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response);
        verify(bookingClient).bookItem(userBookerId, bookingDto);
    }

    @Test
    void updateBookingStatus_ShouldReturnResponseEntity() {
        boolean approved = true;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("updated");

        when(bookingClient.updateBookingStatus(eq(userOwnerId), eq(bookingId), eq(approved))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.updateBookingStatus(userOwnerId, bookingId, approved);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response);
        verify(bookingClient).updateBookingStatus(userOwnerId, bookingId, approved);
    }

    @Test
    void getBookingById_ShouldReturnResponseEntity() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("booking data");

        when(bookingClient.getBooking(eq(userBookerId), eq(bookingId))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getBookingById(bookingId, userBookerId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response);
        verify(bookingClient).getBooking(userBookerId, bookingId);
    }

    @Test
    void getBookingsListByUserId_WithValidState_ShouldReturnResponseEntity() {
        String state = "ALL";
        BookingFilterEnum filter = BookingFilterEnum.from(state).orElse(null);
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("user bookings");

        when(bookingClient.getBookings(eq(userBookerId), eq(filter))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getBookingsListByUserId(state, userBookerId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response);
        verify(bookingClient).getBookings(userBookerId, filter);
    }

    @Test
    void getBookingsListByUserId_WithInvalidState_ShouldThrowException() {
        String invalidState = "INVALID";
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                bookingController.getBookingsListByUserId(invalidState, userBookerId));

        assertTrue(thrown.getMessage().contains(invalidState));
    }

    @Test
    void getBookingsListByOwnerId_WithValidState_ShouldReturnResponseEntity() {
        String state = "ALL";
        BookingFilterEnum filter = BookingFilterEnum.from(state).orElse(null);
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("owner bookings");

        when(bookingClient.getBookingsByOwnerId(eq(userOwnerId), eq(filter))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getBookingsListByOwnerId(state, userOwnerId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response);
        verify(bookingClient).getBookingsByOwnerId(userOwnerId, filter);
    }

    @Test
    void getBookingsListByOwnerId_WithInvalidState_ShouldThrowException() {
        String invalidState = "INVALID";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                bookingController.getBookingsListByOwnerId(invalidState, userOwnerId));

        assertTrue(thrown.getMessage().contains(invalidState));
    }
}
