-- Para los hashes de las contraseñas, utilizar: https://bcrypt-generator.com/
-- Para generar los IDs de usuario, utilizar: https://www.uuidgenerator.net/version4

-- Usuario administrador. Contraseña: dobble_admin
INSERT INTO players(id,is_admin,email,username,password) VALUES (1,1,'admin@example.com','admin','$2a$12$31ScIzRMI5q2RYnQnKIwJOoPCsuO05QZLeI4tlBdQJd2mehNPreaS');
-- Usuario normal. Contraseña: dobble
INSERT INTO players(id,is_admin,email,username,password,profile_icon) VALUES (2,0,'dobble@example.com','dobble','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga',0);
INSERT INTO players(id,is_admin,email,username,password) VALUES (3,0,'dobble2@example.com','dobble2','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga');

-- Partidas actuales, en juego o pasadas, la primera está ya finalizada y la creó el jugador "dobble". 
-- La segunda está en el lobby y la creó "dobble2" 
INSERT INTO games(id,name,start,finish,creator_id,max_players) VALUES ('123e4567-e89b-12d3-a456-426655440000', 'partida1','2023-11-23 18:00:05','2023-11-23 18:03:02',2,6); 
INSERT INTO games(id,name,start,finish,creator_id,max_players) VALUES ('123e4567-e89b-12d3-a456-324833943923', 'partida2',null,null,3,5); 

INSERT INTO game_players(player_id, game_id) VALUES (2,'123e4567-e89b-12d3-a456-426655440000');
INSERT INTO game_players(player_id, game_id) VALUES (3,'123e4567-e89b-12d3-a456-324833943923');

INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (2, 'Campeon Dobble', 'Gana un total de 60 partidas', 'https://i.pinimg.com/originals/83/b1/f3/83b1f39083f8dc4a4e31c1b4b8e8706e.png', 50.0, 'VICTORIES');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (3, 'Leyenda Dobble', 'Gana un total de 100 partidas', 'https://i.pinimg.com/originals/83/b1/f3/83b1f39083f8dc4a4e31c1b4b8e8706e.png', 100.0, 'VICTORIES');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (4, 'Entrando en Calor', 'Juega tu primera partida', 'https://static.vecteezy.com/system/resources/previews/010/898/286/original/game-cube-dice-png.png', 1.0, 'GAMES_PLAYED');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (5, 'Jugador Veterano', 'Juega un total de 200 partidas', 'https://static.vecteezy.com/system/resources/previews/010/898/286/original/game-cube-dice-png.png', 200.0, 'GAMES_PLAYED');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (6, 'Master del Tiempo', 'Encuentra un simbolo coincidente en menos de 5 segundo', 'https://cdn-icons-png.flaticon.com/512/2985/2985534.png', 5.0, 'REACTION_TIME');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (7, 'Ojo de Halcon', 'Encuentra un simbolo coincidente en menos de 1 segundo', 'https://cdn-icons-png.flaticon.com/512/2985/2985534.png', 1.0, 'REACTION_TIME');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (8, 'Dobble Experto', 'Juega Dobble durante 100 horas', 'https://static.vecteezy.com/system/resources/previews/024/097/592/non_2x/timer-chronometer-watch-free-png.png', 100.0, 'TOTAL_PLAY_TIME');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (9, 'Iniciador', 'Gana 1 partida', ' ', 1.0, 'VICTORIES');

INSERT INTO cards(id,icon) VALUES (1,'DELFIN, TIJERAS, SNOWMAN, RAYO, FANTASMA, OBJETIVO, GAFAS, EXCLAMACION'); 

--INSERT INTO card_icons(card_id,icon) VALUES (1,'DELFIN'); 

INSERT INTO decks(id) VALUES (1); 

INSERT INTO decks_cards(deck_id, cards_id) VALUES (1,1);


