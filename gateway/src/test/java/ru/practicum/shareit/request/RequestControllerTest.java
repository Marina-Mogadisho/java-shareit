package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestDtoCreate;
import ru.practicum.shareit.request.dto.RequestResponseDtoCreated;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @SuppressWarnings("deprecation")
    @MockBean
    private RequestClient requestClient;

    @Autowired
    private ObjectMapper mapper;

    private RequestDtoCreate requestDtoCreate;
    private RequestResponseDtoCreated responseDto;

    @BeforeEach
    void setUp() {
        responseDto = new RequestResponseDtoCreated(1L, "Нужна вещь №1", 1L, LocalDateTime.now());
        requestDtoCreate = new RequestDtoCreate("Нужна вещь №1");

    }

    @Test
    void saveRequest() throws Exception {
        when(requestClient.save(1L, requestDtoCreate)).thenReturn(ResponseEntity.ok(responseDto));

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDtoCreate))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(requestDtoCreate.getDescription())));
    }


    @Test
    void getRequestsListByOwnerId() throws Exception {
        when(requestClient.getRequestsListByOwnerId(1L))
                .thenReturn(ResponseEntity.ok(List.of(responseDto)));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(responseDto.getDescription())));
    }

    @Test
    void getRequests() throws Exception {
        when(requestClient.getRequest(1L, 1L))
                .thenReturn(ResponseEntity.ok(List.of(responseDto)));
        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getRequest() throws Exception {
        when(requestClient.getRequest(1L, 1L))
                .thenReturn(ResponseEntity.ok(responseDto));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}