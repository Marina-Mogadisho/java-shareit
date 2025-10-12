package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoCreate;
import ru.practicum.shareit.request.dto.RequestDtoForGetRequest;
import ru.practicum.shareit.request.dto.ResponseDtoCreated;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = RequestController.class)
@Import(RequestControllerTest.TestConfig.class)
class RequestControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RequestService requestService() {
            return Mockito.mock(RequestService.class);
        }
    }

    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RequestService requestService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void save_shouldReturnCreatedResponseDto() throws Exception {
        Long userId = 1L;
        RequestDtoCreate dtoCreate = new RequestDtoCreate();
        dtoCreate.setDescription("Нужна вещь");

        ResponseDtoCreated responseDtoCreated = new ResponseDtoCreated();

        when(requestService.save(eq(userId), any(RequestDtoCreate.class)))
                .thenReturn(responseDtoCreated);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDtoCreated.getId()));

        verify(requestService, times(1)).save(eq(userId), any(RequestDtoCreate.class));
    }

    @Test
    void getRequestsListByOwnerId_shouldReturnListOfRequestDto() throws Exception {
        Long userId = 1L;

        List<RequestDto> list = List.of(
                new RequestDto("desc1", LocalDateTime.now(), Collections.emptyList()),
                new RequestDto("desc2", LocalDateTime.now().minusDays(1), Collections.emptyList())
        );

        when(requestService.getRequestsListByOwnerId(userId)).thenReturn(list);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(list.size()))
                .andExpect(jsonPath("$[0].description").value("desc1"))
                .andExpect(jsonPath("$[1].description").value("desc2"));

        verify(requestService, times(1)).getRequestsListByOwnerId(userId);
    }

    @Test
    void getRequests_shouldReturnListOfRequestDto() throws Exception {
        Long userId = 1L;

        List<RequestDto> list = List.of(
                new RequestDto("desc1", LocalDateTime.now(), Collections.emptyList()),
                new RequestDto("desc2", LocalDateTime.now().minusDays(1), Collections.emptyList())
        );

        when(requestService.getRequests(userId)).thenReturn(list);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(list.size()))
                .andExpect(jsonPath("$[0].description").value("desc1"))
                .andExpect(jsonPath("$[1].description").value("desc2"));

        verify(requestService, times(1)).getRequests(userId);
    }

    @Test
    void getRequest_shouldReturnRequestDtoForGetRequest() throws Exception {
        Long userId = 1L;
        Long requestId = 123L;

        RequestDtoForGetRequest dto = new RequestDtoForGetRequest(
                requestId,
                "desc",
                LocalDateTime.now(),
                Collections.emptyList()
        );

        when(requestService.getRequest(requestId, userId)).thenReturn(dto);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.description").value("desc"));

        verify(requestService, times(1)).getRequest(requestId, userId);
    }
}
