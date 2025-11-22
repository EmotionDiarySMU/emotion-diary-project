# 감정 일기장 프로젝트 요구사항

## 프로젝트 개요
- **프로젝트명**: 감정 일기장 (Emotion Diary)
- **개발 기간**: 2025년 11월
- **개발 언어**: Java 21
- **GUI 프레임워크**: Swing
- **빌드 도구**: Maven
- **데이터베이스**: MySQL

## 기능 요구사항

### 1. 로그인 및 회원가입 (`login` 브랜치)
- 사용자 인증 시스템
- 회원가입 기능
- 로그인 UI
- 사용자 데이터 관리

### 2. 일기 작성 (`write` 브랜치)
- 일기 작성 UI
- 감정 아이콘 선택
- 데이터베이스 저장
- 입력 유효성 검사

### 3. 일기 열람/수정 (`View` 브랜치)
- 일기 조회 기능
- 일기 수정/삭제
- 날짜별 검색
- 일기 목록 표시

### 4. 통계 및 차트 (`stats` 브랜치)
- 감정 통계 시각화
- JFreeChart를 이용한 차트 생성
- 주간/월간/연간 통계
- 감정 패턴 분석

## 기술 스택

### 백엔드
- Java 21
- JDBC (MySQL 연결)
- Maven (의존성 관리)

### 프론트엔드
- Java Swing
- JFreeChart (통계 차트)

### 데이터베이스
- MySQL
- 테이블: users, diary_entries, emotions

## 개발 환경
- **IDE**: Eclipse, IntelliJ IDEA
- **JDK**: 21
- **Maven**: 3.x
- **MySQL**: 8.x

## 협업 도구
- **버전 관리**: Git, GitHub
- **협업 방식**: Pull Request (PR)
- **코드 리뷰**: GitHub PR Review
- **브랜치 전략**: Feature Branch Workflow

## 브랜치 구조
- `master` / `main`: 안정 버전 (배포용)
- `login`: 로그인/회원가입 기능
- `write`: 일기 작성 기능
- `View`: 일기 열람/수정 기능
- `stats`: 통계/차트 기능

## 프로젝트 구조
```
src/main/java/com/diary/emotion/
├── login/          # 로그인 기능
├── write/          # 일기 작성 기능
├── view/           # 일기 열람 기능
├── stats/          # 통계 기능
├── db/             # 데이터베이스 관리
└── share/          # 공통 유틸리티
```

## 데이터베이스 스키마

### users 테이블
- id (PK)
- username
- password
- created_at

### diary_entries 테이블
- id (PK)
- user_id (FK)
- title
- content
- emotion_icon
- stress_level
- created_at
- updated_at

### emotions 테이블
- id (PK)
- name
- icon_path

## 개발 가이드
- **시작하기**: START_HERE.md 참고
- **Eclipse 사용자**: Eclipse 기준으로 모든 가이드 작성됨
- **자동화**: create-pr.sh, update-from-master.sh 스크립트 제공

## 팀 구성
- 로그인 담당: `login` 브랜치
- 일기 작성 담당: `write` 브랜치
- 일기 열람 담당: `View` 브랜치
- 통계 담당: `stats` 브랜치

