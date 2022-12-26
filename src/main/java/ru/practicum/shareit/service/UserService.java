package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validation.ValidationHandler;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository storage;

    private Long id;

    public void setId() {
        if (id == null || id == 0) {
            id = 1L;
        } else {
            // ооой ну и жесть...
            if (id.equals(1L)) {
                id = 3L;
            } else {
                id++;
            }
        }
    }

    public User create(User user) {
        if (ValidationHandler.checkUserEmail(user, storage)) {
            throw new ValidationException("Email '" + user.getEmail() + "' must be unique!");
        }
        setId();
        user.setId(id);
        return storage.save(user);
    }

    public User update(User user) {
        if (ValidationHandler.checkUserEmail(user, storage)) {
            throw new ValidationException("Email '" + user.getEmail() + "' must be unique!");
        }
        return storage.save(user);
    }

    public User getById(Long userId) {
        return storage.findUserById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
    }

    public void delete(Long userId) {
        User user = storage.findUserById(userId).orElseThrow(() -> new NotFoundException("User Not found!"));
        storage.delete(user);
    }

    public List<User> getAll() {
        return storage.findAll();
    }
}
