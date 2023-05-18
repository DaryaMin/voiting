INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANTS (name)
VALUES ('Пушкин'),
('Воронеж'),
('Дрова');

INSERT INTO MENU (restaurant_id, menu_date, name, price)
VALUES (1, '2023-05-18', 'Суп', 9500),
(1, '2023-05-18', 'Каша', 50055),
(1, '2023-05-18', 'Компот', 10000),
(1, '2023-05-18', 'Борщ', 10000),
(3, '2023-05-18', 'Макароны', 50555),
(3, '2023-05-18', 'Чай', 10500),
(3,'2023-05-18', 'Манты 1', 250);

INSERT INTO VOTE (vote_date, restaurant_id, user_id)
VALUES ('2023-05-10', 1, 1),
('2023-05-10', 1, 2),
('2023-05-13', 2, 1),
('2023-05-11', 1, 1),
('2023-05-11', 2, 2);