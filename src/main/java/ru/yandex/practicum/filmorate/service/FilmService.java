package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final UserDao userDao;


    @Autowired
    public FilmService(FilmDao fs, GenreDao genreDao, UserDao ud) {
        this.filmDao = fs;
        this.genreDao = genreDao;
        this.userDao = ud;
    }

    public List<Film> getAllFilms() {
        List<Film> list = filmDao.findAllFilms();
        log.info("Получен список всех фильмов.");
        return list;
    }

    public Film getFilmById(int id) {
        Film film = filmDao.getFilmById(id);
        film.setGenres(genreDao.getGenresByFilmId(film.getId()));
        film.setLikedByUserIds(filmDao.getFilmLikesList(film.getId()));
        log.info("Получен фильм с идентификатором " + id + ".");
        return film;
    }

    public Film createFilm(Film film) {
        validate(film);
        Film createdFilm = filmDao.createFilm(film);
        if (film.getGenres() != null) {
            genreDao.filmGenreUpdate(film.getGenres(), createdFilm.getId());
        }
        createdFilm.setGenres(genreDao.getGenresByFilmId(createdFilm.getId()));

        log.info("Фильм сохранен.");
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        validate(film);
        Film updatedFilm = filmDao.updateFilm(film);
        genreDao.deleteGenresByFilmId(film.getId());
        if (film.getGenres() != null) {
            genreDao.filmGenreUpdate(film.getGenres(), film.getId());
        }
        updatedFilm.setGenres(genreDao.getGenresByFilmId(film.getId()));
        updatedFilm.setLikedByUserIds(filmDao.getFilmLikesList(film.getId()));
        log.info("Данные фильма обновлены.");
        return updatedFilm;
    }

    public Film addLike(int filmId, int userId) {
        Film film = filmDao.getFilmById(filmId);
        filmDao.addLike(filmId, userId);
        log.info("Пользователь с id " + userId + " поставил лайк фильму " + film.getName() + ".");
        return film;
    }

    public Film removeLike(int filmId, int userId) {
        Film film = filmDao.getFilmById(filmId);
        if (userDao.getUserById(userId) == null || film == null) {
            throw new NotFoundException("Пользователь или фильм не найден");
        }
        filmDao.removeLike(filmId, userId);
        log.info("Пользователь с id " + userId + " удалил лайк у фильма " + film.getName() + ".");
        return film;

    }

    public List<Film> getTopFilmsByLikes(Integer countFilms) {
        if (countFilms < 1) {
            throw new ValidationException("Количество фильмов не может быть меньше 1!", new IOException());
        }
        List<Film> allFilms = filmDao.findMostPopularFilms(countFilms);
        log.info("Список десяти самых популярных фильмов");
        return allFilms.stream().limit(countFilms).collect(Collectors.toList());
    }

    private void validate(Film film) {
        if (film == null) {
            throw new ValidationException("Передан пустой объект", new IOException());
        }
        if (film.getName().isBlank()) {
            throw new ValidationException("Невалидное название", new IOException());
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Невалидное описание", new IOException());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Невалидная дата релиза", new IOException());
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Невалидная продолжительность", new IOException());

        }
    }
}
