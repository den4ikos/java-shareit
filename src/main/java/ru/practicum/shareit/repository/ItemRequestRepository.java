package ru.practicum.shareit.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findItemRequestByRequestorIdOrderByCreatedDesc(Long userId);

    List<ItemRequest> findAllByRequestorIdIsNotOrderByCreatedDesc(Long userId, PageRequest pageRequest);
}
