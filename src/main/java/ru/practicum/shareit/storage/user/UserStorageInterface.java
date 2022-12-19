package ru.practicum.shareit.storage.user;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserStorageInterface {
    User create(User user);

    User update(User user);

    void delete(User user);

    List<User> getAll();

    Optional<User> getById(Long userId);
}
