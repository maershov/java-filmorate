package ru.yandex.practicum.conrollers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.controllers.FilmController;
import ru.yandex.practicum.model.Film;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class FilmControllerTest {

    private FilmController fm = new FilmController();

    @Test
    public void filmCreatePositiveTest() {
        Film film = new Film(1, "film1", "", LocalDateTime.now(), Duration.ofHours(2));
        film.setDescription("desc");
        Film newFilm = fm.createFilm(film);
        Assertions.assertEquals(film, newFilm);
        Assertions.assertEquals(1, fm.getAllFilms().size());
    }

    @Test
    public void filmValidateNegativeDurationTest() {
        Film film = new Film(1, "film1", "", LocalDateTime.now(), Duration.ofHours(-2));
        film.setDescription("desc");
        Film newFilm = fm.createFilm(film);
        Assertions.assertNull(newFilm);
        Assertions.assertEquals(0, fm.getAllFilms().size());
    }

    @Test
    public void filmValidateNegativeDateTest() {
        Film film = new Film(1, "film1", "", LocalDateTime.of(1700, Month.DECEMBER, 12, 1, 1), Duration.ofHours(2));
        film.setDescription("desc");
        Film newFilm = fm.createFilm(film);
        Assertions.assertNull(newFilm);
        Assertions.assertEquals(0, fm.getAllFilms().size());
    }

    @Test
    public void filmValidateNegativeDescTest() {
        Film film = new Film(1, "film1", "", LocalDateTime.of(1700, Month.DECEMBER, 12, 1, 1), Duration.ofHours(2));
        film.setDescription("a".repeat(201));
        Film newFilm = fm.createFilm(film);
        Assertions.assertNull(newFilm);
        Assertions.assertEquals(0, fm.getAllFilms().size());
    }

    @Test
    public void filmValidateNegativeNullTest() {
        Film film = null;
        Film newFilm = fm.createFilm(film);
        Assertions.assertNull(newFilm);
        Assertions.assertEquals(0, fm.getAllFilms().size());
    }
}
