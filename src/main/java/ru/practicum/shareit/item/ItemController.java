package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @GetMapping
    public List<Item> getAllItems(@RequestHeader(Constants.headerUserId) Long userId) {
        return itemService.getAllByUserId(userId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getById(@PathVariable Long itemId) {
        Item item = itemService.getById(itemId);
        return ItemMapper.toDto(item);
    }

    @GetMapping(value = "/search")
    public List<Item> search(@RequestParam(required = false) Map<String, Object> params) {
        return itemService.search(params);
    }

    @PostMapping
    @ResponseBody
    public ItemDto create(@Valid @RequestBody ItemDto item, @RequestHeader(Constants.headerUserId) Long userId) {
        User user = userService.getById(userId);
        Item itemFromDto = itemService.create(ItemMapper.toItem(item));
        itemFromDto.setOwner(user);

        return ItemMapper.toDto(itemFromDto);
    }

    @PatchMapping(value = "/{itemId}")
    @ResponseBody
    public ItemDto update(@RequestBody ItemDto item, @RequestHeader(Constants.headerUserId) Long userId, @PathVariable Long itemId) {
        User user = userService.getById(userId);
        item.setId(itemId);
        Item itemFromDto = itemService.update(ItemMapper.toItem(item), user);

        return ItemMapper.toDto(itemFromDto);
    }
}
