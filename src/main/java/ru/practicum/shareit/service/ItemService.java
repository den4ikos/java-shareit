package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository storage;

    public Item create(Item item) {
        return storage.save(item);
    }

    public Item update(Item item, User user) {
        System.out.println("item: " + item);
        Item currentItem = getById(item.getId());
        if (!currentItem.getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("Users for this item are mismatch");
        }
        return storage.save(item);
    }

    public Item getById(Long itemId) {
        return storage.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
    }

    public List<Item> getAll() {
        return storage.findAll();
    }

    public List<Item> getAllByUserId(Long userId) {
        List<Item> items = storage.findAllByOwner(userId);
        return items;
    }

    public List<Item> search(Map<String, Object> params) {
//        return storage.search(params);
        return null;
    }
}
