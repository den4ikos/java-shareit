package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StatusType;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class CustomValidation {
    public static void isItemAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new BadRequestException("Item is not available!");
        }
    }

    public static void isStartCorrectly(Booking booking) {
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new BadRequestException("The rental start time must be earlier than the end time");
        }
    }

    public static void isItemFromMySelf(Booking booking, User user) {
        if (booking.getItem().getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("You can't rent things from yourself");
        }
    }

    public static void isOwnerCanChangeApproveBookingStatus(Booking booking, User user) {
        if (!booking.getItem().getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("Owner is not found with the right to update the ticket status");
        }
    }

    public static void isBookingStatusApproved(Booking booking) {
        if (booking.getStatus().equals(StatusType.APPROVED)) {
            throw new BadRequestException("You can not change status after approving!");
        }
    }

    public static Boolean isApprovedBookings(List<Booking> bookings, Booking booking) {
        if (bookings.isEmpty()) {
            return true;
        }

        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();

        return bookings.stream()
                .anyMatch(b -> (start.isBefore(b.getStart()) && end.isBefore(b.getStart()))
                        || (start.isAfter(b.getEnd()) && end.isAfter(b.getEnd()))
                );
    }

    public static void isUserAccessToBooking(Booking booking, User user) {
        if (!booking.getItem().getOwner().getId().equals(user.getId()) && !booking.getBooker().getId().equals(user.getId())) {
            throw new NotFoundException("The user with the right to view the status of the request was not found");
        }
    }
}
