package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {
    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);

    int deleteUser(int id);

    User getUserById(int id);

    List<User> getFriendsList(int userId);

    List<User> getMutualFriends(int userId, int friendId);

    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);
}
