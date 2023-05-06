package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private List<Film> films = new ArrayList<>();

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return films;
    }

    @PostMapping("/film")
    public Film createFilm(@RequestBody Film film) {
        try {
            validate(film);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения Film" + ex.getMessage());
            return null;
        }
        films.add(film);
        log.info("Добавлен объект " + film);
        return film;
    }

    @PutMapping("/film")
    public Film updateFilm(@RequestBody Film film) {
        try {
            validate(film);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения Film" + ex.getMessage());
            return null;
        }
        for (Film f : films) {
            if (f.getId() == film.getId()) {
                f = film;
                log.info("Изменен объект " + film);
                return f;
            }
        }
        return null;
    }

    private void validate(Film film) {
        LocalDateTime checkDate = LocalDateTime.of(1895, Month.DECEMBER, 28, 0, 0);
        if (film == null) {
            throw new ValidationException("Передан пустой объект", new IOException());
        }
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("Невалидное название", new IOException());
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Невалидное описание", new IOException());
        } else if (film.getReleaseDate().isBefore(checkDate)) {
            throw new ValidationException("Невалидная дата", new IOException());
        } else if (film.getDuration().isNegative()) {
            throw new ValidationException("Невалидная продолжительность", new IOException());
        }
    }
}
