package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.time.LocalDate;
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

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        try {
            validate(film);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения Film" + ex.getMessage());
            return null;
        }
        film.setId(films.size());
        films.add(film);
        log.info("Добавлен объект " + film);
        return film;
    }

    @PutMapping("/films")
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
        LocalDate checkDate = LocalDate.of(1895, Month.DECEMBER, 28);
        if (film == null) {
            throw new ValidationException("Передан пустой объект", new IOException());
        } else if (film.getName().isEmpty() || film.getName().isBlank()) {
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
