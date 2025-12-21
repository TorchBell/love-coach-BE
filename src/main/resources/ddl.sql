/* Love & Fitness Project - Final Schema
   Fixed: Unified 'user_id' (singular) for all PK/FK columns
*/
drop database if exists love_coach;
-- 1. 데이터베이스 생성 및 선택
CREATE DATABASE IF NOT EXISTS `love_coach` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `love_coach`;

-- 2. 외래키 체크 비활성화 (기존 테이블 삭제를 위해)
SET FOREIGN_KEY_CHECKS = 0;

-- 3. 기존 테이블 삭제 (순서 무관)
DROP TABLE IF EXISTS `user_quest`;
DROP TABLE IF EXISTS `quest`;
DROP TABLE IF EXISTS `chat_log`;
DROP TABLE IF EXISTS `user_gallery`;
DROP TABLE IF EXISTS `gallery`;
DROP TABLE IF EXISTS `user_achievement`;
DROP TABLE IF EXISTS `achievement`;
DROP TABLE IF EXISTS `cardio_log`;
DROP TABLE IF EXISTS `muscle_log`;
DROP TABLE IF EXISTS `cardio_exercise`;
DROP TABLE IF EXISTS `muscle_exercise`;
DROP TABLE IF EXISTS `user_food`;
DROP TABLE IF EXISTS `food`;
DROP TABLE IF EXISTS `user_npc`;
DROP TABLE IF EXISTS `npc`;
DROP TABLE IF EXISTS `users`;

-- ==============================================
-- 4. 테이블 생성 (DDL)
-- ==============================================

