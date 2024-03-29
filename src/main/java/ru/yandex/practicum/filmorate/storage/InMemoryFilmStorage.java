package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(films.size() + 1);
        films.put(film.getId(), film);
        log.info("Добавлен объект " + film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Изменен объект " + film);
            return film;
        }
        throw new RuntimeException("Объект не найден", new IOException());
    }

    @Override
    public Film getFilmById(Integer id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new NotFoundException("Объект не найден");
    }
}
