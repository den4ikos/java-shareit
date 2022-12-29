package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

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
            throw new ValidationException("You can't rent things from yourself");
        }
    }
}
