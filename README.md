# Emotion Diary (감정 일기장) 📝💭

## 프로젝트 소개

**Emotion Diary**는 사용자의 감정 기록을 통해 자신의 상태를 돌아보고 자기 이해를 높여 정서적 안정을 찾을 수 있도록 도와주는 감정 일기장 애플리케이션입니다.

### 주요 목적
- 감정 기록을 통한 자기 이해 향상
- 정서적 안정 및 심리적 웰빙 증진
- 감정 패턴 분석을 통한 자기 성찰 지원

## 주요 기능

- ✍️ **일기 작성**: 날짜별로 감정과 스트레스 레벨을 기록
- 😊 **감정 추적**: 다양한 감정 이모지로 하루의 감정 표현
- 📊 **통계 분석**: 감정 및 스트레스 패턴 시각화
- 🔍 **일기 조회/검색**: 과거 일기 검색 및 수정
- 👤 **사용자 관리**: 로그인/회원가입 기능

## 기술 스택

- **Language**: Java
- **Build Tool**: Maven
- **Database**: MySQL
- **UI**: Java Swing
- **Chart Library**: JFreeChart

## 설치 및 실행 방법

### 사전 요구사항

- **JDK**: Java 8 이상
- **Maven**: 3.6 이상
- **MySQL**: 5.7 이상
- **IDE**: IntelliJ IDEA, Eclipse 등 (선택사항)

### 1. 프로젝트 클론

```bash
git clone <repository-url>
cd emotion-diary-project
```

### 2. 데이터베이스 설정

MySQL에서 데이터베이스를 생성하고 테이블을 설정합니다:

```sql
-- 데이터베이스 생성
CREATE DATABASE emotion_diary;

-- 필요한 테이블 생성 (프로젝트의 SQL 스크립트 참조)
```

데이터베이스 연결 정보를 설정합니다:
- `src/main/java/com/diary/emotion/DB/DatabaseManager.java` 파일에서 DB 연결 정보를 확인/수정

### 3. 의존성 설치

```bash
mvn clean install
```

### 4. 애플리케이션 실행

#### 방법 1: Maven 사용
```bash
mvn exec:java -Dexec.mainClass="com.diary.emotion.share.Main"
```

#### 방법 2: IDE 사용
- `src/main/java/com/diary/emotion/share/Main.java` 파일을 열어 실행

#### 방법 3: JAR 파일 실행
```bash
# JAR 파일 빌드
mvn package

# JAR 파일 실행
java -jar target/emotion-diary-project-1.0-SNAPSHOT.jar
```

## 프로젝트 구조

```
emotion-diary-project/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── diary/
│                   └── emotion/
│                       ├── DB/           # 데이터베이스 관리
│                       ├── login/        # 로그인/회원가입
│                       ├── share/        # 메인 및 공유 기능
│                       ├── stats/        # 통계 분석
│                       ├── view/         # 일기 조회/검색
│                       └── write/        # 일기 작성
├── pom.xml
└── README.md
```

## 사용 방법

1. **회원가입**: 첫 실행 시 회원가입을 진행합니다.
2. **로그인**: 등록한 계정으로 로그인합니다.
3. **일기 작성**: 메인 화면에서 일기 작성 기능을 선택하여 감정과 일기를 기록합니다.
4. **통계 확인**: 통계 메뉴에서 감정 패턴과 스트레스 변화를 확인합니다.
5. **일기 관리**: 과거 일기를 조회하거나 수정/삭제할 수 있습니다.

## 문의처

프로젝트 관련 문의사항은 아래 개발자에게 연락 주시기 바랍니다:

### 개발팀
- **이아진** - ajin1390@gmail.com
- **이연주** - 3yjmy89@gmail.com
- **이지민** - jimin@029000@gmail.com
- **장정아** - isopurple@outlook.com

## 라이선스

이 프로젝트는 교육 목적으로 개발되었습니다.

---

*감정 일기를 통해 더 나은 자신을 만나보세요! 💝*

