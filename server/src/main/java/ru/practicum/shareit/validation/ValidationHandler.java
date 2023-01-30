package ru.practicum.shareit.validation;

import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.user.User;
import java.util.Map;
import java.util.Optional;

public class ValidationHandler {
    public static boolean checkUserEmail(User user, UserRepository storage) {
        Optional<User> u = storage.findUserByEmailAndIdNot(user.getEmail(), user.getId());
        return u.isPresent();
    }

    public static int getAttributeFromRequest(Map<String, Object> params, String key, int defaultValue, int minValue) {
        int data = params.containsKey(key) ? Integer.parseInt((String) params.get(key)) : defaultValue;
        if (data < minValue) {
            throw new BadRequestException(String.format("Parameter %s must be more or equal %x", key, minValue));
        }
        return data;
    }
}
