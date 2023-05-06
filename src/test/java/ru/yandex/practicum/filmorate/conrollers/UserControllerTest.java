package ru.yandex.practicum.filmorate.conrollers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

public class UserControllerTest {

    private UserController uc = new UserController();

    @Test
    public void userCreatePositiveTest() {
        User user = new User(1, "User1@mail.ru", "login", "name", LocalDate.now().minusYears(10));
        User newUser = uc.createUser(user);
        Assertions.assertEquals(user, newUser);
        Assertions.assertEquals(1, uc.getAllUsers().size());
    }

    @Test
    public void userValidateNegativeEmailTest() {
        User user = new User(1, "", "login", "name",LocalDate.now().minusYears(10));
        User newUser = uc.createUser(user);
        Assertions.assertNull(newUser);
        Assertions.assertEquals(0, uc.getAllUsers().size());
    }

    @Test
    public void userValidateNegativeLoginTest() {
        User user = new User(1, "User1", "", "name",LocalDate.now().minusYears(10));
        User newUser = uc.createUser(user);
        Assertions.assertNull(newUser);
        Assertions.assertEquals(0, uc.getAllUsers().size());
    }

    @Test
    public void userValidateNegativeNullTest() {
        User user = null;
        User newUser = uc.createUser(user);
        Assertions.assertNull(newUser);
        Assertions.assertEquals(0, uc.getAllUsers().size());
    }
}
