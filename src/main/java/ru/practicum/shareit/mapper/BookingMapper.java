package ru.practicum.shareit.mapper;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StatusType;
import ru.practicum.shareit.booking.dto.BookingDtoForUser;
import ru.practicum.shareit.booking.dto.BookingDtoToOwnerItem;
import ru.practicum.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.shareit.item.dto.ItemDtoForBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {
    public static BookingDtoToUser bookingDtoToUser(Booking booking) {
        return new BookingDtoToUser(
            booking.getId(),
            booking.getStart(),
            booking.getEnd(),
            new ItemDtoForBookingDto(
                    booking.getItem().getId(),
                    booking.getItem().getName(),
                    booking.getItem().getDescription(),
                    booking.getItem().getAvailable()
            ),
            new User(
                    booking.getBooker().getId(),
                    booking.getBooker().getName(),
                    booking.getBooker().getEmail()
            ),
            booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDtoForUser bookingDtoForUser, Item item, User user) {
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStart(bookingDtoForUser.getStart());
        booking.setEnd(bookingDtoForUser.getEnd());
        booking.setStatus(StatusType.WAITING);
        return booking;
    }

    public static BookingDtoToOwnerItem toBookingDtoToOwnerItemToUser(Booking booking) {
        return new BookingDtoToOwnerItem(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStatus(),
                booking.getStart(),
                booking.getEnd()
        );
    }
}
