package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoForGetRequest;
import ru.practicum.shareit.request.dto.ResponseDtoCreated;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapperTest {

    @Test
    void toRequestDtoCreated_shouldMapAllFields() {
        Long requestId = 10L;
        Long userId = 5L;
        String description = "test description";
        LocalDateTime created = LocalDateTime.now();

        User user = new User();
        user.setId(userId);

        Request request = new Request();
        request.setId(requestId);
        request.setDescription(description);
        request.setRequestor(user);
        request.setCreated(created);

        ResponseDtoCreated dto = RequestMapper.toRequestDtoCreated(request);

        assertEquals(requestId, dto.getId());
        assertEquals(description, dto.getDescription());
        assertEquals(userId, dto.getUserRequestor());
        assertEquals(created, dto.getCreated());
    }

    @Test
    void toRequestsDto_shouldMapFieldsAndItems() {
        String description = "desc";
        LocalDateTime created = LocalDateTime.now();

        User itemOwner = new User();
        itemOwner.setId(2L);

        Item item1 = new Item();
        item1.setId(100L);
        item1.setName("item1");
        item1.setUser(itemOwner);

        Item item2 = new Item();
        item2.setId(101L);
        item2.setName("item2");
        item2.setUser(itemOwner);

        Request request = new Request();
        request.setDescription(description);
        request.setCreated(created);
        request.setItems(List.of(item1, item2));

        RequestDto dto = RequestMapper.toRequestsDto(request);

        assertEquals(description, dto.getDescription());
        assertEquals(created, dto.getCreated());
        assertEquals(2, dto.getItems().size());

        assertEquals(item1.getId(), dto.getItems().get(0).getId());
        assertEquals(item1.getName(), dto.getItems().get(0).getName());
        assertEquals(itemOwner.getId(), dto.getItems().get(0).getOwnerUserId());

        assertEquals(item2.getId(), dto.getItems().get(1).getId());
        assertEquals(item2.getName(), dto.getItems().get(1).getName());
        assertEquals(itemOwner.getId(), dto.getItems().get(1).getOwnerUserId());
    }

    @Test
    void toRequestsDto_shouldReturnEmptyList_whenNoItems() {
        Request request = new Request();
        request.setDescription("desc");
        request.setCreated(LocalDateTime.now());
        request.setItems(Collections.emptyList());

        RequestDto dto = RequestMapper.toRequestsDto(request);

        assertNotNull(dto.getItems());
        assertTrue(dto.getItems().isEmpty());
    }

    @Test
    void toRequestDtoForGetRequest_shouldMapFieldsAndItems() {
        Long requestId = 50L;
        String description = "desc";
        LocalDateTime created = LocalDateTime.now();

        User itemOwner = new User();
        itemOwner.setId(8L);

        Item item = new Item();
        item.setId(200L);
        item.setName("item200");
        item.setUser(itemOwner);

        Request request = new Request();
        request.setId(requestId);
        request.setDescription(description);
        request.setCreated(created);
        request.setItems(List.of(item));

        RequestDtoForGetRequest dto = RequestMapper.toRequestDtoForGetRequest(request);

        assertEquals(requestId, dto.getId());
        assertEquals(description, dto.getDescription());
        assertEquals(created, dto.getCreated());
        assertEquals(1, dto.getItems().size());
        assertEquals(item.getId(), dto.getItems().get(0).getId());
        assertEquals(item.getName(), dto.getItems().get(0).getName());
        assertEquals(itemOwner.getId(), dto.getItems().get(0).getOwnerUserId());
    }

    @Test
    void toRequestDtoForGetRequest_shouldReturnEmptyList_whenNoItems() {
        Request request = new Request();
        request.setId(1L);
        request.setDescription("desc");
        request.setCreated(LocalDateTime.now());
        request.setItems(Collections.emptyList());

        RequestDtoForGetRequest dto = RequestMapper.toRequestDtoForGetRequest(request);
        assertNotNull(dto.getItems());
        assertTrue(dto.getItems().isEmpty());
    }
}
