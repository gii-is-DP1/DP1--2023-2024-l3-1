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


--1, 'PROHIBIDO,MANZANA,BIBERON,DRAGON,MARTILLO,YIN_YANG,TREBOL,FLOR'
INSERT INTO cards (id) VALUES (1);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (1,43), (1,36), (1,4), (1,18), (1,35), (1,54), (1,52), (1,21);
--2, 'OBJETIVO,FANTASMA,TELARANA,SOL,IGLU,CALAVERA,FLOR,CEBRA'
INSERT INTO cards (id) VALUES (2);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (2,39), (2,21), (2,51), (2,49), (2,28), (2,9), (2,22), (2,12);
--3, 'BUHO,MANZANA,FANTASMA,DINOSAURIO,MARIQUITA,ANCLA,TORTUGA,DELFIN'
INSERT INTO cards (id) VALUES (3);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (3,7), (3,37), (3,21), (3,18), (3,35), (3,2), (3,53), (3,17);
--4, 'OBJETIVO,BUHO,CACTUS,AGUA,CANDADO,MARTILLO,LUNA,PINTURA'
INSERT INTO cards (id) VALUES (4);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (4,39), (4,7), (4,8), (4,1), (4,10), (4,36), (4,33), (4,43);
--5, 'MANO_LOGO,COPO_NIEVE,VELA,ANCLA,TREBOL,MUNECO,CALAVERA,PINTURA'
INSERT INTO cards (id) VALUES (5);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (5,34), (5,15), (5,55), (5,2), (5,54), (5,38), (5,9), (5,43);
--6, 'INTERROGACION,OBJETIVO,PERRO,MANZANA,CLAVE_SOL,VELA,RELOJ,FUEGO'
INSERT INTO cards (id) VALUES (6);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (6,29), (6,39), (6,42), (6,37), (6,13), (6,55), (6,47), (6,23);
--7, 'PAYASO,AGUA,LLAVE,CEBRA,MANZANA,MUNECO,QUESO,BOMBILLA'
INSERT INTO cards (id) VALUES (7);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (7,41), (7,1), (7,32), (7,12), (7,37), (7,38), (7,45), (7,5);
--8, 'LAPIZ,GAFAS,CANDADO,FANTASMA,BIBERON,VELA,BOMBILLA,HOJA_CADUCA'
INSERT INTO cards (id) VALUES (8);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (8,31), (8,24), (8,10), (8,21), (8,4), (8,55), (8,5), (8,27);
--9, 'LAPIZ,SOL,CLAVE_SOL,BOMBA,SPIDER,DELFIN,MUNECO,MARTILLO'
INSERT INTO cards (id) VALUES (9);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (9,31), (9,49), (9,13), (9,6), (9,50), (9,17), (9,38), (9,36);
--10, 'LAPIZ,PROHIBIDO,LLAVE,TELARANA,DINOSAURIO,TIJERAS,PINTURA,PERRO'
INSERT INTO cards (id) VALUES (10);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (10,31), (10,44), (10,32), (10,51), (10,18), (10,52), (10,43), (10,42);
--11, 'OJO,CEBRA,CANDADO,DINOSAURIO,ZANAHORIA,SPIDER,TREBOL,FUEGO'
INSERT INTO cards (id) VALUES (11);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (11,40), (11,12), (11,10), (11,18), (11,57), (11,50), (11,54), (11,23);
--12, 'COCHE,BIBERON,IGLU,FUEGO,DELFIN,PINTURA,QUESO,SNOWMAN'
INSERT INTO cards (id) VALUES (12);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (12,14), (12,4), (12,28), (12,23), (12,17), (12,43), (12,45), (12,48);
--13, 'SNOWMAN,OBJETIVO,GAFAS,PROHIBIDO,ANCLA,RAYO,SPIDER,PAYASO'
INSERT INTO cards (id) VALUES (13);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (13,48), (13,39), (13,24), (13,44), (13,2), (13,46), (13,50), (13,41);
--14, 'OJO,FANTASMA,ARBOL,PROHIBIDO,RELOJ,LUNA,MUNECO,COCHE'
INSERT INTO cards (id) VALUES (14);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (14,40), (14,21), (14,3), (14,44), (14,47), (14,33), (14,38), (14,14);
--15, 'COCHE,AGUA,TELARANA,VELA,HIELO,MARIQUITA,DRAGON,SPIDER'
INSERT INTO cards (id) VALUES (15);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (15,14), (15,1), (15,51), (15,55), (15,26), (15,35), (15,19), (15,50);
--16, 'INTERROGACION,MANO_LOGO,COCHE,GAFAS,CEBRA,TORTUGA,MARTILLO,TIJERAS'
INSERT INTO cards (id) VALUES (16);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (16,29), (16,34), (16,14), (16,24), (16,12), (16,53), (16,36), (16,52);
--17, 'OJO,INTERROGACION,LAPIZ,AGUA,IGLU,ANCLA,YIN_YANG,GATO'
INSERT INTO cards (id) VALUES (17);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (17,40), (17,29), (17,31), (17,1), (17,28), (17,2), (17,56), (17,25);
--18, 'PAYASO,COCHE,BUHO,PERRO,SOL,HOJA_CADUCA,GATO,TREBOL'
INSERT INTO cards (id) VALUES (18);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (18,41), (18,14), (18,7), (18,42), (18,49), (18,27), (18,25), (18,54);
--19, 'PAYASO, ARBOL, LABIOS, VELA, DINOSAURIO, IGLU, MARTILLO, CORAZON'
INSERT INTO cards (id) VALUES (19);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (19,41), (19,3), (19,30), (19,55), (19,18), (19,28), (19,36), (19,16);
--20, 'CORAZON, COCHE, CACTUS, MANZANA, RAYO, LAPIZ, ZANAHORIA, CALAVERA'
INSERT INTO cards (id) VALUES (20);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (20,16), (20,14), (20,8), (20,37), (20,46), (20,31), (20,57), (20,9);
--21, 'MANO_LOGO, ARBOL, MANZANA, CANDADO, TELARANA, BOMBA, GATO, SNOWMAN'
INSERT INTO cards (id) VALUES (21);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (21,34), (21,3), (21,37), (21,10), (21,51), (21,6), (21,25), (21,48);
--22, 'LAPIZ, MANO_LOGO, CABALLO_AJEDREZ, MARIQUITA, FUEGO, LUNA, FLOR, PAYASO'
INSERT INTO cards (id) VALUES (22);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (22,31), (22,34), (22,11), (22,35), (22,23), (22,33), (22,22), (22,41);
--23, 'QUESO, CACTUS, PERRO, FANTASMA, LABIOS, MANO_LOGO, YIN_YANG, SPIDER'
INSERT INTO cards (id) VALUES (23);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (23,45), (23,8), (23,42), (23,21), (23,30), (23,34), (23,56), (23,50);
--24, 'PERRO, CANDADO, IGLU, DRAGON, TORTUGA, CABALLO_AJEDREZ, RAYO, MUNECO'
INSERT INTO cards (id) VALUES (24);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (24,42), (24,10), (24,28), (24,19), (24,53), (24,11), (24,46), (24,38);
--25, 'GAFAS, CACTUS, LLAVE, RELOJ, IGLU, BOMBA, MARIQUITA, TREBOL'
INSERT INTO cards (id) VALUES (25);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (25,24), (25,8), (25,32), (25,47), (25,28), (25,6), (25,35), (25,54);
--26, 'FANTASMA, LLAVE, COPO_NIEVE, HIELO, RAYO, FUEGO, GATO, MARTILLO'
INSERT INTO cards (id) VALUES (26);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (26,21), (26,32), (26,15), (26,26), (26,46), (26,23), (26,25),  (26,36);
--27, 'CORAZON, RELOJ, BOMBILLA, TORTUGA, SPIDER, GATO, PINTURA, FLOR'
INSERT INTO cards (id) VALUES (27);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (27,16), (27,47), (27,5), (27,53), (27,50), (27,25), (27,43), (27,22);
--28, 'CACTUS, ARBOL, SOL, BOMBILLA, DRAGON, FUEGO, ANCLA, TIJERAS'
INSERT INTO cards (id) VALUES (28);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (28,8), (28,3), (28,49), (28,5), (28,19), (28,23), (28,2), (28,52);
--29, 'ARBOL, CEBRA, CLAVE_SOL, MARIQUITA, HOJA_CADUCA, YIN_YAN, PINTURA, RAYO'
INSERT INTO cards (id) VALUES (29);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (29,3), (29,12), (29,13), (29,35), (29,27), (29,56), (29,43), (29,46);
--30, 'LABIOS, BIBERON, MARIQUITA, ZANAHORIA, TIJERAS, OBJETIVO, MUNECO, GATO'
INSERT INTO cards (id) VALUES (30);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (30,30), (30,4), (30,35), (30,57), (30,52), (30,39), (30,38), (30,25);
--31, 'CORAZON, PERRO,  CEBRA, BIBERON, BOMBA, HIELO, ANCLA, LUNA'
INSERT INTO cards (id) VALUES (31);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (31,16), (31,42), (31,12), (31,4), (31,6), (31,26), (31,2), (31,33);
--32, 'CABALLO_AJEDREZ, TELARANA, RELOJ, ANCLA, HOJA_CADUCA, MARTILLO, QUESO, ZANAHORIA'
INSERT INTO cards (id) VALUES (32);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (32,11), (32,51), (32,47), (32,2), (32,27), (32,36), (32,45), (32,57);
--33, 'INTERROGACION, TELARANA, LABIOS, BOMBILLA, RAYO, TREBOL, LUNA, DELFIN'
INSERT INTO cards (id) VALUES (33);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (33,29), (33,51), (33,30), (33,5), (33,46), (33,54), (33,33), (33,17);
--34, 'PAYASO, CANDADO, RELOJ, YIN_YAN, HIELO, CALAVERA, DELFIN, TIJERAS'
INSERT INTO cards (id) VALUES (34);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (34,41), (34,10), (34,47), (34,56), (34,26), (34,9), (34,17), (34,52);
--35, 'EXCLAMACION, COCHE, LLAVE, CANDADO, LABIOS, ANCLA, CLAVE_SOL, FLOR'
INSERT INTO cards (id) VALUES (35);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (35,20), (35,14), (35,32), (35,10), (35,30), (35,2), (35,13), (35,22);
--36, 'MANO_LOGO, BUHO, PROHIBIDO, CLAVE_SOL, BOMBILLA, IGLU, HIELO, ZANAHORIA'
INSERT INTO cards (id) VALUES (36);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (36,34), (36,7), (36,44), (36,13), (36,5), (36,28), (36,26), (36,57);
--37, 'LLAVE, SOL, VELA, TORTUGA, YIN_YAN, ZANAHORIA, LUNA, SNOWMAN'
INSERT INTO cards (id) VALUES (37);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (37,32), (37,49), (37,55), (37,53), (37,56), (37,57), (37,33), (37,48);
--38, 'SPIDER, EXCLAMACION, MANZANA, COPO_NIEVE, IGLU, LUNA, HOJA_CADUCA, TIJERAS'
INSERT INTO cards (id) VALUES (38);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (38,50), (38,20), (38,37), (38,15), (38,28), (38,33), (38,27), (38,52);
--39, 'BUHO, VELA, BOMBA, RAYO, TIJERAS, QUESO, FLOR, OJO'
INSERT INTO cards (id) VALUES (39);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (39,7), (39,55), (39,6), (39,46), (39,52), (39,45), (39,22), (39,40);
--40, 'EXCLAMACION, LAPIZ, OBJETIVO, ARBOL, HIELO, TREBOL, TORTUGA, QUESO'
INSERT INTO cards (id) VALUES (40);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (40,20), (40,31), (40,39), (40,3), (40,26), (40,54), (40,53), (40,45);
--41, 'EXCLAMACION, GAFAS, CORAZON, TELARANA, FUEGO, BUHO, YIN_YANG, MUNECO'
INSERT INTO cards (id) VALUES (41);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (41,20), (41,24), (41,16), (41,51), (41,23), (41,7), (41,56), (41,38);
--42 'OBJETIVO, MANO_LOGO, CORAZON, LLAVE, DRAGON, HOJA_CADUCA, DELFIN, OJO'
INSERT INTO cards (id) VALUES (42);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (42,39), (42,34), (42,16), (42,32), (42,19), (42,27), (42,17), (42,40);
--43 'OJO, PAYASO, CACTUS, CLAVE_SOL, COPO_NIEVE, TELARANA, BIBERON, TORTUGA'
INSERT INTO cards (id) VALUES (43);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (43,40), (43,41), (43,8), (43,13), (43,15), (43,51), (43,4), (43,53);
--44 'FLOR, PERRO, AGUA, ARBOL, COPO_NIEVE, ZANAHORIA, GAFAS, DELFIN'
INSERT INTO cards (id) VALUES (44);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (44,22), (44,42), (44,1), (44,3), (44,15), (44,57), (44,24), (44,17);
--45 'OJO, GAFAS, CABALLO_AJEDREZ, MANZANA, LABIOS, SOL, HIELO, PINTURA'
INSERT INTO cards (id) VALUES (45);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (45,40), (45,24), (45,11), (45,37), (45,30), (45,49), (45,26), (45,43);
--46 'CALAVERA, AGUA, LABIOS, BOMBA, TORTUGA, FUEGO, PROHIBIDO, HOJA_CADUCA'
INSERT INTO cards (id) VALUES (46);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (46,9), (46,1), (46,30), (46,6), (46,53), (46,23), (46,44), (46,27);
--47 'GAFAS, CLAVE_SOL, DINOSAURIO, DRAGON, LUNA, CALAVERA, GATO, QUESO'
INSERT INTO cards (id) VALUES (47);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (47,24), (47,13), (47,18), (47,19), (47,33), (47,9), (47,25), (47,45);
--48 'LAPIZ, BUHO, CEBRA, LABIOS, COPO_NIEVE, DRAGON, SNOWMAN, RELOJ'
INSERT INTO cards (id) VALUES (48);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (48,31), (48,7), (48,12), (48,30), (48,15), (48,19), (48,48), (48,47);
--49 'COCHE, CABALLO_AJEDREZ, OBJETIVO, COPO_NIEVE, DINOSAURIO, BOMBILLA, BOMBA, YIN_YANG'
INSERT INTO cards (id) VALUES (49);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (49,14), (49,11), (49,39), (49,15), (49,18), (49,5), (49,6), (49,56);
--50 'INTERROGACION, BUHO, ARBOL, CABALLO_AJEDREZ, LLAVE, BIBERON, CALAVERA, SPIDER'
INSERT INTO cards (id) VALUES (50);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (50,29), (50,7), (50,3), (50,11), (50,32), (50,4), (50,9), (50,50);
--51 'EXCLAMACION, AGUA, SOL, RELOJ, BIBERON, MANO_LOGO, DINOSAURIO, RAYO'
INSERT INTO cards (id) VALUES (51);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (51,20), (51,1), (51,49), (51,47), (51,4), (51,34), (51,18), (51,46);
--52 'PINTURA, INTERROGACION, EXCLAMACION, PAYASO, BOMBA, FANTASMA, DRAGON, ZANAHORIA'
INSERT INTO cards (id) VALUES (52);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (52,43), (52,29), (52,20), (52,41), (52,6), (52,21), (52,19), (52,57);
--53 'CORAZON, AGUA, FANTASMA, CABALLO_AJEDREZ, CLAVE_SOL, TREBOL, TIJERAS, SNOWMAN'
INSERT INTO cards (id) VALUES (53);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (53,16), (53,1), (53,21), (53,11), (53,13), (53,54), (53,52), (53,48);
--54 'INTERROGACION, CORAZON, PROHIBIDO, SOL, CANDADO, MARIQUITA, COPO_NIEVE, QUESO'
INSERT INTO cards (id) VALUES (54);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (54,29), (54,16), (54,44), (54,49), (54,10), (54,35), (54,15), (54,45);
--55 'EXCLAMACION, CACTUS, CABALLO_AJEDREZ, PROHIBIDO, CEBRA, DELFIN, VELA, GATO'
INSERT INTO cards (id) VALUES (55);
INSERT INTO cards_figures(cards_id, figures_id) VALUES (55,20), (55,8), (55,11), (55,44), (55,12), (55,17), (55,55), (55,25);

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
