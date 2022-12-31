package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoToUser;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.service.CommentService;
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

    private final CommentService commentService;

    @GetMapping
    public List<ItemDtoToUser> getAllItems(@RequestHeader(Constants.HEADER_USER_ID) Long userId) {
        log.info("Endpoint request received: 'GET /items with userId: {}'", userId);
        User user = userService.getById(userId);
        return itemService.getUserItems(user);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDtoToUser getById(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @PathVariable Long itemId) {
        log.info("Endpoint request received: 'GET /items/{}'", itemId);
        User user = userService.getById(userId);
        Item item = itemService.getById(itemId);
        if (!item.getOwner().getId().equals(user.getId())) {
            return ItemMapper.toItemDtoToUser(item, itemService.getItemComments(item));
        } else {
            return itemService.getUserItems(user)
                    .stream()
                    .filter(i -> i.getId() == itemId)
                    .findFirst().orElseThrow(() -> new NotFoundException("Items not found"));
        }
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
        item.setOwner(user);
        Item itemFromDto = itemService.create(ItemMapper.toItem(item));

        return ItemMapper.toDto(itemFromDto);
    }

    @PatchMapping(value = "/{itemId}")
    @ResponseBody
    public ItemDto update(@RequestBody ItemDto item, @RequestHeader(Constants.HEADER_USER_ID) Long userId, @PathVariable Long itemId) {
        User user = userService.getById(userId);
        Item oldItem = itemService.getById(itemId);
        itemService.checkUserForItem(oldItem, user);
        item.setId(itemId);
        log.info("Endpoint request received: 'PATCH with item: {} and userId: {} and itemId: {}'", item, userId, itemId);
        Item fillItem = itemService.setFieldsToUpdate(oldItem, item, user);
        Item itemFromDto = itemService.update(fillItem, user);

        return ItemMapper.toDto(itemFromDto);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @Valid @RequestBody CommentDto params, @PathVariable Long itemId) {
        User user = userService.getById(userId);
        Item item = itemService.getById(itemId);
        log.info("Endpoint request received: 'POST with user: {} and item: {} and comment: {}'", user, item, params);
        Comment comment = commentService.save(user, item, params);
        return CommentMapper.toCommentDto(comment);
    }
}
