DELETE FROM comments;
DELETE FROM bookings;
DELETE FROM items;
DELETE FROM requests;
DELETE FROM users;

ALTER TABLE comments ALTER COLUMN id RESTART WITH 1;
ALTER TABLE bookings ALTER COLUMN id RESTART WITH 1;
ALTER TABLE items ALTER COLUMN id RESTART WITH 1;
ALTER TABLE requests ALTER COLUMN id RESTART WITH 1;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;;

INSERT INTO users (name, email) VALUES
('Пользователь №1', 'mail1@mail.ru'),
('Пользователь №2', 'mail2@mail.ru'),
('Пользователь №3', 'mail3@mail.ru'),
('Пользователь №4', 'mail4@mail.ru'),
('Пользователь №5', 'mail5@mail.ru');

INSERT INTO requests (description, requestor, created_date) VALUES
('Нужна вещь №1', 1, '2025-09-12 10:00:00'),
('Нужна вещь №2', 2, '2025-09-13 11:00:00'),
('Нужна вещь №3', 3, '2025-09-14 12:00:00'),
('Нужна вещь №4', 4, '2025-09-15 13:00:00'),
('Нужна вещь №5', 5, '2025-09-16 14:00:00');

INSERT INTO items (name, description, is_available, owner_id, request_id) VALUES
('Название вещи №1', 'Описание вещи №1', true, 1, 1),
('Название вещи №2', 'Описание вещи №2', true, 2, 2),
('Название вещи №3', 'Описание вещи №3', false, 3, 3),
('Название вещи №4', 'Описание вещи №4', true, 4, 4),
('Название вещи №5', 'Описание вещи №5', true, 5, 5);

INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) VALUES
('2025-08-10 09:00:00', '2025-09-10 18:00:00', 1, 2, 'APPROVED'),
('2025-08-11 10:00:00', '2025-09-11 19:00:00', 2, 3, 'WAITING'),
('2025-08-12 11:00:00', '2025-09-12 20:00:00', 3, 1, 'REJECTED'),
('2025-08-13 12:00:00', '2025-09-13 21:00:00', 4, 1, 'APPROVED'),
('2025-08-14 13:00:00', '2025-09-14 22:00:00', 5, 2, 'CANCELED');

INSERT INTO comments (text, item_id, author_id, created_date) VALUES
('Комментарий №1', 1, 1, '2025-08-10 09:00:00'),
('Комментарий №2', 2, 2, '2025-08-10 09:00:00'),
('Комментарий №3', 3, 3, '2025-08-10 09:00:00'),
('Комментарий №4', 4, 4, '2025-08-10 09:00:00'),
('Комментарий №5', 5, 5, '2025-08-10 09:00:00');