package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Film {
    @NonNull
    private int id;
    @NonNull
    private final String name;
    @NonNull
    private String description;
    @NonNull
    private final LocalDate releaseDate;
    @NonNull
    private final Duration duration;

}