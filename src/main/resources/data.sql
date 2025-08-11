
-- Insert dữ liệu language
INSERT INTO `language` (`language_id`, `language_name`, `create_at`, `update_at`) VALUES
('EN', 'English', NOW(), NOW()),
('KO', 'Korean', NOW(), NOW()),
('JA', 'Japanese', NOW(), NOW());

-- Insert dữ liệu category
INSERT INTO `category` (`category_name`, `create_at`, `update_at`) VALUES
('ACTION', NOW(), NOW()),
('PUZZLE', NOW(), NOW()),
('RPG', NOW(), NOW()),
('SPORTS', NOW(), NOW()),
('RACING', NOW(), NOW()),
('ADVENTURE', NOW(), NOW());

-- Insert game và game_name mẫu (ví dụ game 1)
SET @cat_id = (SELECT category_id FROM category WHERE category_name = 'ACTION');
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'WARRIORQ', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Warrior Quest', 'EN', NOW(), NOW(), 1),
(@gid, '전사 퀘스트', 'KO', NOW(), NOW(), 0),
(@gid, '戦士クエスト', 'JA', NOW(), NOW(), 0);

-- Game 2
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'SPACEB', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Space Battle', 'EN', NOW(), NOW(), 1),
(@gid, '우주 전투', 'KO', NOW(), NOW(), 0),
(@gid, '宇宙戦争', 'JA', NOW(), NOW(), 0);

-- Game 3
SET @cat_id = (SELECT category_id FROM category WHERE category_name = 'PUZZLE');
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'PUZZLEM', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Puzzle Master', 'EN', NOW(), NOW(), 1),
(@gid, '퍼즐 마스터', 'KO', NOW(), NOW(), 0),
(@gid, 'パズルマスター', 'JA', NOW(), NOW(), 0);

-- Game 4
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'BRAINCH', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Brain Challenge', 'EN', NOW(), NOW(), 1),
(@gid, '두뇌 챌린지', 'KO', NOW(), NOW(), 0),
(@gid, '脳チャレンジ', 'JA', NOW(), NOW(), 0);

-- Game 5
SET @cat_id = (SELECT category_id FROM category WHERE category_name = 'RPG');
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'DRAGONL', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Dragon Legend', 'EN', NOW(), NOW(), 1),
(@gid, '드래곤 전설', 'KO', NOW(), NOW(), 0),
(@gid, 'ドラゴン伝説', 'JA', NOW(), NOW(), 0);

-- Game 6
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'KINGHERO', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Kingdom Hero', 'EN', NOW(), NOW(), 1),
(@gid, '왕국 영웅', 'KO', NOW(), NOW(), 0),
(@gid, '王国の英雄', 'JA', NOW(), NOW(), 0);

-- Game 7
SET @cat_id = (SELECT category_id FROM category WHERE category_name = 'SPORTS');
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'SOCCST', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Soccer Stars', 'EN', NOW(), NOW(), 1),
(@gid, '축구 스타', 'KO', NOW(), NOW(), 0),
(@gid, 'サッカースターズ', 'JA', NOW(), NOW(), 0);

-- Game 8
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'BASKPRO', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Basketball Pro', 'EN', NOW(), NOW(), 1),
(@gid, '농구 프로', 'KO', NOW(), NOW(), 0),
(@gid, 'バスケットボールプロ', 'JA', NOW(), NOW(), 0);

-- Game 9
SET @cat_id = (SELECT category_id FROM category WHERE category_name = 'RACING');
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'SPDRACER', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Speed Racer', 'EN', NOW(), NOW(), 1),
(@gid, '스피드 레이서', 'KO', NOW(), NOW(), 0),
(@gid, 'スピードレーサー', 'JA', NOW(), NOW(), 0);

-- Game 10
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'DRIFTMS', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Drift Master', 'EN', NOW(), NOW(), 1),
(@gid, '드리프트 마스터', 'KO', NOW(), NOW(), 0),
(@gid, 'ドリフトマスター', 'JA', NOW(), NOW(), 0);

-- Game 11
SET @cat_id = (SELECT category_id FROM category WHERE category_name = 'ACTION');
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'SHADNINJA', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Shadow Ninja', 'EN', NOW(), NOW(), 1),
(@gid, '그림자 닌자', 'KO', NOW(), NOW(), 0),
(@gid, '影の忍者', 'JA', NOW(), NOW(), 0);

-- Game 12
SET @cat_id = (SELECT category_id FROM category WHERE category_name = 'RPG');
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'MYSTADV', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Mystic Adventure', 'EN', NOW(), NOW(), 1),
(@gid, '신비한 모험', 'KO', NOW(), NOW(), 0),
(@gid, '神秘の冒険', 'JA', NOW(), NOW(), 0);

-- Game 13
SET @cat_id = (SELECT category_id FROM category WHERE category_name = 'PUZZLE');
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'CUBESOLV', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Cube Solver', 'EN', NOW(), NOW(), 1),
(@gid, '큐브 솔버', 'KO', NOW(), NOW(), 0),
(@gid, 'キューブソルバー', 'JA', NOW(), NOW(), 0);

-- Game 14
SET @cat_id = (SELECT category_id FROM category WHERE category_name = 'SPORTS');
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'TENNCHAMP', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Tennis Champion', 'EN', NOW(), NOW(), 1),
(@gid, '테니스 챔피언', 'KO', NOW(), NOW(), 0),
(@gid, 'テニスチャンピオン', 'JA', NOW(), NOW(), 0);

-- Game 15
SET @cat_id = (SELECT category_id FROM category WHERE category_name = 'RACING');
INSERT INTO `game` (`category_id`, `game_code`, `create_at`, `update_at`) VALUES (@cat_id, 'MOTORIDE', NOW(), NOW());
SET @gid = LAST_INSERT_ID();

INSERT INTO `game_name` (`game_id`, `game_name`, `language_id`, `create_at`, `update_at`, `is_default`) VALUES
(@gid, 'Moto Rider', 'EN', NOW(), NOW(), 1),
(@gid, '모토 라이더', 'KO', NOW(), NOW(), 0),
(@gid, 'モトライダー', 'JA', NOW(), NOW(), 0);