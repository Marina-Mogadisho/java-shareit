package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.dto.BookingDtoPatchApproved;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatusEnum;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dal.RequestRepository;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Clock fixedClock;

    private final LocalDateTime fixedNow = LocalDateTime.of(2024, 1, 1, 12, 0);

/*
    @BeforeEach
    void setUp() {
        // Устанавливаем фиксированное время для тестов
        bookingService.setCurrentDate(fixedNow);
    }

 */
@BeforeEach
void setUp() {
    fixedClock = Clock.fixed(fixedNow.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    // Подменяем clock в сервисе на фиксированный для тестов
    ReflectionTestUtils.setField(bookingService, "clock", fixedClock);
}


    private BookingDtoGet createBookingDto(Long id, BookingStatusEnum status,
                                           LocalDateTime start, LocalDateTime end) {
        BookingDtoGet dto = new BookingDtoGet();
        dto.setId(id);
        dto.setStatus(status);
        dto.setStart(start);
        dto.setEnd(end);
        return dto;
    }

    // save() успешный кейс
    @Test
    void save_ShouldCreateBooking_WhenValid() {
        Long userId = 1L;
        Long itemId = 2L;

        BookingDtoCreate dto = new BookingDtoCreate();
        dto.setItemId(itemId);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(10L);
            return b;
        });

        BookingDto result = bookingService.save(dto, userId);

        assertNotNull(result);
        assertEquals(itemId, result.getItem().getId());
        assertEquals(userId, result.getBooker().getId());
        assertEquals(BookingStatusEnum.WAITING, result.getStatus());
        verify(bookingRepository).save(any());
    }

    @Test
    void save_ShouldThrow_WhenUserBookerIdNull() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.save(new BookingDtoCreate(), null));
        assertEquals("Не указан создатель запроса на бронирование вещи.", ex.getMessage());
    }

    @Test
    void save_ShouldThrow_WhenBookingDtoCreateNull() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.save(null, 1L));
        assertEquals("При создании заявки на бронирование не указано тело запроса.", ex.getMessage());
    }

    @Test
    void save_ShouldThrow_WhenItemIdNullInDto() {
        BookingDtoCreate dto = new BookingDtoCreate();
        dto.setStart(LocalDateTime.now());
        dto.setEnd(LocalDateTime.now().plusDays(1));
        when(userRepository.findById(any())).thenReturn(Optional.of(new User())); // для userId

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.save(dto, 1L));
        assertEquals("Нужно указать id вещи для оформления заявки на бронирование.", ex.getMessage());
    }

    @Test
    void save_ShouldThrow_WhenItemNotFound() {
        BookingDtoCreate dto = new BookingDtoCreate();
        dto.setItemId(5L);
        dto.setStart(LocalDateTime.now());
        dto.setEnd(LocalDateTime.now().plusDays(1));
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(5L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.save(dto, 1L));
        assertTrue(ex.getMessage().contains("нет вещи с переданным id"));
    }

    @Test
    void save_ShouldThrow_WhenItemUnavailable() {
        BookingDtoCreate dto = new BookingDtoCreate();
        dto.setItemId(5L);
        dto.setStart(LocalDateTime.now());
        dto.setEnd(LocalDateTime.now().plusDays(1));
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        Item item = new Item();
        item.setAvailable(false);
        when(itemRepository.findById(5L)).thenReturn(Optional.of(item));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.save(dto, 1L));
        assertEquals("Вещь не доступна для аренды.", ex.getMessage());
    }

    @Test
    void save_shouldThrowValidationException_whenStartIsNull() {
        Long userId = 1L;
        Long itemId = 10L;

        BookingDtoCreate dtoCreate = new BookingDtoCreate();
        dtoCreate.setItemId(itemId);
        dtoCreate.setStart(null);  // должна вызвать исключение
        dtoCreate.setEnd(LocalDateTime.now().plusDays(1));

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true); // вещь доступна

        // Моки, которые необходимы, чтобы не выбросилось исключение по item и user
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.save(dtoCreate, userId));

        assertEquals("Нужно указать дату начала бронирования.", ex.getMessage());
    }

    @Test
    void save_shouldThrowValidationException_whenEndIsNull() {
        Long userId = 1L;
        Long itemId = 10L;

        BookingDtoCreate dtoCreate = new BookingDtoCreate();
        dtoCreate.setItemId(itemId);
        dtoCreate.setStart(LocalDateTime.now().plusDays(1));
        dtoCreate.setEnd(null);  // должна вызвать исключение

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);

        // Моки
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.save(dtoCreate, userId));

        assertEquals("Нужно указать дату окончания бронирования.", ex.getMessage());
    }


    // updateBookingStatus успешный кейс (утверждение)
    @Test
    void updateBookingStatus_ShouldApproveBooking_WhenOwnerIsCorrect() {
        Long bookingId = 1L;
        Long ownerId = 2L;

        User owner = new User();
        owner.setId(ownerId);

        Item item = new Item();
        item.setUser(owner);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setStatus(BookingStatusEnum.WAITING);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BookingDtoPatchApproved dto = bookingService.updateBookingStatus(bookingId, true, ownerId);

        assertEquals(BookingStatusEnum.APPROVED, booking.getStatus());
        assertNotNull(dto);
        verify(bookingRepository).save(booking);
    }

    @Test
    void updateBookingStatus_ShouldThrow_WhenUserIsNotOwner() {
        Long bookingId = 1L;
        Long ownerId = 2L;
        Long otherUserId = 3L;

        User owner = new User();
        owner.setId(ownerId);

        User otherUser = new User();
        otherUser.setId(otherUserId);

        Item item = new Item();
        item.setUser(owner);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(otherUserId)).thenReturn(Optional.of(otherUser));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.updateBookingStatus(bookingId, true, otherUserId));
        assertEquals("Статус у бронирования может изменить только владелец.", ex.getMessage());
    }

    @Test
    void updateBookingStatus_ShouldThrow_WhenBookingNotFound() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.updateBookingStatus(1L, true, 1L));
        assertEquals("Бронирование не найдено", ex.getMessage());
    }

    @Test
    void updateBookingStatus_ShouldThrow_WhenUserOwnerNotFound() {
        Booking booking = new Booking();
        Item item = new Item();
        User owner = new User();
        owner.setId(1L);
        item.setUser(owner);
        booking.setItem(item);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.updateBookingStatus(1L, true, 1L));
        assertTrue(ex.getMessage().contains("В БД нет пользователя с id"));
    }

    // getBookingById успешный кейс, пользователь - автор бронирования
    @Test
    void getBookingById_ShouldReturnBooking_WhenUserIsBooker() {
        Long bookingId = 1L;
        Long userId = 10L;

        User booker = new User();
        booker.setId(userId);

        User owner = new User();
        owner.setId(20L);

        Item item = new Item();
        item.setUser(owner);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(booker);
        booking.setItem(item);

        when(userRepository.findById(userId)).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        BookingDtoGet result = bookingService.getBookingById(bookingId, userId);
        assertNotNull(result);
    }

    // getBookingById успешный кейс, пользователь - владелец вещи
    @Test
    void getBookingById_ShouldReturnBooking_WhenUserIsOwner() {
        Long bookingId = 1L;
        Long ownerId = 20L;

        User booker = new User();
        booker.setId(10L);

        User owner = new User();
        owner.setId(ownerId);

        Item item = new Item();
        item.setUser(owner);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(booker);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        BookingDtoGet result = bookingService.getBookingById(bookingId, ownerId);
        assertNotNull(result);
    }

    // getBookingById исключение - пользователь не найден
    @Test
    void getBookingById_ShouldThrowNotFound_WhenUserNotFound() {
        Long bookingId = 1L;
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(bookingId, userId));
        assertEquals("В БД  нет пользователя с переданным id = 99", ex.getMessage());
    }

    // getBookingById исключение - бронирование не найдено
    @Test
    void getBookingById_ShouldThrowNotFound_WhenBookingNotFound() {
        Long bookingId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(bookingId, userId));
        assertEquals("Бронирование не найдено", ex.getMessage());
    }

    // getBookingById исключение, пользователь не автор бронирования и не владелец
    @Test
    void getBookingById_ShouldThrowValidation_WhenUserNotOwnerOrBooker() {
        Long bookingId = 1L;
        Long userId = 99L;

        User booker = new User();
        booker.setId(10L);

        User owner = new User();
        owner.setId(20L);

        Item item = new Item();
        item.setUser(owner);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(booker);
        booking.setItem(item);

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.getBookingById(bookingId, userId));
        assertEquals("Невозможно получить данные о бронировании. Автор запроса не является владельцем вещи" +
                " или автором запроса на бронирование.", ex.getMessage());
    }

    // Тест для getBookingsListByUserId - пользователь найден, бронирования есть, фильтрация вызывается
    @Test
    void getBookingsListByUserId_ReturnsFilteredBookingList_WhenBookingsExist() {
        Long userId = 1L;
        String state = "ALL";

        User user = new User();
        user.setId(userId);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setBooker(user);

        // Моки
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.getBookingsListByBookerUserId(userId)).thenReturn(List.of(booking));

        // Заменим метод фильтрации на заглушку (для упрощения)
        BookingServiceImpl spyService = spy(bookingService);
        doReturn(List.of(BookingMapper.toBookingDtoGet(booking))).when(spyService).filterBookings(anyList(), eq(state));

        List<BookingDtoGet> result = spyService.getBookingsListByUserId(userId, state);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository).findById(userId);
        verify(bookingRepository).getBookingsListByBookerUserId(userId);
        verify(spyService).filterBookings(anyList(), eq(state));
    }

    // Тест для getBookingsListByUserId - пользователь не найден -> исключение
    @Test
    void getBookingsListByUserId_ThrowsNotFound_WhenUserNotFound() {
        Long userId = 1L;
        String state = "ALL";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingsListByUserId(userId, state));
        assertEquals("В БД  нет пользователя с переданным id = " + userId, ex.getMessage());
    }

    // Тест для getBookingsListByUserId - бронирований нет -> исключение
    @Test
    void getBookingsListByUserId_ThrowsNotFound_WhenNoBookings() {
        Long userId = 1L;
        String state = "ALL";

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.getBookingsListByBookerUserId(userId)).thenReturn(List.of());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingsListByUserId(userId, state));
        assertEquals("У пользователя с id " + userId + " нет никаких бронирований.", ex.getMessage());
    }

    // Тест для getBookingsListByOwnerUserId - нормальный случай
    @Test
    void getBookingsListByOwnerUserId_ReturnsFilteredBookingList_WhenBookingsExist() {
        Long ownerId = 2L;
        String state = "ALL";

        User owner = new User();
        owner.setId(ownerId);

        Item item1 = new Item();
        item1.setId(100L);
        item1.setUser(owner);

        Booking booking1 = new Booking();
        booking1.setId(10L);
        booking1.setItem(item1);

        // моки
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(itemRepository.findItemsFromUserId(ownerId)).thenReturn(List.of(item1));
        when(bookingRepository.getBookingsListByItem(item1)).thenReturn(List.of(booking1));

        BookingServiceImpl spyService = spy(bookingService);
        doReturn(List.of(BookingMapper.toBookingDtoGet(booking1))).when(spyService).filterBookings(anyList(), eq(state));

        List<BookingDtoGet> result = spyService.getBookingsListByOwnerUserId(ownerId, state);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository).findById(ownerId);
        verify(itemRepository).findItemsFromUserId(ownerId);
        verify(bookingRepository).getBookingsListByItem(item1);
        verify(spyService).filterBookings(anyList(), eq(state));
    }

    // Тест для getBookingsListByOwnerUserId - Нет id владельца -> исключение
    @Test
    void getBookingsListByOwnerUserId_ThrowsNotFound_WhenOwnerIdIsNull() {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingsListByOwnerUserId(null, "ALL"));
        assertEquals("Не указан id пользователя", ex.getMessage());
    }

    // Тест для getBookingsListByOwnerUserId - пользователь не найден
    @Test
    void getBookingsListByOwnerUserId_ThrowsNotFound_WhenUserNotFound() {
        Long ownerId = 2L;
        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingsListByOwnerUserId(ownerId, "ALL"));
        assertEquals("В БД  нет пользователя с переданным id = " + ownerId, ex.getMessage());
    }

    // Тест для getBookingsListByOwnerUserId - у пользователя нет вещей
    @Test
    void getBookingsListByOwnerUserId_ThrowsNotFound_WhenNoItems() {
        Long ownerId = 2L;
        User owner = new User();
        owner.setId(ownerId);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(itemRepository.findItemsFromUserId(ownerId)).thenReturn(List.of());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingsListByOwnerUserId(ownerId, "ALL"));
        assertEquals("У пользователя с id " + ownerId + " нет вещей.", ex.getMessage());
    }

    // Тест для getBookingsListByOwnerUserId - у вещей нет бронирований
    @Test
    void getBookingsListByOwnerUserId_ThrowsNotFound_WhenNoBookings() {
        Long ownerId = 2L;
        User owner = new User();
        owner.setId(ownerId);

        Item item1 = new Item();
        item1.setId(100L);
        item1.setUser(owner);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(itemRepository.findItemsFromUserId(ownerId)).thenReturn(List.of(item1));
        when(bookingRepository.getBookingsListByItem(item1)).thenReturn(List.of());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingsListByOwnerUserId(ownerId, "ALL"));
        assertEquals("У вещей пользователя с id " + ownerId + " нет никаких бронирований.", ex.getMessage());
    }

    @Test
    void filterBookings_Current_ReturnsOnlyCurrentApproved() {
        List<BookingDtoGet> bookings = List.of(
                createBookingDto(1L, BookingStatusEnum.APPROVED,
                        fixedNow.minusDays(1), fixedNow.plusDays(1)), // current
                createBookingDto(2L, BookingStatusEnum.APPROVED,
                        fixedNow.minusDays(5), fixedNow.minusDays(2)), // past
                createBookingDto(3L, BookingStatusEnum.WAITING,
                        fixedNow.plusDays(1), fixedNow.plusDays(2))  // future but status waiting
        );

        List<BookingDtoGet> result = bookingService.filterBookings(bookings, "CURRENT");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void filterBookings_Past_ReturnsOnlyPastApproved() {
        List<BookingDtoGet> bookings = List.of(
                createBookingDto(1L, BookingStatusEnum.APPROVED,
                        fixedNow.minusDays(10), fixedNow.minusDays(5)),  // past
                createBookingDto(2L, BookingStatusEnum.APPROVED,
                        fixedNow.minusDays(1), fixedNow.plusDays(1)), // current
                createBookingDto(3L, BookingStatusEnum.REJECTED,
                        fixedNow.minusDays(20), fixedNow.minusDays(15))  // rejected
        );

        List<BookingDtoGet> result = bookingService.filterBookings(bookings, "PAST");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void filterBookings_Future_ReturnsOnlyFutureApproved() {
        List<BookingDtoGet> bookings = List.of(
                createBookingDto(1L, BookingStatusEnum.APPROVED,
                        fixedNow.plusDays(1), fixedNow.plusDays(2)),  // future
                createBookingDto(2L, BookingStatusEnum.APPROVED,
                        fixedNow.minusDays(5), fixedNow.minusDays(1)), // past
                createBookingDto(3L, BookingStatusEnum.WAITING,
                        fixedNow.plusDays(3), fixedNow.plusDays(4))  // waiting future
        );

        List<BookingDtoGet> result = bookingService.filterBookings(bookings, "FUTURE");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void filterBookings_Waiting_ReturnsOnlyWaiting() {
        List<BookingDtoGet> bookings = List.of(
                createBookingDto(1L, BookingStatusEnum.WAITING,
                        fixedNow.minusDays(1), fixedNow.plusDays(1)),
                createBookingDto(2L, BookingStatusEnum.APPROVED,
                        fixedNow.minusDays(5), fixedNow.plusDays(1)),
                createBookingDto(3L, BookingStatusEnum.REJECTED,
                        fixedNow.minusDays(20), fixedNow.minusDays(15))
        );

        List<BookingDtoGet> result = bookingService.filterBookings(bookings, "WAITING");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void filterBookings_Rejected_ReturnsOnlyRejected() {
        List<BookingDtoGet> bookings = List.of(
                createBookingDto(1L, BookingStatusEnum.REJECTED,
                        fixedNow.minusDays(1), fixedNow.plusDays(1)),
                createBookingDto(2L, BookingStatusEnum.APPROVED,
                        fixedNow.minusDays(5), fixedNow.plusDays(1)),
                createBookingDto(3L, BookingStatusEnum.WAITING,
                        fixedNow.minusDays(20), fixedNow.minusDays(15))
        );

        List<BookingDtoGet> result = bookingService.filterBookings(bookings, "REJECTED");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void filterBookings_Default_ReturnsAll() {
        List<BookingDtoGet> bookings = List.of(
                createBookingDto(1L, BookingStatusEnum.REJECTED,
                        fixedNow.minusDays(1), fixedNow.plusDays(1)),
                createBookingDto(2L, BookingStatusEnum.APPROVED,
                        fixedNow.minusDays(5), fixedNow.plusDays(1)),
                createBookingDto(3L, BookingStatusEnum.WAITING,
                        fixedNow.minusDays(20), fixedNow.minusDays(15))
        );

        List<BookingDtoGet> result = bookingService.filterBookings(bookings, "UNKNOWN_STATE");

        assertEquals(bookings.size(), result.size());
    }
}
