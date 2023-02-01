package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingDtoForUser;
import ru.practicum.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.shareit.booking.validation.CustomBookingValidation;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoToUser;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.service.BookingService;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    @PostMapping
    public BookingDtoToUser create(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @Valid @RequestBody BookingDtoForUser params) {
        User user = userService.getById(userId);
        Item item = itemService.getById(params.getItemId());
        CustomBookingValidation.isItemAvailable(item);
        log.info("Endpoint request received: 'POST with booking: {} and userId: {}'", params, userId);
        Booking booking = BookingMapper.toBooking(params, item, user);
        CustomBookingValidation.isStartCorrectly(booking);
        CustomBookingValidation.isItemFromMySelf(booking, user);
        return BookingMapper.bookingDtoToUser(bookingService.create(booking));
    }

    @PatchMapping(value = "{bookingId}")
    public BookingDtoToUser update(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        User user = userService.getById(userId);
        log.info("Endpoint request received: 'PATCH with user: {} and bookingId: {} and approved: {}'", user, bookingId, approved);
        Booking booking = bookingService.updateStatus(user, bookingId, approved);
        return BookingMapper.bookingDtoToUser(booking);
    }

    @GetMapping
    public List<BookingDtoToUser> getAll(
            @RequestHeader(Constants.HEADER_USER_ID) Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        User user = userService.getById(userId);
        log.info("Endpoint request received: 'GET with user: {} and state: {}'", user, state);
        return bookingService.getAll(user, state, bookingService.convertParamsToMap(from, size))
                .stream()
                .map(BookingMapper::bookingDtoToUser)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "{bookingId}")
    public BookingDtoToUser findById(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @PathVariable Long bookingId) {
        User user = userService.getById(userId);
        log.info("Endpoint request received: 'GET with user: {} and bookingId: {}'", user, bookingId);
        Booking booking = bookingService.findById(user, bookingId);
        return BookingMapper.bookingDtoToUser(booking);
    }

    @GetMapping(value = "/owner")
    public List<BookingDtoToUser> getAllByOwner(
            @RequestHeader(Constants.HEADER_USER_ID) Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam Map<String, Object> params) {
        User user = userService.getById(userId);
        log.info("Endpoint request received: 'GET with user: {} and bookingId: {}'", user, state);
        List<ItemDtoToUser> userItems = itemService.getUserItems(user);
        if (userItems.isEmpty()) {
            throw new NotFoundException("User has no any item");
        }
        return bookingService.getAllForOwner(user, state, params)
                .stream()
                .map(BookingMapper::bookingDtoToUser)
                .collect(Collectors.toList());
    }
}
