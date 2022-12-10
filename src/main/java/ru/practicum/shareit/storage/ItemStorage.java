package ru.practicum.shareit.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.storage.item.ItemStorageInterface;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemStorage implements ItemStorageInterface {

    private List<Item> items = new ArrayList<>();

    private Long id;
    @Override
    public Item create(Item item) {
        getId();
        item.setId(id);
        items.add(item);
        return item;
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
        return items;
    }

    @Override
    public Item getById(Long id) {
        return null;
    }

    private void getId() {
        if (id == null || id == 0) {
            id = 1L;
        } else {
            id++;
        }
    }
}
