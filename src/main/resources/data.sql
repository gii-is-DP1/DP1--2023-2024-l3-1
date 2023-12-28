-- Para los hashes de las contraseñas, utilizar: https://bcrypt-generator.com/
-- Para generar los IDs de usuario, utilizar: https://www.uuidgenerator.net/version4

-- Usuario administrador. Contraseña: dobble_admin
INSERT INTO players(id,is_admin,email,username,password) VALUES (1,1,'admin@example.com','admin','$2a$12$31ScIzRMI5q2RYnQnKIwJOoPCsuO05QZLeI4tlBdQJd2mehNPreaS');
-- Usuario normal. Contraseña: dobble
INSERT INTO players(id,is_admin,email,username,password,profile_icon) VALUES (2,0,'dobble@example.com','dobble','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga','DELFIN');
INSERT INTO players(id,is_admin,email,username,password,profile_icon) VALUES (3,0,'dobble2@example.com','dobble2','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga','MANO_LOGO');

-- Partidas actuales, en juego o pasadas, la primera está ya finalizada y la creó el jugador "dobble". 
-- La segunda está en el lobby y la creó "dobble2" 
INSERT INTO games(id,name,start,finish,raw_creator_id,max_players) VALUES ('123e4567-e89b-12d3-a456-426655440000', 'partida1','2023-11-23 18:00:05','2023-11-23 18:03:02',2,6); 
INSERT INTO games(id,name,start,finish,raw_creator_id,max_players) VALUES ('123e4567-e89b-12d3-a456-324833943923', 'partida2',null,null,3,5); 
INSERT INTO games(id,name,start,finish,raw_creator_id,max_players) VALUES ('123e4567-e89b-12d3-a456-324833943924', 'partida3','2023-12-24 14:00:54',null,3,5); 

INSERT INTO games_raw_players(raw_players_id, game_id) VALUES (2,'123e4567-e89b-12d3-a456-426655440000');
INSERT INTO games_raw_players(raw_players_id, game_id) VALUES (3,'123e4567-e89b-12d3-a456-324833943923');

INSERT INTO game_players(id,player_id, game_id) VALUES (1,2,'123e4567-e89b-12d3-a456-426655440000');
INSERT INTO game_players(id,player_id, game_id) VALUES (2,3,'123e4567-e89b-12d3-a456-324833943923');



INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (2, 'Campeón Dobble', 'Gana un total de 60 partidas', 'https://i.pinimg.com/originals/83/b1/f3/83b1f39083f8dc4a4e31c1b4b8e8706e.png', 50.0, 'VICTORIES');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (3, 'Leyenda Dobble', 'Gana un total de 100 partidas', 'https://i.pinimg.com/originals/83/b1/f3/83b1f39083f8dc4a4e31c1b4b8e8706e.png', 100.0, 'VICTORIES');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (4, 'Entrando en Calor', 'Juega tu primera partida', 'https://static.vecteezy.com/system/resources/previews/010/898/286/original/game-cube-dice-png.png', 1.0, 'GAMES_PLAYED');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (5, 'Jugador Veterano', 'Juega un total de 200 partidas', 'https://static.vecteezy.com/system/resources/previews/010/898/286/original/game-cube-dice-png.png', 200.0, 'GAMES_PLAYED');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (6, 'Máster del Tiempo', 'Encuentra un símbolo coincidente en menos de 5 segundo', 'https://cdn-icons-png.flaticon.com/512/2985/2985534.png', 5.0, 'REACTION_TIME');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (7, 'Ojo de Halcón', 'Encuentra un símbolo coincidente en menos de 1 segundo', 'https://cdn-icons-png.flaticon.com/512/2985/2985534.png', 1.0, 'REACTION_TIME');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (8, 'Dobble Experto', 'Juega Dobble durante 100 horas', 'https://static.vecteezy.com/system/resources/previews/024/097/592/non_2x/timer-chronometer-watch-free-png.png', 100.0, 'TOTAL_PLAY_TIME');
INSERT INTO achievement (id, name, description, badge_image, threshold, metric) VALUES (9, 'Iniciador', 'Gana 1 partida', null, 1.0, 'VICTORIES');


