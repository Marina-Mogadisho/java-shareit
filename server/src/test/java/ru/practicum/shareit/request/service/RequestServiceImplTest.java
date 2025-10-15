package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dal.RequestRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoCreate;
import ru.practicum.shareit.request.dto.ResponseDtoCreated;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Test
    void save_shouldSaveAndReturnDtoCreated() {
        Long userId = 1L;
        RequestDtoCreate dtoCreate = new RequestDtoCreate();
        dtoCreate.setDescription("Нужна вещь");

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.save(any(Request.class))).thenAnswer(invocation -> {
            Request req = invocation.getArgument(0);
            req.setId(100L); // Пример ID, который возвращает база
            return req;
        });

        ResponseDtoCreated response = requestService.save(userId, dtoCreate);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(100L, response.getId());

        verify(userRepository, times(1)).findById(userId);
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void save_shouldThrowValidationException_ifDescriptionIsBlank() {
        Long userId = 1L;
        RequestDtoCreate dtoCreate = new RequestDtoCreate();
        dtoCreate.setDescription("   ");

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ValidationException thrown = assertThrows(ValidationException.class,
                () -> requestService.save(userId, dtoCreate));

        assertEquals("Нельзя создать запрос на вещь с пустым описанием.", thrown.getMessage());

        verify(requestRepository, never()).save(any());
    }

    @Test
    void getRequestsListByOwnerId_shouldReturnSortedRequestsDto() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("emailTest@mail.ru");

        Request newerRequest = new Request();
        newerRequest.setDescription("Новый запрос");
        newerRequest.setCreated(LocalDateTime.parse("2026-10-16T12:12:00"));
        newerRequest.setRequestor(user);
        newerRequest.setItems(Collections.emptyList());

        Request olderRequest = new Request();
        olderRequest.setDescription("Старый запрос");
        olderRequest.setCreated(LocalDateTime.now().minusDays(1));
        olderRequest.setRequestor(user);
        olderRequest.setItems(Collections.emptyList());

        List<Request> requests = new ArrayList<>();
        requests.add(olderRequest);
        requests.add(newerRequest);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findRequestsFromUserId(userId)).thenReturn(requests);

        List<RequestDto> result = requestService.getRequestsListByOwnerId(userId);

        assertEquals(2, result.size());
        // Проверяем сортировку: самый новый первый
        assertEquals("Новый запрос", result.get(0).getDescription());
        assertEquals("Старый запрос", result.get(1).getDescription());

        verify(userRepository, times(1)).findById(userId);
        verify(requestRepository, times(1)).findRequestsFromUserId(userId);
    }

    @Test
    void getRequestsListByOwnerId_shouldThrowNotFoundException_ifNoRequests() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findRequestsFromUserId(userId)).thenReturn(Collections.emptyList());

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> requestService.getRequestsListByOwnerId(userId));

        assertEquals("У пользователя с id " + userId + " нет запросов на создание вещей.", thrown.getMessage());

        verify(userRepository, times(1)).findById(userId);
        verify(requestRepository, times(1)).findRequestsFromUserId(userId);
    }

    @Test
    void getRequests_shouldReturnSortedRequestsDto() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Request request1 = new Request();
        request1.setDescription("Запрос 1");
        request1.setCreated(LocalDateTime.now().minusDays(1));
        request1.setRequestor(user);
        request1.setItems(Collections.emptyList());

        Request request2 = new Request();
        request2.setDescription("Запрос 2");
        request2.setCreated(LocalDateTime.now());
        request2.setRequestor(user);
        request2.setItems(Collections.emptyList());

        List<Request> requests = new ArrayList<>();
        requests.add(request1);
        requests.add(request2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findAll()).thenReturn(requests);

        List<RequestDto> result = requestService.getRequests(userId);

        assertEquals(2, result.size());
        assertEquals("Запрос 2", result.get(0).getDescription()); // более новый первый
        assertEquals("Запрос 1", result.get(1).getDescription());

        verify(userRepository, times(1)).findById(userId);
        verify(requestRepository, times(1)).findAll();
    }
}
