# 감정 일기장 통계 기능 (Emotion Diary Statistics)

**Stats 브랜치** - 감정 일기 데이터를 분석하여 시각화된 통계 그래프로 제공

---

## 📊 주요 기능

### 통계 분석
- **감정 통계**: 선택한 기간 동안의 감정별 횟수와 평균 수치를 막대 차트로 표시
- **스트레스 추이**: 시간에 따른 스트레스 변화를 꺾은선 그래프로 표시
- **평균 스트레스 지수**: 선택한 기간의 평균 스트레스 수치 계산

### 기간 선택
- **주간**: 특정 주의 일별 통계 (월~일)
- **월간**: 특정 달의 주별 통계 (1주~5주)
- **연간**: 특정 년도의 월별 통계 (1월~12월)

---

## 🛠 기술 스택

- **언어**: Java 17
- **GUI**: Java Swing
- **차트**: JFreeChart 1.5.3
- **데이터베이스**: MySQL 8.0
- **JDBC**: MySQL Connector/J 8.0.33
- **빌드**: Maven

---

## 🚀 빠른 시작

### 1. 사전 준비
- Java 17 이상
- MySQL 8.0 이상 (실행 중이어야 함)

### 2. 프로젝트 클론
```bash
git clone <repository-url>
cd emotion-diary-project
git checkout stats
```

### 3. 데이터베이스 설정
프로젝트 루트에 `db.properties` 파일 생성:

```bash
cp db.properties.example db.properties
```

`db.properties` 파일 편집:
```properties
db.url=jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=UTC
db.user=root
db.password=YOUR_MYSQL_PASSWORD_HERE
```

### 4. 실행
```
IDE에서 Main.java 실행:
src/main/java/com/diary/emotion/Main.java
```

프로그램 실행 시 자동으로:
- emotion_diary 데이터베이스 생성
- 필요한 테이블 생성 (user, diary, emotion, question)
- 통계 GUI 창 표시

---

## 📁 데이터베이스 구조

### 테이블

#### 1. user (사용자)
- `user_id` VARCHAR(20) PRIMARY KEY
- `user_pw` VARCHAR(20)

#### 2. diary (일기)
- `entry_id` INTEGER PRIMARY KEY AUTO_INCREMENT
- `user_id` VARCHAR(20) FOREIGN KEY
- `title` VARCHAR(50)
- `content` TEXT
- `stress_level` INTEGER
- `entry_date` DATETIME

#### 3. emotion (감정)
- `emotion_id` INTEGER PRIMARY KEY AUTO_INCREMENT
- `entry_id` INTEGER FOREIGN KEY
- `emotion_level` INTEGER
- `emoji_icon` VARCHAR(10)

#### 4. question (질문)
- `question_id` INTEGER PRIMARY KEY AUTO_INCREMENT
- `question_text` VARCHAR(100)

---

## 📦 패키지 구조

```
src/main/java/com/diary/emotion/
├── Main.java                   - 프로그램 실행 진입점
├── DatabaseUtil.java           - DB 초기화 및 연결
├── MainView.java               - 메인 GUI 창
├── StatisticsView.java         - 통계 차트 UI (View)
├── StatisticsController.java   - 이벤트 처리 및 로직 (Controller)
└── StatisticsDAO.java          - ���이터베이스 조회 (DAO)
```

**MVC 패턴** 적용:
- **Model**: StatisticsDAO (데이터 접근)
- **View**: StatisticsView (UI 표시)
- **Controller**: StatisticsController (비즈니스 로직)

---

## 🧪 테스트 데이터

테스트용 샘플 데이터는 `test` 디렉토리에 있습니다:
- `TestDataInserter.java`: 테스트 데이터 자동 생성
- `IntegrationTest.java`: 통합 테스트 실행

```bash
# 테스트 데이터 삽입
Run: src/main/java/com/diary/emotion/test/TestDataInserter.java

# 통합 테스트
Run: src/main/java/com/diary/emotion/test/IntegrationTest.java
```

---

## 📚 문서

- **STATS_TEST_RESULT.md**: 통계 기능 테스트 결과 및 검증
- **REQUIREMENTS.md**: 프로젝트 요구사항 명세

---

## ⚙️ 설정 파일

### db.properties
데이터베이스 연결 정보를 담고 있습니다.
- `.gitignore`에 등록되어 있어 Git에 커밋되지 않습니다
- 보안을 위해 비밀번호는 절대 공개하지 마세요

---

## 📊 통계 기능 상세

### 감정 차트
- **X축**: 12가지 감정 (😊행복, 😆신남, 😍설렘, 😌편안, 😂재미, 🤗고마움, 😢슬픔, 😠분노, 😰불안, 😅민망, 😧당황, 😔미안함)
- **Y축**: 퍼센트 (0-100%)
- **막대**: 파란색(횟수), 연한 파란색(평균 수치)

### 스트레스 차트
- **X축**: 시간 단위 (요일/주차/월)
- **Y축**: 스트레스 수치 (0-100)
- **그래프**: 파란색 꺾은선

### 평균 스트레스
- 선택한 기간의 모든 일기 스트레스 지수 평균
- 화면 하단에 표시

---

## 🔧 문제 해결

### MySQL 연결 오류
- MySQL 서버가 실행 중인지 확인
- `db.properties` 파일의 비밀번호가 정확한지 확인
- 포트 번호 확인 (기본: 3306)

### 데이터가 표시되지 않음
- 데이터베이스에 일기 데이터가 있는지 확인
- TestDataInserter.java로 샘플 데이터 생성

### 차트가 비어있음
- 선택한 기간에 데이터가 있는지 확인
- 주간/월간/연간 콤보박스에서 올바른 날짜 선택

---

## 🎯 개발 정보

- **브랜치**: stats
- **목적**: 감정 일기 데이터 시각화 및 통계 분석
- **상태**: 테스트 완료

---

**개발자**: 감정 일기장 팀  
**최종 업데이트**: 2025년 11월 20일

