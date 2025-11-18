# 데이터베이스 스키마

## 목차
1. [데이터베이스 개요](#1-데이터베이스-개요)
2. [테이블 상세 설명](#2-테이블-상세-설명)
3. [관계도 (ERD)](#3-관계도-erd)
4. [SQL 스크립트](#4-sql-스크립트)
5. [인덱스 및 최적화](#5-인덱스-및-최적화)
6. [샘플 데이터](#6-샘플-데이터)

---

## 1. 데이터베이스 개요

### 1.1 기본 정보
- **데이터베이스명**: `emotion_diary`
- **문자셋**: `utf8mb4` (이모지 저장 지원)
- **Collation**: `utf8mb4_unicode_ci` (대소문자 구분 없는 유니코드)
- **엔진**: InnoDB (기본)
- **MySQL 버전**: 8.0.33

### 1.2 생성 시점
- 애플리케이션 최초 실행 시 `DatabaseUtil.createDatabase()` 호출
- 데이터베이스가 이미 존재하면 생성 건너뜀
- 자동으로 4개의 테이블 생성

### 1.3 연결 정보
```
URL: jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=UTC
User: root
Password: REMOVED_PASSWORD (환경에 맞게 변경 필요)
```

---

## 2. 테이블 상세 설명

### 2.1 user 테이블

**목적**: 사용자 계정 정보 저장

#### 스키마
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
|--------|-------------|-----------|------|
| user_id | VARCHAR(20) | PRIMARY KEY | 사용자 아이디 (로그인 ID) |
| user_pw | VARCHAR(20) | NOT NULL | 사용자 비밀번호 (평문 저장 - 추후 암호화 필요) |

#### SQL
```sql
CREATE TABLE user (
    user_id VARCHAR(20) PRIMARY KEY,
    user_pw VARCHAR(20) NOT NULL
);
```

#### 특징
- **기본키**: `user_id`
- **보안 이슈**: 비밀번호가 평문으로 저장됨
  - **TODO**: SHA-256 또는 BCrypt로 암호화 필요
  - **권장 컬럼 타입**: VARCHAR(64) 이상

#### 샘플 데이터
```sql
INSERT INTO user (user_id, user_pw) VALUES ('testuser', 'password123');
INSERT INTO user (user_id, user_pw) VALUES ('john', 'john1234');
```

---

### 2.2 diary 테이블

**목적**: 일기 본문 및 스트레스 수치 저장

#### 스키마
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
|--------|-------------|-----------|------|
| entry_id | INTEGER | PRIMARY KEY, AUTO_INCREMENT | 일기 고유 ID (자동 증가) |
| user_id | VARCHAR(20) | NOT NULL, FOREIGN KEY | 작성자 ID (user 테이블 참조) |
| title | VARCHAR(50) | NOT NULL | 일기 제목 |
| content | TEXT | | 일기 본문 (긴 텍스트 저장) |
| stress_level | INTEGER | NOT NULL | 스트레스 수치 (0~100) |
| entry_date | DATETIME | NOT NULL | 일기 작성 날짜 및 시간 |

#### SQL
```sql
CREATE TABLE diary (
    entry_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL,
    title VARCHAR(50) NOT NULL,
    content TEXT,
    stress_level INTEGER NOT NULL,
    entry_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);
```

#### 특징
- **기본키**: `entry_id` (자동 증가)
- **외래키**: `user_id` → `user(user_id)`
- **제약 조건**:
  - `stress_level`: 0~100 범위 (애플리케이션 레벨에서 검증)
  - `entry_date`: 일기 작성 시점 저장 (검색/정렬 기준)

#### 인덱스 권장
```sql
-- 사용자별 일기 조회 최적화
CREATE INDEX idx_user_id ON diary(user_id);

-- 날짜별 조회 최적화
CREATE INDEX idx_entry_date ON diary(entry_date);

-- 복합 인덱스 (사용자 + 날짜)
CREATE INDEX idx_user_date ON diary(user_id, entry_date);
```

#### 샘플 데이터
```sql
INSERT INTO diary (user_id, title, content, stress_level, entry_date)
VALUES ('testuser', '좋은 하루', '오늘은 날씨가 좋았다.', 30, '2025-11-18 14:30:00');

INSERT INTO diary (user_id, title, content, stress_level, entry_date)
VALUES ('testuser', '힘든 하루', '오늘은 일이 많았다.', 75, '2025-11-17 20:00:00');
```

---

### 2.3 emotion 테이블

**목적**: 일기별 감정 정보 저장 (최대 4개)

#### 스키마
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
|--------|-------------|-----------|------|
| emotion_id | INTEGER | PRIMARY KEY, AUTO_INCREMENT | 감정 레코드 고유 ID |
| entry_id | INTEGER | NOT NULL, FOREIGN KEY | 일기 ID (diary 테이블 참조) |
| emotion_level | INTEGER | NOT NULL | 감정 수치 (0~100) |
| emoji_icon | VARCHAR(10) | NOT NULL | 감정 이모지 (😊, 😢 등) |

#### SQL
```sql
CREATE TABLE emotion (
    emotion_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    entry_id INTEGER NOT NULL,
    emotion_level INTEGER NOT NULL,
    emoji_icon VARCHAR(10) NOT NULL,
    FOREIGN KEY (entry_id) REFERENCES diary(entry_id)
);
```

#### 특징
- **기본키**: `emotion_id` (자동 증가)
- **외래키**: `entry_id` → `diary(entry_id)`
- **1:N 관계**: 하나의 일기(diary)는 최대 4개의 감정(emotion)을 가짐
- **이모지 저장**: UTF8MB4 문자셋 필수
- **제약 조건**:
  - `emotion_level`: 0~100 범위
  - 하나의 `entry_id`에 최대 4개 레코드 (애플리케이션 레벨 검증)

#### 인덱스 권장
```sql
-- 일기별 감정 조회 최적화
CREATE INDEX idx_entry_id ON emotion(entry_id);

-- 이모지별 통계 조회 최적화
CREATE INDEX idx_emoji ON emotion(emoji_icon);
```

#### 감정 이모지 목록

##### 긍정적 감정
| 감정명 | 이모지 | emoji_icon 값 |
|--------|--------|---------------|
| 행복 | 😊 | 😊 |
| 신남 | 😆 | 😆 |
| 설렘 | 😍 | 😍 |
| 편안 | 😌 | 😌 |
| 재미 | 😂 | 😂 |
| 고마움 | 🤗 | 🤗 |

##### 부정적 감정
| 감정명 | 이모지 | emoji_icon 값 |
|--------|--------|---------------|
| 슬픔 | 😢 | 😢 |
| 분노 | 😠 | 😠 |
| 불안 | 😰 | 😰 |
| 민망 | 😅 | 😅 |
| 당황 | 😧 | 😧 |
| 미안함 | 😔 | 😔 |

#### 샘플 데이터
```sql
-- entry_id = 1 (좋은 하루)에 대한 감정들
INSERT INTO emotion (entry_id, emotion_level, emoji_icon)
VALUES (1, 80, '😊');  -- 행복: 80

INSERT INTO emotion (entry_id, emotion_level, emoji_icon)
VALUES (1, 60, '😆');  -- 신남: 60

INSERT INTO emotion (entry_id, emotion_level, emoji_icon)
VALUES (1, 70, '🤗');  -- 고마움: 70

-- entry_id = 2 (힘든 하루)에 대한 감정들
INSERT INTO emotion (entry_id, emotion_level, emoji_icon)
VALUES (2, 65, '😢');  -- 슬픔: 65

INSERT INTO emotion (entry_id, emotion_level, emoji_icon)
VALUES (2, 50, '😰');  -- 불안: 50
```

---

### 2.4 question 테이블

**목적**: 사용자에게 제시할 질문 저장 (추후 확장용)

#### 스키마
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
|--------|-------------|-----------|------|
| question_id | INTEGER | PRIMARY KEY, AUTO_INCREMENT | 질문 고유 ID |
| question_text | VARCHAR(100) | NOT NULL | 질문 내용 |

#### SQL
```sql
CREATE TABLE question (
    question_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    question_text VARCHAR(100) NOT NULL
);
```

#### 특징
- **기본키**: `question_id` (자동 증가)
- **용도**: 일기 작성 시 감정 유도 질문 표시
- **현재 상태**: 미사용 (추후 확장 예정)

#### 샘플 데이터
```sql
INSERT INTO question (question_text) VALUES ('오늘 하루 중 가장 기억에 남는 순간은?');
INSERT INTO question (question_text) VALUES ('오늘 나에게 고마운 사람은 누구인가요?');
INSERT INTO question (question_text) VALUES ('오늘의 스트레스 원인은 무엇인가요?');
```

---

## 3. 관계도 (ERD)

```
┌──────────────┐
│     user     │
├──────────────┤
│ user_id (PK) │◄─────┐
│ user_pw      │      │
└──────────────┘      │
                      │ 1
                      │
                      │
                      │ N
┌──────────────────┐  │
│      diary       │  │
├──────────────────┤  │
│ entry_id (PK)    │◄─┼─────┐
│ user_id (FK)     │──┘     │
│ title            │        │
│ content          │        │ 1
│ stress_level     │        │
│ entry_date       │        │
└──────────────────┘        │
                            │
                            │ N
                  ┌─────────────────┐
                  │    emotion      │
                  ├─────────────────┤
                  │ emotion_id (PK) │
                  │ entry_id (FK)   │──┘
                  │ emotion_level   │
                  │ emoji_icon      │
                  └─────────────────┘

┌──────────────────┐
│    question      │  (독립 테이블)
├──────────────────┤
│ question_id (PK) │
│ question_text    │
└──────────────────┘
```

### 관계 설명
1. **user ↔ diary**: 1:N
   - 한 사용자는 여러 개의 일기를 작성할 수 있음
   - `diary.user_id` → `user.user_id`

2. **diary ↔ emotion**: 1:N (최대 4)
   - 하나의 일기는 최대 4개의 감정을 가짐
   - `emotion.entry_id` → `diary.entry_id`

3. **question**: 독립
   - 현재는 다른 테이블과 관계 없음
   - 추후 `diary_question` 중간 테이블 추가 예정

---

## 4. SQL 스크립트

### 4.1 전체 데이터베이스 생성 스크립트

```sql
-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS emotion_diary 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 데이터베이스 선택
USE emotion_diary;

-- user 테이블 생성
CREATE TABLE user (
    user_id VARCHAR(20) PRIMARY KEY,
    user_pw VARCHAR(20) NOT NULL
);

-- diary 테이블 생성
CREATE TABLE diary (
    entry_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL,
    title VARCHAR(50) NOT NULL,
    content TEXT,
    stress_level INTEGER NOT NULL,
    entry_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- emotion 테이블 생성
CREATE TABLE emotion (
    emotion_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    entry_id INTEGER NOT NULL,
    emotion_level INTEGER NOT NULL,
    emoji_icon VARCHAR(10) NOT NULL,
    FOREIGN KEY (entry_id) REFERENCES diary(entry_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- question 테이블 생성
CREATE TABLE question (
    question_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    question_text VARCHAR(100) NOT NULL
);

-- 인덱스 생성
CREATE INDEX idx_user_id ON diary(user_id);
CREATE INDEX idx_entry_date ON diary(entry_date);
CREATE INDEX idx_entry_id ON emotion(entry_id);
CREATE INDEX idx_emoji ON emotion(emoji_icon);
```

### 4.2 테이블 삭제 스크립트

```sql
-- 외래키 제약으로 순서 중요
DROP TABLE IF EXISTS emotion;
DROP TABLE IF EXISTS diary;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS question;
```

### 4.3 데이터베이스 완전 삭제

```sql
DROP DATABASE IF EXISTS emotion_diary;
```

---

## 5. 인덱스 및 최적화

### 5.1 현재 인덱스

| 테이블 | 인덱스명 | 컬럼 | 타입 |
|--------|----------|------|------|
| user | PRIMARY | user_id | PRIMARY KEY |
| diary | PRIMARY | entry_id | PRIMARY KEY |
| diary | idx_user_id | user_id | INDEX |
| diary | idx_entry_date | entry_date | INDEX |
| emotion | PRIMARY | emotion_id | PRIMARY KEY |
| emotion | idx_entry_id | entry_id | INDEX |
| emotion | idx_emoji | emoji_icon | INDEX |
| question | PRIMARY | question_id | PRIMARY KEY |

### 5.2 쿼리 최적화 가이드

#### 사용자별 일기 조회
```sql
-- 인덱스 활용: idx_user_id
SELECT * FROM diary 
WHERE user_id = 'testuser'
ORDER BY entry_date DESC;
```

#### 날짜별 일기 조회
```sql
-- 인덱스 활용: idx_entry_date
SELECT * FROM diary 
WHERE entry_date BETWEEN '2025-11-01' AND '2025-11-30'
ORDER BY entry_date;
```

#### 감정별 통계
```sql
-- 인덱스 활용: idx_emoji
SELECT emoji_icon, AVG(emotion_level) as avg_level
FROM emotion
WHERE entry_id IN (
    SELECT entry_id FROM diary WHERE user_id = 'testuser'
)
GROUP BY emoji_icon;
```

#### JOIN 쿼리 최적화
```sql
-- 일기와 감정 정보 함께 조회
SELECT d.*, e.emoji_icon, e.emotion_level
FROM diary d
LEFT JOIN emotion e ON d.entry_id = e.entry_id
WHERE d.user_id = 'testuser'
ORDER BY d.entry_date DESC;
```

### 5.3 성능 모니터링

```sql
-- 쿼리 실행 계획 확인
EXPLAIN SELECT * FROM diary WHERE user_id = 'testuser';

-- 인덱스 사용 통계
SHOW INDEX FROM diary;

-- 테이블 통계 업데이트
ANALYZE TABLE diary;
```

---

## 6. 샘플 데이터

### 6.1 테스트 데이터 생성

```sql
-- 사용자 생성
INSERT INTO user (user_id, user_pw) VALUES 
('testuser', 'password123'),
('john', 'john1234'),
('alice', 'alice5678');

-- testuser의 일기 데이터
INSERT INTO diary (user_id, title, content, stress_level, entry_date) VALUES
('testuser', '프로젝트 시작', '오늘부터 감정 일기 프로젝트를 시작했다.', 45, '2025-11-01 09:00:00'),
('testuser', '좋은 뉴스', '프로젝트 진행이 순조롭다!', 25, '2025-11-05 14:30:00'),
('testuser', '스트레스받는 날', '버그 수정에 시간이 너무 오래 걸렸다.', 80, '2025-11-10 18:00:00'),
('testuser', '성공적인 배포', '드디어 첫 번째 버전을 배포했다!', 20, '2025-11-15 16:00:00');

-- 감정 데이터 (일기별 2-4개)
-- entry_id = 1 (프로젝트 시작)
INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES
(1, 70, '😆'),  -- 신남
(1, 50, '😰'),  -- 불안
(1, 60, '😍');  -- 설렘

-- entry_id = 2 (좋은 뉴스)
INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES
(2, 85, '😊'),  -- 행복
(2, 75, '🤗'),  -- 고마움
(2, 80, '😌');  -- 편안

-- entry_id = 3 (스트레스받는 날)
INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES
(3, 70, '😢'),  -- 슬픔
(3, 65, '😠'),  -- 분노
(3, 80, '😰'),  -- 불안
(3, 55, '😔');  -- 미안함

-- entry_id = 4 (성공적인 배포)
INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES
(4, 95, '😊'),  -- 행복
(4, 90, '😂'),  -- 재미
(4, 85, '🤗'),  -- 고마움
(4, 88, '😆');  -- 신남

-- 질문 데이터
INSERT INTO question (question_text) VALUES
('오늘 하루 중 가장 기억에 남는 순간은?'),
('오늘 나에게 고마운 사람은 누구인가요?'),
('오늘의 스트레스 원인은 무엇인가요?'),
('내일은 어떤 하루가 되길 바라나요?'),
('오늘 나는 어떤 감정을 가장 많이 느꼈나요?');
```

### 6.2 데이터 확인 쿼리

```sql
-- 전체 데이터 개수 확인
SELECT 
    (SELECT COUNT(*) FROM user) as users,
    (SELECT COUNT(*) FROM diary) as diaries,
    (SELECT COUNT(*) FROM emotion) as emotions,
    (SELECT COUNT(*) FROM question) as questions;

-- testuser의 일기와 감정 조회
SELECT d.entry_id, d.title, d.stress_level, d.entry_date,
       GROUP_CONCAT(CONCAT(e.emoji_icon, ':', e.emotion_level) SEPARATOR ', ') as emotions
FROM diary d
LEFT JOIN emotion e ON d.entry_id = e.entry_id
WHERE d.user_id = 'testuser'
GROUP BY d.entry_id
ORDER BY d.entry_date;
```

---

## 7. 향후 개선 사항

### 7.1 보안 개선
```sql
-- user 테이블 비밀번호 암호화
ALTER TABLE user MODIFY user_pw VARCHAR(64) NOT NULL COMMENT 'SHA-256 해시';

-- 비밀번호 솔트 컬럼 추가
ALTER TABLE user ADD COLUMN password_salt VARCHAR(32);
```

### 7.2 기능 확장
```sql
-- 일기 카테고리 테이블
CREATE TABLE category (
    category_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(20) NOT NULL
);

-- diary 테이블에 카테고리 추가
ALTER TABLE diary ADD COLUMN category_id INTEGER;
ALTER TABLE diary ADD FOREIGN KEY (category_id) REFERENCES category(category_id);

-- 일기 공유 기능
CREATE TABLE diary_share (
    share_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    entry_id INTEGER NOT NULL,
    shared_with VARCHAR(20) NOT NULL,
    shared_date DATETIME NOT NULL,
    FOREIGN KEY (entry_id) REFERENCES diary(entry_id),
    FOREIGN KEY (shared_with) REFERENCES user(user_id)
);
```

### 7.3 성능 개선
```sql
-- 파티셔닝 (날짜별)
ALTER TABLE diary PARTITION BY RANGE (YEAR(entry_date)) (
    PARTITION p2024 VALUES LESS THAN (2025),
    PARTITION p2025 VALUES LESS THAN (2026),
    PARTITION p2026 VALUES LESS THAN (2027)
);
```

---

*이 스키마 문서는 데이터베이스의 모든 구조를 상세히 설명합니다. 실제 구현 시 `DatabaseUtil.java`의 `createDatabase()` 메소드를 참조하세요.*

