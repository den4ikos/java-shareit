package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StatusType;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.validation.CustomCommentValidation;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.repository.CommentRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final BookingRepository bookingRepository;

    public Comment save(User user, Item item, CommentDto comment) {
        CustomCommentValidation.isCommentForOwnItem(user, item);
        List<Booking> bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(user.getId(), LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(0, 1))
                .stream()
                .filter(b -> b.getStatus().equals(StatusType.APPROVED))
                .collect(Collectors.toList());
        if (bookings.isEmpty()) {
            throw new BadRequestException("You have no right to leave the comment");
        }

        return commentRepository.save(CommentMapper.toComment(user, item, comment.getText()));
    }
}
