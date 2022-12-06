package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.StatusType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumStatusTypePatternValidator implements ConstraintValidator<EnumStatusTypePattern, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return Arrays.stream(StatusType.values()).anyMatch(v -> v.name().equals(value));
    }
}
