package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.mappers.UserMapper;
import ru.practicum.shareit.services.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseBody
    public UserDto create(@Valid @RequestBody UserDto user) {
        User u = UserMapper.toUser(user);
        User createdUser = service.create(u);
        return UserMapper.toDto(createdUser);
    }
}
