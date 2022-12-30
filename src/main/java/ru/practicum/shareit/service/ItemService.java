package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StatusType;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoToUser;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.repository.CommentRepository;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository storage;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public Item create(Item item) {
        return storage.save(item);
    }

    public Item update(Item item, User user) {
        return storage.save(item);
    }

    public Item getById(Long itemId) {
        return storage.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
    }

    public List<Item> getAll() {
        return storage.findAll();
    }

    public List<Item> getAllByUserId(Long userId) {
        return storage.findAllByOwnerIdOrderById(userId);
    }

    public List<Item> search(Map<String, Object> params) {
        if (params.containsKey("text")) {
            String searchText = params.get("text").toString();
            if (searchText.isBlank() || searchText.isEmpty()) return List.of();
            return storage.searchAllByParams(params.get("text").toString());
        }
        return List.of();
    }

    public Item setFieldsToUpdate(Item item, ItemDto itemFromRequest, User user) {
        Item i = new Item();
        i.setId(itemFromRequest.getId());
        if (itemFromRequest.getName() != null) {
            i.setName(itemFromRequest.getName());
        } else {
            i.setName(item.getName());
        }
        if (itemFromRequest.getDescription() != null) {
            i.setDescription(itemFromRequest.getDescription());
        } else {
            i.setDescription(item.getDescription());
        }
        if (itemFromRequest.getAvailable() != null) {
            i.setAvailable(itemFromRequest.getAvailable());
        } else {
            i.setAvailable(item.getAvailable());
        }
        i.setOwner(user);
        if (itemFromRequest.getRequest() != null) {
            i.setRequest(itemFromRequest.getRequest());
        } else {
            i.setRequest(item.getRequest());
        }
        return i;
    }

    public void checkUserForItem(Item item, User user) {
        if (item.getOwner() != null && !item.getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("Users for this item are mismatch");
        }
    }

    public List<ItemDtoToUser> getUserItems(User user) {
        List<Item> items = storage.findAllByOwnerIdOrderById(user.getId());
        if (items.isEmpty()) {
            throw new NotFoundException("Item not found!");
        }

        List<ItemDtoToUser> userItemsAndBookings = items.stream()
                .map(item -> new ItemDtoToUser(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getAvailable(),
                        null,
                        null,
                        getItemComments(item)
                ))
                .collect(Collectors.toList());
        userItemsAndBookings
                .forEach(item -> {
                    Optional<Booking> lastBooking = bookingRepository.findTopByItemIdAndEndIsBeforeAndStatusIsOrderByEndDesc(
                            item.getId(),
                            LocalDateTime.now(),
                            StatusType.APPROVED
                    );
                    Optional<Booking> nextBooking = bookingRepository.findTopByItemIdAndStartIsAfterAndStatusIsNotAndStatusIsNotOrderByEndDesc(
                            item.getId(),
                            LocalDateTime.now(),
                            StatusType.CANCELED,
                            StatusType.REJECTED
                    );
                    nextBooking.ifPresent(booking -> item.setNextBooking(BookingMapper.toBookingDtoToOwnerItemToUser(booking)));
                    lastBooking.ifPresent(booking -> item.setLastBooking(BookingMapper.toBookingDtoToOwnerItemToUser(booking)));
                });
        return userItemsAndBookings;
    }
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    public List<CommentDto> getItemComments(Item item) {
        List<Comment> comments = commentRepository.findCommentsByItemId(item.getId());
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
