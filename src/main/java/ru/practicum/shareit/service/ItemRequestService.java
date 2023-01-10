package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.repository.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validation.ValidationHandler;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;

    public ItemRequest add(ItemRequest request) {
        return itemRequestRepository.save(request);
    }

    public List<ItemRequest> getRequests(User user) {
        return itemRequestRepository.findItemRequestByRequestorIdOrderByCreatedDesc(user.getId());
    }

    public ItemRequest getById(Long requestId) {
        return itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Item Request not found!"));
    }

    public List<ItemRequest> getAll(User user, Map<String, Object> params) {
        int from = ValidationHandler.getAttributeFromRequest(params, "from", 0, 0);
        int size = ValidationHandler.getAttributeFromRequest(params, "size", 10, 1);
        return itemRequestRepository.findAllByRequestorIdIsNotOrderByCreatedDesc(user.getId(), PageRequest.of(from / size, size));
    }
}
