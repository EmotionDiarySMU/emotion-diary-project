-- 테스트 데이터 추가 스크립트
-- 사용법: mysql -u root -p < test_data.sql

USE emotion_diary;

-- 테스트 사용자 추가 (중복 방지)
INSERT IGNORE INTO user (user_id, user_pw) VALUES ('testuser', 'test123');
INSERT IGNORE INTO user (user_id, user_pw) VALUES ('john', 'password');
INSERT IGNORE INTO user (user_id, user_pw) VALUES ('admin', 'admin1234');

-- 테스트 일기 데이터 추가 (testuser용)
INSERT INTO diary (user_id, title, content, stress_level, entry_date)
VALUES
('testuser', '오늘 좋은 하루', '오늘은 정말 좋은 하루였다. 친구들과 즐거운 시간을 보냈다.', 20, '2025-11-18 14:30:00'),
('testuser', '스트레스 받은 날', '일이 너무 많아서 힘들었다.', 80, '2025-11-17 22:00:00'),
('testuser', '평범한 하루', '그냥 평범한 하루였다.', 50, '2025-11-16 18:00:00'),
('testuser', '행복한 주말', '가족과 함께 좋은 시간을 보냈다.', 10, '2025-11-15 16:00:00'),
('testuser', '힘든 월요일', '월요일은 항상 힘들다.', 70, '2025-11-11 09:00:00');

-- 감정 데이터 추가 (최근 일기에 대해)
-- entry_id는 자동 증가되므로, 마지막 5개 일기에 대해 추가
SET @entry1 = (SELECT entry_id FROM diary WHERE user_id = 'testuser' ORDER BY entry_date DESC LIMIT 1 OFFSET 0);
SET @entry2 = (SELECT entry_id FROM diary WHERE user_id = 'testuser' ORDER BY entry_date DESC LIMIT 1 OFFSET 1);
SET @entry3 = (SELECT entry_id FROM diary WHERE user_id = 'testuser' ORDER BY entry_date DESC LIMIT 1 OFFSET 2);
SET @entry4 = (SELECT entry_id FROM diary WHERE user_id = 'testuser' ORDER BY entry_date DESC LIMIT 1 OFFSET 3);
SET @entry5 = (SELECT entry_id FROM diary WHERE user_id = 'testuser' ORDER BY entry_date DESC LIMIT 1 OFFSET 4);

-- 오늘 좋은 하루 (긍정적 감정)
INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES
(@entry1, 90, '😊'),
(@entry1, 85, '😆'),
(@entry1, 80, '🤗');

-- 스트레스 받은 날 (부정적 감정)
INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES
(@entry2, 75, '😰'),
(@entry2, 70, '😠'),
(@entry2, 60, '😔');

-- 평범한 하루 (중립적)
INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES
(@entry3, 50, '😌'),
(@entry3, 45, '😊');

-- 행복한 주말 (긍정적 감정)
INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES
(@entry4, 95, '😍'),
(@entry4, 90, '😂'),
(@entry4, 85, '😊'),
(@entry4, 80, '🤗');

-- 힘든 월요일 (부정적 감정)
INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES
(@entry5, 65, '😰'),
(@entry5, 60, '😅'),
(@entry5, 55, '😔');

-- 데이터 확인
SELECT '=== 사용자 목록 ===' as '';
SELECT * FROM user;

SELECT '=== 일기 목록 (testuser) ===' as '';
SELECT entry_id, title, stress_level, entry_date FROM diary WHERE user_id = 'testuser' ORDER BY entry_date DESC;

SELECT '=== 통계: 평균 스트레스 ===' as '';
SELECT AVG(stress_level) as avg_stress FROM diary WHERE user_id = 'testuser';

SELECT '=== 통계: 감정별 평균 ===' as '';
SELECT e.emoji_icon, AVG(e.emotion_level) as avg_level, COUNT(*) as count
FROM emotion e
JOIN diary d ON e.entry_id = d.entry_id
WHERE d.user_id = 'testuser'
GROUP BY e.emoji_icon
ORDER BY avg_level DESC;

