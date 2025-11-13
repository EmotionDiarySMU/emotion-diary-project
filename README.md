# 감정 일기장 프로그램 (Emotion Diary) 😊

> 우울증 등으로 감정적으로 힘들어하는 사람들에게 작은 위로를 전하고자, 감정을 기록하고 피드백을 받을 수 있는 일기장 프로그램

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## 📋 목차

- [프로젝트 소개](#-프로젝트-소개)
- [주요 기능](#-주요-기능)
- [기술 스택](#-기술-스택)
- [설치 방법](#-설치-방법)
- [사용 방법](#-사용-방법)
- [프로젝트 구조](#-프로젝트-구조)
- [개발 문서](#-개발-문서)
- [스크린샷](#-스크린샷)
- [향후 계획](#-향후-계획)
- [기여하기](#-기여하기)
- [라이선스](#-라이선스)

## 🌟 프로젝트 소개

**Emotion Diary**는 자신의 감정을 기록하고 시각화하여 정서적 안정과 자기 이해를 돕는 Java Swing 기반 데스크톱 애플리케이션입니다.

### 핵심 가치
- **감정 인식**: 하루의 감정을 명확히 인식하고 기록
- **자기 성찰**: 감정 패턴을 그래프로 시각화하여 자기 이해 증진
- **정서적 지원**: 감정 기록을 통한 심리적 안정감 제공

## ✨ 주요 기능

### 1. 사용자 인증
- 🔐 회원가입 및 로그인
- 👤 개인별 독립적인 일기 관리
- 🔒 세션 기반 보안

### 2. 일기 작성
- ✍️ 제목 및 내용 입력
- 😊 감정 선택 (최대 4개)
- 📊 각 감정별 수치 기록 (0-100)
- 💆 스트레스 수치 기록 (0-100)
- 📅 작성 일시 자동 기록

### 3. 일기 열람 및 관리
- 📖 날짜별 일기 목록 조회
- 🔍 제목 검색 기능
- ⬆️⬇️ 오름차순/내림차순 정렬
- ✏️ 일기 수정
- 🗑️ 일기 삭제

### 4. 통계 및 시각화
- 📈 감정 수치 그래프 (막대 차트)
- 📉 스트레스 수치 그래프 (꺾은선 차트)
- 🎯 평균 스트레스 수치 표시
- 📆 주간/월간/연간 조회 모드

## 🛠 기술 스택

### 개발 환경
- **언어**: Java 17
- **빌드 도구**: Maven 3.6+
- **IDE**: IntelliJ IDEA / Eclipse

### 주요 라이브러리
- **GUI**: Java Swing
- **데이터베이스**: MySQL 8.0.33
- **JDBC**: MySQL Connector/J 8.0.33
- **차트**: JFreeChart 1.5.3
- **테스트**: JUnit 4.13.2

### 디자인 패턴
- **MVC Pattern**: Model-View-Controller 아키텍처
- **DAO Pattern**: 데이터 접근 계층 분리
- **Singleton Pattern**: 세션 관리

## 📦 설치 방법

### 1. 사전 요구사항
- **JDK 17** 이상 설치
- **Maven** 3.6 이상 설치
- **MySQL** 8.0 이상 설치 및 실행 중

### 2. 저장소 클론
```bash
git clone https://github.com/yourusername/emotion-diary.git
cd emotion-diary
```

### 3. MySQL 설정
```sql
-- MySQL 서버 실행 확인
mysql -u root -p

-- 데이터베이스는 애플리케이션 첫 실행 시 자동 생성됩니다.
```

### 4. 데이터베이스 설정 수정
`src/main/java/share/DatabaseUtil.java` 파일에서 MySQL 접속 정보를 수정하세요:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password"; // 본인의 비밀번호로 수정
```

### 5. 의존성 설치 및 빌드
```bash
mvn clean install
```

### 6. 애플리케이션 실행
```bash
# Maven으로 실행
mvn exec:java -Dexec.mainClass="com.diary.emotion.AppLauncher"

# 또는 IDE에서 AppLauncher.java 실행
```

## 🚀 사용 방법

### 1. 회원가입 및 로그인
1. 애플리케이션 실행 시 로그인 화면이 나타납니다
2. "회원가입" 버튼을 클릭하여 계정을 생성합니다
3. 아이디와 비밀번호를 입력하여 로그인합니다

### 2. 일기 작성
1. 상단의 "일기 쓰기" 탭을 클릭합니다
2. 제목과 내용을 입력합니다
3. 스트레스 수치를 슬라이더로 조절합니다 (0-100)
4. "감정 추가" 버튼으로 최대 4개의 감정을 선택합니다
5. 각 감정의 수치를 슬라이더로 조절합니다
6. "저장" 버튼을 클릭하여 일기를 저장합니다

### 3. 일기 열람
1. "열람" 탭을 클릭합니다
2. 목록에서 일기를 선택하여 상세 내용을 확인합니다
3. 검색창에 키워드를 입력하여 제목 검색이 가능합니다
4. 정렬 옵션으로 날짜/제목 기준 정렬이 가능합니다
5. 상세보기에서 "수정" 또는 "삭제" 버튼을 사용합니다

### 4. 통계 확인
1. "통계" 탭을 클릭합니다
2. 주간/월간/연간 모드를 선택합니다
3. 날짜를 선택하여 원하는 기간의 통계를 확인합니다
4. 감정 수치 그래프와 스트레스 추이를 확인합니다

## 📁 프로젝트 구조

```
emotion-diary/
├── src/main/java/
│   ├── com.diary.emotion/          # 메인 애플리케이션
│   │   ├── AppLauncher.java        # 진입점
│   │   ├── MainApplication.java    # 메인 화면
│   │   ├── model/                  # 데이터 모델
│   │   ├── auth/                   # 로그인/회원가입
│   │   ├── write/                  # 일기 작성
│   │   ├── view/                   # 일기 열람
│   │   └── statistics/             # 통계
│   └── share/                      # 공통 유틸리티
│       ├── DatabaseUtil.java
│       ├── SessionManager.java
│       └── Constants.java
├── docs/                           # 프로젝트 문서
│   ├── 01_PROJECT_OVERVIEW.md
│   ├── 02_TECHNICAL_DESIGN.md
│   ├── 03_DEVELOPMENT_GUIDE.md
│   ├── 04_TODO_LIST.md
│   └── 05_DATABASE_SCHEMA.md
├── pom.xml                         # Maven 설정
└── README.md
```

## 📚 개발 문서

상세한 개발 문서는 `docs/` 폴더에서 확인할 수 있습니다:

- **[프로젝트 개요](docs/01_PROJECT_OVERVIEW.md)**: 프로젝트 전반적인 소개
- **[기술 설계 문서](docs/02_TECHNICAL_DESIGN.md)**: 아키텍처 및 클래스 설계
- **[개발 가이드](docs/03_DEVELOPMENT_GUIDE.md)**: 코딩 컨벤션 및 개발 순서
- **[TODO 리스트](docs/04_TODO_LIST.md)**: 상세한 개발 단계별 체크리스트
- **[데이터베이스 스키마](docs/05_DATABASE_SCHEMA.md)**: DB 구조 및 쿼리 예제

## 📸 스크린샷

### 로그인 화면
*로그인 및 회원가입 화면*

### 일기 작성 화면
*감정 선택 및 스트레스 수치 입력*

### 일기 열람 화면
*목록 조회 및 상세보기*

### 통계 화면
*감정 그래프 및 스트레스 추이*

## 🔮 향후 계획

### 기능 추가
- [ ] 비밀번호 해싱 (BCrypt)
- [ ] 일기 백업/복원 기능
- [ ] 테마 변경 (다크 모드)
- [ ] 일기 내보내기 (PDF, TXT)
- [ ] 감정 패턴 분석 리포트
- [ ] 알림 기능 (일기 작성 리마인더)

### 기술적 개선
- [ ] Connection Pool 적용 (HikariCP)
- [ ] Logger 프레임워크 도입
- [ ] 단위 테스트 작성
- [ ] 설정 파일 외부화

## 🤝 기여하기

기여는 언제나 환영합니다! 다음 단계를 따라주세요:

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

## 👥 개발자

- **Your Name** - [GitHub](https://github.com/yourusername)

## 🙏 감사의 말

- JFreeChart 라이브러리 제공
- MySQL 커뮤니티
- Java Swing 튜토리얼 및 예제 제공자들

---

**Made with ❤️ for mental wellness**
