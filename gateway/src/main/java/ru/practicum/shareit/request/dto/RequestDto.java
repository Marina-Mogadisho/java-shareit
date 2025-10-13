package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoItemIdAndNameAndOwnerId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor// будет сгенерирован конструктор с одним параметром для каждого поля класса
public class RequestDto {
    @NotBlank(message = "Описание запроса не может быть пустым.")
    @NotNull
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime created; //дата создания запроса

    /*
    Для каждого запроса может быть указан ответ, если он есть,
     в виде списка в формате: id вещи, название, id владельца.
     */
    List<ItemDtoItemIdAndNameAndOwnerId> items;
}



