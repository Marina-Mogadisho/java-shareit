package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemDtoItemIdAndNameAndOwnerId;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoForGetRequest;
import ru.practicum.shareit.request.dto.ResponseDtoCreated;
import ru.practicum.shareit.request.model.Request;

import java.util.Collections;
import java.util.stream.Collectors;

public class RequestMapper {
    public static ResponseDtoCreated toRequestDtoCreated(Request request) {
        return new ResponseDtoCreated(
                request.getId(),
                request.getDescription(),
                request.getRequestor().getId(),
                request.getCreated()
        );
    }

    public static RequestDto toRequestsDto(Request request) {
        return new RequestDto(
                request.getDescription(),
                request.getCreated(),
                (request.getItems() != null && !request.getItems().isEmpty())
                        ? request.getItems().stream()
                        .map(item -> new ItemDtoItemIdAndNameAndOwnerId(
                                item.getId(),
                                item.getName(),
                                item.getUser().getId()  // владелец вещи
                        ))
                        .collect(Collectors.toList())
                        : Collections.emptyList()
        );
    }


    public static RequestDtoForGetRequest toRequestDtoForGetRequest(Request request) {
        return new RequestDtoForGetRequest(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                (request.getItems() != null && !request.getItems().isEmpty())
                        ? request.getItems().stream()
                        .map(item -> new ItemDtoItemIdAndNameAndOwnerId(
                                item.getId(),
                                item.getName(),
                                item.getUser().getId()  // владелец вещи
                        ))
                        .collect(Collectors.toList())
                        : Collections.emptyList()
        );
    }
}
