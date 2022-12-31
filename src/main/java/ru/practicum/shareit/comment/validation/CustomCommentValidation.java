package ru.practicum.shareit.comment.validation;

import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class CustomCommentValidation {
    public static void isCommentForOwnItem(User user, Item item) {
        if (user.getId().equals(item.getOwner().getId())) {
            throw new BadRequestException("The user doesn't have the right to leave a comment on his own item");
        }
    }
}
