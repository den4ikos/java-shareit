package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.storage.user.UserStorageInterface;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validation.ValidationHandler;

import java.util.List;

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

    public User update(User user) {
        if (ValidationHandler.checkUserEmail(user, storage)) {
            throw new ValidationException("Email must be unique!");
        }
        return storage.update(user);
    }

    public User getById(Long userId) {
        return storage.getById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
    }

    public void delete(Long userId) {
        User user = storage.getById(userId).orElseThrow(() -> new NotFoundException("User Not found!"));
        storage.delete(user);
    }

    public List<User> getAll() {
        return storage.getAll();
    }
}
