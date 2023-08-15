package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.mparating.MpaRatingDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {

    private final MpaRatingDao mpaRatingDao;

    public List<Mpa> findAllRatings() {
        log.info("Получен список всех рейтингов MPA");
        return mpaRatingDao.getAllRatings();
    }

    public Mpa getById(int id) {
        if (id < 0) {
            throw new NotFoundException("Неверный идентификатор рейтинга MPA");
        }
        log.info("Получен рейтинг MPA с ID" + id);
        return mpaRatingDao.getById(id);
    }

}
