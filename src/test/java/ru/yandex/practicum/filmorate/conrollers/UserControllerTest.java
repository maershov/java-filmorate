package ru.yandex.practicum.filmorate.conrollers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    private final UserController userController;

    @Test
    public void userCreatePositiveTest() {
        User user = new User(1, "User1@mail.ru", "login", "name", LocalDate.now().minusYears(10));
        user.setName("name");
        User newUser = userController.createUser(user);
        Assertions.assertEquals(user, newUser);
        Assertions.assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    public void userUpdatetest() {
        User user = new User(2, "User1@mail.ru", "login", "name", LocalDate.now().minusYears(10));
        user.setName("name");
        User newUser = userController.createUser(user);
        newUser.setEmail("newemail@mail.ru");
        User updatedUser = userController.updateUser(newUser);
        Assertions.assertEquals(updatedUser, newUser);
    }

    @Test
    public void userValidateNegativeEmailTest() {
        User user = new User(1, "", "login", "name", LocalDate.now().minusYears(10));
        user.setName("name");
        Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void userValidateNegativeLoginTest() {
        User user = new User(1, "User1", "", "name", LocalDate.now().minusYears(10));
        user.setName("name");
        Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void userValidateNegativeNullTest() {
        User user = null;
        Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
    }
}
