package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM genres WHERE id = ?", this::buildGenre, id);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException("Жанр с id " + id + " не найден.");
        }
    }

    @Override
    public List<Genre> findAllGenres() {
        return jdbcTemplate.query("SELECT * FROM genres", this::buildGenre);
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String qs = "SELECT * FROM genres g " +
                "INNER JOIN films_genres fg on g.id = fg.genres_id " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(qs, this::buildGenre, filmId);
    }

    @Override
    public void filmGenreUpdate(List<Genre> genreList, Integer filmId) {
        List<Genre> withoutRepetitions = genreList.stream().distinct().collect(Collectors.toList());
        jdbcTemplate.batchUpdate(
                "INSERT INTO films_genres (genres_id,film_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, withoutRepetitions.get(i).getId());
                        ps.setInt(2, filmId);
                    }

                    public int getBatchSize() {
                        return withoutRepetitions.size();
                    }
                });
    }

    @Override
    public void deleteGenresByFilmId(int filmId) {
        jdbcTemplate.update("DELETE FROM films_genres WHERE film_id = ?", filmId);
    }

    private Genre buildGenre(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }

    public static Genre buildGenreByRs(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
