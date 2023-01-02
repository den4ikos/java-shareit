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
            id++;
        }
    }

    public User create(User user) {
        setId();
        user.setId(id);
        if (ValidationHandler.checkUserEmail(user, storage)) {
            throw new ValidationException("Email '" + user.getEmail() + "' must be unique!");
        }
        return storage.save(user);
    }

    public User update(User user) {
        if (ValidationHandler.checkUserEmail(user, storage)) {
            throw new ValidationException("Email '" + user.getEmail() + "' must be unique!");
        }
        return storage.save(user);
    }

    public User getById(Long userId) {
        return storage.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
    }

    public void delete(Long userId) {
        User user = storage.findById(userId).orElseThrow(() -> new NotFoundException("User Not found!"));
        storage.delete(user);
    }

    public List<User> getAll() {
        return storage.findAll();
    }

    public User setFieldsToUpdate(User currentUser, User requestedUser) {
        User newUser = new User();
        newUser.setId(currentUser.getId());
        if (requestedUser.getName() != null) {
            newUser.setName(requestedUser.getName());
        } else {
            newUser.setName(currentUser.getName());
        }

        if (requestedUser.getEmail() != null) {
            newUser.setEmail(requestedUser.getEmail());
        } else {
            newUser.setEmail(currentUser.getEmail());
        }

        return newUser;
    }
}
