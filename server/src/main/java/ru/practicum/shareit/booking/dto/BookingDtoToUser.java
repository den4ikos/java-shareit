package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.StatusType;
import ru.practicum.shareit.item.dto.ItemDtoForBookingDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoToUser {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    ItemDtoForBookingDto item;
    User booker;
    StatusType status;
}
