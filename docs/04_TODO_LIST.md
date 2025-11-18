# TODO 리스트

## 프로젝트 완료 조건
✅ **이 TODO 리스트의 모든 항목이 완료되면 프로젝트가 완료됩니다.**

---

## 1. 핵심 기능 (High Priority) 🔴

### 1.1 로그인/회원가입 시스템 [진행 중 🔄]
- [x] **UserDAO 클래스 구현** ✅
  - [x] `authenticateUser(userId, password)` - 로그인 검증
  - [x] `registerUser(userId, password)` - 회원가입
  - [x] `userExists(userId)` - 아이디 중복 확인

- [x] **세션 관리 클래스 구현** (Session.java) ✅
  - [x] 현재 로그인 사용자 ID 저장
  - [x] `getCurrentUserId()` - 현재 사용자 ID 반환
  - [x] `setCurrentUserId(userId)` - 로그인 시 사용자 설정
  - [x] `logout()` - 로그아웃
  - [x] `isLoggedIn()` - 로그인 상태 확인

- [x] **LoginView 클래스 구현** (JPanel) ✅
  - [x] 아이디 입력 필드 (JTextField)
  - [x] 비밀번호 입력 필드 (JPasswordField)
  - [x] 로그인 버튼
  - [x] 회원가입 버튼
  - [x] UI 디자인 (파스텔 톤 적용)

- [x] **SignUpView 클래스 구현** (JPanel) ✅
  - [x] 아이디 입력 필드
  - [x] 비밀번호 입력 필드
  - [x] 비밀번호 확인 필드
  - [x] 회원가입 버튼
  - [x] 뒤로가기 버튼

- [x] **LoginController 클래스 구현** ✅
  - [x] 로그인 검증 로직
  - [x] 세션 관리 (현재 로그인 사용자 저장)
  - [x] LoginView ↔ MainApplication 화면 전환

- [x] **SignUpController 클래스 구현** ✅
  - [x] 회원가입 검증 로직
  - [x] 아이디 중복 확인
  - [x] 비밀번호 일치 확인

- [x] **AppLauncher 수정** ✅
  - [x] 최초 실행 시 LoginView 표시
  - [x] 로그인 성공 시 MainApplication으로 전환
  - [x] SignUpView 통합

---

### 1.2 일기 쓰기 기능 ✅ **완료!**
- [x] **WriteDiaryView 클래스 구현** (JPanel) ✅
  - [x] 제목 입력 필드 (JTextField)
  - [x] 내용 입력 영역 (JTextArea + JScrollPane)
  - [x] 감정 선택 UI (12개 이모지 체크박스, 최대 4개)
  - [x] 선택된 감정별 수치 입력 슬라이더 (JSlider, 0-100)
  - [x] 스트레스 수치 입력 슬라이더 (JSlider, 0-100)
  - [x] 저장 버튼
  - [x] 취소 버튼

- [x] **WriteDiaryController 클래스 구현** ✅
  - [x] 입력값 검증 (제목, 감정 개수 등)
  - [x] 감정 4개 제한 로직
  - [x] 저장 버튼 클릭 시 DiaryDAO 호출

- [x] **DiaryDAO 클래스 구현** ✅
  - [x] `saveDiary(userId, title, content, stressLevel, date)` - 일기 저장
  - [x] `saveEmotion(entryId, emotionLevel, emojiIcon)` - 감정 저장
  - [x] `saveDiaryWithEmotions()` - 트랜잭션 처리 완료
  - [x] 트랜잭션 처리 (일기 저장 실패 시 감정도 롤백)

- [x] **MainApplication에 WriteDiaryView 통합** ✅
  - [x] "일기 쓰기" 버튼 클릭 시 WriteDiaryView 표시
  - [x] 임시 writePanel을 실제 WriteDiaryView로 교체

---

### 1.3 일기 열람 기능 ✅ **완료!**
- [x] **ViewDiaryListView 클래스 구현** (JPanel) ✅
  - [x] 검색 패널 (상단)
    - [x] 제목 검색 입력 필드
    - [x] 검색 버튼 및 전체보기 버튼
  - [x] 정렬 옵션 (최신순/오래된순) - JComboBox
  - [x] 일기 목록 테이블 (JTable)
    - 컬럼: 번호, 날짜, 제목, 스트레스, 감정
  - [x] 상세보기 버튼
  - [x] 수정 버튼
  - [x] 삭제 버튼

- [x] **ViewDiaryController 클래스 구현** ✅
  - [x] 일기 목록 로드
  - [x] 제목별 검색 로직
  - [x] 정렬 로직 (최신순/오래된순)
  - [x] 수정 버튼 클릭 시 EditDiaryView로 전환
  - [x] 삭제 확인 및 처리

