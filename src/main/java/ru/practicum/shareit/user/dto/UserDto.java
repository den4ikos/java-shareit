package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;

    private Set<User> users = new HashSet<>();
}
