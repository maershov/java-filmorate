package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDaoTests {

    private final UserDao userDao;

    @Test
    void createUserInDatabase() {

        User user1 = new User(1, "user1@gmail.com", "user1", "user1", LocalDate.of(2000, 1, 1));
        userDao.createUser(user1);

        assertEquals(1, userDao.findAllUsers().size());

        assertEquals("user1", userDao.getUserById(1).getLogin());
        assertEquals("user1@gmail.com", userDao.getUserById(1).getEmail());
        assertEquals("user1", userDao.getUserById(1).getName());

    }

    @Test
    void updateUserWithExistingUser() {

        User user = new User(1, "user2@mail.ru", "user2222", "user2", LocalDate.of(2000, 1, 1));
        userDao.createUser(user);

        User user1 = new User(1, "user3@gmail.com", "user3", "user3", LocalDate.of(2000, 1, 1));

        userDao.updateUser(user1);

        assertEquals("user3", userDao.getUserById(1).getLogin());
        assertEquals("user3", userDao.getUserById(1).getName());
        assertEquals("user3@gmail.com", userDao.getUserById(1).getEmail());
        assertEquals(LocalDate.of(2000, 1, 1), userDao.getUserById(1).getBirthday());
    }

    @Test
    void getUserByIdFromDatabase() {

        User user1 = new User(1, "user1@gmail.com", "user1", "user1", LocalDate.of(2000, 1, 1));
        userDao.createUser(user1);

        User user2 = new User(2, "user2@mail.ru", "user2222", "user2", LocalDate.of(2000, 1, 1));
        userDao.createUser(user2);

        assertEquals("user1", userDao.getUserById(1).getName());
        assertEquals("user2", userDao.getUserById(2).getName());
    }

    @Test
    void findAllUsers() {

        User user1 = new User(1, "user1@gmail.com", "user1", "user1", LocalDate.of(2000, 1, 1));
        userDao.createUser(user1);

        User user2 = new User(2, "user2@mail.ru", "user2222", "user2", LocalDate.of(2000, 1, 1));
        userDao.createUser(user2);

        List<User> users = userDao.findAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void addFriendForAUser() {

        User user1 = new User(1, "user1@gmail.com", "user1", "user1", LocalDate.of(2000, 1, 1));
        userDao.createUser(user1);

        User user2 = new User(2, "user2@mail.ru", "user2222", "user2", LocalDate.of(2000, 1, 1));
        userDao.createUser(user2);

        User user3 = new User(1, "user3@gmail.com", "user3", "user3", LocalDate.of(2000, 1, 1));
        userDao.createUser(user3);

        userDao.addFriend(1, 2);
        userDao.addFriend(1, 3);

        List<User> friendsList = userDao.getFriendsList(user1.getId());

        assertEquals(friendsList.size(), 2);
    }

    @Test
    void removeFriendFromUserFriendsList() {

        User user1 = new User(1, "user1@gmail.com", "user1", "user1", LocalDate.of(2000, 1, 1));
        userDao.createUser(user1);

        User user2 = new User(2, "user2@mail.ru", "user2222", "user2", LocalDate.of(2000, 1, 1));
        userDao.createUser(user2);

        User user3 = new User(1, "user3@gmail.com", "user3", "user3", LocalDate.of(2000, 1, 1));
        userDao.createUser(user3);

        userDao.addFriend(1, 2);
        userDao.addFriend(1, 3);

        userDao.removeFriend(1, 2);

        List<User> friendsList = userDao.getFriendsList(user1.getId());

        assertEquals(friendsList.size(), 1);
    }

    @Test
    void getFriendsListForUser() {
        User user1 = new User(1, "user1@gmail.com", "user1", "user1", LocalDate.of(1980, 6, 6));
        userDao.createUser(user1);

        User user2 = new User(2, "user2@mail.ru", "user2222", "user2", LocalDate.of(1976, 11, 2));
        userDao.createUser(user2);

        User user3 = new User(3, "user3@gmail.com", "user3", "user3", LocalDate.of(1990, 5, 5));
        userDao.createUser(user3);

        userDao.addFriend(1, 2);
        userDao.addFriend(1, 3);

        List<User> friendsList = userDao.getFriendsList(user1.getId());

        assertEquals(friendsList.size(), 2);
        assertEquals(friendsList.get(0), user2);
        assertEquals(friendsList.get(1), user3);
    }

    @Test
    public void getMutualFriendsListForUser() {
        User user1 = new User(1, "user1@gmail.com", "user1", "user1", LocalDate.of(2000, 1, 1));
        userDao.createUser(user1);

        User user2 = new User(2, "user4@mail.ru", "user412", "user4", LocalDate.of(2000, 1, 1));
        userDao.createUser(user2);

        User user3 = new User(3, "user5@mail.ru", "user588", "user5", LocalDate.of(2000, 1, 1));
        userDao.createUser(user3);

        User user4 = new User(4, "user6@mail.ru", "user676", "user6", LocalDate.of(2000, 1, 1));
        userDao.createUser(user4);

        User user5 = new User(5, "user7@mail.ru", "user7", "user7", LocalDate.of(2000, 1, 1));
        userDao.createUser(user5);

        User user6 = new User(6, "user8@mail.ru", "sasha", "Aleksandr", LocalDate.of(2000, 1, 1));
        userDao.createUser(user6);

        userDao.addFriend(user1.getId(), user3.getId());
        userDao.addFriend(user2.getId(), user3.getId());
        userDao.addFriend(user4.getId(), user3.getId());
        userDao.addFriend(user5.getId(), user3.getId());
        userDao.addFriend(user6.getId(), user3.getId());

        userDao.addFriend(user2.getId(), user1.getId());
        userDao.addFriend(user4.getId(), user1.getId());
        userDao.addFriend(user5.getId(), user1.getId());
        userDao.addFriend(user6.getId(), user1.getId());
        userDao.addFriend(user3.getId(), user1.getId());

        userDao.addFriend(user2.getId(), user4.getId());
        userDao.addFriend(user4.getId(), user2.getId());
        userDao.addFriend(user5.getId(), user2.getId());
        userDao.addFriend(user6.getId(), user2.getId());
        userDao.addFriend(user3.getId(), user4.getId());

        List<User> mutualFriends = userDao.getMutualFriends(user4.getId(), user5.getId());
        System.out.println(mutualFriends);
        assertEquals(mutualFriends.get(0), user1);
        assertEquals(mutualFriends.get(1), user2);
        assertEquals(mutualFriends.get(2), user3);

        List<User> userCommonFriends = userDao.getMutualFriends(user4.getId(), user6.getId());
        System.out.println(userCommonFriends);
        assertEquals(mutualFriends.get(0), user1);
        assertEquals(mutualFriends.get(1), user2);
        assertEquals(mutualFriends.get(2), user3);

        List<User> mutualFriends1 = userDao.getMutualFriends(user1.getId(), user2.getId());
        System.out.println(mutualFriends1);
        assertEquals(mutualFriends1.get(0).getName(), user3.getName());

        List<User> mutualFriends2 = userDao.getMutualFriends(user2.getId(), user3.getId());
        System.out.println(mutualFriends2);
        assertEquals(mutualFriends2.get(0).getName(), user1.getName());
        assertEquals(mutualFriends2.get(1).getName(), user4.getName());
    }
}
