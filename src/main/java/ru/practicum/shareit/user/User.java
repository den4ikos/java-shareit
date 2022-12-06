package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class User {

    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @Email(message = "Please provide a valid email address")
    private String email;
}
