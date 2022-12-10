package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.storage.item.ItemStorageInterface;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorageInterface storage;

    public Item create(Item item) {
        return storage.create(item);
    }
}
