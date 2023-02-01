package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.StatusType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoToOwnerItem {
    Long id;
    long bookerId;
    StatusType status;
    LocalDateTime start;
    LocalDateTime end;
}

