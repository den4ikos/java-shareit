package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader(Constants.HEADER_USER_ID) Long userId,
                                             @RequestBody @Valid ItemRequestDto itemRequestDto) {

        return itemRequestClient.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findUserRequests(@RequestHeader(Constants.HEADER_USER_ID) Long userId) {
        return itemRequestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllRequests(@RequestHeader(Constants.HEADER_USER_ID) Long userId,
                                                  @RequestParam(defaultValue = "0") @Min(0) Long from,
                                                  @RequestParam(defaultValue = "10") @Min(1) Long size) {
        return itemRequestClient.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findUserRequest(@RequestHeader(Constants.HEADER_USER_ID) Long userId, @PathVariable Long requestId) {
        return itemRequestClient.getUserRequest(userId, requestId);
    }
}
