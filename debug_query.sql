-- 디버그: 실제 저장된 모든 이모지 확인
USE emotion_diary;

-- 이번 주 데이터 상세 확인
SELECT
    d.entry_id,
    DATE_FORMAT(d.entry_date, '%Y-%m-%d %H:%i') as entry_date,
    e.emoji_icon,
    e.emotion_level,
    CHAR_LENGTH(e.emoji_icon) as char_len,
    LENGTH(e.emoji_icon) as byte_len,
    HEX(e.emoji_icon) as hex_value
FROM diary d
JOIN emotion e ON d.entry_id = e.entry_id
WHERE d.user_id = 'test1234'
  AND DATE(d.entry_date) BETWEEN '2025-11-17' AND '2025-11-23'
ORDER BY d.entry_id DESC, e.emotion_id;

-- 감정별 집계
SELECT
    e.emoji_icon,
    COUNT(*) as count,
    AVG(e.emotion_level) as avg_level,
    HEX(e.emoji_icon) as hex_value
FROM diary d
JOIN emotion e ON d.entry_id = e.entry_id
WHERE d.user_id = 'test1234'
  AND DATE(d.entry_date) BETWEEN '2025-11-17' AND '2025-11-23'
GROUP BY e.emoji_icon
ORDER BY count DESC;

