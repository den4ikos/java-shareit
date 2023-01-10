package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StatusType;
import ru.practicum.shareit.booking.validation.CustomBookingValidation;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validation.ValidationHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking updateStatus(User user, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found!"));
        CustomBookingValidation.isOwnerCanChangeApproveBookingStatus(booking, user);
        CustomBookingValidation.isBookingStatusApproved(booking);
        List<Booking> approvedAllBookings = bookingRepository.findByItemOwnerIdAndEndIsAfterAndStatusIs(user.getId(), LocalDateTime.now(), StatusType.APPROVED);
        if (approved && CustomBookingValidation.isApprovedBookings(approvedAllBookings, booking)) {
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
        CustomBookingValidation.isUserAccessToBooking(booking, user);
        return booking;
    }

    public List<Booking> getAll(User user, String state, Map<String, Object> params) {
        int from = ValidationHandler.getAttributeFromRequest(params, "from", 0, 0);
        int size = ValidationHandler.getAttributeFromRequest(params, "size", 10, 1);
        int page = from / size;
        try {
            StatusType status = StatusType.valueOf(state);
            List<Booking> bookingList = null;
            switch (status) {
                case ALL:
                    bookingList = bookingRepository.findByBookerIdOrderByEndDesc(user.getId(), PageRequest.of(page, size));
                    break;
                case FUTURE:
                    bookingList = bookingRepository.findByBookerIdAndStartIsAfterAndEndIsAfterOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(page, size));
                    break;
                case PAST:
                    bookingList = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(page, size));
                    break;
                case CURRENT:
                    bookingList = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(page, size));
                    break;
                default:
                    bookingList = bookingRepository.findByBookerIdAndStatusEqualsOrderByEndDesc(user.getId(), status, PageRequest.of(page, size));
                    break;
            }
            return bookingList;
        } catch (RuntimeException e) {
            throw new BadRequestException(String.format("Unknown state: %s", state));
        }
    }

    public List<Booking> getAllForOwner(User user, String state, Map<String, Object> params) {
        int from = ValidationHandler.getAttributeFromRequest(params, "from", 0, 0);
        int size = ValidationHandler.getAttributeFromRequest(params, "size", 10, 1);
        int page = from / size;
        try {
            StatusType state1 = StatusType.valueOf(state);
            List<Booking> bookingList;
            switch (state1) {
                case ALL:
                    bookingList = bookingRepository.findByItemOwnerIdOrderByEndDesc(user.getId(), PageRequest.of(page, size));
                    break;
                case PAST:
                    bookingList = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(page, size));
                    break;
                case FUTURE:
                    bookingList = bookingRepository.findByItemOwnerIdAndStartIsAfterAndEndIsAfterOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(page, size));
                    break;
                case CURRENT:
                    bookingList = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(page, size));
                    break;
                default:
                    bookingList = bookingRepository.findByItemOwnerIdAndStatusEqualsOrderByEndDesc(user.getId(), state1, PageRequest.of(page, size));
                    break;
            }
            return bookingList;
        } catch (RuntimeException e) {
            throw new BadRequestException(String.format("Unknown state: %s", state));
        }
    }

    public Map<String, Object> convertParamsToMap(Integer from, Integer size) {
        return new HashMap<>() {{
            put("from", Integer.toString(from));
            put("size", Integer.toString(size));
        }};
    }
}
