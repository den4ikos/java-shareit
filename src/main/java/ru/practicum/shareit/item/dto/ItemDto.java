package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name must be not empty")
    private String name;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description must not be empty")
    private String description;

    @NotNull(message = "Available is required")
    private Boolean available;
    private User owner;
    private String request;
}
