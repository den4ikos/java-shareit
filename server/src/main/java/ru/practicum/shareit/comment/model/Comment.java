package ru.practicum.shareit.comment.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comments", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "comment")
    String text;
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;
    LocalDateTime created;
}
