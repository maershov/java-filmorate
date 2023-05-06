package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        try {
            validate(user);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения объекта User" + ex.getMessage());
            throw ex;
        }
        user.setId(users.size() + 1);
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Добавлен объект " + user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        try {
            validate(user);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения объекта User" + ex.getMessage());
            throw ex;
        }
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        }
        throw new ValidationException("Объект не найден", new IOException());
    }

    private void validate(User user) {
        if (user == null) {
            throw new ValidationException("Передан пустой объект", new IOException());
        } else if (user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            throw new ValidationException("Невалидный email", new IOException());
        } else if (!user.getEmail().contains("@")) {
            throw new ValidationException("Невалидный email - должен содержать @", new IOException());
        } else if (user.getLogin().isBlank() || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Невалидный login", new IOException());
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Невалидная дата рождения - дата в будущем", new IOException());
        }
    }
}
