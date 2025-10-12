package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatusEnum;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dal.RequestRepository;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data.sql")
class ItemServiceImplTest {

    private final ItemService itemService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;
    private User user;
    private User authorUser;
    private Item item;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .name("Main user")
                .email("main@mail.ru")
                .build());

        authorUser = userRepository.save(User.builder()
                .name("Author user")
                .email("author@mail.ru")
                .build());

        item = itemRepository.save(Item.builder()
                .name("Шляпа летняя")
                .description("Очень удобная летняя шляпа")
                .available(true)
                .user(user)
                .build());
    }

    private final ItemDtoCreate itemDtoCreate = new ItemDtoCreate(
            "Название вещи",
            "Описание вещи",
            true,
            1L
    );

    private final ItemDto itemDto = new ItemDto(
            1L,
            "Название вещи",
            "Описание вещи",
            true,
            1L,
            1L
    );

    private final ItemDtoUpdate itemDtoUpdate = new ItemDtoUpdate(
            "Название вещи",
            "Описание вещи",
            true
    );

    private Long userId = 1L;

    private ItemDtoCreate validItemDto() {
        ItemDtoCreate dto = new ItemDtoCreate();
        dto.setName("Item name");
        dto.setDescription("Item description");
        dto.setAvailable(true);
        return dto;
    }

    @Test
    void saveItem() {
        ItemDtoResponseForCreate itemDtoResponseForCreate = itemService.save(1L, itemDtoCreate);
        assertThat(itemDtoCreate.getDescription(), equalTo(itemDtoResponseForCreate.getDescription()));
        assertThrows(NotFoundException.class, () ->
                itemService.save(100L, itemDtoCreate));
    }

    @Test
    void update() {
        ItemDto itemDto1 = itemService.update(1L, 1L, itemDtoUpdate);
        assertThat(itemDtoUpdate.getDescription(), equalTo(itemDto1.getDescription()));
        assertThrows(NotFoundException.class, () ->
                itemService.save(100L, itemDtoCreate));
    }

    @Test
    void findAll() {
        List<ItemDtoResponse> items = itemService.findAll();

        assertNotNull(items);
        assertFalse(items.isEmpty(), "Список не должен быть пустым");


        ItemDtoResponse firstItem = items.getFirst();
        assertNotNull(firstItem.getName());
        assertNotNull(firstItem.getDescription());
        assertNotNull(firstItem.getAvailable());

        assertEquals("Название вещи №1", firstItem.getName());
        assertEquals("Описание вещи №1", firstItem.getDescription());
        assertTrue(firstItem.getAvailable());
    }

    @Test
    void delete() {
        Long userId = 1L;
        Long itemId = 1L;

        Set<Long> idsBefore = itemService.findAllIdItems();
        assertTrue(idsBefore.contains(itemId), "ItemId должен быть в наборе до удаления");

        itemService.delete(userId, itemId);

        Set<Long> idsAfter = itemService.findAllIdItems();
        assertFalse(idsAfter.contains(itemId), "ItemId не должен быть в наборе после удаления");
    }

    @Test
    void findById() {
        Long itemId = 1L;
        Long userId = 1L;

        ItemDtoComments item = itemService.findById(userId, itemId);

        assertNotNull(item, "Item должен быть найден");
        assertEquals("Название вещи №1", item.getName(), "Имя предмета не совпадает");
        assertEquals("Описание вещи №1", item.getDescription(), "Описание предмета не совпадает");
        assertNotNull(item.getAvailable(), "Поле available не должно быть null");
    }

    @Test
    void findAllIdItems() {
        Set<Long> listItemRequest = itemService.findAllIdItems();
        assertThat(listItemRequest.size(), equalTo(6));
    }

    @Test
    void findItemsFromUserId() {
        List<ItemDtoList> listItemRequest = itemService.findItemsFromUserId(5L);
        assertThat(listItemRequest.size(), equalTo(1));
        ItemDtoList item = listItemRequest.getFirst();
        String name = item.getName();
        assertThat(name, equalTo("Название вещи №5"));
        assertThrows(NotFoundException.class, () ->
                itemService.findItemsFromUserId(100L));
    }

    @Test
    void findItemsBySearch_shouldReturnOnlyAvailableAndMatchingItems() {
        // Создадим еще одну вещь не доступную
        Item unavailableItem = itemRepository.save(Item.builder()
                .name("Шляпа зимняя")
                .description("Теплая зимняя")
                .available(false)
                .user(user)
                .build());

        List<ItemDtoResponse> foundItems = itemService.findItemsBySearch(user.getId(), "шляпа");

        assertFalse(foundItems.isEmpty(), "Должны найти вещи по тексту");
        assertTrue(foundItems.stream().allMatch(ItemDtoResponse::getAvailable),
                "Все найденные вещи должны быть доступны");
        assertTrue(foundItems.stream().anyMatch(i -> i.getName().toLowerCase().contains("шляпа")),
                "Должен быть первый item");

        assertFalse(foundItems.stream()
                        .anyMatch(i -> i.getName().equals(unavailableItem.getName())),
                "Не должен быть недоступный item");
    }

    @Test
    void saveComment_shouldSaveCommentWhenBookingCompleted() {
        // Создаем законченный booking для authorUser и item
        bookingRepository.save(Booking.builder()
                .item(item)
                .booker(authorUser)
                .start(LocalDateTime.now().minusDays(10))
                .end(LocalDateTime.now().minusDays(5))
                .status(BookingStatusEnum.APPROVED)
                .build());

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Отличная вещь!");

        CommentDto commentResponse = itemService.saveComment(authorUser.getId(), item.getId(), commentRequestDto);

        assertNotNull(commentResponse);
        assertEquals("Отличная вещь!", commentResponse.getText());
        assertEquals(authorUser.getName(), commentResponse.getAuthorName());
    }

    @Test
    void saveComment_shouldThrowWhenUserHasNoBookings() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Отзыв");

        Long someUserId = authorUser.getId();

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.saveComment(someUserId, item.getId(), commentRequestDto));

        assertTrue(exception.getMessage().contains("Пользователь не может оставить отзыв"));
    }

    @Test
    void saveComment_shouldThrowWhenUserHasNoCompletedBookings() {
        // Создадим booking который еще не завершен (end в будущем)
        bookingRepository.save(Booking.builder()
                .item(item)
                .booker(authorUser)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(5))
                .status(BookingStatusEnum.APPROVED)
                .build());

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Отзыв");
        ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.saveComment(authorUser.getId(), item.getId(), commentRequestDto));

        assertTrue(exception.getMessage().contains("Пользователь не может оставить отзыв"));
    }

    @Test
    void save_shouldThrowValidationException_whenNameIsNull() {
        ItemDtoCreate dto = validItemDto();
        dto.setName(null);

        // user уже есть в БД благодаря setUp

        ValidationException ex = assertThrows(ValidationException.class,
                () -> itemService.save(user.getId(), dto));

        assertEquals("Нельзя создать вещь с пустым названием.", ex.getMessage());
    }


    @Test
    void save_shouldThrowValidationException_whenDescriptionIsNull() {
        ItemDtoCreate dto = validItemDto();
        dto.setDescription(null);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> itemService.save(userId, dto));

        assertEquals("Нельзя создать вещь с пустым описанием.", ex.getMessage());
    }

    @Test
    void save_shouldThrowValidationException_whenAvailableIsNull() {
        ItemDtoCreate dto = validItemDto();
        dto.setAvailable(null);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> itemService.save(userId, dto));

        assertEquals("Статус о том, доступна или нет вещь для аренды должен быть задан.", ex.getMessage());
    }

    @Test
    void save_shouldThrowValidationException_whenRequestIdNotFound() {
        ItemDtoCreate dto = validItemDto();
        dto.setRequestId(999L); // несуществующий id

        ValidationException ex = assertThrows(ValidationException.class,
                () -> itemService.save(userId, dto));

        assertEquals("При попытке сохранения вещи, поле Request с указанным id = 999 не найдено в базе данных.",
                ex.getMessage());
    }

    @Test
    void save_shouldSetRequestNull_whenRequestIdIsNull() {
        ItemDtoCreate dto = validItemDto();
        dto.setRequestId(null); // отсутствие запроса

        ItemDtoResponseForCreate response = itemService.save(userId, dto);

        assertNotNull(response);
    }

    @Test
    void update_whenUserNotOwner_throwsValidationException() {
        // Создаем пользователя и владельца с разными ID
        User user = userRepository.save(User.builder()
                .name("Main user")
                .email("main-unique@mail.ru") // Уникальный email
                .build());

        User owner = userRepository.save(User.builder()
                .name("Owner user")
                .email("owner-unique@mail.ru") // Уникальный email
                .build());

        // Создаем предмет с владельцем
        Item item = itemRepository.save(Item.builder()
                .name("Item name")
                .description("Item description")
                .available(true)
                .user(owner)
                .build());

        Long userId = user.getId();
        Long itemId = item.getId();

        ItemDtoUpdate dtoUpdate = new ItemDtoUpdate();
        dtoUpdate.setName("New name");

        // Проверка исключения
        ValidationException ex = assertThrows(ValidationException.class, () ->
                itemService.update(userId, itemId, dtoUpdate));

        assertEquals("Эту операцию с item может совершить только ее владелец.", ex.getMessage());
    }


    @Test
    void update_whenFieldsNull_oldValuesUsed() {
        Long userId = user.getId();
        Long itemId = item.getId();

        ItemDtoUpdate dtoUpdate = new ItemDtoUpdate(); // все поля null

        ItemDto result = itemService.update(userId, itemId, dtoUpdate);

        // Проверяем что возвращаемый DTO сконвертирован с этими же значениями
        assertEquals(itemId, result.getId());
        assertEquals("Шляпа летняя", result.getName());
        assertEquals("Очень удобная летняя шляпа", result.getDescription());
        assertTrue(result.getAvailable());
    }
}


