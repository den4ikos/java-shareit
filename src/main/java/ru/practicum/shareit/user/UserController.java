package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.mappers.UserMapper;
import ru.practicum.shareit.services.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseBody
    public UserDto create(@Valid @RequestBody User user) {
        User createdUser = service.create(user);
        return UserMapper.toDto(createdUser);
    }

    @PatchMapping(value = "/{userId}")
    @ResponseBody
    public UserDto update(@Valid @RequestBody UserDto user, @PathVariable Long userId) {
        user.setId(userId);
        User u = UserMapper.toUser(user);
        User updatedUser = service.update(u);
        return UserMapper.toDto(updatedUser);
    }

    @GetMapping(value = "/{userId}")
    public UserDto getById(@PathVariable Long userId) {
        User user = service.getById(userId);
        return UserMapper.toDto(user);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable Long userId) {
        service.delete(userId);
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }
}
