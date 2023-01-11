package ru.practicum.shareit.comment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;
    @NotBlank
    String text;
    Long itemId;
    String authorName;
    LocalDateTime created;

    public CommentDto(Long id, String text, Long itemId, String authorName) {
        this.id = id;
        this.text = text;
        this.itemId = itemId;
        this.authorName = authorName;
    }
}
