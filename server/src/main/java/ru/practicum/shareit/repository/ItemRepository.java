package ru.practicum.shareit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerIdOrderById(Long ownerId);

    @Query(value = "select * from items i where lower(i.name) like lower(concat('%', ?1, '%')) or lower(i.description) like lower(concat('%', ?1, '%')) and i.is_available=true", nativeQuery = true)
    List<Item> searchAllByParams(String text);

    List<Item> findAllByRequestId(Long requestId);
}
