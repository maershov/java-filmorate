package ru.yandex.practicum.filmorate.conrollers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmControllerTest {

    private final FilmController filmController;

    @Test
    public void filmCreatePositiveTest() {
        Film film = Film.builder().id(1).name("film1").description("").releaseDate(LocalDate.now()).duration(100).mpa(new Mpa(4, "R")).build();
        film.setDescription("desc");
        Film newFilm = filmController.createFilm(film);
        Assertions.assertEquals(film, newFilm);
        Assertions.assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    public void filmUpdatetest() {
        Film film = Film.builder().id(3).name("film1").description("").releaseDate(LocalDate.now()).duration(100).mpa(new Mpa(4, "R")).genres(new ArrayList<Genre>()).likedByUserIds(new ArrayList<Integer>()).build();
        film.setDescription("desc");
        Film newFilm = filmController.createFilm(film);
        newFilm.setDescription("new desc");
        Film updatedFilm = filmController.updateFilm(newFilm);
        Assertions.assertEquals(newFilm, updatedFilm);
        Assertions.assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    public void filmValidateNegativeDurationTest() {
        Film film = Film.builder().id(1).name("film1").description("").releaseDate(LocalDate.now()).duration(-100).build();
        film.setDescription("desc");
        Assertions.assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmValidateNegativeDateTest() {
        Film film = Film.builder().id(1).name("film1").description("").releaseDate(LocalDate.of(1700, Month.DECEMBER, 12)).duration(100).build();
        film.setDescription("desc");
        Assertions.assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmValidateNegativeDescTest() throws ValidationException {
        Film film = Film.builder().id(1).name("film1").description("").releaseDate(LocalDate.of(1700, Month.DECEMBER, 12)).duration(100).build();
        film.setDescription("a".repeat(201));
        Assertions.assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmValidateNegativeNullTest() {
        Film film = null;
        Assertions.assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }
}
