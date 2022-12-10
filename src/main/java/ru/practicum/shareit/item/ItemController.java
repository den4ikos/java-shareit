package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    @ResponseBody
    public ItemDto create(@Valid @RequestBody ItemDto item, @RequestHeader(Constants.headerUserId) Long userId) {
        Item itemFromDto = itemService.create(ItemMapper.toItem(item));
        User user = userService.getById(userId);
        itemFromDto.setOwner(user);
        return ItemMapper.toDto(itemFromDto);
    }
}
