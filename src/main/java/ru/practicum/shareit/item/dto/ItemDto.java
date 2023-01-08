package ru.practicum.shareit.item.dto;

import lombok.*;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemDto {
    private Long id;

    @NotBlank(message = "Name must be not empty")
    @Nullable
    private String name;

    @NotBlank(message = "Description must not be empty")
    @Nullable
    private String description;

    @NotNull(message = "Available is required")
    private Boolean available;
    private User owner;
    private Long requestId;
}
