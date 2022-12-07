package ru.practicum.shareit.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.storages.user.UserStorageInterface;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validation.ValidationHandler;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorageInterface storage;

    public User create(User user) {
        if (ValidationHandler.checkUserEmail(user, storage)) {
            throw new ValidationException("Email must be unique!");
        }
        return storage.create(user);
    }
}
