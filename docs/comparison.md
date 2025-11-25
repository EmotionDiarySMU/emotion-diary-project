# backend-code vs src 경로 기능적 차이점 분석

## 📌 주요 기능적 차이점 요약

### 1. **DatabaseManager.java**

#### backend-code 버전:
- `checkLogin()`: **인스턴스 메서드** (non-static)
- `registerUser()`: 반환 타입 **int** (성공=1, 중복=0, 에러=-1)
- `getEntries()`: SELECT 쿼리에서 `SELECT *` 사용

#### src 버전:
- `checkLogin()`: **static 메서드**
- `registerUser()`: 반환 타입 **boolean** (성공=true, 실패=false)
- `searchEntries()`: 메서드명이 다르며, SELECT 쿼리에서 명시적으로 컬럼명 나열
- `updateEntry(DiaryEntry entry)`: 오버로드 메서드 추가 (DiaryEntry 객체를 받음)

**기능 차이:**
- static/non-static 차이로 인해 호출 방식이 다름
- registerUser 반환 타입이 달라 에러 처리 방식 다름
- src는 더 상세한 에러 처리 및 메서드 오버로딩 제공

---

### 2. **WriteDiaryGUI.java**

#### backend-code 버전:
- `emotionValues` 배열을 사용하여 감정 수치 저장
- `validateAndSaveEmotionValue()`: 감정 수치 검증 메서드 존재
- `validateAndSaveStressValue()`: 스트레스 수치 검증 메서드 존재
- `checkAndClear()`: 저장 여부 확인 후 초기화
- `SingleIconChooserDialog`를 필드로 가지고 있음 (미리 초기화)
- `saveButton` 클릭 시 직접 DB 저장 로직 구현

#### src 버전:
- `EmotionSlotPanel` 객체 배열 사용 (캡슐화된 감정 슬롯)
- `emotionValues` 배열 없음 - EmotionSlotPanel 내부에서 관리
- `validateAndSaveEmotionValue()`, `validateAndSaveStressValue()` 메서드 없음
- `saveOrFinish()`: 질문 답변 여부 확인 다이얼로그 추가
- `saveEntry()`: 저장 로직이 분리되어 있음
- `isAnswerToQuestion` 플래그를 DiaryEntry에 설정

**기능 차이:**
- **backend-code**: 직접적인 값 관리 및 검증
- **src**: EmotionSlotPanel로 캡슐화, 질문 답변 기능 추가

---

### 3. **ViewDiaryPanel.java**

#### backend-code 버전:
- `WriteDiaryGUI`를 **상속**받아 구현
- 컴포넌트들을 직접 수정 불가로 설정 (setEditable(false))
- 마우스 리스너를 제거하여 아이콘 선택 불가하게 함
- 배경색을 darkseaGreen으로 변경
- southPanel에서 버튼 제거 후 수정/삭제 버튼 추가

#### src 버전:
- **독립적인 JPanel**로 구현 (상속 안 함)
- EmotionSlotPanel에 `isEditable=false` 파라미터 전달
- `fillEntry()` 메서드에서 `EmotionPanelStyler.applyViewEmotionAreaStyle()` 호출
- 날짜 라벨 추가 (작성일 표시)
- `currentEntry` 필드로 현재 조회 중인 일기 저장

**기능 차이:**
- **backend-code**: 상속 기반, 기존 컴포넌트 수정
- **src**: 독립적 구현, 스타일 시스템 적용, 날짜 표시 기능

---

### 4. **ModifyPanel.java**

#### backend-code 버전:
- `WriteDiaryGUI`를 **상속**받아 구현
- questionLabel만 숨김
- 버튼만 교체 (취소, 수정완료)

#### src 버전:
- **독립적인 JPanel**로 구현 (상속 안 함)
- `getUpdatedEntry()`: 수정된 DiaryEntry 객체 반환 메서드 존재
- `fillEntry()` 메서드에서 `EmotionPanelStyler.applyModifyEmotionAreaStyle()` 호출
- 날짜 라벨 추가

**기능 차이:**
- **backend-code**: 간단한 상속 구조
- **src**: 독립적 구현, DiaryEntry 객체 기반 업데이트

---

### 5. **ExtraWindow.java**

#### backend-code 버전:
- 수정 완료 시 직접 감정 값 수집 (`emotionValues` 배열 사용)
- `validateAndSaveEmotionValue()`, `validateAndSaveStressValue()` 직접 호출
- DB 업데이트 후 entry 객체 수동으로 갱신
- `updateTitleBar()` 메서드로 창 제목 업데이트

#### src 버전:
- `modifyPanel.getUpdatedEntry()`로 업데이트된 DiaryEntry 객체 획득
- `DatabaseManager.updateEntry(DiaryEntry)` 오버로드 메서드 사용
- 검증 로직이 ModifyPanel 내부로 캡슐화됨
- `positionWindowNextToMain()`: 메인 창 옆에 자동 배치 기능

**기능 차이:**
- **backend-code**: 직접적인 값 수집 및 검증
- **src**: 객체 지향적 접근, 자동 창 배치 기능

---

### 6. **SearchDiaryPanel.java**

#### backend-code 버전:
- `JList<String>` 사용
- `DefaultListModel` 사용
- `refreshDiaryModel()`: 검색 시마다 호출
- 날짜 미선택 시 `null` 반환 (조건부 쿼리)
- 정렬 버튼: entry_id 기준 정렬

