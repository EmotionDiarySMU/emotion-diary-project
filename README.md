# Emotion Diary (감정 일기장) 📝💭

<p align="center">
  <b>감정 기록을 통해 자기 이해와 정서적 안정을 찾는 데스크톱 일기장 애플리케이션</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Maven-3.6+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white"/>
  <img src="https://img.shields.io/badge/Swing-GUI-orange?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/JFreeChart-1.5.4-blue?style=for-the-badge"/>
</p>

---

## 📖 목차

- [프로젝트 소개](#-프로젝트-소개)
- [주요 기능](#-주요-기능)
- [시스템 아키텍처](#-시스템-아키텍처)
- [기술 스택](#-기술-스택)
- [프로젝트 구조](#-프로젝트-구조)
- [데이터베이스 스키마](#-데이터베이스-스키마)
- [설치 및 실행 방법](#-설치-및-실행-방법)
- [사용 방법](#-사용-방법)
- [스크린샷](#-스크린샷)
- [기여 가이드](#-기여-가이드)
- [개발팀](#-개발팀)
- [라이선스](#-라이선스)

---

## 🎯 프로젝트 소개

**Emotion Diary**는 사용자의 일상 감정과 스트레스 수치를 기록하고 분석하여 자기 이해를 높이고 정서적 안정을 찾을 수 있도록 도와주는 **Java 기반 데스크톱 애플리케이션**입니다.

### 개발 목적

- 📊 **감정 패턴 분석**: 주간/월간/연간 단위로 감정과 스트레스 변화를 시각화
- 🧠 **자기 성찰 지원**: 매일 제공되는 질문을 통해 깊이 있는 일기 작성 유도
- 💪 **정서적 웰빙 증진**: 감정 기록 습관화를 통한 심리적 안정 도모
- 🔒 **개인 데이터 보안**: 사용자별 계정 관리 및 MySQL 기반 안전한 데이터 저장

### 핵심 가치

| 가치 | 설명 |
|:---:|---|
| **직관성** | 이모지 기반 감정 선택으로 누구나 쉽게 사용 |
| **분석력** | JFreeChart를 활용한 다양한 통계 차트 제공 |
| **지속성** | 50개의 랜덤 질문으로 매일 새로운 글감 제공 |
| **안전성** | 트랜잭션 기반 데이터 무결성 보장 |

---

## ✨ 주요 기능

### 1. 👤 사용자 인증 시스템

| 기능 | 설명 |
|---|---|
| **회원가입** | 아이디/비밀번호 유효성 검증, 중복 ID 체크 |
| **로그인** | 인증 후 메인 화면으로 자동 전환 |
| **로그아웃** | 저장하지 않은 변경사항 확인 후 안전하게 로그아웃 |
| **계정삭제** | 비밀번호 재확인 후 모든 데이터(일기, 감정) 완전 삭제 |

### 2. ✍️ 일기 작성 기능

- **오늘의 질문**: 50개의 사전 정의된 질문 중 랜덤으로 1개 제공
- **제목 및 내용**: 최대 30,000자 지원 (자동 줄바꿈)
- **감정 기록**: 12개 이모지 중 최대 4개 선택 + 각 감정별 강도(1-100) 설정
  - 😊 😆 😍 😌 😂 😩 😢 😠 😧 😰 😅 😔
- **스트레스 수치**: 슬라이더 + 직접 입력 (0-100)
- **자동 저장 알림**: 변경사항 있을 시 종료 전 저장 여부 확인

### 3. 🔍 일기 조회 및 검색

- **제목 검색**: 부분 일치 검색 지원
- **날짜 범위 검색**: 시작일 ~ 종료일 필터링
- **정렬**: 최신순 / 오래된순 정렬
- **상세 조회**: 별도 창에서 일기 내용 확인
- **다중 창 지원**: 여러 일기를 동시에 열람 가능 (중복 열기 방지)

### 4. ✏️ 일기 수정 및 삭제

- **수정 모드**: 열람 화면에서 수정 모드로 전환
- **수정 완료**: 변경사항 DB 반영 + 수정일 자동 기록
- **취소 기능**: 수정 내용 원상복구
- **삭제 확인**: 이중 확인 후 영구 삭제 (감정 데이터 포함)

### 5. 📊 통계 분석 (MVC 패턴)

| 기간 | 스트레스 차트 | 감정 차트 |
|---|---|---|
| **주간** | 월~일 요일별 평균 | 해당 주 감정 빈도 및 평균 강도 |
| **월간** | 1~5주차별 평균 | 해당 월 감정 빈도 및 평균 강도 |
| **연간** | 1~12월별 평균 | 해당 연도 감정 빈도 및 평균 강도 |

- **평균 스트레스 지수**: 선택 기간의 전체 평균 표시
- **막대 차트**: 기간별 스트레스 변화 추이
- **감정 분포**: 각 이모지별 사용 빈도(%) 및 평균 강도

---

## 🏗 시스템 아키텍처

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                            Presentation Layer (View)                        │
│  ┌─────────────────┐ ┌──────────────────┐ ┌────────────────────────────┐   │
│  │AuthenticationFrame│ │    MainView      │ │     StatisticsView         │   │
│  │  - LoginPanel    │ │  - WriteDiaryGUI │ │  - 차트 패널 (JFreeChart)  │   │
│  │  - SignUpPanel   │ │  - SearchPanel   │ │  - 날짜 선택기             │   │
│  │  - SuccessPanel  │ │  - ViewPanel     │ │  - 기간 필터 (주간/월간/연간)│   │
│  └─────────────────┘ └──────────────────┘ └────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                        Controller / Business Logic                          │
│  ┌────────────────────────────────────────────────────────────────────────┐ │
│  │                      StatisticsController (MVC)                         │ │
│  │  - 차트 데이터 조회 및 가공                                              │ │
│  │  - View ↔ DAO 중개                                                      │ │
│  │  - 날짜 범위 계산 (주/월/연 단위)                                        │ │
│  └────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           Data Access Layer (DAO)                           │
│  ┌─────────────────────┐ ┌───────────────────┐ ┌────────────────────────┐  │
│  │  DatabaseManager    │ │  StatisticsDAO    │ │  QuestionDBManager     │  │
│  │  - CRUD 일기/감정   │ │  - 통계 쿼리 전문  │ │  - 질문 초기화/조회    │  │
│  │  - 사용자 인증      │ │  - 스트레스 평균   │ │  - 랜덤 질문 제공      │  │
│  │  - 트랜잭션 관리    │ │  - 감정 집계       │ │                        │  │
│  └─────────────────────┘ └───────────────────┘ └────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                              Database Layer                                  │
│                       MySQL 8.0 (emotion_diary DB)                          │
│  ┌─────────┐   ┌─────────────┐   ┌─────────────┐   ┌─────────────────────┐ │
│  │  user   │───│    diary    │───│   emotion   │   │      question       │ │
│  │(1:N)    │   │   (1:N)     │   │             │   │  (50개 사전 질문)   │ │
│  └─────────┘   └─────────────┘   └─────────────┘   └─────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🛠 기술 스택

### Backend & Frontend

| 구분 | 기술 | 버전 | 용도 |
|:---:|---|:---:|---|
| **Language** | Java | 21 | 핵심 개발 언어 |
| **GUI Framework** | Swing | - | 데스크톱 UI 구현 |
| **Build Tool** | Maven | 3.6+ | 의존성 및 빌드 관리 |
| **Database** | MySQL | 8.0+ | 데이터 영속화 |
| **JDBC Driver** | mysql-connector-j | 8.4.0 | DB 연결 |
| **Chart Library** | JFreeChart | 1.5.4 | 통계 차트 시각화 |
| **Logging** | SLF4J Simple | 2.0.9 | 로깅 처리 |

### 주요 라이브러리 의존성 (pom.xml)

```xml
<dependencies>
    <!-- MySQL JDBC Driver -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.4.0</version>
    </dependency>
    
    <!-- JFreeChart for Statistics -->
    <dependency>
        <groupId>org.jfree</groupId>
        <artifactId>jfreechart</artifactId>
        <version>1.5.4</version>
    </dependency>
    
    <!-- SLF4J Logging -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>2.0.9</version>
    </dependency>
</dependencies>
```

---

## 📁 프로젝트 구조

```
emotion-diary-project/
├── 📄 pom.xml                          # Maven 빌드 설정
├── 📄 README.md                        # 프로젝트 문서
│
├── 📂 src/main/java/com/diary/emotion/
│   │
│   ├── 📂 DB/                          # 데이터베이스 계층
│   │   ├── DatabaseManager.java        # 핵심 DB 관리 (CRUD, 트랜잭션)
│   │   ├── StatisticsDAO.java          # 통계 전용 데이터 액세스
│   │   ├── QuestionDBManager.java      # 질문 데이터 관리 (초기화/조회)
│   │   ├── DBDebugUtil.java            # 디버깅용 유틸리티
│   │   ├── DiaryEntry.java             # 일기 엔티티 (DTO)
│   │   └── Emotion.java                # 감정 엔티티 (DTO)
│   │
│   ├── 📂 login/                       # 인증 모듈
│   │   └── AuthenticationFrame.java    # 로그인/회원가입 UI (CardLayout)
│   │
│   ├── 📂 share/                       # 공유 모듈
│   │   ├── Main.java                   # 애플리케이션 진입점
│   │   ├── MainView.java               # 메인 프레임 (싱글톤, CardLayout)
│   │   ├── SaveQuestion.java           # 종료 시 저장 확인 유틸리티
│   │   └── DeleteAccountDialog.java    # 계정 삭제 확인 다이얼로그
│   │
│   ├── 📂 stats/                       # 통계 모듈 (MVC 패턴)
│   │   ├── StatisticsView.java         # 통계 UI (차트 표시)
│   │   ├── StatisticsController.java   # 통계 비즈니스 로직
│   │   └── StatisticsDAO.java          # 통계 데이터 액세스
│   │
│   ├── 📂 view/                        # 일기 조회/수정 모듈
│   │   ├── SearchDiaryPanel.java       # 일기 검색 및 목록 표시
│   │   ├── ViewDiaryPanel.java         # 일기 상세 조회 (읽기 전용)
│   │   ├── ModifyPanel.java            # 일기 수정 패널
│   │   ├── ExtraWindow.java            # 상세 조회/수정 별도 창
│   │   └── DateSelectorPanel.java      # 날짜 선택 컴포넌트
│   │
│   └── 📂 write/                       # 일기 작성 모듈
│       ├── WriteDiaryGUI.java          # 일기 작성 메인 UI
│       ├── SingleIconChooserDialog.java# 감정 이모지 선택 팝업
│       ├── LengthFilter.java           # 텍스트 길이 제한 필터
│       ├── NumericRangeFilter.java     # 숫자 범위 입력 필터
│       └── QuestionDBManager.java      # (중복 - DB 패키지로 이동 권장)
│
└── 📂 target/                          # 빌드 결과물
    └── classes/                        # 컴파일된 클래스 파일
```

---

## 🗄 데이터베이스 스키마

### ERD (Entity Relationship Diagram)

```
┌─────────────────┐        ┌──────────────────────┐        ┌────────────────┐
│      user       │        │        diary         │        │    emotion     │
├─────────────────┤        ├──────────────────────┤        ├────────────────┤
│ user_id (PK)    │───────▶│ entry_id (PK, AI)    │───────▶│ emotion_id(PK) │
│ user_pw         │  1:N   │ user_id (FK)         │  1:N   │ entry_id (FK)  │
└─────────────────┘        │ title                │        │ emotion_level  │
                           │ content              │        │ emoji_icon     │
                           │ stress_level         │        └────────────────┘
                           │ entry_date           │
                           │ modify_date          │
                           └──────────────────────┘

┌───────────────────────┐
│       question        │
├───────────────────────┤
│ question_id (PK, AI)  │
│ question_text         │  ← 50개 사전 정의 질문
└───────────────────────┘
```

### 테이블 상세

#### 1. user (사용자)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `user_id` | VARCHAR(20) | PRIMARY KEY | 사용자 아이디 |
| `user_pw` | VARCHAR(20) | NOT NULL | 비밀번호 |

#### 2. diary (일기)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `entry_id` | INT | PRIMARY KEY, AUTO_INCREMENT | 일기 고유 ID |
| `user_id` | VARCHAR(20) | FOREIGN KEY, NOT NULL | 작성자 ID |
| `title` | VARCHAR(50) | - | 일기 제목 |
| `content` | TEXT | - | 일기 내용 (최대 30,000자) |
| `stress_level` | INT | NOT NULL | 스트레스 수치 (0-100) |
| `entry_date` | DATETIME | NOT NULL | 작성 일시 |
| `modify_date` | DATETIME | - | 수정 일시 |

#### 3. emotion (감정)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `emotion_id` | INT | PRIMARY KEY, AUTO_INCREMENT | 감정 고유 ID |
| `entry_id` | INT | FOREIGN KEY, NOT NULL | 연결된 일기 ID |
| `emotion_level` | INT | NOT NULL | 감정 강도 (1-100) |
| `emoji_icon` | VARCHAR(10) | NOT NULL, utf8mb4 | 이모지 아이콘 |

#### 4. question (질문)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `question_id` | INT | PRIMARY KEY, AUTO_INCREMENT | 질문 고유 ID |
| `question_text` | VARCHAR(100) | NOT NULL | 질문 텍스트 |

---

## 🚀 설치 및 실행 방법

### 사전 요구사항

| 요구사항 | 최소 버전 | 확인 명령어 |
|---|:---:|---|
| **JDK** | 21 | `java -version` |
| **Maven** | 3.6 | `mvn -version` |
| **MySQL** | 8.0 | `mysql --version` |

### Step 1: 프로젝트 클론

```bash
git clone <repository-url>
cd emotion-diary-project
```

### Step 2: 데이터베이스 설정

#### 2-1. MySQL 서버 시작 확인

```bash
# Windows
net start mysql80

# macOS/Linux
sudo systemctl start mysql
```

#### 2-2. DB 연결 정보 설정

`src/main/java/com/diary/emotion/DB/DatabaseManager.java` 파일을 열어 본인의 MySQL 설정에 맞게 수정:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useUnicode=true";
private static final String DB_ID = "root";         // MySQL 사용자명
private static final String DB_PW = "your_password"; // MySQL 비밀번호
```

> **📌 참고**: 프로그램 첫 실행 시 `emotion_diary` 데이터베이스와 모든 테이블이 자동으로 생성됩니다.

### Step 3: 의존성 설치 및 빌드

```bash
# 의존성 다운로드 및 빌드
mvn clean install

# 테스트 제외하고 빌드
mvn clean install -DskipTests
```

### Step 4: 애플리케이션 실행

#### 방법 1: Maven Exec 플러그인 사용

```bash
mvn exec:java -Dexec.mainClass="com.diary.emotion.share.Main"
```

#### 방법 2: IDE에서 직접 실행

1. IntelliJ IDEA / Eclipse에서 프로젝트 열기
2. `src/main/java/com/diary/emotion/share/Main.java` 파일 찾기
3. `main()` 메서드 실행 (Run)

#### 방법 3: JAR 파일 빌드 후 실행

```bash
# JAR 파일 빌드
mvn package

# JAR 파일 실행
java -jar target/emotion-diary-project-1.0-SNAPSHOT.jar
```

---

## 📖 사용 방법

### 1️⃣ 회원가입 및 로그인

1. 첫 실행 시 **"회원가입"** 버튼 클릭
2. 아이디(최대 20자), 비밀번호(최대 20자) 입력
3. 비밀번호 확인 후 **"가입하기"** 클릭
4. 5초 후 자동으로 로그인 화면 이동 (또는 버튼 클릭)
5. 가입한 계정으로 **로그인**

### 2️⃣ 일기 작성

1. 메뉴바에서 **"쓰기"** 탭 선택
2. 오늘의 질문을 참고하여 **제목** 및 **내용** 작성
3. **감정 이모지** 클릭 → 최대 4개 선택 + 강도 입력 (1-100)
4. **스트레스 수치** 슬라이더 또는 직접 입력 (0-100)
5. **"저장하기"** 버튼 클릭

### 3️⃣ 일기 조회 및 검색

1. 메뉴바에서 **"열람"** 탭 선택
2. 제목 키워드 또는 날짜 범위로 **검색**
3. 목록에서 일기 클릭 → 새 창에서 **상세 조회**
4. **"수정하기"** 버튼으로 수정 모드 진입
5. **"삭제"** 버튼으로 일기 영구 삭제

### 4️⃣ 통계 확인

1. 메뉴바에서 **"통계"** 탭 선택
2. 기간 선택: **주간 / 월간 / 연간**
3. 년/월/주 드롭다운으로 원하는 기간 선택
4. **평균 스트레스 지수** 및 **차트** 확인
5. 감정별 **사용 빈도(%)** 및 **평균 강도** 확인

### 5️⃣ 계정 관리

1. 메뉴바 우측 **"사용자: {아이디}"** 메뉴 클릭
2. **로그아웃**: 저장되지 않은 변경사항 확인 후 로그아웃
3. **계정삭제**: 경고 확인 → 비밀번호 재입력 → 모든 데이터 삭제

---

## 📸 스크린샷

> 📌 *스크린샷은 추후 업데이트 예정입니다.*

| 화면 | 설명 |
|:---:|---|
| 🔐 로그인/회원가입 | 인증 화면 (CardLayout 기반 전환) |
| ✍️ 일기 작성 | 질문, 제목, 내용, 감정, 스트레스 입력 |
| 🔍 일기 열람 | 검색, 목록, 상세 조회/수정/삭제 |
| 📊 통계 분석 | 주간/월간/연간 차트 시각화 |

---

## 🤝 기여 가이드

### 기여 방법

1. **Fork** 저장소
2. **Feature Branch** 생성: `git checkout -b feature/새로운기능`
3. **Commit**: `git commit -m "feat: 새로운 기능 추가"`
4. **Push**: `git push origin feature/새로운기능`
5. **Pull Request** 생성

### 커밋 컨벤션

| 타입 | 설명 |
|---|---|
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `docs` | 문서 수정 |
| `style` | 코드 포맷팅 (로직 변경 없음) |
| `refactor` | 코드 리팩토링 |
| `test` | 테스트 코드 추가 |
| `chore` | 빌드 설정, 의존성 업데이트 |

### 코드 스타일

- **들여쓰기**: 4 spaces (탭 미사용)
- **인코딩**: UTF-8
- **주석**: 한글 주석 권장, JavaDoc 형식 사용

---

## 👥 개발팀

| 이름 | 이메일 | 역할 |
|:---:|---|---|
| **이아진** | ajin1390@gmail.com | - |
| **이연주** | 3yjmy89@gmail.com | - |
| **이지민** | jimin029000@gmail.com | - |
| **장정아** | isopurple@outlook.com | - |

---

## 📜 라이선스

이 프로젝트는 **교육 목적**으로 개발되었습니다.

---

<p align="center">
  <i>감정 일기를 통해 더 나은 자신을 만나보세요! 💝</i>
</p>

<p align="center">
  <b>Made with ❤️ by Emotion Diary Team</b>
</p>
