package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {
    @NonNull
    private int id;
    @NonNull
    @Email(message = "Email should be valid")
    @Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
    @NotBlank
    @NotNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private final LocalDate birthday;
}
