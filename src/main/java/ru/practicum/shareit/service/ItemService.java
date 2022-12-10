package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.storage.item.ItemStorageInterface;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorageInterface storage;

    public Item create(Item item) {
        return storage.create(item);
    }

    public Item update(Item item, User user) {
        Item currentItem = storage.getById(item.getId());
        if (!currentItem.getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("Users for this item are mismatch");
        }
        return storage.update(item);
    }

    public Item getById(Long itemId) {
        return storage.getById(itemId);
    }

    public List<Item> getAll() {
        return storage.getAll();
    }

    public List<Item> getAllByUserId(Long userId) {
        List<Item> items = storage.getByUserId(userId);
        return items;
    }

    public List<Item> search(Map<String, Object> params) {
        return storage.search(params);
    }
}
