# 감정 일기장 (Emotion Diary) 😊

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue?style=for-the-badge&logo=apache-maven)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Swing](https://img.shields.io/badge/Swing-GUI-green?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

**감정을 기록하고 시각화하여 정서적 안정과 자기 이해를 돕는 데스크톱 애플리케이션**

[기능 소개](#-주요-기능) • [빠른 시작](#-빠른-시작) • [설치 가이드](#-설치-가이드) • [문서](#-문서) • [기여하기](#-기여하기)

</div>

---

## 📖 목차

- [프로젝트 소개](#-프로젝트-소개)
- [주요 기능](#-주요-기능)
- [기술 스택](#-기술-스택)
- [빠른 시작](#-빠른-시작)
- [설치 가이드](#-설치-가이드)
- [사용 방법](#-사용-방법)
- [프로젝트 구조](#-프로젝트-구조)
- [데이터베이스](#-데이터베이스)
- [문서](#-문서)
- [스크린샷](#-스크린샷)
- [로드맵](#-로드맵)
- [기여하기](#-기여하기)
- [라이선스](#-라이선스)
- [문의](#-문의)

---

## 🌟 프로젝트 소개

**Emotion Diary**는 일상의 감정과 스트레스를 체계적으로 기록하고, 이를 시각화된 통계 그래프로 분석할 수 있는 Java Swing 기반 데스크톱 애플리케이션입니다. 감정적으로 힘든 시기를 겪는 사람들에게 자기 성찰과 정서적 안정을 제공하고자 개발되었습니다.

### 🎯 핵심 가치

- **감정 인식**: 12가지 세분화된 감정으로 하루의 감정을 명확히 기록
- **자기 성찰**: 시간에 따른 감정 패턴을 그래프로 시각화하여 자기 이해 증진
- **정서적 지원**: 일기 작성을 통한 감정 정리 및 심리적 안정감 제공
- **데이터 기반 분석**: 주간/월간/연간 통계로 장기적인 감정 변화 추적

### ✨ 프로젝트 특징

- ✅ **완전한 CRUD 기능** - 일기 작성, 조회, 수정, 삭제
- ✅ **사용자 인증 시스템** - 개인별 독립적인 일기 관리
- ✅ **실시간 통계 분석** - JFreeChart 기반 시각화
- ✅ **직관적인 UI/UX** - 파스텔 톤의 부드러운 디자인
- ✅ **MVC 아키텍처** - 확장 가능한 구조
- ✅ **트랜잭션 처리** - 데이터 무결성 보장

---

## ✨ 주요 기능

### 🔐 1. 사용자 인증
- **회원가입**: 아이디 중복 확인 및 비밀번호 검증
- **로그인**: 세션 기반 사용자 인증
- **세션 관리**: 로그인 상태 유지 및 안전한 로그아웃

### ✍️ 2. 일기 작성
- **감정 선택**: 12가지 감정 중 최대 4개 선택 가능
  - **긍정 감정 (6개)**: 😊 행복, 😆 신남, 😍 설렘, 😌 편안, 😂 재미, 🤗 고마움
  - **부정 감정 (6개)**: 😢 슬픔, 😠 분노, 😰 불안, 😅 민망, 😧 당황, 😔 미안함
- **감정 수치**: 각 감정별 0-100 범위 슬라이더로 강도 입력
- **스트레스 기록**: 0-100 범위의 스트레스 수치 입력
- **제목/내용**: 일기 제목 및 본문 작성
- **자동 저장**: 작성 일시 자동 기록

### 📖 3. 일기 열람 및 관리
- **목록 조회**: 사용자별 일기 목록 테이블 뷰
- **검색 기능**: 제목 키워드 검색
- **정렬 옵션**: 최신순/오래된순 정렬
- **상세보기**: 일기 전체 내용 확인 (감정, 스트레스, 작성일 포함)
- **수정 기능**: 기존 일기 내용 편집 (제목, 내용, 감정, 스트레스)
- **삭제 기능**: 확인 대화상자 후 일기 삭제

### 📊 4. 통계 분석
- **감정 통계 차트**: 
  - 막대 차트로 긍정/부정 감정 분류 표시
  - 선택 기간 내 감정별 평균 수치 계산
- **스트레스 추이 그래프**:
  - 꺾은선 차트로 시간에 따른 스트레스 변화 표시
  - 주간: 일별 데이터, 월간: 일별 데이터, 연간: 월별 데이터
- **평균 스트레스 지수**: 선택 기간의 평균 스트레스 수치 표시
- **기간 선택**: 주간/월간/연간 모드 및 상세 날짜 선택
- **실시간 갱신**: 콤보박스 변경 시 자동 데이터 업데이트

---

## 🛠 기술 스택

### 개발 환경
| 항목 | 기술 | 버전 |
|------|------|------|
| 언어 | Java | 17 |
| 빌드 도구 | Maven | 3.6+ |
| 데이터베이스 | MySQL | 8.0.33 |
| IDE | IntelliJ IDEA / Eclipse | - |

### 주요 라이브러리
| 라이브러리 | 용도 | 버전 |
|------------|------|------|
| Java Swing | GUI 프레임워크 | JDK 내장 |
| MySQL Connector/J | JDBC 드라이버 | 8.0.33 |
| JFreeChart | 차트 시각화 | 1.5.3 |
| JUnit | 단위 테스트 | 4.13.2 |

### 아키텍처 및 디자인 패턴
- **MVC Pattern**: Model-View-Controller 아키텍처로 계층 분리
- **DAO Pattern**: 데이터 접근 계층 분리로 유지보수성 향상
- **Singleton Pattern**: 세션 관리 클래스에 적용

### 데이터베이스
- **MySQL 8.0.33**: UTF8MB4 문자셋으로 이모지 지원
- **InnoDB 엔진**: 트랜잭션 및 외래키 제약 지원
- **자동 초기화**: 애플리케이션 첫 실행 시 DB 및 테이블 자동 생성

---

## 🚀 빠른 시작

### 사전 요구사항
```bash
# Java 17 이상 설치 확인
java -version

# Maven 3.6+ 설치 확인
mvn -version

# MySQL 8.0+ 실행 확인
mysql -u root -p
```

### 3단계 빠른 실행

```bash
# 1️⃣ 프로젝트 클론
git clone https://github.com/yourusername/emotion-diary-project.git
cd emotion-diary-project

# 2️⃣ MySQL 비밀번호 설정 (4개 파일)
# 다음 파일들을 열어 DB_PASSWORD 수정:
# - src/main/java/com/diary/emotion/model/DatabaseUtil.java
# - src/main/java/com/diary/emotion/model/UserDAO.java
# - src/main/java/com/diary/emotion/model/StatisticsDAO.java
# - src/main/java/com/diary/emotion/model/DiaryDAO.java

# 3️⃣ 빌드 및 실행
mvn clean compile exec:java
```

### 테스트 데이터 (선택사항)

```bash
# 샘플 사용자 및 일기 데이터 추가
mysql -u root -p < test_data.sql

# 테스트 계정 정보
# 아이디: testuser
# 비밀번호: test123
```

---

## 📥 설치 가이드

### 1. 저장소 클론
```bash
git clone https://github.com/yourusername/emotion-diary-project.git
cd emotion-diary-project
```

### 2. MySQL 데이터베이스 설정

#### 2.1. MySQL 실행 확인
```bash
mysql -u root -p
```

#### 2.2. 비밀번호 설정
다음 **4개 파일**에서 `DB_PASSWORD` 상수를 수정하세요:

**파일 경로:**
1. `src/main/java/com/diary/emotion/model/DatabaseUtil.java`
2. `src/main/java/com/diary/emotion/model/UserDAO.java`
3. `src/main/java/com/diary/emotion/model/StatisticsDAO.java`
4. `src/main/java/com/diary/emotion/model/DiaryDAO.java`

**수정 내용:**
```java
private static final String DB_PASSWORD = "본인의_MySQL_비밀번호";
```

> 💡 **참고**: 데이터베이스 `emotion_diary` 및 테이블은 애플리케이션 첫 실행 시 자동으로 생성됩니다.

### 3. Maven 의존성 설치 및 빌드
```bash
# 의존성 다운로드 및 컴파일
mvn clean compile

# 테스트 실행 (선택사항)
mvn test
```

### 4. 애플리케이션 실행

#### 방법 1: Maven으로 실행
```bash
mvn exec:java
```

#### 방법 2: IDE에서 실행
1. IntelliJ IDEA 또는 Eclipse에서 프로젝트 열기
2. `src/main/java/com/diary/emotion/AppLauncher.java` 파일 찾기
3. `main` 메서드 실행 (▶️ 버튼 클릭)

### 5. 초기 설정
1. 애플리케이션 실행 시 로그인 화면 표시
2. "회원가입" 버튼 클릭하여 새 계정 생성
3. 로그인 후 일기 작성 시작!

---

## 💻 사용 방법

### 1️⃣ 회원가입 및 로그인

1. **회원가입**
   - 애플리케이션 실행 → "회원가입" 버튼 클릭
   - 아이디, 비밀번호, 비밀번호 확인 입력
   - "회원가입" 버튼으로 계정 생성
   
2. **로그인**
   - 아이디와 비밀번호 입력
   - "로그인" 버튼 클릭 또는 Enter 키 입력

### 2️⃣ 일기 작성

1. 상단 **"일기 쓰기"** 탭 선택
2. **제목** 입력 (필수)
3. **내용** 입력 (선택)
4. **감정 선택** (1-4개)
   - 긍정/부정 감정 체크박스 선택
   - 슬라이더로 감정 수치 조절 (0-100)
5. **스트레스 수치** 입력 (0-100)
6. **"저장"** 버튼 클릭

> ⚠️ **제한사항**: 감정은 최대 4개까지 선택 가능

### 3️⃣ 일기 열람 및 관리

1. 상단 **"열람"** 탭 선택
2. **일기 목록**에서 원하는 일기 클릭 선택
3. 작업 선택:
   - **상세보기**: 일기 전체 내용 확인
   - **수정**: 일기 편집 화면으로 이동
   - **삭제**: 확인 후 일기 삭제
4. **검색**: 상단 검색창에 제목 키워드 입력
5. **정렬**: 드롭다운에서 "최신순" 또는 "오래된순" 선택

### 4️⃣ 통계 확인

1. 상단 **"통계"** 탭 선택
2. **조회 기간** 선택
   - 주간: 특정 주의 월요일~일요일
   - 월간: 특정 월의 1일~말일
   - 연간: 특정 년도의 1월~12월
3. **날짜 선택**
   - 년도, 월, 주차 콤보박스 사용
4. **차트 확인**
   - **감정 통계 차트**: 긍정/부정 감정별 평균 수치
   - **스트레스 추이 그래프**: 시간에 따른 변화
   - **평균 스트레스 지수**: 선택 기간의 평균값

---

## 📁 프로젝트 구조

```
emotion-diary-project/
├── src/main/java/com/diary/emotion/
│   ├── AppLauncher.java                 # 애플리케이션 진입점
│   │
│   ├── model/                           # Model 레이어 (데이터)
│   │   ├── DatabaseUtil.java           # DB 초기화 및 연결
│   │   ├── UserDAO.java                # 사용자 데이터 접근
│   │   ├── DiaryDAO.java               # 일기 데이터 접근
│   │   └── StatisticsDAO.java          # 통계 데이터 접근
│   │
│   ├── view/                            # View 레이어 (화면)
│   │   ├── LoginView.java              # 로그인 화면
│   │   ├── SignUpView.java             # 회원가입 화면
│   │   ├── MainApplication.java        # 메인 컨테이너
│   │   ├── WriteDiaryView.java         # 일기 작성 화면
│   │   ├── ViewDiaryListView.java      # 일기 목록 화면
│   │   ├── EditDiaryView.java          # 일기 수정 화면
│   │   └── StatisticsView.java         # 통계 화면
│   │
│   ├── controller/                      # Controller 레이어 (로직)
│   │   ├── LoginController.java        # 로그인 컨트롤러
│   │   ├── SignUpController.java       # 회원가입 컨트롤러
│   │   ├── WriteDiaryController.java   # 일기 작성 컨트롤러
│   │   ├── ViewDiaryController.java    # 일기 목록 컨트롤러
│   │   ├── EditDiaryController.java    # 일기 수정 컨트롤러
│   │   └── StatisticsController.java   # 통계 컨트롤러
│   │
│   └── util/                            # Utility 레이어
│       └── Session.java                # 세션 관리 (Singleton)
│
├── docs/                                # 프로젝트 문서
│   ├── 00_DOCUMENTATION_COMPLETE.md    # 문서화 요약
│   ├── 01_PROJECT_OVERVIEW.md          # 프로젝트 개요
│   ├── 02_API_REFERENCE.md             # API 레퍼런스
│   ├── 03_DEVELOPMENT_GUIDE.md         # 개발 가이드
│   ├── 04_TODO_LIST.md                 # 개발 체크리스트
│   ├── 05_DATABASE_SCHEMA.md           # DB 스키마
│   ├── 06_CURRENT_STATUS_REPORT.md     # 진행 상황
│   ├── PROJECT_COMPLETION_REPORT.md    # 프로젝트 완료 보고서
│   └── start_project.md                # 시작 가이드
│
├── pom.xml                              # Maven 설정
├── test_data.sql                        # 테스트 데이터
└── README.md                            # 프로젝트 README
```

---

## 🗄️ 데이터베이스

### 데이터베이스 개요
- **데이터베이스명**: `emotion_diary`
- **문자셋**: UTF8MB4 (이모지 지원)
- **Collation**: utf8mb4_unicode_ci
- **엔진**: InnoDB (트랜잭션 지원)

### 테이블 구조

#### 1. `user` - 사용자 테이블
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| user_id | VARCHAR(20) | PRIMARY KEY | 사용자 아이디 |
| user_pw | VARCHAR(20) | NOT NULL | 비밀번호 (평문) |

#### 2. `diary` - 일기 테이블
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| entry_id | INTEGER | PRIMARY KEY, AUTO_INCREMENT | 일기 ID |
| user_id | VARCHAR(20) | FOREIGN KEY | 작성자 ID |
| title | VARCHAR(50) | NOT NULL | 일기 제목 |
| content | TEXT | | 일기 내용 |
| stress_level | INTEGER | NOT NULL | 스트레스 수치 (0-100) |
| entry_date | DATETIME | NOT NULL | 작성 일시 |

#### 3. `emotion` - 감정 테이블
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| emotion_id | INTEGER | PRIMARY KEY, AUTO_INCREMENT | 감정 ID |
| entry_id | INTEGER | FOREIGN KEY | 일기 ID |
| emotion_level | INTEGER | NOT NULL | 감정 수치 (0-100) |
| emoji_icon | VARCHAR(10) | | 이모지 아이콘 |

#### 4. `question` - 질문 테이블
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| question_id | INTEGER | PRIMARY KEY, AUTO_INCREMENT | 질문 ID |
| question_text | VARCHAR(255) | NOT NULL | 질문 내용 |

### ERD (Entity Relationship Diagram)
```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│    user     │       │    diary    │       │   emotion   │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ user_id (PK)│───┐   │ entry_id(PK)│───┐   │emotion_id(PK)│
│ user_pw     │   └──→│ user_id (FK)│   └──→│ entry_id(FK)│
└─────────────┘       │ title       │       │emotion_level│
                      │ content     │       │ emoji_icon  │
                      │stress_level │       └─────────────┘
                      │ entry_date  │
                      └─────────────┘
```

> 📖 **상세 스키마**: `docs/05_DATABASE_SCHEMA.md` 참조

---

## 📚 문서

프로젝트의 상세한 문서는 `docs/` 디렉토리에서 확인할 수 있습니다:

| 문서 | 설명 |
|------|------|
| [📋 프로젝트 개요](docs/01_PROJECT_OVERVIEW.md) | 프로젝트 소개, 기능 정의, 감정 정의 |
| [📘 API 레퍼런스](docs/02_API_REFERENCE.md) | 클래스 및 메서드 상세 설명 |
| [📗 개발 가이드](docs/03_DEVELOPMENT_GUIDE.md) | 개발 환경 설정, 코딩 컨벤션, FAQ |
| [✅ TODO 리스트](docs/04_TODO_LIST.md) | 개발 체크리스트 (95% 완료) |
| [🗄️ DB 스키마](docs/05_DATABASE_SCHEMA.md) | 데이터베이스 구조 및 쿼리 예제 |
| [📊 진행 상황](docs/06_CURRENT_STATUS_REPORT.md) | 현재 개발 상태 및 완료 기능 |
| [🎉 완료 보고서](docs/PROJECT_COMPLETION_REPORT.md) | 프로젝트 최종 완료 보고서 |
| [🚀 시작 가이드](docs/start_project.md) | 초보자를 위한 단계별 실행 가이드 |

---

## 📸 스크린샷

### 로그인 화면
> 파스텔 블루 배경의 로그인 및 회원가입 화면

### 일기 작성 화면
> 12가지 감정 선택 및 슬라이더로 수치 입력

### 일기 목록 화면
> 제목 검색, 정렬, 상세보기/수정/삭제 기능

### 통계 화면
> 감정 막대 차트 및 스트레스 꺾은선 그래프

> 💡 **참고**: `docs/screenshot/` 디렉토리에서 더 많은 스크린샷 확인 가능

---

## 🗺️ 로드맵

### ✅ 완료 (v1.0.0)
- [x] 사용자 인증 시스템
- [x] 일기 CRUD 기능
- [x] 감정 및 스트레스 기록
- [x] 통계 시각화 (주간/월간/연간)
- [x] MVC 패턴 적용
- [x] 데이터베이스 트랜잭션 처리

### 🔄 향후 계획 (v2.0.0)

#### 보안
- [ ] 비밀번호 암호화 (BCrypt)
- [ ] SQL Injection 방지 강화
- [ ] 세션 타임아웃 구현

#### 기능 확장
- [ ] 날짜 범위 검색
- [ ] 감정별 필터링
- [ ] 일기 내보내기 (PDF, TXT)
- [ ] 감정 분석 AI 통합
- [ ] 다국어 지원 (영어, 일본어)

#### UI/UX 개선
- [ ] 다크 모드
- [ ] 반응형 윈도우 크기
- [ ] 애니메이션 효과
- [ ] 푸시 알림 (일기 작성 리마인더)

#### 성능 최적화
- [ ] Connection Pool (HikariCP)
- [ ] 페이지네이션 (대용량 데이터)
- [ ] 인덱스 최적화
- [ ] 캐싱 (Redis)

---

## 🤝 기여하기

기여는 언제나 환영합니다! 다음 방법으로 참여해주세요:

### 버그 리포트
1. [Issues](https://github.com/yourusername/emotion-diary-project/issues)에서 기존 이슈 확인
2. 새로운 이슈 생성 시 템플릿 작성
3. 재현 방법 및 환경 정보 제공

### Pull Request
1. 저장소 Fork
2. Feature 브랜치 생성 (`git checkout -b feature/AmazingFeature`)
3. 변경사항 커밋 (`git commit -m 'Add some AmazingFeature'`)
4. 브랜치에 Push (`git push origin feature/AmazingFeature`)
5. Pull Request 생성

### 코딩 컨벤션
- Java 코드 스타일: [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- 커밋 메시지: [Conventional Commits](https://www.conventionalcommits.org/)
- 주석: JavaDoc 형식 사용

> 📖 **상세 가이드**: `docs/03_DEVELOPMENT_GUIDE.md` 참조

---

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

```
MIT License

Copyright (c) 2025 Emotion Diary Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction...
```

---

## 📞 문의

### 프로젝트 관련
- **GitHub Issues**: [이슈 페이지](https://github.com/yourusername/emotion-diary-project/issues)
- **Email**: your.email@example.com
- **웹사이트**: https://your-website.com

### 지원 및 도움말
1. [FAQ 문서](docs/03_DEVELOPMENT_GUIDE.md#faq) 확인
2. [문제 해결 가이드](docs/start_project.md#문제-해결) 참조
3. [GitHub Discussions](https://github.com/yourusername/emotion-diary-project/discussions)에서 질문

---

## 🙏 감사의 말

이 프로젝트는 다음 오픈소스 프로젝트 및 커뮤니티의 도움을 받았습니다:

- [JFreeChart](https://www.jfree.org/jfreechart/) - 차트 시각화 라이브러리
- [MySQL](https://www.mysql.com/) - 데이터베이스 관리 시스템
- [Apache Maven](https://maven.apache.org/) - 빌드 도구
- Java Swing 커뮤니티의 튜토리얼 및 예제

---

## 📊 프로젝트 통계

- **코드 라인 수**: ~3,500줄
- **Java 파일**: 19개
- **문서 파일**: 9개
- **개발 기간**: 2025년 10월 - 11월
- **버전**: 1.0.0
- **완성도**: 95% (핵심 기능 100% 완료)

---

<div align="center">

**Made with ❤️ for mental wellness**

[⬆️ 맨 위로 돌아가기](#감정-일기장-emotion-diary-)

</div>

