package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;
import static ru.yandex.practicum.filmorate.dao.genre.GenreDaoImpl.buildGenreByRs;

@Repository
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAllFilms() {
        String qs = "SELECT * FROM films AS f " +
                "LEFT JOIN MPA_rating m ON m.MPA_id = f.mpa_rating_id;";
        List<Film> allFilms = jdbcTemplate.query(qs, this::buildFilm);

        addGenresToFilmsList(allFilms);
        addLikesToFilmsList(allFilms);

        return allFilms;
    }

    @Override
    public Film createFilm(Film film) {
        Map<String, Object> films = new HashMap<>();
        films.put("name", film.getName());
        films.put("description", film.getDescription());
        films.put("release_date", Date.valueOf(film.getReleaseDate()));
        films.put("duration", film.getDuration());
        films.put("mpa_rating_id", film.getMpa().getId());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(films).intValue());

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String qs = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_rating_id = ? WHERE film_id = ?";
        int result = jdbcTemplate.update(qs, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (result == 0) {
            throw new NotFoundException("Фильм не найден.");
        }
        return getFilmById(film.getId());
    }

    @Override
    public int deleteFilm(int id) {
        final String qs = "DELETE FROM films WHERE film_id = ?";
        return jdbcTemplate.update(qs, id);
    }

    @Override
    public Film getFilmById(int id) {
        String qs = "SELECT * FROM films AS f " +
                "LEFT JOIN MPA_rating m ON m.MPA_id = f.mpa_rating_id " +
                "WHERE f.film_id = ?;";
        try {
            return jdbcTemplate.queryForObject(qs, this::buildFilm, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Фильм не найден.");
        }
    }

    @Override
    public List<Film> findMostPopularFilms(int count) {
        final String qs = "SELECT * FROM films AS f " +
                "LEFT JOIN MPA_rating m ON m.MPA_id = f.mpa_rating_id " +
                "LEFT OUTER JOIN film_likes fl on f.film_id = fl.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(fl.film_id) " +
                "DESC LIMIT ?";

        List<Film> popularFilm = jdbcTemplate.query(qs, this::buildFilm, count);

        addGenresToFilmsList(popularFilm);
        addLikesToFilmsList(popularFilm);
        return popularFilm;
    }

    @Override
    public void addLike(int filmId, int userId) {
        jdbcTemplate.update("INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)", filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        jdbcTemplate.update("DELETE FROM film_likes WHERE user_id = ? AND film_id = ?", filmId, userId);
    }

    @Override
    public List<Integer> getFilmLikesList(int filmId) {
        final String qs = "SELECT user_id FROM film_likes WHERE film_id = ?;";

        return jdbcTemplate.queryForList(qs, Integer.class, filmId);
    }

    private void addGenresToFilmsList(List<Film> films) {
        String filmIds = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        final String sqlQuery = "SELECT * FROM genres g, films_genres fg WHERE fg.genres_id = g.id AND fg.film_id IN (" + filmIds + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getInt("film_id"));
            film.getGenres().add(buildGenreByRs(rs));
        });
    }

    private void addLikesToFilmsList(List<Film> films) {
        String qs = "SELECT * FROM film_likes;";

        Map<Integer, Film> filmById = films.stream()
                .collect(Collectors.toMap(Film::getId, identity()));

        jdbcTemplate.query(qs, (rs) -> {
            Integer filmId = rs.getInt("film_id");
            Integer userId = rs.getInt("user_id");

            Optional.ofNullable(filmById.get(filmId))
                    .ifPresent(film -> film.getLikedByUserIds().add(userId));
        });
    }

    private Film buildFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(Mpa.builder()
                        .id(rs.getInt("MPA_id"))
                        .name(rs.getString("MPA_name"))
                        .build())
                .genres(new ArrayList<>())
                .likedByUserIds(new ArrayList<>())
                .build();
    }
}
