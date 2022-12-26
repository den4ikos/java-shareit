package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseBody
    public UserDto create(@Valid @RequestBody User user) {
        User createdUser = service.create(user);
        log.info("Endpoint request received: 'GET /users with user: {}'", user.toString());
        return UserMapper.toDto(createdUser);
    }

    @PatchMapping(value = "/{userId}")
    @ResponseBody
    public UserDto update(@Valid @RequestBody UserDto user, @PathVariable Long userId) {
        log.info("Endpoint request received: 'PATCH /users with user: {} and userId: {}'", user.toString(), userId);
        user.setId(userId);
        User u = UserMapper.toUser(user);
        System.out.println(u);
        User updatedUser = service.update(u);
        return UserMapper.toDto(updatedUser);
    }

    @GetMapping(value = "/{userId}")
    public UserDto getById(@PathVariable Long userId) {
        log.info("Endpoint request received: 'GET /users/{userId} with userId: {}'", userId);
        User user = service.getById(userId);
        return UserMapper.toDto(user);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Endpoint request received: 'DELETE userId: {}'", userId);
        service.delete(userId);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Endpoint request received: 'GET'");
        return service.getAll();
    }
}