#### src 버전:
- `JTable` 사용
- `DefaultTableModel` 사용
- `refreshList()` 메서드 추가
- 날짜 미선택 시 기본값 설정 (1900-01-01 ~ 2099-12-31)
- 정렬 버튼: entry_date 기준 정렬
- **최대 50개 항목만 표시** (성능 최적화)
- 빈 행 추가하여 최소 50줄 유지 (UI 일관성)

**기능 차이:**
- **backend-code**: JList 기반, 단순 구현
- **src**: JTable 기반, UI 개선 및 성능 최적화

---

### 7. **AuthenticationFrame.java**

#### backend-code 버전:
- LoginPanel, SignUpPanel이 **내부 클래스**로 구현
- `DatabaseManager`를 **인스턴스로 생성**하여 사용
- `registerUser()` 반환값이 int이므로 조건 분기 필요

#### src 버전:
- LoginPanel, SignUpPanel이 **별도 클래스 파일**로 분리
- `DatabaseManager`를 **static 메서드**로 호출
- `registerUser()` 반환값이 boolean이므로 간단한 조건 처리
- `showSuccess(String id)`: 회원가입 성공 메시지 전달 메서드 추가

**기능 차이:**
- **backend-code**: 모든 패널이 한 파일에 집중
- **src**: 관심사 분리, 모듈화된 구조

---

### 8. **SingleIconChooserDialog.java**

#### backend-code 버전:
- 생성자: `(Component parent, JLabel[] iconLabels, Color bgColor)`
- `allIconLabels` 배열로 중복 체크
- `currentIconInSlot`으로 현재 슬롯 아이콘 저장

#### src 버전:
- 생성자: `(JFrame parent, String currentSelection, List<String> usedIcons)`
- `usedIcons` 리스트를 **생성자에서 직접 받음** (중복 체크 간소화)
- 더 세련된 UI 스타일 (둥근 모서리 버튼)

**기능 차이:**
- **backend-code**: 모든 라벨 배열을 받아 내부에서 중복 체크
- **src**: 사용 중인 아이콘 목록만 받아 효율적으로 처리

---

### 9. **SaveQuestion.java**

#### backend-code 버전:
- 파라미터: `(JFrame frame, WriteDiaryGUI panel, int exitProgram)`
- WriteDiaryGUI만 지���

#### src 버전:
- **오버로드된 메서드**: WriteDiaryGUI와 ModifyPanel 둘 다 지원
- ModifyPanel용 별도 메서드 존재

**기능 차이:**
- **backend-code**: WriteDiaryGUI만 처리
- **src**: 다형성 활용, 여러 패널 타입 지원

---

### 10. **MainView.java**

#### backend-code 버전:
- 간단한 CardLayout 전환
- 탭 전환 시 저장 확인 로직 없음
- 메뉴바에 버튼 추가

#### src 버전:
- 탭 전환 시 `isModified` 체크하여 **자동 저장 확인**
- "No" 선택 시 **작성 중 내용 초기화** 기능
- 커스텀 탭 UI (활성 탭 하이라이트, 호버 효과)
- `StatisticsController` 통합

**기능 차이:**
- **backend-code**: 기본적인 화면 전환만
- **src**: 데이터 보호 로직, 향상된 UX

---

## 🎯 주요 기능 로직 차이점 정리

### A. **데이터 관리 방식**
- **backend-code**: 직접적인 값 관리 (배열, 직접 검증)
- **src**: 캡슐화된 컴포넌트 (EmotionSlotPanel), 객체 지향적 설계

### B. **에러 처리**
- **backend-code**: 단순 boolean 또는 int 반환
- **src**: 상세한 에러 메시지, 예외 처리 강화

### C. **UI/UX 개선**
- **backend-code**: 기본 Swing 컴포넌트 사용
- **src**: 커스텀 UI 컴포넌트, 테마 시스템, 스타일 적용

### D. **구조적 설계**
- **backend-code**: 상속 기반 (ViewDiaryPanel, ModifyPanel이 WriteDiaryGUI 상속)
- **src**: 컴포지션 기반 (각 패널이 독립적)

### E. **추가 기능 (src만 있는 기능)**
1. 질문 답변 기능 (isAnswerToQuestion)
2. 통계 화면 통합 (StatisticsController)
3. 날짜 라벨 표시
4. 탭 전환 시 자동 저장 확인
5. 테이블 기반 일기 목록 (JList → JTable)
6. 최대 50개 항목 제한 (성능 최적화)
7. 창 자동 배치 기능
8. 커스텀 UI 테마 시스템

---

## ⚠️ 주의사항

**디자인을 건드리지 않고 backend-code의 기능을 src에 적용하려면:**

1. **DatabaseManager**: src의 static 메서드들을 유지하면서 backend-code의 로직만 보완
2. **WriteDiaryGUI**: EmotionSlotPanel 구조를 유지하면서 검증 로직만 추가
3. **ViewDiaryPanel/ModifyPanel**: 독립적 구조를 유지하면서 필요한 메서드만 추가
4. **ExtraWindow**: 기존 구조 유지하면서 업데이트 로직만 개선

**핵심은:** src의 UI 구조와 스타일은 그대로 두고, backend-code에 있는 **검증 로직**, **에러 처리**, **데이터 관리 로직**만 이식하는 것입니다.
