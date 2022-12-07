package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank
    @NotNull
    private String name;

    @NotNull
    @NotBlank
    @Email(message = "Please provide a valid email address")
    private String email;
}
