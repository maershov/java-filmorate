package ru.yandex.practicum.filmorate.dao.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", this::buildUser);
    }

    @Override
    public User createUser(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        final String qs = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        int result = jdbcTemplate.update(qs, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());
        if (result != 1) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return user;
    }

    @Override
    public int deleteUser(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
    }

    @Override
    public User getUserById(int id) {
        String qs = "SELECT * FROM users WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(qs, this::buildUser, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    @Override
    public List<User> getFriendsList(int userId) {
        String qs = "SELECT u.* FROM friends f " +
                "JOIN users u on f.friend_id = u.user_id " +
                "WHERE f.user_id = ?;";
        return jdbcTemplate.query(qs, this::buildUser, userId);
    }

    @Override
    public List<User> getMutualFriends(int userId, int friendId) {
        String qs = "SELECT u.* FROM friends f " +
                "JOIN users u ON f.friend_id = u.user_id " +
                "WHERE f.user_id = ? OR f.user_id = ?" +
                "GROUP BY f.friend_id " +
                "HAVING COUNT(f.user_id) > 1;";
        return jdbcTemplate.query(qs, this::buildUser, userId, friendId);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String qs = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(qs, userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String qs = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(qs, userId, friendId);
    }

    private User buildUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }
}
