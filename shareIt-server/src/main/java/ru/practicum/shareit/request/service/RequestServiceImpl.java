package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDtoItemIdAndNameAndOwnerId;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dal.RequestRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoCreate;
import ru.practicum.shareit.request.dto.RequestDtoForGetRequest;
import ru.practicum.shareit.request.dto.ResponseDtoCreated;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;


    @Override
    public ResponseDtoCreated save(Long userRequestor, RequestDtoCreate requestDtoCreate) {
        log.info("Получен запрос пользователя на создание запроса вещи, идентификатор пользователя: {}", userRequestor);

        User user = getUserById(userRequestor); //проверяем есть ли в БД такой пользователь, если есть, возвращаем его объект

        if (requestDtoCreate.getDescription().isBlank()) {
            throw new ValidationException("Нельзя создать запрос на вещь с пустым описанием.");
        }
        Request request = new Request();
        request.setDescription(requestDtoCreate.getDescription());
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());

        Request requestSave = requestRepository.save(request);
        log.info("Request сохранен и ему присвоен id: {}", requestSave.getId());
        return RequestMapper.toRequestDtoCreated(requestSave);
    }

    /**
     * GET /requests — получить список своих запросов вместе с данными об ответах на них.
     * Запросы должны возвращаться отсортированными от более новых к более старым.
     *
     * @return List<RequestDtoList> - Для каждого запроса должны быть указаны:
     * String description -описание
     * LocalDateTime created; - дата и время создания
     * List<ItemDtoItemIdAndNameAndOwnerId> items; -список ответов в формате: id вещи, название, id владельца.
     */
    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsListByOwnerId(Long userRequestor) {
        log.info("Получен запрос на получение своих запросов на вещь вместе с данными об ответах на них," +
                " идентификатор пользователя: {}", userRequestor);

        getUserById(userRequestor); // Проверяем есть ли в БД такой пользователь, если есть, возвращаем его объект

        // Достаём список запросов на вещь, автор userRequestor
        List<Request> requests = requestRepository.findRequestsFromUserId(userRequestor);
        if (requests.isEmpty()) {
            throw new NotFoundException("У пользователя с id " + userRequestor + " нет запросов на создание вещей.");
        }

        // Сортируем запросы от более новых к более старым по дате создания
        requests.sort((r1, r2) -> r2.getCreated().compareTo(r1.getCreated()));

        //преобразую каждый объект Request в объект RequestDto
        return requests.stream()
                .map(request -> new RequestDto(
                        request.getDescription(),
                        request.getCreated(),
                        request.getItems() != null //Если у запроса есть связанные предметы (items)
                                ? request.getItems().stream()//преобразуем их в список объектов ItemDtoItemIdAndNameAndOwnerId
                                .map(item -> new ItemDtoItemIdAndNameAndOwnerId(
                                        item.getId(),
                                        item.getName(),
                                        item.getUser().getId()))
                                .collect(Collectors.toList())
                                : Collections.emptyList()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequests(Long userId) {
        log.info("Получен запрос на получение всех запросов на вещи.");
        getUserById(userId); // Проверяем есть ли в БД такой пользователь, если есть, возвращаем его объект


        List<Request> requests = requestRepository.findAll();
        // Сортируем запросы от более новых к более старым по дате создания
        requests.sort((r1, r2) -> r2.getCreated().compareTo(r1.getCreated()));


        return requests.stream()
                .map(request -> new RequestDto(
                        request.getDescription(),
                        request.getCreated(),
                        request.getItems() != null
                                ? request.getItems().stream()
                                .map(item -> new ItemDtoItemIdAndNameAndOwnerId(
                                        item.getId(),
                                        item.getName(),
                                        item.getUser().getId()))
                                .collect(Collectors.toList())
                                : Collections.emptyList()
                ))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public RequestDtoForGetRequest getRequest(Long requestId, Long userId) {
        getUserById(userId); // Проверяем есть ли в БД такой пользователь, если есть, возвращаем его объект
        Request request = requestRepository.findByIdWithItemsAndUsers(requestId)
                .orElseThrow(() -> new NotFoundException("В БД  нет запроса с переданным id = " + requestId));
        return RequestMapper.toRequestDtoForGetRequest(request);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("В БД  нет пользователя с переданным id = " + userId));
    }
}
