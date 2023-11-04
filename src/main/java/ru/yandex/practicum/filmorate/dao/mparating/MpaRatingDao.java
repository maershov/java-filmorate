package ru.yandex.practicum.filmorate.dao.mparating;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaRatingDao {

    Mpa getById(int id);

    List<Mpa> getAllRatings();
}
