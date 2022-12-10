package ru.practicum.shareit.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.storage.user.UserStorageInterface;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserStorage implements UserStorageInterface {
    private List<User> users = new ArrayList<>();
    private Long id;

    public void setId() {
        if (id == null || id == 0) {
            id = 1L;
        } else {
            id++;
        }
    }

    @Override
    public User create(User user) {
        setId();
        user.setId(id);
        users.add(user);

        return user;
    }

    @Override
    public User update(User user) {
        User currentUser = getById(user.getId()).orElseThrow(() -> new NotFoundException("User not found"));
        currentUser.setName(user.getName() != null ? user.getName() : currentUser.getName());
        currentUser.setEmail(user.getEmail() != null ? user.getEmail() : currentUser.getEmail());
        return currentUser;
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public Optional<User> getById(Long userId) {
        return users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst();
    }
}
