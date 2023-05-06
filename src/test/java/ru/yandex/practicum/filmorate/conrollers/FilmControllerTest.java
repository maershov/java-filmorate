package ru.yandex.practicum.filmorate.conrollers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;

public class FilmControllerTest {

    private FilmController fm = new FilmController();

    @Test
    public void filmCreatePositiveTest() {
        Film film = new Film(1, "film1", "", LocalDate.now(), Duration.ofHours(2));
        film.setDescription("desc");
        Film newFilm = fm.createFilm(film);
        Assertions.assertEquals(film, newFilm);
        Assertions.assertEquals(1, fm.getAllFilms().size());
    }

    @Test
    public void filmValidateNegativeDurationTest() {
        Film film = new Film(1, "film1", "", LocalDate.now(), Duration.ofHours(-2));
        film.setDescription("desc");
        Film newFilm = fm.createFilm(film);
        Assertions.assertNull(newFilm);
        Assertions.assertEquals(0, fm.getAllFilms().size());
    }

    @Test
    public void filmValidateNegativeDateTest() {
        Film film = new Film(1, "film1", "", LocalDate.of(1700, Month.DECEMBER, 12), Duration.ofHours(2));
        film.setDescription("desc");
        Film newFilm = fm.createFilm(film);
        Assertions.assertNull(newFilm);
        Assertions.assertEquals(0, fm.getAllFilms().size());
    }

    @Test
    public void filmValidateNegativeDescTest() {
        Film film = new Film(1, "film1", "", LocalDate.of(1700, Month.DECEMBER, 12), Duration.ofHours(2));
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
