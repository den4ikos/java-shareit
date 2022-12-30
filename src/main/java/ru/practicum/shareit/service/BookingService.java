package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StatusType;
import ru.practicum.shareit.booking.validation.CustomValidation;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.repository.CommentRepository;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking updateStatus(User user, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found!"));
        CustomValidation.isOwnerCanChangeApproveBookingStatus(booking, user);
        CustomValidation.isBookingStatusApproved(booking);
        List<Booking> approvedAllBookings = bookingRepository.findByItemOwnerIdAndEndIsAfterAndStatusIs(user.getId(), LocalDateTime.now(), StatusType.APPROVED);
        if (approved && CustomValidation.isApprovedBookings(approvedAllBookings, booking)) {
            booking.setStatus(StatusType.APPROVED);
        } else {
            booking.setStatus(StatusType.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    public Booking findById(User user, Long bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        CustomValidation.isUserAccessToBooking(booking, user);
        return booking;
    }

    public List<Booking> getAll(User user, String state) {
        try {
            StatusType status = StatusType.valueOf(state);
            List<Booking> bookingList = null;
            switch (status) {
                case ALL:
                    bookingList = bookingRepository.findByBookerIdOrderByEndDesc(user.getId());
                    break;
                case FUTURE:
                    bookingList = bookingRepository.findByBookerIdAndStartIsAfterAndEndIsAfterOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now());
                    break;
                case PAST:
                    bookingList = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now());
                    break;
                case CURRENT:
                    bookingList = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now());
                    break;
                default:
                    bookingList = bookingRepository.findByBookerIdAndStatusEqualsOrderByEndDesc(user.getId(), status);
                    break;
            }
            return bookingList;
        } catch (RuntimeException e) {
            throw new BadRequestException(String.format("Unknown state: %s", state));
        }
    }

    public List<Booking> getAllForOwner(User user, String state) {
        try {
            StatusType state1 = StatusType.valueOf(state);
            List<Booking> bookingList;
            switch (state1) {
                case ALL:
                    bookingList = bookingRepository.findByItemOwnerIdOrderByEndDesc(user.getId());
                    break;
                case PAST:
                    bookingList = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now());
                    break;
                case FUTURE:
                    bookingList = bookingRepository.findByItemOwnerIdAndStartIsAfterAndEndIsAfterOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now());
                    break;
                case CURRENT:
                    bookingList = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now());
                    break;
                default:
                    bookingList = bookingRepository.findByItemOwnerIdAndStatusEqualsOrderByEndDesc(user.getId(), state1);
                    break;
            }
            return bookingList;
        } catch (RuntimeException e) {
            throw new BadRequestException(String.format("Unknown state: %s", state));
        }
    }
}
