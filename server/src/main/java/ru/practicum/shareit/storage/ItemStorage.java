package ru.practicum.shareit.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.storage.item.ItemStorageInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ItemStorage implements ItemStorageInterface {
    private List<Item> items = new ArrayList<>();

    private Long id;

    public void setId() {
        if (id == null || id == 0) {
            id = 1L;
        } else {
            id++;
        }
    }

    @Override
    public Item create(Item item) {
        setId();
        item.setId(id);
        items.add(item);
        return item;
    }

    @Override
    public Item update(Item item) {
        Item currentItem = getById(item.getId());

        currentItem.setName(item.getName() != null ? item.getName() : currentItem.getName());
        currentItem.setAvailable(item.getAvailable() != null ? item.getAvailable() : currentItem.getAvailable());
        currentItem.setDescription(item.getDescription() != null ? item.getDescription() : currentItem.getDescription());

        return currentItem;
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
        return items
                .stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item not found!"));
    }

    @Override
    public List<Item> getByUserId(Long userId) {
        return items.stream()
                .filter(i -> i.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(Map<String, Object> params) {
        Stream<Item> items = getAll().stream();
        if (params.containsKey("text")) {
            String formattingText = params.get("text")
                    .toString()
                    .toLowerCase(Locale.ROOT)
                    .trim();

            if (formattingText.isBlank() || formattingText.isEmpty()) {
                return List.of();
            }

            items = items.filter(
                    i -> (i.getName().toLowerCase(Locale.ROOT).contains(formattingText) ||
                    i.getDescription().toLowerCase(Locale.ROOT).contains(formattingText)) &&
                    i.getAvailable() == Boolean.TRUE
            );
        }

        return items.collect(Collectors.toList());
    }
}
