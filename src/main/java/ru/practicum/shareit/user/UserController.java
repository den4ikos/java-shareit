package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserDtoMapper;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserDtoMapper mapper;

    @PostMapping
    @ResponseBody
    public UserDto create(@Valid @RequestBody User user) {
        return mapper.toDto(user);
    }
}
