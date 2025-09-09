package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoComments;
import ru.practicum.shareit.item.dto.ItemDtoList;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

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
                item.getUser().getId()// id владельца вещи
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

