package ru.yandex.practicum.filmorate.conrollers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

public class FilmControllerTest {

    private FilmController fm = new FilmController();

    @Test
    public void filmCreatePositiveTest() {
        Film film = new Film(1, "film1", "", LocalDate.now(), 100);
        film.setDescription("desc");
        Film newFilm = fm.createFilm(film);
        Assertions.assertEquals(film, newFilm);
        Assertions.assertEquals(1, fm.getAllFilms().size());
    }

    @Test
    public void filmUpdatetest() {
        Film film = new Film(3, "film1", "", LocalDate.now(), 100);
        film.setDescription("desc");
        Film newFilm = fm.createFilm(film);
        newFilm.setDescription("new desc");
        Film updatedFilm = fm.updateFilm(newFilm);
        Assertions.assertEquals(newFilm, updatedFilm);
        Assertions.assertEquals(1, fm.getAllFilms().size());
    }

    @Test
    public void filmValidateNegativeDurationTest() {
        Film film = new Film(1, "film1", "", LocalDate.now(), -100);
        film.setDescription("desc");
        Assertions.assertThrows(ValidationException.class, () -> fm.createFilm(film));
    }

    @Test
    public void filmValidateNegativeDateTest() {
        Film film = new Film(1, "film1", "", LocalDate.of(1700, Month.DECEMBER, 12), 100);
        film.setDescription("desc");
        Assertions.assertThrows(ValidationException.class, () -> fm.createFilm(film));
    }

    @Test
    public void filmValidateNegativeDescTest() throws ValidationException {
        Film film = new Film(1, "film1", "", LocalDate.of(1700, Month.DECEMBER, 12), 100);
        film.setDescription("a".repeat(201));
        Assertions.assertThrows(ValidationException.class, () -> fm.createFilm(film));
    }

    @Test
    public void filmValidateNegativeNullTest() {
        Film film = null;
        Assertions.assertThrows(ValidationException.class, () -> fm.createFilm(film));
    }
}
