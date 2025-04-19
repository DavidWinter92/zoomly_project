DROP DATABASE IF EXISTS `zoomlytest`;
CREATE DATABASE IF NOT EXISTS `zoomlytest`; 
USE `zoomlytest`;

-- Create 'users' table first (referenced by 'reservations')
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `account_type` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `users` WRITE;
INSERT INTO `users` VALUES 
(1,'adminsss','main','admin@test.com','Password1!','admin'),
(2,'test','user','user@test.com','Password1!','user'),
(3,'test','user','user2@gmail.com','password1!','user'),
(4,'asdf','asdf','use12@gmail.com','password1!','user'),
(5,'testtttt','test','user@test1.com','Password3!','user'),
(8,'user','test','user@test3.com','Password2!','admin'),
(9,'test','test','test@test.test','Password1!','admin'),
(10,'david','test','user44@gmail.com','password1!','user'),
(12,'user','test','user3@gmail.com','password1!','user'),
(13,'user','test','user4@gmail.com','password1!','user'),
(14,'user','test','user5@gmail.com','password1!','user'),
(15,'user','test','user6@gmail.com','password1!','user'),
(16,'user','test','user7@gmail.com','password1!','user'),
(17,'user','test','user8@gmail.com','password1!','user'),
(18,'user','test','user9@gmail.com','password1!','user'),
(19,'user','test','user10@gmail.com','password1!','user'),
(20,'user','test','user11@gmail.com','password1!','user'),
(21,'user','test','user12@gmail.com','password1!','user'),
(22,'user','test','user13@gmail.com','password1!','user'),
(23,'user','test','user14@gmail.com','password1!','user'),
(24,'user','test','user15@gmail.com','password1!','user'),
(25,'user','test','user16@gmail.com','password1!','user'),
(26,'user','test','user17@gmail.com','password1!','user'),
(27,'user','test','user18@gmail.com','password1!','user'),
(28,'user','test','user19@gmail.com','password1!','user'),
(29,'user','test','user270@gmail.com','password1!','user'),
(30,'david','test','user444@gmail.com','password1!','user'),
(31,'david','test','user89@gmail.com','password1!','user'),
(32,'david','test','user62@gmail.com','password1!','user');
UNLOCK TABLES;

-- Create 'vehicles' table next (referenced by 'reservations')
DROP TABLE IF EXISTS `vehicles`;
CREATE TABLE `vehicles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `make` varchar(50) NOT NULL,
  `model` varchar(50) NOT NULL,
  `year` int NOT NULL,
  `mileage` int DEFAULT NULL,
  `vin` varchar(17) NOT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `description` text,
  `price_per_day` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `vin` (`vin`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `vehicles` WRITE;
INSERT INTO `vehicles` VALUES 
(8,'Toyota','Corolla',2019,156684,'SG681E82','E:\\Documents\\College\\Repo\\Java\\zoomly_project\\src\\main\\resources\\vehicle_imgs\\2019_corolla.png','test',13.51),
(9,'Toyota','Corolla',2019,156684,'SG235421','E:\\Documents\\College\\Repo\\Java\\zoomly_project\\src\\main\\resources\\vehicle_imgs\\2019_corolla.png','test',30.00),
(11,'Toyota','Corolla',2019,2359,'SF865G78','E:\\Documents\\College\\Repo\\Java\\zoomly_project\\src\\main\\resources\\vehicle_imgs\\2019_corolla.png','this is a test',49.00),
(12,'Honda','Civic',2019,156684,'SG681E81','zoomly_project\\src\\main\\resources\\vehicle_imgs\\2019_corolla.png','test',13.51),
(14,'Tesla','Model X',2022,156684,'SG63554S','E:\\Documents\\College\\Repo\\Java\\zoomly_project\\src\\main\\resources\\vehicle_imgs\\2019_corolla.png','You know the Grammys are a joke when Future doesn\'t win Best Everything This is a true fact: I never had a fear of heights until I fell off a roof I think of a lot of good ideas when going to the bathroom - I guess I have a real stream of consciousness To Catch A Predator would have been a great name for a Steve Irwin show. Mintslavicia I don\'t need a big house, just a two-floor condo - you could say I have lofty expectations A tagline for a car company that prides itself on its morals and ethics: Take the High Road Why don\'t we call glasses duocles ',32.63),
(15,'Toyota','Corolla',2019,2359,'SF865679','E:\\Documents\\College\\Repo\\Java\\zoomly_project\\src\\main\\resources\\vehicle_imgs\\2019_corolla.png','this is a test',49.00);
UNLOCK TABLES;

-- Create 'reservations' table last (it references 'users' and 'vehicles')
DROP TABLE IF EXISTS `reservations`;
CREATE TABLE `reservations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `vehicle_id` int DEFAULT NULL,
  `pickup_date` date DEFAULT NULL,
  `dropoff_date` date DEFAULT NULL,
  `total_charge` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `vehicle_id` (`vehicle_id`),
  CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `reservations` WRITE;
INSERT INTO `reservations` VALUES 
(2,2,9,'2025-04-06','2025-04-08',121.59),
(4,2,8,'2025-04-22','2025-04-24',300.00),
(18,2,11,'2025-04-21','2025-04-22',49.00),
(19,2,12,'2025-04-06','2025-04-23',229.67),
(20,2,12,'2025-04-28','2025-04-29',13.51);
UNLOCK TABLES;