INSERT INTO figures(id, icon) VALUES (1, 'AGUA'),
 (2, 'ANCLA'), (3, 'ARBOL'), (4, 'BIBERON'), (5, 'BOMBILLA'),
(6, 'BOMBA'), (7, 'BUHO'), (8, 'CACTUS'), (9, 'CALAVERA'), (10, 'CANDADO'),
(11, 'CABALLO_AJEDREZ'),(12, 'CEBRA'), (13, 'CLAVE_SOL'), (14, 'COCHE'), (15, 'COPO_NIEVE'),
(16, 'CORAZON'), (17, 'DELFIN'), (18, 'DINOSAURIO'), (19, 'DRAGON'),
(20, 'EXCLAMACION'), (21, 'FANTASMA'), (22, 'FLOR'), (23, 'FUEGO'),
(24, 'GAFAS'), (25, 'GATO'), (26, 'HIELO'), (27, 'HOJA_CADUCA'), (28, 'IGLU'),
(29, 'INTERROGACION'), (30, 'LABIOS'), (31, 'LAPIZ'), (32, 'LLAVE'),
(33, 'LUNA'), (34, 'MANO_LOGO'), (35, 'MARIQUITA'), (36, 'MARTILLO'),
(37, 'MANZANA'), (38, 'MUNECO'), (39, 'OBJETIVO'), (40, 'OJO'), (41, 'PAYASO'),
(42, 'PERRO'), (43, 'PINTURA'), (44, 'PROHIBIDO'), (45, 'QUESO'), (46, 'RAYO'),
(47, 'RELOJ'), (48, 'SNOWMAN'), (49, 'SOL'), (50, 'SPIDER'), (51, 'TELARANA'), (52, 'TIJERAS'),
(53, 'TORTUGA'), (54, 'TREBOL'), (55, 'VELA'), (56, 'YIN_YANG'), (57, 'ZANAHORIA');

--'PROHIBIDO,MANZANA,BIBERON,DRAGON,MARTILLO,YIN_YANG,TREBOL,FLOR'
INSERT INTO cards (id) VALUES (1);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (1,43), (1,36), (1,4), (1,18), (1,35), (1,54), (1,52), (1,21);
--'OBJETIVO,FANTASMA,TELARANA,SOL,IGLU,CALAVERA,FLOR,CEBRA'
INSERT INTO cards (id) VALUES (2);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (2,39), (2,21), (2,51), (2,49), (2,28), (2,9), (2,22), (2,12);
--'BUHO,MANZANA,FANTASMA,DINOSAURIO,MARIQUITA,ANCLA,TORTUGA,DELFIN'
INSERT INTO cards (id) VALUES (3);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (3,7), (3,37), (3,21), (3,18), (3,35), (3,2), (3,53), (3,17);
--'OBJETIVO,BUHO,CACTUS,AGUA,CANDADO,MARTILLO,LUNA,PINTURA'
--INSERT INTO cards (id) VALUES (4);
--INSERT INTO cards_figures(cards_id, figures_id) VALUES (4,), (4,), (4,), (4,), (4,), (4,), (4,), (4,);

--INSERT INTO cards (id) VALUES (5);
--INSERT INTO cards_figures(cards_id, figures_id) VALUES (5,), (5,), (5,), (5,), (5,), (5,), (5,), (5,);

--INSERT INTO cards (id) VALUES (6);
--INSERT INTO cards_figures(cards_id, figures_id) VALUES (6,), (6,), (6,), (6,), (6,), (6,), (6,), (6,);

--INSERT INTO cards (id) VALUES (7);
--INSERT INTO cards_figures(cards_id, figures_id) VALUES (7,), (7,), (7,), (7,), (7,), (7,), (7,), (7,);

