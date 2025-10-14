package ru.practicum.shareit.booking.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Setter
    private LocalDateTime currentDate;

    @PostConstruct
    public void init() {
        if (currentDate == null) {
            currentDate = LocalDateTime.now();
        }
    }

    /**
     * POST /bookings
     */
    @Override
    public BookingDto save(BookingDtoCreate bookingDtoCreate, Long userBookerId) {
        log.info("Получен запрос на аренду вещи:  {}", bookingDtoCreate);
        //проверяем, что создатель запроса на бронирование вещи указан
        if (userBookerId == null) {
            throw new ValidationException("Не указан создатель запроса на бронирование вещи.");
        }

        if (bookingDtoCreate == null) {
            throw new ValidationException("При создании заявки на бронирование не указано тело запроса.");
        }
        // проверяем, что создатель запроса существует в БД
        User userBooker = getUserById(userBookerId);

        if (bookingDtoCreate.getItemId() == null) {
            throw new NotFoundException("Нужно указать id вещи для оформления заявки на бронирование.");
        }

        Long itemId = bookingDtoCreate.getItemId();
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new NotFoundException("Бронирование невозможно. В БД  нет вещи с переданным id = " + itemId);
        }

        if (bookingDtoCreate.getStart() == null) {
            throw new ValidationException("Нужно указать дату начала бронирования.");
        }
        if (bookingDtoCreate.getEnd() == null) {
            throw new ValidationException("Нужно указать дату окончания бронирования.");
        }

        Item item = itemOptional.get();

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для аренды.");
        }

        Booking booking = new Booking();
        booking.setStart(bookingDtoCreate.getStart()); // добавляем в бронирование время начала бронирования
        booking.setEnd(bookingDtoCreate.getEnd());// добавляем в бронирование время окончания бронирования
        booking.setItem(item); // добавляем в бронирование вещь
        booking.setBooker(userBooker);// Добавляем создателя запроса на бронирование вещи - арендатора
        booking.setStatus(BookingStatusEnum.WAITING);//Cтатус бронирования WAITING — «ожидает подтверждения».

        Booking createBooking = bookingRepository.save(booking);
        log.info("Заявка на бронирование успешно создана: {}", createBooking);
        return BookingMapper.toBookingDto(createBooking);
    }

    @Transactional
    public BookingDtoPatchApproved updateBookingStatus(Long bookingId, boolean approved, Long userOwnerId) {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
            userRepository.findById(userOwnerId)
                    .orElseThrow(() -> new ValidationException("В БД нет пользователя с id = " + userOwnerId));

            Long ownerId = booking.getItem().getUser().getId();
            if (!ownerId.equals(userOwnerId)) {
                throw new ValidationException("Статус у бронирования может изменить только владелец.");
            }

            booking.setStatus(approved ? BookingStatusEnum.APPROVED : BookingStatusEnum.REJECTED);
            Booking saved = bookingRepository.save(booking);
            return BookingMapper.toBookingDtoPatch(saved);
        } catch (Exception ex) {
            log.error("Ошибка при обновлении статуса бронирования", ex);
            throw ex; // чтобы стек был в логах
        }
    }

    /**
     * Получение данных о конкретном бронировании
     * Может быть выполнено либо автором бронирования, либо владельцем вещи
     */
    @Override
    @Transactional(readOnly = true)
    public BookingDtoGet getBookingById(Long bookingId, Long bookerUserId) {

        // проверяем, что автор запроса есть в БД
        log.info("begin getBookingById");
        User user = getUserById(bookerUserId);
        log.info("getBookingById user Ok");

        //проверяем, что бронирование с переданным id существует в БД
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        //проверяем, что автор запроса на получение данных является владельцем вещи или автором запроса на бронирование
        User booker = booking.getBooker(); // достаем из бронирования автора запроса
        User owner = booking.getItem().getUser(); // достаем из бронирования владельца вещи
        if (!booker.equals(user)) { // если запрос на получение данных выполнил НЕ автор запроса на бронирование идем дальше
            if (!owner.equals(user)) { // и если не владелец вещи - выбрасываем исключение
                throw new ValidationException("Невозможно получить данные о бронировании. " +
                        "Автор запроса не является владельцем вещи или автором запроса на бронирование.");
            }
        }

        log.info("Запрос на получении данных о конкретном бронировании {}, успешно обработан.", booking);
        return BookingMapper.toBookingDtoGet(booking);
    }


    /**
     * GET /bookings?state={state}.
     * Получение списка всех бронирований текущего пользователя (букера), того кто оформлял запрос на бронирование.
     * Не владелец вещи
     * Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoGet> getBookingsListByUserId(Long bookerUserId, String state) {

        // проверяем, что автор запроса есть в БД
        User user = getUserById(bookerUserId);

        // Вызываю метод выдачи бронирований арендатора (надеюсь JPA правильно понимает логику по названию метода)
        List<Booking> bookings = bookingRepository.getBookingsListByBookerUserId(bookerUserId);

        if (bookings.isEmpty())
            throw new NotFoundException("У пользователя с id " + bookerUserId + " нет никаких бронирований.");

        List<BookingDtoGet> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDtoGet)
                .toList();

        return filterBookings(bookingsDto, state);//Метод фильтрации бронирований по датам и статусу
    }

    /**
     * GET /bookings/owner?state={state}
     * предназначен для получения списка бронирований для всех вещей, принадлежащих владельцу вещи.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoGet> getBookingsListByOwnerUserId(Long ownerId, String state) {

        if (ownerId == null) {
            throw new NotFoundException("Не указан id пользователя");
        }
        // проверяем, что автор запроса есть в БД
        getUserById(ownerId);

        //достали список вещей указанного владельца
        List<Item> itemsByOwner = itemRepository.findItemsFromUserId(ownerId);

        if (itemsByOwner.isEmpty())
            throw new NotFoundException("У пользователя с id " + ownerId + " нет вещей.");

        //Достали бронирования для всех вещей владельца
        //Если у вещи не было бронирований, то в результирующий список List<Booking> bookings
        // ничего не добавится для этой вещи.
        // Метод flatMap просто пропустит пустые потоки и добавит бронирования только для тех вещей, у которых они есть.
        List<Booking> bookings = itemsByOwner.stream()
                .flatMap(item -> bookingRepository.getBookingsListByItem(item).stream())
                .toList();

        if (bookings.isEmpty())
            throw new NotFoundException("У вещей пользователя с id " + ownerId + " нет никаких бронирований.");

        List<BookingDtoGet> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDtoGet)
                .toList();

        return filterBookings(bookingsDto, state);//Метод фильтрации бронирований по датам и статусу

    }

    /**
     * Метод фильтрации бронирований по датам и статусу
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoGet> filterBookings(List<BookingDtoGet> bookingsDto, String state) {
        return switch (state) {
            case "CURRENT" -> // Логика для получения текущих бронирований
                    bookingsDto.stream()
                            .filter(booking -> booking.getStatus().equals(BookingStatusEnum.APPROVED))
                            .filter(booking -> booking.getStart().isBefore(currentDate)
                                    && booking.getEnd().isAfter(currentDate))
                            .sorted(Comparator.comparing(BookingDtoGet::getStart).reversed())
                            .toList();
            case "PAST" -> // Логика для получения завершенных бронирований
                    bookingsDto.stream()
                            .filter(booking -> booking.getStatus().equals(BookingStatusEnum.APPROVED))
                            .filter(booking -> booking.getEnd().isBefore(currentDate))
                            .sorted(Comparator.comparing(BookingDtoGet::getStart).reversed())
                            .toList();
            case "FUTURE" -> // Логика для получения будущих бронирований
                    bookingsDto.stream()
                            .filter(booking -> booking.getStatus().equals(BookingStatusEnum.APPROVED))
                            .filter(booking -> booking.getStart().isAfter(currentDate))
                            .sorted(Comparator.comparing(BookingDtoGet::getStart).reversed())
                            .toList();
            case "WAITING" -> // Логика для получения бронирований ожидающих подтверждения
                    bookingsDto.stream()
                            .filter(booking -> booking.getStatus().equals(BookingStatusEnum.WAITING))
                            .sorted(Comparator.comparing(BookingDtoGet::getStart).reversed())
                            .toList();
            case "REJECTED" -> // Логика для получения отклоненных бронирований
                    bookingsDto.stream()
                            .filter(booking -> booking.getStatus().equals(BookingStatusEnum.REJECTED))
                            .sorted(Comparator.comparing(BookingDtoGet::getStart).reversed())
                            .toList();
            default -> bookingsDto.stream().toList();
        };
    }

    /**
     * Проверяем по id пользователя, что пользователь есть в БД. Если есть, то возвращается объект типа User
     */
    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("В БД  нет пользователя с переданным id = " + userId);
        }
        return userOptional.get();
    }
}

