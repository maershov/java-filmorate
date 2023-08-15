package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoTests {

    private final GenreDao genreDao;

    @Test
    void getGenreById() {
        Genre genre1 = genreDao.getById(1);
        Genre genre2 = genreDao.getById(3);
        Genre genre3 = genreDao.getById(5);

        assertEquals("Комедия", genre1.getName());
        assertEquals("Мультфильм", genre2.getName());
        assertEquals("Документальный", genre3.getName());
    }

    @Test
    void findAllGenres() {
        List<Genre> list = genreDao.findAllGenres();

        assertEquals(6, list.size());

        assertEquals("Драма", list.get(1).getName());
        assertEquals("Триллер", list.get(3).getName());
        assertEquals("Боевик", list.get(5).getName());
    }
}

