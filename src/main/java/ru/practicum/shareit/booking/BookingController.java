package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingDtoForUser;
import ru.practicum.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.shareit.booking.validation.CustomValidation;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.service.BookingService;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;

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
        CustomValidation.isItemAvailable(item);
        Booking booking = BookingMapper.toBooking(params, item, user);
        CustomValidation.isStartCorrectly(booking);
        CustomValidation.isItemFromMySelf(booking, user);
        return BookingMapper.bookingDtoToUser(bookingService.create(booking));
    }
}
