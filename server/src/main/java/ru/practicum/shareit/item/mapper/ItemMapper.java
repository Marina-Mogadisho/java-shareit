package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDtoForItemCreate;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),// статус о том, доступна или нет вещь для аренды;
                item.getUser().getId(), // id владельца вещи
                item.getRequest() != null ? item.getRequest().getId() : null // запрос на создание вещи, если вещь создали по запросу
        );
    }


    public static ItemDtoResponseForCreate toItemDtoResponseForCreate(Item item) {
        RequestDtoForItemCreate requestDto = null;
        if (item.getRequest() != null) {
            requestDto = new RequestDtoForItemCreate(
                    item.getRequest().getDescription(),
                    item.getRequest().getCreated()
            );
        }

        return new ItemDtoResponseForCreate(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getUser().getId(),
                requestDto
        );
    }

    public static ItemDtoComments toItemDtoComments(Item item, List<Comment> comments,
                                                    Booking lastBookingNew, Booking nextBookingNew) {
        return new ItemDtoComments(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),// статус о том, доступна или нет вещь для аренды;
                item.getUser(), // id владельца вещи
                lastBookingNew,
                nextBookingNew,
                comments
        );
    }

    public static ItemDtoResponse toItemDtoResponse(Item item) {
        return new ItemDtoResponse(
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static ItemDtoList toItemDtoList(Item item, Booking booking) {
        LocalDateTime start;
        LocalDateTime end;

        if (booking != null) {
            start = booking.getStart();
            end = booking.getEnd();
        } else {
            start = null;
            end = null;
        }
        return new ItemDtoList(item.getName(), item.getDescription(), start, end);
    }
}

