package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDaoTests {

    private final FilmDao filmDao;
    private final UserDao userDao;

    @Test
    void createFilm() {

        Film film = Film.builder().id(1).name("film").description("desc").releaseDate(LocalDate.of(2023, 1, 1)).duration(100).mpa(new Mpa(4, "R")).build();
        filmDao.createFilm(film);

        assertEquals(1, filmDao.findAllFilms().size());

        assertEquals("R", filmDao.getFilmById(1).getMpa().getName());
        assertEquals("film", filmDao.getFilmById(1).getName());
        assertEquals(100, filmDao.getFilmById(1).getDuration());

    }

    @Test
    void updateFilm() {

        Film film = Film.builder().id(1).name("film").description("desc").releaseDate(LocalDate.of(2023, 1, 1)).duration(100).mpa(new Mpa(4, "R")).build();
        filmDao.createFilm(film);
        Film film2 = Film.builder().id(1).name("film2").description("desc2").releaseDate(LocalDate.of(2023, 1, 1)).duration(120).mpa(new Mpa(4, "R")).build();

        filmDao.updateFilm(film2);

        assertEquals("R", filmDao.getFilmById(1).getMpa().getName());
        assertEquals("film2", filmDao.getFilmById(1).getName());
        assertEquals(120, filmDao.getFilmById(1).getDuration());
    }

    @Test
    void getFilmByIdFromDatabase() {

        Film film = Film.builder().id(1).name("film").description("desc").releaseDate(LocalDate.of(2023, 1, 1)).duration(100).mpa(new Mpa(4, "R")).build();
        filmDao.createFilm(film);
        Film film2 = Film.builder().id(1).name("film2").description("desc2").releaseDate(LocalDate.of(2023, 1, 1)).duration(120).mpa(new Mpa(4, "R")).build();

        filmDao.createFilm(film2);

        assertEquals("film", filmDao.getFilmById(1).getName());
        assertEquals("film2", filmDao.getFilmById(2).getName());
    }

    @Test
    void findAllFilmsFromDatabase() {

        Film film = Film.builder().id(1).name("film").description("desc").releaseDate(LocalDate.of(2023, 1, 1)).duration(100).mpa(new Mpa(4, "R")).build();
        filmDao.createFilm(film);
        Film film2 = Film.builder().id(1).name("film2").description("desc2").releaseDate(LocalDate.of(2023, 1, 1)).duration(120).mpa(new Mpa(4, "R")).build();

        filmDao.createFilm(film2);

        List<Film> films = filmDao.findAllFilms();

        assertEquals(2, films.size());
    }

    @Test
    void addLikeToAddedFilm() {

        Film film1 = Film.builder().id(1).name("film").description("desc").releaseDate(LocalDate.of(2023, 1, 1)).duration(100).mpa(new Mpa(4, "R")).build();
        filmDao.createFilm(film1);


        User testUser = new User(1, "user@mail.ru", "User1", "UserName",
                LocalDate.of(1990, 1, 1));
        userDao.createUser(testUser);

        User testUser1 = new User(1, "user2@mail.ru", "User2", "UserName2",
                LocalDate.of(1990, 1, 1));
        userDao.createUser(testUser1);

        filmDao.addLike(film1.getId(), testUser.getId());
        filmDao.addLike(film1.getId(), testUser1.getId());

        List<Integer> filmLikes = filmDao.getFilmLikesList(film1.getId());

        assertEquals(filmLikes.size(), 2);
    }

    @Test
    void removeLikeFromAddedFilm() {

        Film film1 = Film.builder().id(1).name("film").description("desc").releaseDate(LocalDate.of(2023, 1, 1)).duration(100).mpa(new Mpa(4, "R")).build();
        filmDao.createFilm(film1);


        User testUser = new User(1, "user@mail.ru", "User1", "UserName",
                LocalDate.of(1990, 1, 1));
        userDao.createUser(testUser);

        User testUser1 = new User(1, "user2@mail.ru", "User2", "UserName2",
                LocalDate.of(1990, 1, 1));
        userDao.createUser(testUser1);

        filmDao.addLike(film1.getId(), testUser.getId());
        filmDao.addLike(film1.getId(), testUser1.getId());
        filmDao.removeLike(film1.getId(), testUser.getId());

        List<Integer> filmLikesList = filmDao.getFilmLikesList(film1.getId());

        assertEquals(filmLikesList.size(), 1);
    }

    @Test
    public void findMostPopularFilms() {
        Film film1 = Film.builder().id(1).name("film").description("desc").releaseDate(LocalDate.of(2023, 1, 1)).duration(100).mpa(new Mpa(4, "R")).build();
        filmDao.createFilm(film1);

        Film film2 = Film.builder().id(2).name("film2").description("desc2").releaseDate(LocalDate.of(2023, 1, 1)).duration(120).mpa(new Mpa(4, "R")).build();

        filmDao.createFilm(film2);

        Film film3 = Film.builder().id(3).name("film3").description("desc3").releaseDate(LocalDate.of(2023, 1, 1)).duration(120).mpa(new Mpa(4, "R")).build();

        filmDao.createFilm(film3);

        User user = new User(1, "user@mail.ru", "User1", "UserName",
                LocalDate.of(1990, 1, 1));
        userDao.createUser(user);

        filmDao.addLike(film1.getId(), user.getId());
//        filmDao.addLike(film2.getId(), user.getId());

        List<Film> popularFilm = filmDao.findMostPopularFilms(1);

        assertEquals(popularFilm.size(), 1);
        assertEquals("film", popularFilm.get(0).getName());

    }
}