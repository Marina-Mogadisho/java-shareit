package ru.practicum.shareit.request.dal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.model.Request;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "/data.sql")
class RequestRepositoryTest {

    @Autowired
    private RequestRepository requestRepository;

    @Test
    void findRequestsFromUserId_shouldReturnRequestsWithItemsAndUsers_forGivenUserId() {
        Long userId = 1L; // из data.sql

        List<Request> requests = requestRepository.findRequestsFromUserId(userId);

        assertFalse(requests.isEmpty());
        for (Request r : requests) {
            assertEquals(userId, r.getRequestor().getId());
            assertNotNull(r.getItems());
            // Проверяем, что к каждому предмету привязан пользователь (владелец)
            r.getItems().forEach(item -> assertNotNull(item.getUser()));
        }
    }

    @Test
    void findRequestsFromUserIdWithData_shouldReturnRequestDto_forGivenUserId() {
        Long userId = 1L;
        List<Request> request = requestRepository.findRequestsFromUserIdWithData(userId);

        assertFalse(request.isEmpty());
        request.forEach(dto -> {
            assertNotNull(dto.getDescription());
            assertNotNull(dto.getCreated());
            assertNotNull(dto.getItems());
            // В RequestDto может быть список items - проверяйте по структуре вашего класса
        });
    }

    @Test
    void findByIdWithItemsAndUsers_shouldReturnRequestWithItemsAndUsers_whenIdExists() {
        Long requestId = 1L;

        Optional<Request> optionalRequest = requestRepository.findByIdWithItemsAndUsers(requestId);

        assertTrue(optionalRequest.isPresent());
        Request request = optionalRequest.get();

        assertEquals(requestId, request.getId());
        assertNotNull(request.getItems());
        request.getItems().forEach(item -> assertNotNull(item.getUser()));
    }

    @Test
    void findByIdWithItemsAndUsers_shouldReturnEmpty_whenRequestDoesNotExist() {
        Long nonExistentId = 999L;

        Optional<Request> optionalRequest = requestRepository.findByIdWithItemsAndUsers(nonExistentId);

        assertTrue(optionalRequest.isEmpty());
    }
}