- [x] **EditDiaryView 클래스 구현** (JPanel) ✅
  - [x] WriteDiaryView와 유사한 UI
  - [x] 기존 데이터를 필드에 로드
  - [x] 수정 완료 버튼
  - [x] 취소 버튼

- [x] **EditDiaryController 클래스 구현** ✅
  - [x] 기존 일기 데이터 로드
  - [x] 수정 완료 시 DiaryDAO 호출
  - [x] 입력값 검증

- [x] **DiaryDAO에 메소드 추가** ✅
  - [x] `getDiariesByUserId(userId)` - 사용자별 일기 목록
  - [x] `searchByTitle(userId, keyword)` - 제목 검색
  - [x] `searchByDate(userId, startDate, endDate)` - 날짜 검색
  - [x] `getDiaryById(entryId)` - 특정 일기 조회
  - [x] `updateDiary(entryId, title, content, stressLevel)` - 일기 수정
  - [x] `deleteDiary(entryId)` - 일기 삭제
  - [x] `getEmotionsByEntryId(entryId)` - 일기별 감정 조회
  - [x] `deleteEmotionsByEntryId(entryId)` - 감정 삭제
  - [x] `updateDiaryWithEmotions(entryId, ...)` - 일기 및 감정 동시 수정

- [x] **MainApplication에 ViewDiaryListView 통합** ✅
  - [x] "열람" 버튼 클릭 시 ViewDiaryListView 표시
  - [x] 임시 viewPanel을 실제 ViewDiaryListView로 교체
  - [x] EditDiaryView를 CardLayout에 추가
  - [x] 수정 완료/취소 시 화면 전환 처리

---

### 1.4 통계 기능 완성 ✅ **완료!**
- [x] **StatisticsDAO 완성** ✅
  - [x] `getEmotionData()` 실제 DB 쿼리 구현
    - [x] 감정별 평균 계산
    - [x] 긍정/부정 분류
  - [x] `getStressData()` 실제 DB 쿼리 구현
    - [x] 주간: 7일치 데이터 (일별)
    - [x] 월간: 해당 월의 모든 날짜 (일별)
    - [x] 연간: 12개월 데이터 (월별)

- [x] **StatisticsView 개선** ✅
  - [x] 빈 데이터 처리 기능 내장
  - [x] 차트 색상 최적화

- [x] **StatisticsController 개선** ✅
  - [x] TEMP_USER_ID를 Session.getCurrentUserId()로 변경
  - [x] 날짜 계산 로직 검증 완료
  - [x] 감정 차트 데이터 로드 활성화

---

## 2. 데이터베이스 (Medium Priority) 🟡

### 2.1 보안 개선
- [ ] **비밀번호 암호화**
  - [ ] SHA-256 또는 BCrypt 라이브러리 추가
  - [ ] UserDAO에 암호화 로직 적용
  - [ ] user 테이블 user_pw 컬럼 타입 변경 (VARCHAR(64))

### 2.2 데이터 검증
- [ ] **제약 조건 추가**
  - [ ] stress_level CHECK 제약 (0~100)
  - [ ] emotion_level CHECK 제약 (0~100)
  - [ ] entry_id당 emotion 레코드 최대 4개 검증 (트리거 또는 애플리케이션 레벨)

### 2.3 인덱스 최적화
- [ ] 성능 테스트 후 필요한 인덱스 추가
- [ ] 복합 인덱스 적용 (user_id + entry_date)

---

## 3. UI/UX 개선 (Low Priority) 🟢

### 3.1 디자인 통일
- [ ] **모든 화면에 파스텔 톤 적용**
  - [ ] LoginView
  - [ ] SignUpView
  - [ ] WriteDiaryView
  - [ ] ViewDiaryListView
  - [ ] EditDiaryView
  - [ ] ✅ StatisticsView (완료)

### 3.2 사용성 개선
- [ ] **입력 검증 메시지**
  - [ ] 빈 필드 경고
  - [ ] 비밀번호 불일치 경고
  - [ ] 감정 4개 초과 경고

- [ ] **확인 대화상자**
  - [ ] 일기 삭제 전 확인 (JOptionPane)
  - [ ] 로그아웃 확인

- [ ] **로딩 인디케이터**
  - [ ] DB 조회 중 로딩 표시

### 3.3 아이콘 및 이미지
- [ ] 로그인 화면에 앱 로고 추가
- [ ] 감정 버튼에 이모지 크게 표시
- [ ] 통계 화면에 감정 이모지 범례 추가

---

## 4. 코드 리팩토링 (Low Priority) 🟢

