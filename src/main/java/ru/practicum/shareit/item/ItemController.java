package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(Constants.HEADER_USER_ID) Long userId) {
        log.info("Endpoint request received: 'GET /items with userId: {}'", userId);
        return itemService.getAllByUserId(userId)
                .stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getById(@PathVariable Long itemId) {
        log.info("Endpoint request received: 'GET /items/{}'", itemId);
        Item item = itemService.getById(itemId);
        return ItemMapper.toDto(item);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> search(@RequestParam(required = false) Map<String, Object> params) {
        log.info("Endpoint request received: 'GET /items/search with params: {}'", params.toString());
        return itemService.search(params)
                .stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseBody
    public ItemDto create(@Valid @RequestBody ItemDto item, @RequestHeader(Constants.HEADER_USER_ID) Long userId) {
        log.info("Endpoint request received: 'POST with item: {} and userId: {}'", item.toString(), userId);
        User user = userService.getById(userId);
        Item itemFromDto = itemService.create(ItemMapper.toItem(item));
        itemFromDto.setOwner(user);

        return ItemMapper.toDto(itemFromDto);
    }

    @PatchMapping(value = "/{itemId}")
    @ResponseBody
    public ItemDto update(@RequestBody ItemDto item, @RequestHeader(Constants.HEADER_USER_ID) Long userId, @PathVariable Long itemId) {
        User user = userService.getById(userId);
        item.setId(itemId);
        log.info("Endpoint request received: 'PATCH with item: {} and userId: {} and itemId: {}'", item.toString(), userId, itemId);
        Item itemFromDto = itemService.update(ItemMapper.toItem(item), user);

        return ItemMapper.toDto(itemFromDto);
    }
}
