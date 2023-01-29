package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/items")
@RequiredArgsConstructor
@Valid
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping()
    public ResponseEntity<Object> addItem(@RequestHeader(Constants.HEADER_USER_ID) Long userId,
                                          @RequestBody @Valid ItemDto itemDto) {
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping(value = "/{itemId}")
        public ResponseEntity<Object> updateItem(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @PathVariable Long itemId,
                                             @RequestBody ItemDto itemDto) {
        if (itemDto.getName() == null && itemDto.getDescription() == null && itemDto.getAvailable() == null) {
            throw new IllegalArgumentException("Поля значений пустые");
        }
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> findItem(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @PathVariable Long itemId) {
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findUserItems(@RequestHeader(Constants.HEADER_USER_ID) Long userId,
                                                @RequestParam(defaultValue = "0") @Min(0) Long from,
                                                @RequestParam(defaultValue = "10") @Min(1) Long size) {
        return itemClient.findUserItems(userId, from, size);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @RequestParam String text,
                                              @RequestParam(defaultValue = "0") @Min(0) Long from,
                                              @RequestParam(defaultValue = "10") @Min(1) Long size) {
        return itemClient.searchItems(userId, text, from, size);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @PathVariable Long itemId,
                                             @RequestBody @Valid CommentDto commentDto) {
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