-- 4-1. 사용자 (Users)
CREATE TABLE `users` (
  `user_id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '사용자 ID (PK)',
  `email` varchar(100) NOT NULL UNIQUE COMMENT '이메일',
  `password` varchar(255) NOT NULL COMMENT '비밀번호',
  `nickname` varchar(50) NOT NULL COMMENT '닉네임',
  `gender` char(1) COMMENT '성별 (M/F)',
  `birth_date` date COMMENT '생년월일',
  `credit` int DEFAULT 0 COMMENT '보유 크레딧',
  `is_active` boolean DEFAULT TRUE NOT NULL COMMENT '활성 여부',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '사용자 정보';

-- 4-2. NPC
CREATE TABLE `npc` (
  `npc_id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT 'NPC ID',
  `name` varchar(50) NOT NULL COMMENT '이름',
  `personality` text COMMENT '성격',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT 'NPC 메타데이터';

CREATE TABLE `user_npc` (
  `user_npc_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT 'FK: users.user_id',
  `npc_id` bigint NOT NULL COMMENT 'FK: npc.npc_id',
  `affection_score` int DEFAULT 0,
  `current_state` varchar(20) DEFAULT 'NORMAL',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '유저-NPC 관계';

-- 4-3. 식단 (Food)
CREATE TABLE `food` (
  `food_id` varchar(20) PRIMARY KEY COMMENT '식품코드',
  `food_name` varchar(200) NOT NULL,
  `calory` decimal(10,2),
  `water` decimal(10,2),
  `protein` decimal(10,2),
  `fat` decimal(10,2),
  `carb` decimal(10,2),
  `sugar` decimal(10,2),
  `weight` decimal(10,2)
) COMMENT '식품 영양 정보';

CREATE TABLE `user_food` (
  `user_food_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT 'FK: users.user_id',
  `food_id` varchar(20) NOT NULL COMMENT 'FK: food.food_id',
  `date` date NOT NULL,
  `quantity` decimal(5,2) DEFAULT 1.0,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '식단 기록';

-- 4-4. 운동 (Muscle / Cardio)
CREATE TABLE `muscle_exercise` (
  `muscle_exercise_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `part` varchar(50)
) COMMENT '근력 운동 종목';

CREATE TABLE `cardio_exercise` (
  `cardio_exercise_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `met_value` decimal(5,2)
) COMMENT '유산소 운동 종목';

CREATE TABLE `muscle_log` (
  `muscle_log_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT 'FK: users.user_id',
  `muscle_exercise_id` bigint NOT NULL,
  `date` date NOT NULL,
  `set_count` int,
  `reps_per_set` int,
  `weight` decimal(5,2),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '근력 운동 기록';

CREATE TABLE `cardio_log` (
  `cardio_log_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT 'FK: users.user_id',
  `cardio_exercise_id` bigint NOT NULL,
  `date` date NOT NULL,
  `duration_minutes` int,
  `burned_kcal` decimal(10,2),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '유산소 운동 기록';

-- 4-5. 갤러리 & 업적
CREATE TABLE `gallery` (
  `gallery_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `b_cut_image_url` varchar(255),
  `letter` text,
  `unlock_condition` varchar(255)
) COMMENT '갤러리';

CREATE TABLE `achievement` (
  `achievement_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` text,
  `condition_type` varchar(50),
  `icon_url` varchar(255),
  `reward_gallery_id` bigint
) COMMENT '업적';

CREATE TABLE `user_achievement` (
  `user_achievement_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT 'FK: users.user_id',
  `achievement_id` bigint NOT NULL,
  `achieved_at` datetime DEFAULT CURRENT_TIMESTAMP
) COMMENT '유저 달성 업적';

CREATE TABLE `user_gallery` (
  `user_gallery_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT 'FK: users.user_id',
  `gallery_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
) COMMENT '유저 보유 갤러리';

-- 4-6. 채팅 & 퀘스트
CREATE TABLE `chat_log` (
  `chat_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT 'FK: users.user_id',
  `npc_id` bigint NOT NULL,
  `message_user` text,
  `message_ai` text,
  `context` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
) COMMENT '채팅 로그';

CREATE TABLE `quest` (
  `quest_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `content` text,
  `npc_id` bigint,
  `credit` int DEFAULT 0
) COMMENT '퀘스트';

CREATE TABLE `user_quest` (
  `user_quest_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT 'FK: users.user_id',
  `quest_id` bigint NOT NULL,
  `is_achieved` boolean DEFAULT FALSE,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
) COMMENT '유저 퀘스트';


-- ==============================================
-- 5. 관계 설정 (Foreign Keys) - 이름 통일 (user_id)
-- ==============================================

-- Users 관련 FK
ALTER TABLE `user_npc` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
ALTER TABLE `user_food` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
ALTER TABLE `muscle_log` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
ALTER TABLE `cardio_log` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
ALTER TABLE `user_achievement` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
ALTER TABLE `user_gallery` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
ALTER TABLE `chat_log` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
ALTER TABLE `user_quest` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

-- 기타 FK
ALTER TABLE `user_npc` ADD FOREIGN KEY (`npc_id`) REFERENCES `npc` (`npc_id`);
ALTER TABLE `user_food` ADD FOREIGN KEY (`food_id`) REFERENCES `food` (`food_id`);
ALTER TABLE `muscle_log` ADD FOREIGN KEY (`muscle_exercise_id`) REFERENCES `muscle_exercise` (`muscle_exercise_id`);
ALTER TABLE `cardio_log` ADD FOREIGN KEY (`cardio_exercise_id`) REFERENCES `cardio_exercise` (`cardio_exercise_id`);
ALTER TABLE `achievement` ADD FOREIGN KEY (`reward_gallery_id`) REFERENCES `gallery` (`gallery_id`);
ALTER TABLE `user_achievement` ADD FOREIGN KEY (`achievement_id`) REFERENCES `achievement` (`achievement_id`);
ALTER TABLE `user_gallery` ADD FOREIGN KEY (`gallery_id`) REFERENCES `gallery` (`gallery_id`);
ALTER TABLE `chat_log` ADD FOREIGN KEY (`npc_id`) REFERENCES `npc` (`npc_id`);
ALTER TABLE `quest` ADD FOREIGN KEY (`npc_id`) REFERENCES `npc` (`npc_id`);
ALTER TABLE `user_quest` ADD FOREIGN KEY (`quest_id`) REFERENCES `quest` (`quest_id`);

-- 6. 외래키 체크 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;