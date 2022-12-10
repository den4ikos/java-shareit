package ru.practicum.shareit.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.storage.item.ItemStorageInterface;

import java.util.List;

public class ItemStorage implements ItemStorageInterface {
    @Override
    public Item create(Item item) {
        return null;
    }

    @Override
    public Item update(Item item) {
        return null;
    }

    @Override
    public void delete(Item item) {

    }

    @Override
    public List<Item> getAll() {
        return null;
    }

    @Override
    public Item getById(Long id) {
        return null;
    }
}
