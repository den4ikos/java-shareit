package ru.practicum.shareit.storage.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemStorageInterface {
    Item create(Item item);

    Item update(Item item);

    void delete(Item item);

    List<Item> getAll();

    Item getById(Long id);

    List<Item> getByUserId(Long userId);

    List<Item> search(Map<String, Object> params);
}
