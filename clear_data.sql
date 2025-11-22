-- 데이터베이스의 모든 데이터 삭제 (테이블 구조는 유지)
-- 사용법: mysql -u root -p emotion_diary < clear_data.sql

USE emotion_diary;

-- 외래키 제약조건 때문에 순서대로 삭제해야 함

-- 1. emotion 테이블 데이터 삭제 (diary를 참조)
DELETE FROM emotion;

-- 2. diary 테이블 데이터 삭제 (user를 참조)
DELETE FROM diary;

-- 3. question 테이블 데이터 삭제
DELETE FROM question;

-- 4. user 테이블 데이터 삭제
DELETE FROM user;

-- AUTO_INCREMENT 초기화 (선택사항)
ALTER TABLE emotion AUTO_INCREMENT = 1;
ALTER TABLE diary AUTO_INCREMENT = 1;
ALTER TABLE question AUTO_INCREMENT = 1;

-- 삭제 결과 확인
SELECT '=== 데이터 삭제 완료 ===' as '';
SELECT 'user 테이블:' as '', COUNT(*) as count FROM user;
SELECT 'diary 테이블:' as '', COUNT(*) as count FROM diary;
SELECT 'emotion 테이블:' as '', COUNT(*) as count FROM emotion;
SELECT 'question 테이블:' as '', COUNT(*) as count FROM question;

