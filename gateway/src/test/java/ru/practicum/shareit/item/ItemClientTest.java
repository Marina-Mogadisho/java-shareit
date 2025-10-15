package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertNotNull;


@ExtendWith(MockitoExtension.class)
class ItemClientTest {

    @Mock
    private RestTemplateBuilder builder;

    @Mock
    private RestTemplate restTemplate;

    private ItemClient itemClient;

    private final String serverUrl = "http://localhost:8080";

    private final Long userId = 1L;
    private final Long itemId = 2L;

    @BeforeEach
    void setUp() throws Exception {
        when(builder.uriTemplateHandler(any(UriTemplateHandler.class))).thenReturn(builder);
        when(builder.requestFactory((Supplier<ClientHttpRequestFactory>) any())).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);

        itemClient = new ItemClient(serverUrl, builder);

        // Подмена поля rest (RestTemplate) на мок
        Field restField = BaseClient.class.getDeclaredField("rest");
        restField.setAccessible(true);
        restField.set(itemClient, restTemplate);
    }

    @Test
    void saveItem_shouldCallPost() {
        ItemDtoCreate createDto = new ItemDtoCreate("Новая вещь", "Описание вещи", true, 1L);
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("OK");

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.saveItem(userId, createDto);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void update_shouldCallPatch() {
        ItemDtoUpdate updateDto = new ItemDtoUpdate("Название вещи", "Описание вещи", true);
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Updated");

        when(restTemplate.exchange(
                contains("/" + itemId),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.update(userId, itemId, updateDto);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void delete_shouldCallDelete() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(restTemplate.exchange(
                contains("/" + itemId),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        itemClient.delete(userId, itemId);

        verify(restTemplate).exchange(
                contains("/" + itemId),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void findById_shouldCallGet() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Item");

        when(restTemplate.exchange(
                contains("/" + itemId),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.findById(userId, itemId);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void findItemsFromUserId_shouldCallGet() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Items from user");

        when(restTemplate.exchange(
                eq(""), // Пустой путь
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.findItemsFromUserId(userId);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void findItemsBySearch_shouldCallGetWithParams() {
        String searchText = "text";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Search results");

        Map<String, Object> params = Map.of("text", searchText);

        when(restTemplate.exchange(
                eq("/search?text={text}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(params)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.findItemsBySearch(userId, searchText);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }


    @Test
    void saveComment_shouldCallPost() {
        CommentRequestDto commentDto = new CommentRequestDto("Comment text");
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Comment saved");

        when(restTemplate.exchange(
                contains("/" + itemId + "/comment"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.saveComment(userId, itemId, commentDto);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }
}
