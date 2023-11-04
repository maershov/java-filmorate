package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.mparating.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDaoTests {

    private final MpaRatingDao mpaDao;

    @Test
    void getMpaRatingById() {
        Mpa mpaRating1 = mpaDao.getById(1);
        Mpa mpaRating2 = mpaDao.getById(3);
        Mpa mpaRating3 = mpaDao.getById(5);

        assertEquals("G", mpaRating1.getName());
        assertEquals("PG-13", mpaRating2.getName());
        assertEquals("NC-17", mpaRating3.getName());
    }

    @Test
    void getAllMpaRating() {
        List<Mpa> listMpaRating = mpaDao.getAllRatings();

        assertEquals(5, listMpaRating.size());

        assertEquals("PG", listMpaRating.get(1).getName());
        assertEquals("R", listMpaRating.get(3).getName());
        assertEquals("NC-17", listMpaRating.get(4).getName());
    }
}
