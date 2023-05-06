package ru.yandex.practicum.model;

import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class Film {
    @NonNull
    private final int id;
    @NonNull
    private final String name;
    @NonNull
    private String description;
    @NonNull
    private final LocalDateTime releaseDate;
    @NonNull
    private final Duration duration;

}