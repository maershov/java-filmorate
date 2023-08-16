package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao us) {
        this.userDao = us;
    }

    public List<User> getAllUsers() {
        log.info("Получен список всех пользователей");
        return userDao.findAllUsers();
    }

    public User createUser(User user) {
        validate(user);
        log.info("Пользователь сохранен");
        return userDao.createUser(user);
    }

    public User updateUser(User user) {
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        validate(user);
        log.info("Данные пользователя обновлены");
        return userDao.updateUser(user);
    }

    public int deleteUser(int id) {
        log.info("Пользователь удален");
        return userDao.deleteUser(id);
    }

    public User getUserById(int id) {
        if (id < 0) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        log.info("Получен пользователь с id " + id);
        return userDao.getUserById(id);
    }

    public User addFriend(int userId, int friendId) {
        if (userId < 0 || friendId < 0) {
            throw new NotFoundException("Пользователь с id " + userId + " или " + friendId + " не найден");
        }

        log.info("Пользователь " + userId + " добавлен в список друзей " + friendId);
        userDao.addFriend(userId, friendId);
        return userDao.getUserById(userId);
    }

    public void removeFriend(int userId, int friendId) {
        if (userId < 0 || friendId < 0) {
            throw new NotFoundException("Пользователь с id " + userId + " или " + friendId + " не найден");
        }

        log.info("Пользователь " + userId + " удален из списка друзей " + friendId);
        userDao.removeFriend(userId, friendId);
    }

    public List<User> getUserFriendsList(int userId) {
        if (userId < 0) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        log.info("Список друзей пользователя " + userId);
        return userDao.getFriendsList(userId);
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        if (userId < 0 || friendId < 0) {
            throw new NotFoundException("Пользователь с id " + userId + " или " + friendId + " не найден");
        }
        log.info("Получен список общих друзей пользователя " + userId + " и пользователя " + friendId + ".");
        return userDao.getMutualFriends(userId, friendId);
    }

    private void validate(User user) {
        if (user == null) {
            throw new ValidationException("Передан пустой объект", new IOException());
        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Невалидный адрес почты", new IOException());
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("Невалидный логин", new IOException());
        } else if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Невалидная дата рождения", new IOException());
        }
    }
}
