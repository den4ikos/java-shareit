package ru.practicum.shareit.user.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserDtoMapper {
    private Long userDtoId;

    void setUserDtoId() {
        if (userDtoId == null) {
            userDtoId = 0L;
        }
        userDtoId++;
    }
    public UserDto toDto(User user) {
        setUserDtoId();
        user.setId(userDtoId);

        Long id = user.getId();
        String name = user.getName();
        String email = user.getEmail();

        return new UserDto(id, name, email);
    }
}
