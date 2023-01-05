package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.service.ItemRequestService;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto add(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @Valid @RequestBody ItemRequestDto request) {
        User user = userService.getById(userId);
        ItemRequest itemRequest = itemRequestService.add(ItemRequestMapper.toItemRequest(request, user));
        return ItemRequestMapper.toItemRequestDto(itemRequest, Collections.emptyList());
    }

    @GetMapping
    public List<ItemRequestDto> getRequests(@RequestHeader(Constants.HEADER_USER_ID) Long userId) {
        User user = userService.getById(userId);
        List<ItemRequest> requests = itemRequestService.getRequests(user);
        return requests.stream()
                .map(r -> ItemRequestMapper.toItemRequestDto(r, itemService.getItemByRequest(r)))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/all")
    public List<ItemRequestDto> getAll(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @RequestParam Map<String, Object> params) {
        User user = userService.getById(userId);
        List<ItemRequest> requests = itemRequestService.getAll(user, params);
        return requests.stream()
                .map(r -> ItemRequestMapper.toItemRequestDto(r, itemService.getItemByRequest(r)))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{requestId}")
    public ItemRequestDto getUserRequest(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @PathVariable Long requestId) {
        User user = userService.getById(userId);
        ItemRequest request = itemRequestService.getById(requestId);
        List<Item> items = itemService.getItemByRequest(request);
        return ItemRequestMapper.toItemRequestDto(request, items);
    }
}
