# 데이터베이스 스키마 문서

## 1. 데이터베이스 개요

### 1.1 데이터베이스명
`emotion_diary`

### 1.2 문자셋
- **Character Set**: `utf8mb4`
- **Collation**: `utf8mb4_unicode_ci`
- **이유**: 이모지 저장 지원, 대소문자 구분 없는 검색

### 1.3 ERD (Entity Relationship Diagram)

```
┌─────────────────┐
│     user        │
│─────────────────│
│ user_id (PK)    │
│ user_pw         │
└─────────────────┘
         │
         │ 1:N
         │
         ▼
┌─────────────────┐
│     diary       │
│─────────────────│
│ entry_id (PK)   │
│ user_id (FK)    │
│ title           │
│ content         │
│ stress_level    │
│ entry_date      │
└─────────────────┘
         │
         │ 1:N (최대 4개)
         │
         ▼
┌─────────────────┐
│    emotion      │
│─────────────────│
│ emotion_id (PK) │
│ entry_id (FK)   │
│ emoji_icon      │
│ emotion_level   │
└─────────────────┘

┌─────────────────┐
│   question      │
│─────────────────│
│ question_id (PK)│
│ question_text   │
└─────────────────┘
(향후 확장용)
```

## 2. 테이블 상세 설명

### 2.1 user (사용자 테이블)

#### 테이블 설명
애플리케이션 사용자 정보를 저장하는 테이블

