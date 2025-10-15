package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.dto.BookingDtoPatchApproved;
import ru.practicum.shareit.booking.model.BookingStatusEnum;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@SuppressWarnings("deprecation") // подавляем предупреждение на весь класс
class BookingControllerTest {


    @MockBean
    private BookingService bookingService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;


    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);


    @Test
    void save_shouldReturnBookingDto_whenValidRequest() throws Exception {
        BookingDtoCreate createDto = new BookingDtoCreate();
        createDto.setItemId(1L);
        createDto.setStart(LocalDateTime.of(2025, 10, 10, 10, 0));
        createDto.setEnd(LocalDateTime.of(2025, 10, 12, 10, 0));


        BookingDto responseDto = new BookingDto();
        responseDto.setId(1L);

        Item item = new Item();
        item.setId(1L);
        responseDto.setItem(item);

        User user = new User();
        user.setId(2L);
        responseDto.setBooker(user);

        responseDto.setStatus(BookingStatusEnum.WAITING);

        when(bookingService.save(any(), eq(2L))).thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2L)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.item.id").value(responseDto.getItem().getId()))
                .andExpect(jsonPath("$.booker.id").value(responseDto.getBooker().getId()))
                .andExpect(jsonPath("$.status").value("WAITING"));

    }

    @Test
    void updateBookingStatus_shouldReturnUpdatedStatus() throws Exception {
        BookingDtoPatchApproved patchDto = new BookingDtoPatchApproved();
        patchDto.setStatus(BookingStatusEnum.APPROVED);

        when(bookingService.updateBookingStatus(1L, true, 1L)).thenReturn(patchDto);

        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBookingById_shouldReturnBooking() throws Exception {
        BookingDtoGet dtoGet = new BookingDtoGet();
        dtoGet.setId(1L);
        Item item = new Item();
        item.setId(1L);
        dtoGet.setItem(item);

        when(bookingService.getBookingById(1L, 1L)).thenReturn(dtoGet);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dtoGet.getId()))
                .andExpect(jsonPath("$.item.id").value(dtoGet.getItem().getId()));
    }

    @Test
    void getBookingsListByUserId_shouldReturnList() throws Exception {
        BookingDtoGet dtoGet = new BookingDtoGet();
        dtoGet.setId(1L);

        when(bookingService.getBookingsListByUserId(1L, "ALL")).thenReturn(List.of(dtoGet));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(dtoGet.getId()));
    }

    @Test
    void getBookingsListByOwnerId_shouldReturnList() throws Exception {
        BookingDtoGet dtoGet = new BookingDtoGet();
        dtoGet.setId(2L);

        when(bookingService.getBookingsListByOwnerUserId(1L, "ALL")).thenReturn(List.of(dtoGet));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(dtoGet.getId()));
    }
}
