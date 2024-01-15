-- Para los hashes de las contraseñas, utilizar: https://bcrypt-generator.com/
-- Para generar los IDs de usuario, utilizar: https://www.uuidgenerator.net/version4

-- Usuario administrador. Contraseña: dobble_admin
INSERT INTO players(id,is_admin,email,username,password) VALUES (1,1,'admin@example.com','admin','$2a$12$31ScIzRMI5q2RYnQnKIwJOoPCsuO05QZLeI4tlBdQJd2mehNPreaS');
-- Usuarios normales. Contraseña: dobble
INSERT INTO players(id,is_admin,email,username,password,profile_icon) VALUES (2,0,'dobble@example.com','dobble','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga','DELFIN');
INSERT INTO players(id,is_admin,email,username,password,profile_icon) VALUES (3,0,'dobble2@example.com','dobble2','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga','MANO_LOGO');
INSERT INTO players(id,is_admin,email,username,password,profile_icon) VALUES (4,0,'dobble3@example.com','dobble3','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga','MANO_LOGO');
INSERT INTO players(id,is_admin,email,username,password,profile_icon) VALUES (5,0,'dobble4@example.com','dobble4','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga','MANO_LOGO');
INSERT INTO players(id,is_admin,email,username,password,profile_icon) VALUES (6,0,'dobble5@example.com','dobble5','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga','MANO_LOGO');
INSERT INTO players(id,is_admin,email,username,password,profile_icon) VALUES (7,0,'dobble6@example.com','dobble6','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga','MANO_LOGO');
INSERT INTO players(id,is_admin,email,username,password,profile_icon) VALUES (8,0,'dobble7@example.com','dobble7','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga','MANO_LOGO');


--INSERT INTO games(id, name, start, max_players) VALUES ('123e4567-e89b-12d3-a456-426655440000', 'partidaDobble', null,  8);

--INSERT INTO game_players(id, player_id, game_id, strikes) VALUES (1, 2, '123e4567-e89b-12d3-a456-426655440000', 0);

--UPDATE games SET creator_id = 1 WHERE id = '123e4567-e89b-12d3-a456-426655440000';

--INSERT INTO game_players(id,player_id,game_id, strikes) VALUES (2,3,'123e4567-e89b-12d3-a456-426655440000',0);
--INSERT INTO game_players(id,player_id,game_id, strikes) VALUES (3,4,'123e4567-e89b-12d3-a456-426655440000',0);
--INSERT INTO game_players(id,player_id,game_id, strikes) VALUES (4,5,'123e4567-e89b-12d3-a456-426655440000',0);
--INSERT INTO game_players(id,player_id,game_id, strikes) VALUES (5,6,'123e4567-e89b-12d3-a456-426655440000',0);
--INSERT INTO game_players(id,player_id,game_id, strikes) VALUES (6,7,'123e4567-e89b-12d3-a456-426655440000',0);
--INSERT INTO game_players(id,player_id,game_id, strikes) VALUES (7,8,'123e4567-e89b-12d3-a456-426655440000',0);









INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (2, 'Campeon Dobble', 'Gana un total de 60 partidas', 'https://i.pinimg.com/originals/83/b1/f3/83b1f39083f8dc4a4e31c1b4b8e8706e.png', 50.0, 'VICTORIES');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (3, 'Leyenda Dobble', 'Gana un total de 100 partidas', 'https://i.pinimg.com/originals/83/b1/f3/83b1f39083f8dc4a4e31c1b4b8e8706e.png', 100.0, 'VICTORIES');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (4, 'Entrando en Calor', 'Juega tu primera partida', 'https://static.vecteezy.com/system/resources/previews/010/898/286/original/game-cube-dice-png.png', 1.0, 'GAMES_PLAYED');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (5, 'Jugador Veterano', 'Juega un total de 200 partidas', 'https://static.vecteezy.com/system/resources/previews/010/898/286/original/game-cube-dice-png.png', 200.0, 'GAMES_PLAYED');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (6, 'Master del Tiempo', 'Encuentra un simbolo coincidente en menos de 5 segundo', 'https://cdn-icons-png.flaticon.com/512/2985/2985534.png', 5.0, 'REACTION_TIME');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (7, 'Ojo de Halcon', 'Encuentra un simbolo coincidente en menos de 1 segundo', 'https://cdn-icons-png.flaticon.com/512/2985/2985534.png', 1.0, 'REACTION_TIME');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (8, 'Dobble Experto', 'Juega Dobble durante 100 horas', 'https://static.vecteezy.com/system/resources/previews/024/097/592/non_2x/timer-chronometer-watch-free-png.png', 100.0, 'TOTAL_PLAY_TIME');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (9, 'Iniciador', 'Gana 1 partida', null, 1.0, 'VICTORIES');
