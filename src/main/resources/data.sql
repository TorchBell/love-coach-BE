
/* Love & Fitness Project - Mock Data
   Updated: user_id (singular) and recent NPC personalities
*/

USE `love_coach`;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Users
INSERT INTO `users` (`email`, `password`, `nickname`, `gender`, `birth_date`, `credit`, `is_active`) VALUES
('ssafy@ssafy.com', '1234', '김싸피', 'M', '1998-05-05', 100, true),
('admin@ssafy.com', 'admin', '관리자', 'F', '1990-01-01', 9999, true),
('test@ssafy.com', 'test', '테스트유저', 'F', '2000-12-25', 0, true);

-- 2. NPC (토마, 벨, 치에)
INSERT INTO `npc` (`name`, `personality`) VALUES
('토마', '식단관리 담당. 지친 마음을 어루만져주는 다정한 치유계. 항상 웃는 얼굴로 유저를 위로하고 격려해준다.'),
('벨', '근력운동 담당. 완벽주의 성향의 까칠한 쿨뷰티. 빈틈을 보이면 가차 없이 지적하며, 효율을 최우선으로 생각한다.'),
('치에', '유산소운동 담당. 엉뚱한 상상력으로 말을 자주 묘하게 오해하는 4차원. 순진한 표정으로 앙큼한 농담을 던지는 귀여운 소악마.');

-- 3. User NPC
INSERT INTO `user_npc` (`user_id`, `npc_id`, `affection_score`, `current_state`) VALUES
(1, 1, 60, 'HAPPY'),
(1, 2, 10, 'NORMAL'),
(1, 3, 30, 'NORMAL');

-- 4. Food
INSERT INTO `food` (`food_id`, `food_name`, `calory`, `water`, `protein`, `fat`, `carb`, `sugar`, `weight`) VALUES
('F001', '현미밥', 150.0, 60.0, 3.5, 1.0, 32.0, 0.5, 100),
('F002', '닭가슴살 샐러드', 120.0, 70.0, 22.0, 2.0, 5.0, 1.0, 200),
('F003', '구운 계란', 70.0, 75.0, 6.0, 5.0, 0.5, 0.0, 50),
('F004', '고구마', 128.0, 65.0, 1.5, 0.2, 30.0, 4.0, 100),
('F005', '아메리카노', 10.0, 99.0, 0.0, 0.0, 2.0, 0.0, 350);

-- 5. User Food (간소화)
INSERT INTO `user_food` (`user_id`, `food_id`, `date`, `quantity`) VALUES
(1, 'F001', DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY), 1.0),
(1, 'F003', DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY), 2.0),
(1, 'F002', DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY), 1.0),
(1, 'F004', CURRENT_DATE, 1.5);

-- 6. Exercises
INSERT INTO `muscle_exercise` (`name`, `part`) VALUES ('스쿼트', '하체'), ('벤치프레스', '가슴'), ('데드리프트', '등');
INSERT INTO `cardio_exercise` (`name`, `met_value`) VALUES ('러닝', 8.0), ('사이클', 6.0);

-- 7. Exercise Logs
INSERT INTO `muscle_log` (`user_id`, `muscle_exercise_id`, `date`, `set_count`, `reps_per_set`, `weight`) VALUES
(1, 1, DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY), 5, 12, 60.0),
(1, 2, CURRENT_DATE, 4, 10, 50.0);

INSERT INTO `cardio_log` (`user_id`, `cardio_exercise_id`, `date`, `duration_minutes`, `burned_kcal`) VALUES
(1, 1, DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY), 30, 300.0),
(1, 2, CURRENT_DATE, 40, 250.0);

-- 8. Gallery
INSERT INTO `gallery` (`title`, `image_url`, `b_cut_image_url`, `letter`, `unlock_condition`) VALUES
('첫 만남', '/images/event/meeting.png', NULL, '반가워! 앞으로 잘 부탁해!', '튜토리얼 완료'),
('해변의 데이트', '/images/event/beach.png', '/images/event/beach_b.png', '여름 바다는 정말 시원하네~', '호감도 50 달성'),
('크리스마스 파티', '/images/event/xmas.png', NULL, '메리 크리스마스! 선물 고마워.', '겨울 시즌 이벤트 참여');

-- 9. Achievements
INSERT INTO `achievement` (`title`, `description`, `condition_type`, `icon_url`, `reward_gallery_id`) VALUES
('헬린이 탈출', '운동 기록 10회 달성', 'workout_count', '/icons/newbie.png', 1),
('식단 관리자', '식단 기록 3일 연속 작성', 'food_streak', '/icons/salad.png', NULL),
('썸타는 사이', '호감도 50 달성', 'affection', '/icons/heart.png', 2);

-- 10. User Achievement & Gallery
INSERT INTO `user_achievement` (`user_id`, `achievement_id`) VALUES (1, 1);
INSERT INTO `user_gallery` (`user_id`, `gallery_id`, `is_opened`, `is_favorite`) VALUES (1, 1, TRUE, TRUE);

-- 11. Chat Log
INSERT INTO `chat_log` (`user_id`, `npc_id`, `message_user`, `message_ai`, `context`) VALUES
(1, 3, '오늘 너무 힘들어서 운동 쉬고 싶어', '어머, 벌써 지친 거야? 땀 흘리는 모습이 꽤... 귀여운데? 흐흥, 오늘은 봐줄 테니까 내일은 더 뜨겁게 달리자구~', '사용자가 운동 회피 시도, 치에(NPC 3)가 응큼한 농담을 던지며 허락함'),
(1, 1, '나 오늘 샐러드 먹었어!', '정말 잘했어요! 식단 관리도 완벽하네요. 역시 대단해요!', '사용자 식단 보고, 토마(NPC 1)가 칭찬함');

-- 12. Quests & User Quest
INSERT INTO `quest` (`title`, `content`, `npc_id`, `credit`) VALUES
('매일매일 출석체크', '앱에 접속하여 출석체크 하세요.', NULL, 10),
('치에의 유혹', '러닝머신 30분 달리기', 3, 50);

INSERT INTO `user_quest` (`user_id`, `quest_id`, `is_achieved`) VALUES
(1, 1, TRUE),
(1, 2, FALSE);

SET FOREIGN_KEY_CHECKS = 1;