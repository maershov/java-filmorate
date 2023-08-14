package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;

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
    private final Integer duration;
    private final HashSet<Integer> likesUsersIds = new HashSet<>();

}