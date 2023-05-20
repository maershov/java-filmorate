package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage fs) {
        this.filmStorage = fs;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        try {
            validate(film);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения Film" + ex.getMessage());
            throw ex;
        }
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        try {
            validate(film);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения Film" + ex.getMessage());
            throw ex;
        }
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        if (userId <= 0 || filmId <= 0) {
            throw new NotFoundException(String.format(
                    "При добавлении лайка пользователем %s к фильму %s были переданы отрицательные значения",
                    userId, filmId));
        }
        Film film = getFilmById(filmId);
        film.getLikesUsersIds().add(userId);
        updateFilm(film);
        log.info(String.format("Добавлен лайк пользователя %s к фильму %s", userId, filmId));
        return film;
    }

    public Film removeLike(Integer filmId, Integer userId) {
        if (userId <= 0 || filmId <= 0) {
            throw new NotFoundException(String.format(
                    "При удалении лайка пользователем %s к фильму %s были переданы отрицательные значения",
                    userId, filmId));
        }
        Film film = getFilmById(filmId);
        film.getLikesUsersIds().remove(userId);
        updateFilm(film);
        log.info(String.format("Удален лайк пользователя %s к фильму %s", userId, filmId));
        return film;
    }

    public List<Film> getTop10FilmsByLikes() {
        final Integer amount = 10;
        List<Film> films = filmStorage.getAllFilms();
        films.sort((film1, film2) -> Integer.compare(film2.getLikesUsersIds().size(), film1.getLikesUsersIds().size()));
        log.info("Получение топа фильмов по лайкам");
        return films.stream().limit(amount).collect(Collectors.toList());
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
