-- Para los hashes de las contraseñas, utilizar: https://bcrypt-generator.com/
-- Para generar los IDs de usuario, utilizar: https://www.uuidgenerator.net/version4

-- Usuario administrador. Contraseña: dobble_admin
INSERT INTO players(id,is_admin,email,username,password) VALUES (1,1,'admin@example.com','admin','$2a$12$31ScIzRMI5q2RYnQnKIwJOoPCsuO05QZLeI4tlBdQJd2mehNPreaS');
-- Usuario normal. Contraseña: dobble
INSERT INTO players(id,is_admin,email,username,password) VALUES (2,0,'dobble@example.com','dobble','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga');
INSERT INTO players(id,is_admin,email,username,password) VALUES (3,0,'dobble2@example.com','dobble2','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga');

-- Partidas actuales, en juego o pasadas, la primera está ya finalizada y la creó el jugador "dobble". 
-- La segunda está en el lobby y la creó "dobble2" 
INSERT INTO games(id,name,start,finish,creator_id,max_players) VALUES ('123e4567-e89b-12d3-a456-426655440000', 'partida1','2023-11-23 18:00:05','2023-11-23 18:03:02',2,6); 
INSERT INTO games(id,name,start,finish,creator_id,max_players) VALUES ('123e4567-e89b-12d3-a456-324833943923', 'partida2',null,null,3,5); 

INSERT INTO games_players(players_id, game_id) VALUES (2,'123e4567-e89b-12d3-a456-426655440000');
INSERT INTO games_players(players_id, game_id) VALUES (3,'123e4567-e89b-12d3-a456-324833943923');