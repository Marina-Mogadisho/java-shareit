package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatusEnum;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDtoForItemCreate;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemController.class)
@Import(ItemControllerTest.TestConfig.class)
class ItemControllerTest {


    @TestConfiguration
    static class TestConfig {
        @Bean
        public ItemService itemService() {
            return Mockito.mock(ItemService.class);
        }
    }

    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ItemService itemService;

    private final User user = User.builder()
            .name("Имя пользователя")
            .email("mail@mail.ru")
            .build();

    private final Request request = Request.builder()
            .description("Описание запроса")
            .requestor(user)
            .created(LocalDateTime.parse("2026-10-16T12:12:00"))
            .build();


    private final Item item = Item.builder()
            .name("Имя вещи")
            .description("Описание вещи")
            .available(true)
            .user(user)
            .request(request)
            .build();

    private final ItemDto itemDto = new ItemDto(
            1L,
            "Название вещи",
            "Описание вещи",
            true,
            1L,
            1L
    );


    private final Booking lastBooking = new Booking(
            1L,
            LocalDateTime.of(2024, 10, 16, 12, 12),
            LocalDateTime.of(2024, 11, 16, 12, 12),
            null,
            null,
            BookingStatusEnum.valueOf("WAITING")

    );
    private final Booking nextBooking = new Booking(
            1L,
            LocalDateTime.of(2026, 10, 16, 12, 12),
            LocalDateTime.of(2026, 11, 16, 12, 12),
            null,
            null,
            BookingStatusEnum.valueOf("WAITING")

    );

    private List<Comment> commentsTest = List.of(new Comment(
            1L, "text", new Item(), new User(), LocalDateTime.of(2024, 10, 16, 12, 12)));

    private final ItemDtoComments itemDtoComments = new ItemDtoComments(
            1L, "Название вещи",
            "Описание вещи",
            true,
            new User(1L, "name", "email@ru"),
            lastBooking,
            nextBooking,
            List.of(new Comment(
                    1L, "text", new Item(), new User(), LocalDateTime.of(2024, 10, 16, 12, 12)
            )));

    private final ItemDtoCreate itemDtoCreate = new ItemDtoCreate(
            "Название вещи",
            "Описание вещи",
            true,
            1L
    );

    private final ItemDtoIdAndName itemDtoIdAndName = new ItemDtoIdAndName(
            1L,
            "Название вещи"
    );

    private final ItemDtoItemIdAndNameAndOwnerId itemDtoItemIdAndNameAndOwnerId = new ItemDtoItemIdAndNameAndOwnerId(
            1L,
            "Название вещи",
            1L
    );

    private final ItemDtoList itemDtoList = new ItemDtoList(
            "Название вещи",
            "Описание вещи",
            LocalDateTime.of(2026, 10, 16, 12, 12),
            LocalDateTime.of(2026, 11, 16, 12, 12)
    );

    private final ItemDtoResponse itemDtoResponse = new ItemDtoResponse(
            "Название вещи",
            "Описание вещи",
            true
    );

    private final RequestDtoForItemCreate requestDtoForItemCreate = new RequestDtoForItemCreate(
            "Описание запроса",
            LocalDateTime.of(2026, 10, 16, 12, 12)
    );

    private final ItemDtoResponseForCreate itemDtoResponseForCreate = new ItemDtoResponseForCreate(
            1L,
            "Название вещи",
            "Описание вещи",
            true,
            1L,
            requestDtoForItemCreate
    );


    private final ItemDtoUpdate itemDtoUpdate = new ItemDtoUpdate(
            "Название вещи",
            "Описание вещи",
            true
    );

    private final CommentRequestDto commentRequestDto = new CommentRequestDto(
            "text"
    );

    private final CommentDto commentDto = new CommentDto(
            1L,
            "text",
            1L,
            "name",
            LocalDateTime.of(2026, 10, 16, 12, 12)
    );

    @Test
    void createItem() throws Exception {
        when(itemService.save(1L, itemDtoCreate))
                .thenReturn(itemDtoResponseForCreate);
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDtoCreate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }


    @Test
    void updateItem() throws Exception {
        when(itemService.update(1L, 1L, itemDtoUpdate))
                .thenReturn(itemDto);
        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDtoUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }


    @Test
    void deleteItem() throws Exception {
        mvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void findByIdItem() throws Exception {
        when(itemService.findById(1L, 1L)).thenReturn(itemDtoComments);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", is(itemDtoComments.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoComments.getDescription())));

    }

    @Test
    void getItemsFromUserId() throws Exception {
        when(itemService.findItemsFromUserId(1L))
                .thenReturn(List.of(itemDtoList));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(itemDtoList.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoList.getDescription())));

    }


    @Test
    void getItemsBySearch() throws Exception {
        when(itemService.findItemsBySearch(1L, "Название вещи"))
                .thenReturn(List.of(itemDtoResponse));
        mvc.perform(get("/items/search?text=Название вещи")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(itemDtoResponse.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoResponse.getDescription())));
    }


    @Test
    void saveComment() throws Exception {
        when(itemService.saveComment(1L, 1L, commentRequestDto))
                .thenReturn(commentDto);
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentRequestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}