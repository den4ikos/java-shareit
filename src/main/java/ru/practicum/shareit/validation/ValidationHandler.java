package ru.practicum.shareit.validation;

import ru.practicum.shareit.storage.user.UserStorageInterface;
import ru.practicum.shareit.user.User;

public class ValidationHandler {
    public static boolean checkUserEmail(User user, UserStorageInterface storage) {
        return storage.getAll().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()));
    }
}
