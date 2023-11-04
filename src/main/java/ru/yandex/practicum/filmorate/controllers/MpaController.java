package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
@Slf4j
public class MpaController {

    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa getRatingById(@PathVariable int id) {
        log.info("Получен запрос на получение рейтинга с ID - " + id);
        return mpaService.getById(id);
    }

    @GetMapping
    public List<Mpa> findAllRatings() {
        log.info("Получен запрос на получение списка MPA");
        return mpaService.findAllRatings();
    }

}
