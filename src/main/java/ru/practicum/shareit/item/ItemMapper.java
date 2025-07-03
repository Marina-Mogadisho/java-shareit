package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDTONameDescription;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),// статус о том, доступна или нет вещь для аренды;
                item.getOwnerUserId(),// id владельца вещи
                item.getRequestId(),// запрос на создание вещи для аренды
                item.numberOfBookings() // сколько раз вещь была в аренде
        );
    }

    public static ItemDtoResponse toItemDtoResponse(Item item) {
        return new ItemDtoResponse(
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequestId(),
                item.numberOfBookings()
        );
    }

    public static ItemDTONameDescription itemDTONameDescription(Item item) {
        return new ItemDTONameDescription(item.getName(), item.getDescription());
    }
}