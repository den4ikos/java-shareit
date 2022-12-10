package ru.practicum.shareit.storage.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorageInterface {
    Item create(Item item);

    Item update(Item item);

    void delete(Item item);

    List<Item> getAll();

    Item getById(Long id);
}
