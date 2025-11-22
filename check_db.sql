-- DB 확인용 SQL 쿼리
USE emotion_diary;

-- 1. test1234 사용자의 일기 목록
SELECT entry_id, title, DATE_FORMAT(entry_date, '%Y-%m-%d %H:%i') as entry_date, stress_level
FROM diary
WHERE user_id = 'test1234'
ORDER BY entry_id DESC;

-- 2. test1234 사용자의 모든 감정 데이터 (상세)
SELECT d.entry_id, DATE_FORMAT(d.entry_date, '%Y-%m-%d %H:%i') as entry_date,
       e.emoji_icon, e.emotion_level
FROM diary d
JOIN emotion e ON d.entry_id = e.entry_id
WHERE d.user_id = 'test1234'
ORDER BY d.entry_id DESC, e.emotion_id;

-- 3. 감정별 집계 (현재 통계 화면에서 보이는 것)
SELECT e.emoji_icon, COUNT(*) as count, AVG(e.emotion_level) as avg_level
FROM diary d
JOIN emotion e ON d.entry_id = e.entry_id
WHERE d.user_id = 'test1234'
GROUP BY e.emoji_icon;

