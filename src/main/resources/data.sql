INSERT INTO USERS (NAME, EMAIL, PASSWORD, CALORIES_PER_DAY)
VALUES ('User', 'user@yandex.ru', '{noop}password', 2005),
       ('Admin', 'admin@gmail.com', '{noop}admin', 1900),
       ('Guest', 'guest@gmail.com', '{noop}guest', 2000);

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANTS (name)
VALUES ('Пушкин'),
('Воронеж'),
('Дрова');

INSERT INTO MENU (restaurant_id, name, price, created)
VALUES (1, 'Суп', 9500, '2023-05-10'),
(1, 'Каша', 50055, '2023-05-10'),
(1, 'Компот', 10000, '2023-05-10'),
(1, 'Борщ', 10000, '2023-05-11'),
(1, 'Макароны', 50555, '2023-05-11'),
(1, 'Чай', 10500, '2023-05-11');

INSERT INTO VOTES (user_id, restaurant_id, date)
VALUES (2, 3, '2023-05-10'),
(1, 2, '2023-05-10'),
(2, 1, '2023-05-10'),
(1, 1, '2023-05-10'),
(2, 2, '2023-05-10');