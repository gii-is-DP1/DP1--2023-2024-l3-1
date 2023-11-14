-- Para los hashes de las contraseñas, utilizar: https://bcrypt-generator.com/
-- Para generar los IDs de usuario, utilizar: https://www.uuidgenerator.net/version4

-- Usuario administrador. Contraseña: dobble_admin
INSERT INTO players(id,is_admin,email,username,password) VALUES (1,1,'admin@example.com','admin','$2a$12$31ScIzRMI5q2RYnQnKIwJOoPCsuO05QZLeI4tlBdQJd2mehNPreaS');
-- Usuario normal. Contraseña: dobble
INSERT INTO players(id,is_admin,email,username,password) VALUES (2,0,'dobble@example.com','dobble','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga');
INSERT INTO players(id,is_admin,email,username,password) VALUES (3,0,'dobble2@example.com','dobble2','$2a$12$pNpPEbQPT3N5k7zdxoIKTeYitzK6UbzCffkbY88QLHr6cGumYj3Ga');
