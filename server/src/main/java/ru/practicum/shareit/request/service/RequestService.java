package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoCreate;
import ru.practicum.shareit.request.dto.RequestDtoForGetRequest;
import ru.practicum.shareit.request.dto.ResponseDtoCreated;

import java.util.List;

public interface RequestService {
    ResponseDtoCreated save(Long userRequestor, RequestDtoCreate requestDtoCreate);

    List<RequestDto> getRequestsListByOwnerId(Long userRequestor);

    List<RequestDto> getRequests(Long userId);

    RequestDtoForGetRequest getRequest(Long requestId, Long userId);
}
