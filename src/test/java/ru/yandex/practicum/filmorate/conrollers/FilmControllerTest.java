package ru.yandex.practicum.filmorate.conrollers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.Month;

public class FilmControllerTest {

    private FilmController fm = new FilmController(new FilmService(new InMemoryFilmStorage()));

    @Test
    public void filmCreatePositiveTest() {
        Film film = Film.builder().id(1).name("film1").description("").releaseDate(LocalDate.now()).duration(100).build();
        film.setDescription("desc");
        Film newFilm = fm.createFilm(film);
        Assertions.assertEquals(film, newFilm);
        Assertions.assertEquals(1, fm.getAllFilms().size());
    }

    @Test
    public void filmUpdatetest() {
        Film film = Film.builder().id(3).name("film1").description("").releaseDate(LocalDate.now()).duration(100).build();
        film.setDescription("desc");
        Film newFilm = fm.createFilm(film);
        newFilm.setDescription("new desc");
        Film updatedFilm = fm.updateFilm(newFilm);
        Assertions.assertEquals(newFilm, updatedFilm);
        Assertions.assertEquals(1, fm.getAllFilms().size());
    }

    @Test
    public void filmValidateNegativeDurationTest() {
        Film film = Film.builder().id(1).name("film1").description("").releaseDate(LocalDate.now()).duration(-100).build();
        film.setDescription("desc");
        Assertions.assertThrows(ValidationException.class, () -> fm.createFilm(film));
    }

    @Test
    public void filmValidateNegativeDateTest() {
        Film film = Film.builder().id(1).name("film1").description("").releaseDate(LocalDate.of(1700, Month.DECEMBER, 12)).duration(100).build();
        film.setDescription("desc");
        Assertions.assertThrows(ValidationException.class, () -> fm.createFilm(film));
    }

    @Test
    public void filmValidateNegativeDescTest() throws ValidationException {
        Film film = Film.builder().id(1).name("film1").description("").releaseDate(LocalDate.of(1700, Month.DECEMBER, 12)).duration(100).build();
        film.setDescription("a".repeat(201));
        Assertions.assertThrows(ValidationException.class, () -> fm.createFilm(film));
    }

    @Test
    public void filmValidateNegativeNullTest() {
        Film film = null;
        Assertions.assertThrows(ValidationException.class, () -> fm.createFilm(film));
    }
}
