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
    public UserDto create(@Valid @RequestBody UserDto user) {
        System.out.println("FROM SERVER: " + user);
        User createdUser = service.create(UserMapper.toUser(user));
        log.info("Endpoint request received: 'GET /users with user: {}'", user.toString());
        return UserMapper.toDto(createdUser);
    }

    @PatchMapping(value = "/{userId}")
    @ResponseBody
    public UserDto update(@RequestBody UserDto user, @PathVariable Long userId) {
        User currentUser = service.getById(userId);
        User u = UserMapper.toUser(user);
        log.info("Endpoint request received: 'PATCH /users with user: {} and userId: {}'", u.toString(), userId);
        User updatedUser = service.update(service.setFieldsToUpdate(currentUser, u));
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