#### DDL
```sql
CREATE TABLE user (
    user_id VARCHAR(20) PRIMARY KEY,
    user_pw VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

#### 컬럼 설명
| 컬럼명 | 데이터 타입 | NULL 허용 | 키 | 기본값 | 설명 |
|--------|------------|----------|-----|--------|------|
| user_id | VARCHAR(20) | NO | PK | - | 사용자 아이디 (4-20자) |
| user_pw | VARCHAR(20) | NO | - | - | 비밀번호 (평문, 추후 해싱 예정) |

#### 제약 조건
- **PRIMARY KEY**: user_id
- **UNIQUE**: user_id (중복 불가)

#### 인덱스
- PRIMARY KEY 인덱스: user_id

#### 샘플 데이터
```sql
INSERT INTO user (user_id, user_pw) VALUES 
('testuser', 'password123'),
('john_doe', 'mypass456');
```

#### 비즈니스 룰
- 아이디는 4-20자 영문, 숫자, 언더스코어만 허용 (애플리케이션 레벨에서 검증)
- 비밀번호는 6-20자 (애플리케이션 레벨에서 검증)
- **보안 주의**: 현재는 평문 저장, 추후 BCrypt 해싱 적용 필요

---

### 2.2 diary (일기 테이블)

#### 테이블 설명
사용자가 작성한 일기 엔트리를 저장하는 메인 테이블

#### DDL
```sql
CREATE TABLE diary (
    entry_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL,
    title VARCHAR(50) NOT NULL,
    content TEXT,
    stress_level INTEGER NOT NULL,
    entry_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

#### 컬럼 설명
| 컬럼명 | 데이터 타입 | NULL 허용 | 키 | 기본값 | 설명 |
|--------|------------|----------|-----|--------|------|
| entry_id | INTEGER | NO | PK | AUTO_INCREMENT | 일기 고유 ID |
| user_id | VARCHAR(20) | NO | FK | - | 작성자 아이디 |
| title | VARCHAR(50) | NO | - | - | 일기 제목 (최대 50자) |
| content | TEXT | YES | - | NULL | 일기 내용 (최대 65,535자) |
| stress_level | INTEGER | NO | - | - | 스트레스 수치 (0-100) |
| entry_date | DATETIME | NO | - | - | 작성 일시 |

#### 제약 조건
- **PRIMARY KEY**: entry_id
- **FOREIGN KEY**: user_id → user(user_id)
  - ON DELETE CASCADE: 사용자 삭제 시 해당 일기도 삭제
- **CHECK** (애플리케이션 레벨): 
  - stress_level BETWEEN 0 AND 100
  - title 길이 1-50자

#### 인덱스
- PRIMARY KEY 인덱스: entry_id
- 외래 키 인덱스: user_id
- **권장 추가 인덱스**:
  ```sql
  CREATE INDEX idx_user_date ON diary(user_id, entry_date DESC);
  ```
  (사용자별 최신 일기 조회 최적화)

#### 샘플 데이터
```sql
INSERT INTO diary (user_id, title, content, stress_level, entry_date) VALUES 
('testuser', '행복한 하루', '오늘은 친구들과 즐거운 시간을 보냈다.', 20, '2024-11-13 14:30:00'),
('testuser', '힘든 하루', '업무가 많아서 스트레스를 많이 받았다.', 80, '2024-11-12 22:00:00');
```

#### 비즈니스 룰
- 제목은 필수 입력
- 내용은 선택 입력 (NULL 허용)
- 스트레스 수치는 0-100 범위
- 작성 일시는 서버 시간 기준 자동 설정

---

### 2.3 emotion (감정 테이블)

#### 테이블 설명
일기에 연결된 감정 정보를 저장하는 테이블 (일기당 최대 4개)

#### DDL
```sql
CREATE TABLE emotion (
    emotion_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    entry_id INTEGER NOT NULL,
    emotion_level INTEGER NOT NULL,
    emoji_icon VARCHAR(10) NOT NULL,
    FOREIGN KEY (entry_id) REFERENCES diary(entry_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

#### 컬럼 설명
| 컬럼명 | 데이터 타입 | NULL 허용 | 키 | 기본값 | 설명 |
|--------|------------|----------|-----|--------|------|
| emotion_id | INTEGER | NO | PK | AUTO_INCREMENT | 감정 고유 ID |
| entry_id | INTEGER | NO | FK | - | 연결된 일기 ID |
| emotion_level | INTEGER | NO | - | - | 감정 강도 (0-100) |
| emoji_icon | VARCHAR(10) | NO | - | - | 감정 이모지 (예: "😊 기쁨") |

#### 제약 조건
- **PRIMARY KEY**: emotion_id
- **FOREIGN KEY**: entry_id → diary(entry_id)
  - ON DELETE CASCADE: 일기 삭제 시 연결된 감정도 삭제
- **CHECK** (애플리케이션 레벨):
  - emotion_level BETWEEN 0 AND 100
  - 한 일기당 최대 4개 감정

#### 인덱스
- PRIMARY KEY 인덱스: emotion_id
- 외래 키 인덱스: entry_id

#### 샘플 데이터
```sql
INSERT INTO emotion (entry_id, emoji_icon, emotion_level) VALUES 
(1, '😊 기쁨', 80),
(1, '😌 평온', 70),
(2, '😔 우울', 60),
(2, '😖 좌절', 50);
```

#### 비즈니스 룰
- 감정 이모지는 Constants.EMOTIONS 배열에서 선택
- 일기당 최소 1개, 최대 4개의 감정 저장
- 감정 레벨은 0-100 범위
- 동일한 entry_id에 대해 같은 emoji_icon 중복 허용 (사용자가 재선택 가능)

---

### 2.4 question (질문 테이블) - 향후 확장용

#### 테이블 설명
사용자에게 제공할 일기 작성 가이드 질문을 저장하는 테이블 (현재 미사용)

#### DDL
```sql
CREATE TABLE question (
    question_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    question_text VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

#### 컬럼 설명
| 컬럼명 | 데이터 타입 | NULL 허용 | 키 | 기본값 | 설명 |
|--------|------------|----------|-----|--------|------|
| question_id | INTEGER | NO | PK | AUTO_INCREMENT | 질문 고유 ID |
| question_text | VARCHAR(100) | NO | - | - | 질문 내용 |

#### 샘플 데이터 (예시)
```sql
INSERT INTO question (question_text) VALUES 
('오늘 가장 기뻤던 순간은 언제였나요?'),
('오늘 어떤 일로 스트레스를 받으셨나요?'),
('내일은 어떤 하루를 보내고 싶으신가요?');
```

#### 향후 확장 계획
- 일기 작성 시 랜덤 질문 제공
- 사용자 맞춤형 질문 추천
- 질문-답변 매칭 테이블 추가

---

## 3. 관계 및 제약 조건

### 3.1 관계 정의

#### user ↔ diary (1:N)
- 한 사용자는 여러 일기를 작성할 수 있음
- 사용자 삭제 시 모든 일기 자동 삭제 (CASCADE)

#### diary ↔ emotion (1:N, 최대 4개)
- 한 일기는 최대 4개의 감정을 가질 수 있음
- 일기 삭제 시 모든 감정 자동 삭제 (CASCADE)

### 3.2 CASCADE 동작

```sql
-- 사용자 삭제 시
DELETE FROM user WHERE user_id = 'testuser';
-- → 해당 사용자의 모든 diary 레코드 삭제
-- → 해당 diary의 모든 emotion 레코드 삭제

-- 일기 삭제 시
DELETE FROM diary WHERE entry_id = 1;
-- → 해당 일기의 모든 emotion 레코드 삭제
```

### 3.3 참조 무결성
- 모든 외래 키는 참조 무결성을 보장
- 존재하지 않는 user_id로 diary 삽입 불가
- 존재하지 않는 entry_id로 emotion 삽입 불가

---

## 4. 쿼리 예제

### 4.1 사용자별 일기 통계
```sql
-- 사용자별 총 일기 수 및 평균 스트레스
SELECT 
    u.user_id,
    COUNT(d.entry_id) AS total_diaries,
    AVG(d.stress_level) AS avg_stress
FROM user u
LEFT JOIN diary d ON u.user_id = d.user_id
GROUP BY u.user_id;
```

### 4.2 최근 일기 조회 (감정 포함)
```sql
-- testuser의 최근 10개 일기 + 감정
SELECT 
    d.entry_id,
    d.title,
    d.entry_date,
    d.stress_level,
    GROUP_CONCAT(e.emoji_icon) AS emotions
FROM diary d
LEFT JOIN emotion e ON d.entry_id = e.entry_id
WHERE d.user_id = 'testuser'
GROUP BY d.entry_id
ORDER BY d.entry_date DESC
LIMIT 10;
```

### 4.3 기간별 감정 분석
```sql
-- 특정 기간 동안 가장 많이 느낀 감정
SELECT 
    e.emoji_icon,
    COUNT(*) AS frequency,
    AVG(e.emotion_level) AS avg_level
FROM emotion e
JOIN diary d ON e.entry_id = d.entry_id
WHERE d.user_id = 'testuser'
  AND d.entry_date BETWEEN '2024-11-01' AND '2024-11-30'
GROUP BY e.emoji_icon
ORDER BY frequency DESC;
```

### 4.4 스트레스 추이 (주간)
```sql
-- 최근 7일간 일별 평균 스트레스
SELECT 
    DATE(entry_date) AS date,
    AVG(stress_level) AS avg_stress
FROM diary
WHERE user_id = 'testuser'
  AND entry_date >= DATE_SUB(NOW(), INTERVAL 7 DAY)
GROUP BY DATE(entry_date)
ORDER BY date;
```

---

## 5. 인덱스 전략

### 5.1 현재 인덱스
```sql
-- user 테이블
PRIMARY KEY (user_id)

-- diary 테이블
PRIMARY KEY (entry_id)
INDEX (user_id)

-- emotion 테이블
PRIMARY KEY (emotion_id)
INDEX (entry_id)
```

### 5.2 권장 추가 인덱스
```sql
-- 일기 조회 성능 향상
CREATE INDEX idx_diary_user_date ON diary(user_id, entry_date DESC);

-- 제목 검색 성능 향상 (FULLTEXT 인덱스)
CREATE FULLTEXT INDEX idx_diary_title ON diary(title);

-- 감정 통계 성능 향상
CREATE INDEX idx_emotion_icon ON emotion(emoji_icon);
```

### 5.3 인덱스 사용 쿼리
```sql
-- idx_diary_user_date 사용
EXPLAIN SELECT * FROM diary 
WHERE user_id = 'testuser' 
ORDER BY entry_date DESC 
LIMIT 10;

-- idx_diary_title 사용 (FULLTEXT 검색)
SELECT * FROM diary 
WHERE MATCH(title) AGAINST('행복' IN NATURAL LANGUAGE MODE);
```

---

## 6. 데이터 마이그레이션

### 6.1 초기 데이터베이스 생성
```java
// DatabaseUtil.createDatabase() 실행
// 또는 MySQL 클라이언트에서 직접 실행
```

### 6.2 백업
```bash
# 전체 데이터베이스 백업
mysqldump -u root -p emotion_diary > emotion_diary_backup.sql

# 특정 테이블만 백업
mysqldump -u root -p emotion_diary user diary emotion > partial_backup.sql
```

### 6.3 복원
```bash
# 백업 파일로부터 복원
mysql -u root -p emotion_diary < emotion_diary_backup.sql
```

---

## 7. 보안 고려사항

### 7.1 현재 상태
- ❌ 비밀번호 평문 저장 (개발 단계)
- ✅ PreparedStatement 사용 (SQL Injection 방지)
- ✅ 외래 키 제약 조건 설정

### 7.2 개선 필요 사항
```sql
-- 비밀번호 컬럼 길이 확장 (해싱 후 저장 위해)
ALTER TABLE user MODIFY user_pw VARCHAR(255);

-- 비밀번호 해싱 적용 (Java 코드)
-- BCrypt 라이브러리 사용 권장
```

### 7.3 접근 제어
```sql
-- 애플리케이션 전용 DB 사용자 생성
CREATE USER 'emotion_app'@'localhost' IDENTIFIED BY 'secure_password';
GRANT SELECT, INSERT, UPDATE, DELETE ON emotion_diary.* TO 'emotion_app'@'localhost';
FLUSH PRIVILEGES;
```

---

## 8. 성능 최적화

### 8.1 쿼리 최적화
- 필요한 컬럼만 SELECT (SELECT * 지양)
- LIMIT 사용으로 불필요한 데이터 로드 방지
- JOIN 시 인덱스 활용

### 8.2 Connection Pool
```xml
<!-- pom.xml에 HikariCP 추가 (선택사항) -->
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>5.0.1</version>
</dependency>
```

### 8.3 캐싱 전략 (향후)
- 자주 조회되는 데이터 메모리 캐싱
- 통계 데이터 미리 계산 후 저장

---

## 9. 데이터 타입 선택 이유

### 9.1 VARCHAR vs TEXT
- **VARCHAR(50)**: 제목 - 길이 제한 명확, 인덱스 효율
- **TEXT**: 내용 - 길이 제한 없음, 유연성

### 9.2 INTEGER vs TINYINT
- **INTEGER**: ID 필드 - AUTO_INCREMENT 범위 충분
- **INTEGER**: stress_level, emotion_level - 통일성 (0-100이지만 INT 사용)

### 9.3 DATETIME vs TIMESTAMP
- **DATETIME**: 타임존 무관, 범위 넓음 (1000-9999년)
- LocalDateTime과 호환성 좋음

---

## 10. 트러블슈팅

### 10.1 외래 키 오류
```
ERROR 1452: Cannot add or update a child row: a foreign key constraint fails
```
**해결**: 참조되는 부모 레코드 먼저 삽입

### 10.2 이모지 저장 오류
```
ERROR 1366: Incorrect string value
```
**해결**: utf8mb4 문자셋 사용 확인

### 10.3 AUTO_INCREMENT 리셋
```sql
-- AUTO_INCREMENT 값 확인
SELECT AUTO_INCREMENT FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'emotion_diary' AND TABLE_NAME = 'diary';

-- 리셋 (주의: 데이터 삭제 후에만)
ALTER TABLE diary AUTO_INCREMENT = 1;
```

---

## 부록: DDL 전체 스크립트

```sql
-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS emotion_diary 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE emotion_diary;

-- user 테이블
CREATE TABLE user (
    user_id VARCHAR(20) PRIMARY KEY,
    user_pw VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- diary 테이블
CREATE TABLE diary (
    entry_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL,
    title VARCHAR(50) NOT NULL,
    content TEXT,
    stress_level INTEGER NOT NULL,
    entry_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- emotion 테이블
CREATE TABLE emotion (
    emotion_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    entry_id INTEGER NOT NULL,
    emotion_level INTEGER NOT NULL,
    emoji_icon VARCHAR(10) NOT NULL,
    FOREIGN KEY (entry_id) REFERENCES diary(entry_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- question 테이블
CREATE TABLE question (
    question_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    question_text VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 인덱스 생성 (권장)
CREATE INDEX idx_diary_user_date ON diary(user_id, entry_date DESC);
```