### 4.1 패키지 구조 정리
- [ ] **share 패키지를 com.diary.emotion으로 이동**
  - [ ] DatabaseUtil.java
  - [ ] MainView.java (또는 삭제)
  - [ ] Main.java (또는 삭제)
  - [ ] StatisticsView.java → com.diary.emotion.view
  - [ ] StatisticsController.java → com.diary.emotion.controller
  - [ ] StatisticsDAO.java → com.diary.emotion.model

### 4.2 코드 정리
- [ ] **중복 코드 제거**
  - [ ] MainApplication과 MainView 통합 결정
  - [ ] DB 연결 정보 중복 제거 (DatabaseUtil로 통일)

- [ ] **주석 정리**
  - [ ] 불필요한 주석 삭제
  - [ ] JavaDoc 형식 통일

### 4.3 에러 처리 개선
- [ ] 모든 SQLException에 대한 사용자 친화적 메시지
- [ ] 로그 시스템 도입 (Log4j 또는 SLF4J)

---

## 5. 테스트 (Low Priority) 🟢

### 5.1 단위 테스트
- [ ] **DAO 테스트**
  - [ ] UserDAO 테스트
  - [ ] DiaryDAO 테스트
  - [ ] StatisticsDAO 테스트

### 5.2 통합 테스트
- [ ] 로그인 → 일기 쓰기 → 열람 → 통계 전체 플로우 테스트

### 5.3 데이터 테스트
- [ ] 대량 데이터 입력 후 성능 테스트
- [ ] 경계값 테스트 (감정 0, 100)

---

## 6. 문서화 (Ongoing) 📝

- [x] **01_PROJECT_OVERVIEW.md** ✅
- [x] **02_API_REFERENCE.md** ✅
- [x] **03_DEVELOPMENT_GUIDE.md** ✅
- [x] **05_DATABASE_SCHEMA.md** ✅
- [x] **04_TODO_LIST.md** ✅ (본 문서)
- [ ] **06_CURRENT_STATUS_REPORT.md** (진행 중)
- [ ] **00_DOCUMENTATION_COMPLETE.md** (모든 문서 완료 후)

---

## 7. 배포 준비 (Future) 🔵

### 7.1 JAR 파일 생성
- [ ] Maven으로 실행 가능한 JAR 패키징
- [ ] 의존성 포함 (Fat JAR)

### 7.2 설치 가이드 작성
- [ ] README.md에 사용자 가이드 추가
- [ ] MySQL 설정 가이드

### 7.3 배포
- [ ] GitHub 릴리즈
- [ ] 버전 태깅 (v1.0.0)

---

## 진행 상황 요약

### 완료된 항목 ✅
- DatabaseUtil (DB 초기화)
- StatisticsView (UI 기본 구조)
- StatisticsController (이벤트 연결)
- StatisticsDAO (평균 스트레스 쿼리)
- MainApplication (메인 화면 뼈대)
- AppLauncher (실행 진입점)

### 진행 중인 항목 🔄
- 문서화 (본 TODO 리스트 작성 중)

### 미착수 항목 ⏳
- 로그인/회원가입 시스템 (최우선)
- 일기 쓰기 기능
- 일기 열람 기능
- 통계 기능 완성 (DAO 쿼리 구현)

---

## 우선순위 기반 다음 단계

### Week 1-2: 로그인 시스템 구축
1. UserDAO 구현
2. LoginView, SignUpView 구현
3. LoginController, SignUpController 구현
4. Session 관리 클래스 구현
5. AppLauncher 수정 (로그인 화면 먼저 표시)

### Week 3-4: 일기 쓰기 구현
1. DiaryDAO 구현 (저장 메소드)
2. WriteDiaryView 구현
3. WriteDiaryController 구현
4. MainApplication 통합

### Week 5-6: 일기 열람 구현
1. DiaryDAO 구현 (조회, 수정, 삭제 메소드)
2. ViewDiaryListView 구현
3. EditDiaryView 구현
4. ViewDiaryController, EditDiaryController 구현
5. MainApplication 통합

### Week 7-8: 통계 완성 및 마무리
1. StatisticsDAO 쿼리 완성
2. UI/UX 개선
3. 버그 수정
4. 테스트
5. 문서 완성

---

## 체크리스트 사용법

1. 작업 시작 전: 이 문서를 열어 다음 할 일 확인
2. 작업 완료 후: `- [ ]`를 `- [x]`로 변경
3. 새로운 작업 발견 시: 해당 섹션에 추가
4. 주간 리뷰: 완료된 항목 비율 확인

---

*이 TODO 리스트의 모든 항목이 완료되면 감정 일기장 프로젝트가 완성됩니다!*

