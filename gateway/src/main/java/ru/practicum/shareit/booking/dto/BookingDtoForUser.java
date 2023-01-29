package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoForUser {
    @NotNull(message = "Item id is required")
    Long itemId;
    @FutureOrPresent(message = "Fill valid date start")
    LocalDateTime start;
    @Future(message = "End date mast be in future")
    LocalDateTime end;
}
