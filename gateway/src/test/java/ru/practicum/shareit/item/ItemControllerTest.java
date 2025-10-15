package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemController.class)
@Import(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("deprecation")
    @MockBean
    private ItemClient itemClient;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    private final Long userId = 1L;
    private final Long itemId = 2L;

    @Test
    void save_shouldReturnCreatedItem() throws Exception {
        ItemDtoCreate itemCreate = new ItemDtoCreate("Название", "Описание", true, 1L);

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(itemCreate);
        when(itemClient.saveItem(eq(userId), any(ItemDtoCreate.class))).thenReturn(responseEntity);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemCreate)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemCreate)));

        verify(itemClient, times(1)).saveItem(eq(userId), any());
    }

    @Test
    void update_shouldReturnUpdatedItem() throws Exception {
        ItemDtoUpdate updateDto = new ItemDtoUpdate("Новое имя", "Новое описание", true);

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(updateDto);
        when(itemClient.update(eq(userId), eq(itemId), any(ItemDtoUpdate.class))).thenReturn(responseEntity);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updateDto)));

        verify(itemClient, times(1)).update(eq(userId), eq(itemId), any());
    }

    @Test
    void delete_shouldCallClientDelete() throws Exception {
        doNothing().when(itemClient).delete(itemId, userId);

        mockMvc.perform(delete("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemClient, times(1)).delete(itemId, userId);
    }

    @Test
    void findById_shouldReturnItem() throws Exception {
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(new ItemDtoUpdate("name", "desc", true));
        when(itemClient.findById(userId, itemId)).thenReturn(responseEntity);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseEntity.getBody())));

        verify(itemClient, times(1)).findById(userId, itemId);
    }

    @Test
    void findItemsFromUserId_shouldReturnList() throws Exception {
        List<ItemDtoUpdate> items = List.of(new ItemDtoUpdate("name1", "desc1", true));
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(items);
        when(itemClient.findItemsFromUserId(userId)).thenReturn(responseEntity);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(items)));

        verify(itemClient, times(1)).findItemsFromUserId(userId);
    }

    @Test
    void findItemsBySearch_shouldReturnItemsBySearch() throws Exception {
        String searchText = "шляп";

        List<ItemDtoUpdate> items = List.of(new ItemDtoUpdate("шляпа", "описание", true));
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(items);

        when(itemClient.findItemsBySearch(userId, searchText)).thenReturn(responseEntity);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", searchText))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(items)));

        verify(itemClient, times(1)).findItemsBySearch(userId, searchText);
    }

    @Test
    void findItemsBySearch_withEmptyText_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", " "))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemClient, never()).findItemsBySearch(anyLong(), anyString());
    }

    @Test
    void saveComment_shouldReturnComment() throws Exception {
        Long authorUserId = userId;
        CommentRequestDto commentRequestDto = new CommentRequestDto("Отличная вещь!");

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(commentRequestDto);
        when(itemClient.saveComment(eq(authorUserId), eq(itemId), any(CommentRequestDto.class))).thenReturn(responseEntity);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", authorUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentRequestDto)));

        verify(itemClient, times(1)).saveComment(authorUserId, itemId, commentRequestDto);
    }
}
