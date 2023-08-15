package ru.yandex.practicum.filmorate.conrollers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

public class UserControllerTest {

    private UserController uc = new UserController(new UserService(new InMemoryUserStorage()));

    @Test
    public void userCreatePositiveTest() {
        User user = new User(1, "User1@mail.ru", "login", "name", LocalDate.now().minusYears(10));
        user.setName("name");
        User newUser = uc.createUser(user);
        Assertions.assertEquals(user, newUser);
        Assertions.assertEquals(1, uc.getAllUsers().size());
    }

    @Test
    public void userUpdatetest() {
        User user = new User(2, "User1@mail.ru", "login", "name", LocalDate.now().minusYears(10));
        user.setName("name");
        User newUser = uc.createUser(user);
        newUser.setEmail("newemail@mail.ru");
        User updatedUser = uc.updateUser(newUser);
        Assertions.assertEquals(updatedUser, newUser);
    }

    @Test
    public void userValidateNegativeEmailTest() {
        User user = new User(1, "", "login", "name", LocalDate.now().minusYears(10));
        user.setName("name");
        Assertions.assertThrows(ValidationException.class, () -> uc.createUser(user));
    }

    @Test
    public void userValidateNegativeLoginTest() {
        User user = new User(1, "User1", "", "name", LocalDate.now().minusYears(10));
        user.setName("name");
        Assertions.assertThrows(ValidationException.class, () -> uc.createUser(user));
    }

    @Test
    public void userValidateNegativeNullTest() {
        User user = null;
        Assertions.assertThrows(ValidationException.class, () -> uc.createUser(user));
    }
}
