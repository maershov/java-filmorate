package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage us) {
        this.userStorage = us;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        try {
            validate(user);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения объекта User" + ex.getMessage());
            throw ex;
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        try {
            validate(user);
        } catch (ValidationException ex) {
            log.info("Ошибка заполнения объекта User" + ex.getMessage());
            throw ex;
        }
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.updateUser(user);
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public User addFriend(Integer userId, Integer friendId) {
        if (userId <= 0 || friendId <= 0) {
            throw new NotFoundException(String.format(
                    "При создании связи между %s и %s были переданы отрицательные значения",
                    userId, friendId));
        }
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriendsIds().add(friendId);
        friend.getFriendsIds().add(userId);
        updateUser(user);
        updateUser(friend);
        log.info(String.format(
                "Пользователь %s добавил в друзья %s",
                userId, friendId));
        return user;
    }

    public User removeFriend(Integer userId, Integer friendId) {
        if (userId <= 0 || friendId <= 0) {
            throw new NotFoundException(String.format(
                    "При удалении связи между %s и %s были переданы отрицательные значения",
                    userId, friendId));
        }
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriendsIds().remove(friendId);
        friend.getFriendsIds().remove(userId);
        updateUser(user);
        updateUser(friend);
        log.info(String.format("Пользователь %s удалил из друзей %s",
                userId, friendId));
        return user;
    }

    public List<User> getUserFriendsList(Integer userId) {
        if (userId <= 0) {
            throw new NotFoundException(String.format(
                    "При поиске списка друзей для %s было передано отрицательные значения",
                    userId));
        }
        User user = userStorage.getUserById(userId);
        List<User> friendsList = new ArrayList<>();
        for (Integer id : user.getFriendsIds()) {
            friendsList.add(userStorage.getUserById(id));
        }
        log.info(String.format("Получен список друзей для пользователя id = %s", user.getId()));
        return friendsList;
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        if (userId <= 0 || friendId <= 0) {
            throw new NotFoundException(String.format(
                    "При получении списка общих друзей между %s и %s были переданы отрицательные значения",
                    userId, friendId));
        }
        log.info(String.format("Получение списка общих пользователей между %s и %s", userId, friendId));
        return getUserById(userId).getFriendsIds().stream()
                .filter(getUserById(friendId).getFriendsIds()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    private void validate(User user) {
        if (user == null) {
            throw new ValidationException("Передан пустой объект", new IOException());
        } else if (user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            throw new ValidationException("Невалидный email", new IOException());
//        } else if (!user.getEmail().contains("@")) {
//            throw new ValidationException("Невалидный email - должен содержать @", new IOException());
        } else if (user.getLogin().isBlank() || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Невалидный login", new IOException());
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Невалидная дата рождения - дата в будущем", new IOException());
        }
    }
}
