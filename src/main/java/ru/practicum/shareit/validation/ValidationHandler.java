package ru.practicum.shareit.validation;

import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.user.User;

import java.util.Optional;

public class ValidationHandler {
    public static boolean checkUserEmail(User user, UserRepository storage) {
        Optional<User> u = storage.findUserByEmail(user.getEmail());
        return u.isPresent();
    }
}
