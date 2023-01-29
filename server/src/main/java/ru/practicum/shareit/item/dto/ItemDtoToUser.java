package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDtoToOwnerItem;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ItemDtoToUser {
    long id;
    String name;
    String description;
    Boolean available;
    BookingDtoToOwnerItem lastBooking;
    BookingDtoToOwnerItem nextBooking;
    List<CommentDto> comments;
}
