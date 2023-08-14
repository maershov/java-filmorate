-- delete from MPA_RATING;
merge INTO MPA_rating (MPA_ID, MPA_NAME) KEY(MPA_id) VALUES (1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');
-- delete from  genres;
merge INTO genres (id, name) KEY(id) VALUES (1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'), (4, 'Триллер'), (5, 'Документальный'), (6, 'Боевик');