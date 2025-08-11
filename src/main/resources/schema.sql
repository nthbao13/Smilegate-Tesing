create database gaming;
use gaming;

CREATE TABLE `game` (
  `game_id` int PRIMARY KEY auto_increment,
  `category_id` int,
  `game_code` varchar(20),
  `create_at` datetime,
  `update_at` datetime
);

CREATE TABLE `category` (
  `category_id` int PRIMARY KEY auto_increment,
  `category_name` varchar(100) unique,
  `create_at` datetime,
  `update_at` datetime
);

CREATE TABLE `game_name` (
  `name_id` int PRIMARY KEY auto_increment,
  `game_id` int,
  `game_name` varchar(100),
  `language_id` varchar(5),
  `create_at` datetime,
  `update_at` datetime,
  `is_default` bit
);

CREATE TABLE `language` (
  `language_id` varchar(5) PRIMARY KEY ,
  `language_name` varchar(100),
  `create_at` datetime,
  `update_at` datetime
);

ALTER TABLE `game` ADD FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`);

ALTER TABLE `game_name` ADD FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`);

ALTER TABLE `game_name` ADD FOREIGN KEY (`language_id`) REFERENCES `language` (`language_id`);

