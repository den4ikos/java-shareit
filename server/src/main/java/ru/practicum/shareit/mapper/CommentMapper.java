package ru.practicum.shareit.mapper;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment toComment(User user, Item item, String text) {
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setText(text);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }
}
