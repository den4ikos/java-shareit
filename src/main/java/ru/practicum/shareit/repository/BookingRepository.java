package ru.practicum.shareit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StatusType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByItemOwnerIdAndEndIsAfterAndStatusIs(Long userId, LocalDateTime time, StatusType status);

    List<Booking> findByBookerIdOrderByEndDesc(Long id);

    Optional<Booking> findTopByItemIdAndEndIsBeforeAndStatusIsOrderByEndDesc(Long id, LocalDateTime now, StatusType approved);

    Optional<Booking> findTopByItemIdAndStartIsAfterAndStatusIsNotAndStatusIsNotOrderByEndDesc(Long itemId, LocalDateTime time, StatusType status, StatusType status1);

    List<Booking> findByItemOwnerIdAndStartIsAfterAndEndIsAfterOrderByEndDesc(Long id, LocalDateTime now, LocalDateTime now1);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(Long userId, LocalDateTime time, LocalDateTime time1);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(Long userId, LocalDateTime time, LocalDateTime time1);

    List<Booking> findByItemOwnerIdAndStatusEqualsOrderByEndDesc(Long userId, StatusType status);

    List<Booking> findByBookerIdAndStartIsAfterAndEndIsAfterOrderByEndDesc(Long bookerId, LocalDateTime time, LocalDateTime time1);

    List<Booking> findByItemOwnerIdOrderByEndDesc(Long ownerId);

    List<Booking> findByBookerIdAndStatusEqualsOrderByEndDesc(Long bookerID, StatusType status);
}
