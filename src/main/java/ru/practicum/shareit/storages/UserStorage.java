package ru.practicum.shareit.storages;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.storages.user.UserStorageInterface;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserStorage implements UserStorageInterface {
    private List<User> users = new ArrayList<>();
    private Long id;
    @Override
    public User create(User user) {
        getId();
        user.setId(id);
        users.add(user);

        return user;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(Long userId) {

    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public Optional<User> getById(Long userId) {
        return Optional.empty();
    }

    private void getId() {
        if (id == null || id == 0) {
            id = 1L;
        } else {
            id++;
        }
    }
}
