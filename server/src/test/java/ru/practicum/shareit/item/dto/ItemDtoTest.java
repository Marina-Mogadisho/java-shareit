package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatusEnum;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDtoForItemCreate;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> jacksonTester1;
    private final ItemDto itemDto = new ItemDto(
            1L,
            "Название вещи",
            "Описание вещи",
            true,
            1L,
            1L
    );

    @Autowired
    private JacksonTester<ItemDtoComments> jacksonTester2;

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


    @Autowired
    private JacksonTester<ItemDtoCreate> jacksonTester3;
    private final ItemDtoCreate itemDtoCreate = new ItemDtoCreate(
            "Название вещи",
            "Описание вещи",
            true,
            1L
    );

    @Autowired
    private JacksonTester<ItemDtoIdAndName> jacksonTester4;
    private final ItemDtoIdAndName itemDtoIdAndName = new ItemDtoIdAndName(
            1L,
            "Название вещи"
    );

    @Autowired
    private JacksonTester<ItemDtoItemIdAndNameAndOwnerId> jacksonTester5;
    private final ItemDtoItemIdAndNameAndOwnerId itemDtoItemIdAndNameAndOwnerId = new ItemDtoItemIdAndNameAndOwnerId(
            1L,
            "Название вещи",
            1L
    );

    @Autowired
    private JacksonTester<ItemDtoList> jacksonTester6;
    private final ItemDtoList itemDtoList = new ItemDtoList(
            "Название вещи",
            "Описание вещи",
            LocalDateTime.of(2026, 10, 16, 12, 12),
            LocalDateTime.of(2026, 11, 16, 12, 12)
    );

    @Autowired
    private JacksonTester<ItemDtoResponse> jacksonTester7;
    private final ItemDtoResponse itemDtoResponse = new ItemDtoResponse(
            "Название вещи",
            "Описание вещи",
            true
    );

    @Autowired
    private JacksonTester<ItemDtoResponseForCreate> jacksonTester8;

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

    @Autowired
    private JacksonTester<ItemDtoUpdate> jacksonTester9;
    private final ItemDtoUpdate itemDtoUpdate = new ItemDtoUpdate(
            "Название вещи",
            "Описание вещи",
            true
    );

    @Test
    void itemDto() throws Exception {
        JsonContent<ItemDto> jsonContent = jacksonTester1.write(itemDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.ownerUserId").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);

    }

    @Test
    void itemDtoComments() throws Exception {
        JsonContent<ItemDtoComments> jsonContent = jacksonTester2.write(itemDtoComments);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.user.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.lastBooking.start")
                .isEqualTo("2024-10-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.lastBooking.end")
                .isEqualTo("2024-11-16T12:12:00");
        // assertThat(jsonContent).extractingJsonPathArrayValue("$.comments").isEqualTo(commentsTest);


    }

    @Test
    void itemDtoCreate() throws Exception {
        JsonContent<ItemDtoCreate> jsonContent = jacksonTester3.write(itemDtoCreate);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);

    }


    @Test
    void itemDtoIdAndName() throws Exception {
        JsonContent<ItemDtoIdAndName> jsonContent = jacksonTester4.write(itemDtoIdAndName);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
    }

    @Test
    void itemDtoItemIdAndNameAndOwnerId() throws Exception {
        JsonContent<ItemDtoItemIdAndNameAndOwnerId> jsonContent = jacksonTester5.write(itemDtoItemIdAndNameAndOwnerId);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.ownerUserId").isEqualTo(1);
    }


    @Test
    void itemDtoList() throws Exception {
        JsonContent<ItemDtoList> jsonContent = jacksonTester6.write(itemDtoList);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo("2026-10-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo("2026-11-16T12:12:00");

    }

    @Test
    void itemDtoResponse() throws Exception {
        JsonContent<ItemDtoResponse> jsonContent = jacksonTester7.write(itemDtoResponse);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();

    }

    @Test
    void itemDtoResponseForCreate() throws Exception {
        JsonContent<ItemDtoResponseForCreate> jsonContent = jacksonTester8.write(itemDtoResponseForCreate);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.ownerUserId").isEqualTo(1);


        assertThat(jsonContent).extractingJsonPathStringValue("$.requestDtoForItemCreate.description")
                .isEqualTo("Описание запроса");
        assertThat(jsonContent).extractingJsonPathStringValue("$.requestDtoForItemCreate.created")
                .isEqualTo("2026-10-16T12:12:00");
    }


    @Test
    void itemDtoUpdate() throws Exception {
        JsonContent<ItemDtoUpdate> jsonContent = jacksonTester9.write(itemDtoUpdate);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
    }

    @Test
    void testGettersAndSetters() {
        Item item = new Item();

        User user = new User();
        user.setId(1L);

        Request request = new Request();
        request.setId(2L);

        item.setId(10L);
        item.setName("Hat");
        item.setDescription("Summer hat");
        item.setAvailable(true);
        item.setUser(user);
        item.setRequest(request);

        assertEquals(Optional.of(10L).get(), item.getId());
        assertEquals("Hat", item.getName());
        assertEquals("Summer hat", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(user, item.getUser());
        assertEquals(request, item.getRequest());
    }

    @Test
    void testEquals_sameObject() {
        Item item = new Item();
        item.setId(1L);

        assertEquals(item, item);
    }

    @Test
    void testEquals_nullObject() {
        Item item = new Item();
        assertNotEquals(item, null);
    }

    @Test
    void testEquals_differentClass() {
        Item item = new Item();
        assertNotEquals(item, "string");
    }

    @Test
    void testEquals_differentIds() {
        Item item1 = new Item();
        item1.setId(1L);
        Item item2 = new Item();
        item2.setId(2L);

        assertNotEquals(item1, item2);
    }

    @Test
    void testEquals_sameId() {
        Item item1 = new Item();
        item1.setId(1L);
        Item item2 = new Item();
        item2.setId(1L);

        assertEquals(item1, item2);
    }

    @Test
    void testEquals_idIsNull() {
        Item item1 = new Item();
        Item item2 = new Item();

        // equals true только если id != null, иначе false
        assertNotEquals(item1, item2);
    }

    @Test
    void testHashCode_consistency() {
        Item item = new Item();
        int hashCode1 = item.hashCode();
        int hashCode2 = item.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCode_defaultClassHashCode() {
        // Если id == null и прокси нет, хешкод должен совпадать с классом
        Item item = new Item();

        int expectedHash = Item.class.hashCode();
        assertEquals(expectedHash, item.hashCode());
    }
}

