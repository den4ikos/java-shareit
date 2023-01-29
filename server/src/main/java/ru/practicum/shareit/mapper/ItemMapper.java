package ru.practicum.shareit.mapper;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoToUser;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public class ItemMapper {
    public static ItemDto toDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest() != null
                        ? item.getRequest().getId()
                        : null
        );
    }

    public static Item toItem(ItemDto item, ItemRequest itemRequest) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                itemRequest
        );
    }

    public static ItemDtoToUser toItemDtoToUser(Item item, List<CommentDto> comments) {
        return new ItemDtoToUser(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                comments
        );
    }
}
