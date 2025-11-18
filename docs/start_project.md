# 감정 일기장 프로젝트 시작 가이드 🚀

> **작성일**: 2025년 11월 18일  
> **버전**: 1.0  
> **대상**: 신규 개발자 및 테스터

---

## 📑 목차

1. [사전 요구사항](#1-사전-요구사항)
2. [환경 설정](#2-환경-설정)
3. [데이터베이스 설정](#3-데이터베이스-설정)
4. [프로젝트 설정](#4-프로젝트-설정)
5. [애플리케이션 실행](#5-애플리케이션-실행)
6. [기능 테스트](#6-기능-테스트)
7. [문제 해결](#7-문제-해결)
8. [추가 정보](#8-추가-정보)

---

## 1. 사전 요구사항

### 필수 소프트웨어

프로젝트를 실행하기 위해 다음 소프트웨어가 설치되어 있어야 합니다:

| 소프트웨어 | 버전 | 확인 방법 | 다운로드 |
|-----------|------|-----------|----------|
| **Java JDK** | 17 이상 | `java -version` | [Oracle](https://www.oracle.com/java/technologies/downloads/) |
| **Maven** | 3.6 이상 | `mvn -version` | [Apache Maven](https://maven.apache.org/download.cgi) |
| **MySQL** | 8.0 이상 | `mysql --version` | [MySQL](https://dev.mysql.com/downloads/mysql/) |

### 버전 확인

터미널에서 다음 명령어로 설치된 버전을 확인하세요:

```bash
# Java 버전 확인
java -version
# 출력 예시: java version "17.0.1"

# Maven 버전 확인
mvn -version
# 출력 예시: Apache Maven 3.8.6

# MySQL 버전 확인
mysql --version
# 출력 예시: mysql Ver 8.0.33
```

**❌ 만약 위 명령어가 작동하지 않는다면:**
- Java가 설치되지 않았거나 환경 변수(PATH)가 설정되지 않은 것입니다.
- 각 소프트웨어를 설치한 후 터미널을 재시작하세요.

---

## 2. 환경 설정

### 2-1. MySQL 서버 시작

MySQL 서버가 실행 중이어야 합니다.

#### macOS (Homebrew 사용 시)
```bash
# MySQL 서버 시작
brew services start mysql

# 또는
mysql.server start

# 상태 확인
brew services list | grep mysql
```

#### Windows
```bash
# 서비스에서 MySQL 시작
net start MySQL80

# 또는 MySQL Workbench에서 서버 시작
```

#### Linux
```bash
# MySQL 서버 시작
sudo systemctl start mysql

# 상태 확인
sudo systemctl status mysql
```

### 2-2. MySQL 접속 테스트

```bash
# MySQL에 root 계정으로 접속
mysql -u root -p

# 비밀번호 입력 후 다음과 같이 표시되면 성공:
# mysql>
```

**✅ 성공 시**: `mysql>` 프롬프트가 나타남  
**❌ 실패 시**: "Access denied" 오류 → 비밀번호를 확인하세요

접속 성공 후 나가기:
```sql
exit;
```

---

## 3. 데이터베이스 설정

### 3-1. 데이터베이스 비밀번호 설정 ⭐ **중요!**

프로젝트 코드에서 MySQL 비밀번호를 수정해야 합니다.

**수정해야 할 파일 (총 3개):**

#### 파일 1: `DatabaseUtil.java`
```bash
경로: src/main/java/com/diary/emotion/model/DatabaseUtil.java
```

```java
// 수정할 라인 찾기 (약 15번째 줄)
private static final String DB_PASSWORD = "REMOVED_PASSWORD";

// 본인의 MySQL root 비밀번호로 변경
private static final String DB_PASSWORD = "본인의_비밀번호";
```

#### 파일 2: `UserDAO.java`
```bash
경로: src/main/java/com/diary/emotion/model/UserDAO.java
```

```java
// 수정할 라인 찾기 (약 20번째 줄)
private static final String DB_PASSWORD = "REMOVED_PASSWORD";

// 본인의 MySQL root 비밀번호로 변경
private static final String DB_PASSWORD = "본인의_비밀번호";
```

#### 파일 3: `StatisticsDAO.java`
```bash
경로: src/main/java/com/diary/emotion/model/StatisticsDAO.java
```

```java
// 수정할 라인 찾기 (약 30번째 줄)
private static final String DB_PASSWORD = "REMOVED_PASSWORD";

// 본인의 MySQL root 비밀번호로 변경
private static final String DB_PASSWORD = "본인의_비밀번호";
```

**💡 팁**: IntelliJ IDEA에서 `Cmd+Shift+F` (macOS) 또는 `Ctrl+Shift+F` (Windows)로 "DB_PASSWORD"를 검색하면 한 번에 찾을 수 있습니다.

### 3-2. 데이터베이스 자동 생성

**중요**: 데이터베이스는 애플리케이션 **최초 실행 시 자동으로 생성**됩니다!

- `emotion_diary` 데이터베이스
- `user`, `diary`, `emotion` 테이블

**수동으로 생성할 필요 없습니다!** ✅

### 3-3. 테스트 데이터 추가 (선택사항)

테스트를 위해 샘플 사용자와 일기 데이터를 추가하려면:

```bash
# 프로젝트 루트 디렉토리에서 실행
cd /Users/iee12/IdeaProjects/emotion-diary-project

# MySQL에 테스트 데이터 입력
mysql -u root -p < test_data.sql
# 비밀번호 입력
```

**테스트 계정 정보:**
- 아이디: `testuser`
- 비밀번호: `test123`

**포함된 데이터:**
- 테스트 사용자 3명
- 샘플 일기 5개
- 감정 데이터 (긍정/부정 다양)

---

## 4. 프로젝트 설정

### 4-1. 프로젝트 디렉토리 이동

```bash
# 터미널에서 프로젝트 디렉토리로 이동
cd /Users/iee12/IdeaProjects/emotion-diary-project

# 현재 위치 확인
pwd
# 출력: /Users/iee12/IdeaProjects/emotion-diary-project
```

### 4-2. Maven 의존성 다운로드

```bash
# Maven 의존성 다운로드 및 프로젝트 정리
mvn clean install

# 진행 과정에서 다음과 같은 메시지가 나타남:
# [INFO] Downloading from central: ...
# [INFO] BUILD SUCCESS
```

**예상 소요 시간**: 첫 실행 시 1~3분 (인터넷 속도에 따라 다름)

**✅ 성공 시**: `BUILD SUCCESS` 메시지 출력  
**❌ 실패 시**: "문제 해결" 섹션 참고

### 4-3. 컴파일

```bash
# 소스 코드 컴파일
mvn compile

# 성공 시 출력:
# [INFO] BUILD SUCCESS
```

---

## 5. 애플리케이션 실행

### 방법 1: Maven 명령어 사용 (권장) ⭐

```bash
# 프로젝트 루트 디렉토리에서 실행
mvn exec:java

# 또는 컴파일과 동시에 실행
mvn clean compile exec:java
```

**실행 과정:**
1. Maven이 프로젝트를 컴파일
2. `AppLauncher.java`의 `main` 메소드 실행
3. 데이터베이스 자동 초기화 (최초 1회)
4. 로그인 화면 표시

**콘솔 출력 예시:**
```
=== 데이터베이스 초기화 시작 ===
[DatabaseUtil] 데이터베이스 'emotion_diary' 생성 완료
[DatabaseUtil] 테이블 생성 완료
=== 데이터베이스 초기화 완료 ===
=== 애플리케이션 시작 완료 ===
```

### 방법 2: IntelliJ IDEA 사용

1. **IntelliJ IDEA에서 프로젝트 열기**
   - `File` → `Open` → 프로젝트 폴더 선택

2. **AppLauncher.java 파일 열기**
   - 경로: `src/main/java/com/diary/emotion/AppLauncher.java`

3. **main 메소드 실행**
   - `main` 메소드 옆의 녹색 실행 버튼 클릭 ▶️
   - 또는 `Ctrl+Shift+F10` (Windows) / `Ctrl+Shift+R` (macOS)

4. **실행 확인**
   - 로그인 창이 나타나면 성공!

### 방법 3: JAR 파일 생성 및 실행

```bash
# JAR 파일 생성
mvn clean package

# JAR 파일 실행
java -jar target/emotion-diary-0.0.1-SNAPSHOT.jar
```

---

## 6. 기능 테스트

### 6-1. 회원가입 테스트

애플리케이션이 실행되면 로그인 화면이 나타납니다.

**단계별 테스트:**

1. **"회원가입" 버튼 클릭**
   
2. **회원가입 정보 입력**
   - 아이디: `mytest` (3~20자)
   - 비밀번호: `1234` (4~20자)
   - 비밀번호 확인: `1234`

3. **"회원가입" 버튼 클릭**
   - ✅ 성공 시: "회원가입이 완료되었습니다!" 메시지
   - ❌ 실패 시: 오류 메시지 확인

4. **자동으로 로그인 화면으로 이동**

**검증 항목:**
- [ ] 아이디 길이 제한 (3~20자)
- [ ] 비밀번호 길이 제한 (4~20자)
- [ ] 비밀번호 일치 확인
- [ ] 아이디 중복 확인
- [ ] "뒤로가기" 버튼 동작

### 6-2. 로그인 테스트

**테스트 계정 사용 (test_data.sql 실행 시):**
- 아이디: `testuser`
- 비밀번호: `test123`

**또는 방금 생성한 계정 사용:**
- 아이디: `mytest`
- 비밀번호: `1234`

**단계별 테스트:**

1. **로그인 정보 입력**
   - 아이디 입력
   - 비밀번호 입력

2. **로그인 실행**
   - "로그인" 버튼 클릭
   - 또는 `Enter` 키 누르기

3. **로그인 성공 확인**
   - ✅ 성공 시: "환영합니다!" 메시지 → 메인 화면 이동
   - ❌ 실패 시: "아이디 또는 비밀번호가 올바르지 않습니다." 메시지

**검증 항목:**
- [ ] 존재하지 않는 아이디 입력 시 오류
- [ ] 잘못된 비밀번호 입력 시 오류
- [ ] 빈 필드 입력 시 경고

### 6-3. 메인 화면 테스트

로그인 성공 후 메인 화면이 나타납니다.

**화면 구성:**
```
┌─────────────────────────────────────┐
│  [일기 쓰기] [열람] [통계]          │  ← 네비게이션 바
├─────────────────────────────────────┤
│                                     │
│        현재 선택된 탭 내용          │
│                                     │
│                                     │
└─────────────────────────────────────┘
```

**탭별 테스트:**

#### 📝 일기 쓰기 탭 (현재 구현 예정)
- 클릭 시 "일기 쓰기 화면 (구현 예정)" 메시지 표시

#### 📖 열람 탭 (현재 구현 예정)
- 클릭 시 "일기 열람 화면 (구현 예정)" 메시지 표시

#### 📊 통계 탭 (현재 구현 완료)
- 클릭 시 통계 화면 표시
- 기간 선택: 주간/월간/연간
- 평균 스트레스 표시
- 감정별 막대 차트 (데이터 있을 경우)
- 스트레스 추이 그래프 (데이터 있을 경우)

**통계 탭 테스트 방법:**

1. **통계 탭 클릭**

2. **기간 선택**
   - "주간" / "월간" / "연간" 콤보박스 선택
   - 연도, 월, 주 선택

3. **데이터 확인**
   - testuser 계정으로 로그인했다면 샘플 데이터 표시
   - 새 계정이라면 "데이터가 없습니다" 표시

### 6-4. 데이터베이스 확인

MySQL에서 직접 데이터를 확인할 수 있습니다.

```bash
# MySQL 접속
mysql -u root -p

# 데이터베이스 선택
USE emotion_diary;

# 사용자 목록 확인
SELECT * FROM user;

# 일기 목록 확인 (testuser의 경우)
SELECT entry_id, title, stress_level, entry_date 
FROM diary 
WHERE user_id = 'testuser' 
ORDER BY entry_date DESC;

# 감정 데이터 확인
SELECT e.emoji_icon, e.emotion_level, d.title 
FROM emotion e 
JOIN diary d ON e.entry_id = d.entry_id 
WHERE d.user_id = 'testuser';

# 평균 스트레스 확인
SELECT AVG(stress_level) as avg_stress 
FROM diary 
WHERE user_id = 'testuser';
```

---

## 7. 문제 해결

### 문제 1: "데이터베이스 초기화 실패" 메시지

**원인:**
- MySQL 서버가 실행되지 않음
- 비밀번호가 잘못됨

**해결 방법:**

```bash
# 1. MySQL 서버 상태 확인
brew services list | grep mysql
# 또는
ps aux | grep mysql

# 2. MySQL 서버 시작
brew services start mysql

# 3. 비밀번호 확인
mysql -u root -p
# 비밀번호 입력 후 접속되는지 확인

# 4. 코드에서 비밀번호 수정
# DatabaseUtil.java, UserDAO.java, StatisticsDAO.java
```

### 문제 2: "Access denied for user 'root'@'localhost'"

**원인:**
- 코드에 설정된 비밀번호와 실제 MySQL 비밀번호가 다름

**해결 방법:**

1. **실제 MySQL 비밀번호 확인**
   ```bash
   mysql -u root -p
   # 어떤 비밀번호로 접속되는지 확인
   ```

2. **코드 수정**
   - `DatabaseUtil.java`
   - `UserDAO.java`
   - `StatisticsDAO.java`
   
   위 3개 파일의 `DB_PASSWORD` 값을 실제 비밀번호로 변경

### 문제 3: Maven 빌드 실패 "BUILD FAILURE"

**원인:**
- Java 버전 불일치
- 인터넷 연결 문제

**해결 방법:**

```bash
# 1. Java 버전 확인 (17 이상이어야 함)
java -version

# 2. Maven 캐시 삭제
rm -rf ~/.m2/repository

# 3. 다시 빌드
mvn clean install -U

# 4. IntelliJ에서 프로젝트 다시 로드
# File → Invalidate Caches / Restart
```

### 문제 4: "Cannot find symbol" 컴파일 오류

**원인:**
- 패키지 구조 문제
- import 누락

**해결 방법:**

```bash
# Maven 프로젝트 다시 빌드
mvn clean compile

# IntelliJ에서
# 1. File → Project Structure → Modules
# 2. src/main/java를 "Sources"로 설정
# 3. Apply → OK
```

### 문제 5: 로그인이 안 됨

**원인:**
- 회원가입을 하지 않음
- 아이디/비밀번호 오류

**해결 방법:**

```bash
# MySQL에서 사용자 확인
mysql -u root -p

USE emotion_diary;
SELECT * FROM user;

# 결과가 비어있다면 회원가입 필요
# 또는 test_data.sql 실행
```

### 문제 6: 화면이 나타나지 않음

**원인:**
- GUI 디스플레이 문제

**해결 방법:**

```bash
# macOS에서
# 시스템 환경설정 → 보안 및 개인 정보 보호
# Java 실행 허용 확인

# 터미널에서 실행 로그 확인
mvn exec:java 2>&1 | tee output.log
```

---

## 8. 추가 정보

### 8-1. 프로젝트 구조

```
emotion-diary-project/
├── src/main/java/com/diary/emotion/
│   ├── AppLauncher.java           # 애플리케이션 진입점 ⭐
│   ├── model/                     # 데이터 계층
│   │   ├── DatabaseUtil.java     # DB 초기화
│   │   ├── UserDAO.java           # 사용자 데이터 접근
│   │   └── StatisticsDAO.java     # 통계 데이터 접근
│   ├── view/                      # UI 계층
│   │   ├── LoginView.java         # 로그인 화면
│   │   ├── SignUpView.java        # 회원가입 화면
│   │   ├── MainApplication.java   # 메인 화면
│   │   └── StatisticsView.java    # 통계 화면
│   ├── controller/                # 비즈니스 로직
│   │   ├── LoginController.java
│   │   ├── SignUpController.java
│   │   └── StatisticsController.java
│   └── util/                      # 유틸리티
│       └── Session.java           # 세션 관리
├── pom.xml                        # Maven 설정 ⭐
├── test_data.sql                  # 테스트 데이터
├── QUICKSTART.md                  # 빠른 시작 가이드
├── LOGIN_SYSTEM_COMPLETE.md       # 로그인 시스템 완료 보고서
└── docs/                          # 문서
    ├── 01_PROJECT_OVERVIEW.md
    ├── 02_API_REFERENCE.md
    ├── 03_DEVELOPMENT_GUIDE.md
    ├── 04_TODO_LIST.md
    ├── 05_DATABASE_SCHEMA.md
    ├── 06_CURRENT_STATUS_REPORT.md
    └── start_project.md           # 본 문서 ⭐
```

### 8-2. 핵심 파일 설명

| 파일 | 역할 | 중요도 |
|------|------|--------|
| `AppLauncher.java` | 애플리케이션 시작점, main() 메소드 포함 | ⭐⭐⭐ |
| `pom.xml` | Maven 의존성 및 빌드 설정 | ⭐⭐⭐ |
| `DatabaseUtil.java` | 데이터베이스 자동 생성 | ⭐⭐⭐ |
| `Session.java` | 로그인 사용자 정보 관리 | ⭐⭐ |
| `test_data.sql` | 테스트용 샘플 데이터 | ⭐ |

### 8-3. 사용된 기술 스택

| 분류 | 기술 | 용도 |
|------|------|------|
| **언어** | Java 17 | 메인 프로그래밍 언어 |
| **빌드** | Maven 3.8+ | 의존성 관리 및 빌드 |
| **UI** | Java Swing | GUI 프레임워크 |
| **DB** | MySQL 8.0 | 데이터베이스 |
| **차트** | JFreeChart 1.5.3 | 통계 그래프 |
| **패턴** | MVC | 아키텍처 패턴 |

### 8-4. 데이터베이스 스키마

```sql
-- user 테이블
CREATE TABLE user (
    user_id VARCHAR(20) PRIMARY KEY,
    user_pw VARCHAR(20) NOT NULL
);

-- diary 테이블
CREATE TABLE diary (
    entry_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL,
    title VARCHAR(50) NOT NULL,
    content TEXT,
    stress_level INTEGER NOT NULL,
    entry_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- emotion 테이블
CREATE TABLE emotion (
    emotion_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    entry_id INTEGER NOT NULL,
    emotion_level INTEGER NOT NULL,
    emoji_icon VARCHAR(10),
    FOREIGN KEY (entry_id) REFERENCES diary(entry_id)
);
```

### 8-5. 주요 의존성 (pom.xml)

```xml
<!-- JFreeChart - 통계 그래프 -->
<dependency>
    <groupId>org.jfree</groupId>
    <artifactId>jfreechart</artifactId>
    <version>1.5.3</version>
</dependency>

<!-- MySQL Connector -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.0.33</version>
</dependency>
```

### 8-6. 유용한 Maven 명령어

```bash
# 의존성 다운로드 및 설치
mvn clean install

# 컴파일만
mvn compile

# 컴파일 + 실행
mvn clean compile exec:java

# 테스트 실행
mvn test

# JAR 파일 생성
mvn package

# 프로젝트 정리
mvn clean

# 의존성 트리 보기
mvn dependency:tree

# 의존성 업데이트
mvn clean install -U
```

### 8-7. IntelliJ IDEA 단축키

| 기능 | macOS | Windows/Linux |
|------|-------|---------------|
| 프로젝트 실행 | `Ctrl+R` | `Shift+F10` |
| 전체 검색 | `Cmd+Shift+F` | `Ctrl+Shift+F` |
| 파일 찾기 | `Cmd+Shift+O` | `Ctrl+Shift+N` |
| 자동 import | `Cmd+Alt+O` | `Ctrl+Alt+O` |
| 코드 정렬 | `Cmd+Alt+L` | `Ctrl+Alt+L` |

### 8-8. 참고 문서

프로젝트에 대한 자세한 정보는 다음 문서를 참고하세요:

| 문서 | 내용 |
|------|------|
| [01_PROJECT_OVERVIEW.md](01_PROJECT_OVERVIEW.md) | 프로젝트 전체 개요 |
| [02_API_REFERENCE.md](02_API_REFERENCE.md) | API 명세 |
| [03_DEVELOPMENT_GUIDE.md](03_DEVELOPMENT_GUIDE.md) | 개발 가이드 |
| [04_TODO_LIST.md](04_TODO_LIST.md) | 할 일 목록 |
| [05_DATABASE_SCHEMA.md](05_DATABASE_SCHEMA.md) | DB 스키마 상세 |
| [06_CURRENT_STATUS_REPORT.md](06_CURRENT_STATUS_REPORT.md) | 현재 진행 상황 |
| [QUICKSTART.md](QUICKSTART.md) | 빠른 시작 가이드 |
| [LOGIN_SYSTEM_COMPLETE.md](LOGIN_SYSTEM_COMPLETE.md) | 로그인 시스템 완료 보고 |

---

## 📞 지원

### 질문이 있으신가요?

1. **문서 확인**: `docs/` 폴더의 문서들을 먼저 확인하세요
2. **문제 해결**: 위의 "7. 문제 해결" 섹션 참고
3. **로그 확인**: 콘솔 출력 메시지 확인

### 버그 리포트

버그를 발견하셨다면:
1. 오류 메시지 전체 복사
2. 실행 환경 정보 (OS, Java 버전, MySQL 버전)
3. 재현 단계

---

## ✅ 실행 체크리스트

프로젝트를 처음 실행하기 전에 다음 항목을 확인하세요:

- [ ] Java 17 이상 설치 확인
- [ ] Maven 3.6 이상 설치 확인
- [ ] MySQL 8.0 이상 설치 확인
- [ ] MySQL 서버 실행 중
- [ ] **DB 비밀번호 3개 파일에 설정** ⭐
- [ ] `mvn clean install` 성공
- [ ] `mvn compile` 성공
- [ ] (선택) `test_data.sql` 실행

모든 항목을 확인했다면:

```bash
mvn clean compile exec:java
```

**🎉 성공적으로 실행되었다면 로그인 화면이 나타납니다!**

---

**작성자**: GitHub Copilot  
**프로젝트**: Emotion Diary (감정 일기장)  
**최종 수정**: 2025년 11월 18일

