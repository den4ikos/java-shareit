package ru.practicum.shareit.booking;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.validation.EnumStatusTypePattern;

import java.time.LocalDateTime;

@Data
public class Booking {

    private Long id;

    @DateTimeFormat
    private LocalDateTime start;

    @DateTimeFormat
    private LocalDateTime end;

    private Long item;

    private Long booker;

    @EnumStatusTypePattern
    private StatusType status;

}
