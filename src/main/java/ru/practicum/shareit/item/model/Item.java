package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    Long id;

    @NotBlank(message = "Название вещи не может быть пустым.")
    String name;

    String description;
    Boolean available; // статус о том, доступна или нет вещь для аренды;

    @NotBlank(message = "У вещи должен быть владелец.")
    Long ownerUserId;

    Long requestId; // ссылка на запрос другого пользователя, если вещь была создана по его запросу

    Integer numberOfBookings; // сколько раз вещь была в аренде

    public boolean isAvailable() {
        return available;
    }

    public Integer numberOfBookings() {
        return numberOfBookings;
    }
}