--INSERT INTO cards (id) VALUES (8);
--INSERT INTO cards_figures(cards_id, figures_id) VALUES (8,), (8,), (8,), (8,), (8,), (8,), (8,), (8,);

--INSERT INTO cards (id) VALUES (9);
--INSERT INTO cards_figures(cards_id, figures_id) VALUES (9,), (9,), (9,), (9,), (9,), (9,), (9,), (9,);





--INSERT INTO cards (id, icons) VALUES (2,'OBJETIVO,FANTASMA,TELARANA,SOL,IGLU,CALAVERA,FLOR,CEBRA' );
--INSERT INTO cards (id, icons) VALUES (3, 'BUHO,MANZANA,FANTASMA,DINOSAURIO,MARIQUITA,ANCLA,TORTUGA,DELFIN');
--INSERT INTO cards (id, icons) VALUES (4, 'OBJETIVO,BUHO,CACTUS,AGUA,CANDADO,MARTILLO,LUNA,PINTURA');
--INSERT INTO cards (id, icons) VALUES (5, 'MANO_LOGO,COPO_NIEVE,VELA,ANCLA,TREBOL,MUNECO,CALAVERA,PINTURA');
--INSERT INTO cards (id, icons) VALUES (6, 'INTERROGACION,OBJETIVO,PERRO,MANZANA,CLAVE_SOL,VELA,RELOJ,FUEGO');
--INSERT INTO cards (id, icons) VALUES (7, 'PAYASO,AGUA,LLAVE,CEBRA,MANZANA,MUNECO,QUESO,BOMBILLA');
--INSERT INTO cards (id, icons) VALUES (8, 'LAPIZ,GAFAS,CANDADO,FANTASMA,BIBERON,VELA,BOMBILLA,HOJA_CADUCA');
--INSERT INTO cards (id, icons) VALUES (9, 'LAPIZ,SOL,CLAVE_SOL,BOMBA,SPIDER,DELFIN,MUNECO,MARTILLO');
--INSERT INTO cards (id, icons) VALUES (10, 'LAPIZ,PROHIBIDO,LLAVE,TELARANA,DINOSAURIO,TIJERAS,PINTURA,PERRO');
--INSERT INTO cards (id, icons) VALUES (11, 'OJO,CEBRA,CANDADO,DINOSAURIO,ZANAHORIA,SPIDER,TREBOL,FUEGO');
--INSERT INTO cards (id, icons) VALUES (12, 'COCHE,BIBERON,IGLU,FUEGO,DELFIN,PINTURA,QUESO,SNOWMAN');
--INSERT INTO cards (id, icons) VALUES (13, 'SNOWMAN,OBJETIVO,GAFAS,PROHIBIDO,ANCLA,RAYO,SPIDER,PAYASO');
--INSERT INTO cards (id, icons) VALUES (14, 'OJO,FANTASMA,ARBOL,PROHIBIDO,RELOJ,LUNA,MUNECO,COCHE');
--INSERT INTO cards (id, icons) VALUES (15, 'COCHE,AGUA,TELARANA,VELA,HIELO,MARIQUITA,DRAGON,SPIDER');
--INSERT INTO cards (id, icons) VALUES (16, 'INTERROGACION,MANO_LOGO,COCHE,GAFAS,CEBRA,TORTUGA,MARTILLO,TIJERAS');
--INSERT INTO cards (id, icons) VALUES (17, 'OJO,INTERROGACION,LAPIZ,AGUA,IGLU,ANCLA,YIN_YANG,GATO');
--INSERT INTO cards (id, icons) VALUES (18, 'PAYASO,COCHE,BUHO,PERRO,SOL,HOJA_CADUCA,GATO,TREBOL');
--INSERT INTO cards (id, icons) VALUES (19, 'PAYASO,ARBOL,LABIOS,VELA,DINOSAURIO,IGLU,MARTILLO,CORAZON');
