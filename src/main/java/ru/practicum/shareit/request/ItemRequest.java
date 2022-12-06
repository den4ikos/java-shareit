package ru.practicum.shareit.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ItemRequest {

    private Long id;

    @NotNull
    @NotBlank
    private String description;

    private Long requestor;

    @DateTimeFormat
    private LocalDateTime created;
}
