package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        try {
            validate(film);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения Film" + ex.getMessage());
            throw ex;
        }
        film.setId(films.size() + 1);
        films.put(film.getId(), film);
        log.info("Добавлен объект " + film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            validate(film);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения Film" + ex.getMessage());
            throw ex;
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Изменен объект " + film);
            return film;
        }
        throw new ValidationException("Объект не найден", new IOException());
    }

    @Override
    public Film getFilmById(Integer id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new ValidationException("Объект не найден", new IOException());
    }

    private void validate(Film film) {
        LocalDate checkDate = LocalDate.of(1895, Month.DECEMBER, 28);
        if (film == null) {
            throw new ValidationException("Передан пустой объект", new IOException());
        } else if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("Невалидное название", new IOException());
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Невалидное описание", new IOException());
        } else if (film.getReleaseDate().isBefore(checkDate)) {
            throw new ValidationException("Невалидная дата", new IOException());
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Невалидная продолжительность", new IOException());
        }
    }
}
