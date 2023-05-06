package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return users;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        try {
            validate(user);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения объекта User" + ex.getMessage());
            return null;
        }
        user.setId(users.size());
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.add(user);
        log.info("Добавлен объект " + user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        try {
            validate(user);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения объекта User" + ex.getMessage());
            return null;
        }
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        for (User u : users) {
            if (u.getId() == user.getId()) {
                u = user;
                log.info("изменен объект " + user);
                return u;
            }
        }
        return null;
    }

    private void validate(User user) {
        if (user == null) {
            throw new ValidationException("Передан пустой объект", new IOException());
        }
        if(user.getEmail().isEmpty() || user.getEmail().isBlank()) {
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
