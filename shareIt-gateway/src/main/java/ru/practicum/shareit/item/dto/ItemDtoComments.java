package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDtoComments {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @NotBlank(message = "Название вещи не может быть пустым.")
    @NotNull
    String name;

    String description;
    Boolean available; // статус о том, доступна или нет вещь для аренды;

    @NotBlank(message = "У вещи должен быть владелец.")
    Long userId; // id владельца вещи

    BookingDto lastBooking;
    BookingDto nextBooking;
    List<CommentDto> comments;
}
