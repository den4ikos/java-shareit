package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Item {

    private Long id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String description;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Byte available;

    private Long owner;

    private String request;
}
