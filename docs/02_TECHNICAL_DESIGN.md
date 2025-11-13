# 기술 설계 문서

## 1. 아키텍처 설계

### 1.1 전체 아키텍처
```
┌─────────────────────────────────────────────────────────────┐
│                     Presentation Layer                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  AuthView    │  │  WriteView   │  │   ViewPanel  │      │
│  │  (로그인)     │  │  (일기작성)   │  │  (일기열람)   │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│  ┌──────────────┐                                           │
│  │StatisticsView│                                           │
│  │  (통계)       │                                           │
│  └──────────────┘                                           │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                      Business Layer                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │AuthController│  │WriteController│ │ViewController │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│  ┌──────────────┐                                           │
│  │StatsController│                                          │
│  └──────────────┘                                           │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                     Data Access Layer                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   UserDAO    │  │   DiaryDAO   │  │  EmotionDAO  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│  ┌──────────────┐                                           │
│  │StatisticsDAO │                                           │
│  └──────────────┘                                           │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                      Database Layer                         │
│                   MySQL (emotion_diary)                     │
└─────────────────────────────────────────────────────────────┘
```

### 1.2 MVC 패턴 적용

#### Model (데이터)
- **UserModel**: 사용자 정보
- **DiaryModel**: 일기 정보
- **EmotionModel**: 감정 정보

#### View (화면)
- **AuthView**: 로그인/회원가입 화면
- **WriteView**: 일기 작성 화면
- **ViewPanel**: 일기 열람 화면
- **StatisticsView**: 통계 화면

#### Controller (제어)
- **AuthController**: 인증 로직
- **WriteController**: 작성 로직
- **ViewController**: 열람 로직
- **StatisticsController**: 통계 로직

## 2. 클래스 설계

### 2.1 공통 클래스

#### SessionManager (싱글톤)
```java
public class SessionManager {
    private static SessionManager instance;
    private String currentUserId;
    private LocalDateTime loginTime;
    
    public static SessionManager getInstance();
    public void login(String userId);
    public void logout();
    public String getCurrentUserId();
    public boolean isLoggedIn();
}
```

#### Constants
```java
public class Constants {
    // 색상
    public static final Color PASTEL_BLUE = new Color(230, 240, 255);
    public static final Color PASTEL_YELLOW = new Color(255, 255, 220);
    
    // 폰트
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    
    // 크기
    public static final Dimension MAIN_WINDOW_SIZE = new Dimension(550, 750);
    
    // 감정 리스트
    public static final String[] EMOTIONS = {
        "😊 기쁨", "😢 슬픔", "😠 분노", "😰 불안",
        "😌 평온", "😔 우울", "😖 좌절", "💖 사랑"
    };
}
```

#### DatabaseUtil
```java
public class DatabaseUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "U9Bsi7sj1*";
    
    public static Connection getConnection() throws SQLException;
    public static boolean createDatabase();
    public static void closeResources(Connection, PreparedStatement, ResultSet);
}
```

### 2.2 Model 클래스

#### UserModel
```java
public class UserModel {
    private String userId;
    private String userPw;
    
    // Constructor, Getters, Setters
}
```

#### DiaryModel
```java
public class DiaryModel {
    private int entryId;
    private String userId;
    private String title;
    private String content;
    private int stressLevel;
    private LocalDateTime entryDate;
    private List<EmotionModel> emotions;
    
    // Constructor, Getters, Setters
}
```

#### EmotionModel
```java
public class EmotionModel {
    private int emotionId;
    private int entryId;
    private String emojiIcon;
    private int emotionLevel;
    
    // Constructor, Getters, Setters
}
```

### 2.3 인증 모듈 (auth 패키지)

#### AuthView
```java
public class AuthView extends JPanel {
    private JTextField userIdField;
    private JPasswordField userPwField;
    private JButton loginButton;
    private JButton signupButton;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    public AuthView();
    private void initUI();
    private JPanel createLoginPanel();
    private JPanel createSignupPanel();
}
```

#### AuthController
```java
public class AuthController {
    private AuthView view;
    private UserDAO dao;
    
    public AuthController(AuthView view, UserDAO dao);
    public boolean login(String userId, String password);
    public boolean signup(String userId, String password);
    private boolean validateInput(String userId, String password);
    private void showMessage(String message);
}
```

#### UserDAO
```java
public class UserDAO {
    public boolean createUser(String userId, String password);
    public boolean authenticateUser(String userId, String password);
    public boolean userExists(String userId);
    public boolean deleteUser(String userId);
}
```

### 2.4 일기 작성 모듈 (write 패키지)

#### WriteView
```java
public class WriteView extends JPanel {
    private JTextField titleField;
    private JTextArea contentArea;
    private JSlider stressSlider;
    private JLabel stressValueLabel;
    private EmotionSelectorPanel emotionPanel;
    private JButton saveButton;
    private JButton clearButton;
    
    public WriteView();
    private void initUI();
    public DiaryModel getDiaryData();
    public void clearForm();
}
```

#### EmotionSelectorPanel
```java
public class EmotionSelectorPanel extends JPanel {
    private List<EmotionInputPanel> emotionInputs;
    private JButton addEmotionButton;
    private static final int MAX_EMOTIONS = 4;
    
    public EmotionSelectorPanel();
    private void addEmotionInput();
    private void removeEmotionInput(EmotionInputPanel panel);
    public List<EmotionModel> getEmotions();
    public void clear();
}
```

#### EmotionInputPanel
```java
public class EmotionInputPanel extends JPanel {
    private JComboBox<String> emotionCombo;
    private JSlider levelSlider;
    private JLabel levelValueLabel;
    private JButton removeButton;
    
    public EmotionInputPanel(ActionListener removeListener);
    public String getSelectedEmotion();
    public int getEmotionLevel();
}
```

#### WriteController
```java
public class WriteController {
    private WriteView view;
    private DiaryDAO diaryDao;
    private EmotionDAO emotionDao;
    
    public WriteController(WriteView view, DiaryDAO dao, EmotionDAO emotionDao);
    public void saveDiary();
    private boolean validateDiary(DiaryModel diary);
    private void showSuccessMessage();
    private void showErrorMessage(String message);
}
```

#### DiaryDAO
```java
public class DiaryDAO {
    public int createDiary(DiaryModel diary);
    public boolean updateDiary(DiaryModel diary);
    public boolean deleteDiary(int entryId);
    public DiaryModel getDiaryById(int entryId);
    public List<DiaryModel> getDiariesByUser(String userId);
    public List<DiaryModel> getDiariesByDateRange(String userId, LocalDate start, LocalDate end);
}
```

#### EmotionDAO
```java
public class EmotionDAO {
    public boolean createEmotions(int entryId, List<EmotionModel> emotions);
    public List<EmotionModel> getEmotionsByEntryId(int entryId);
    public boolean deleteEmotionsByEntryId(int entryId);
}
```

### 2.5 일기 열람 모듈 (view 패키지)

#### ViewPanel
```java
public class ViewPanel extends JPanel {
    private JPanel filterPanel;
    private JTextField searchField;
    private JComboBox<String> sortCombo;
    private JButton searchButton;
    private DiaryListPanel diaryListPanel;
    private DiaryDetailPanel detailPanel;
    private CardLayout cardLayout;
    
    public ViewPanel();
    private void initUI();
    private JPanel createFilterPanel();
    public void refreshDiaryList();
}
```

#### DiaryListPanel
```java
public class DiaryListPanel extends JPanel {
    private JList<DiaryModel> diaryList;
    private DefaultListModel<DiaryModel> listModel;
    private JScrollPane scrollPane;
    
    public DiaryListPanel();
    public void setDiaries(List<DiaryModel> diaries);
    public void addSelectionListener(ListSelectionListener listener);
    public DiaryModel getSelectedDiary();
}
```

#### DiaryDetailPanel
```java
public class DiaryDetailPanel extends JPanel {
    private JLabel titleLabel;
    private JTextArea contentArea;
    private JLabel stressLabel;
    private EmotionDisplayPanel emotionPanel;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;
    
    public DiaryDetailPanel();
    public void setDiary(DiaryModel diary);
    private void initUI();
}
```

#### ViewController
```java
public class ViewController {
    private ViewPanel view;
    private DiaryDAO diaryDao;
    private EmotionDAO emotionDao;
    
    public ViewController(ViewPanel view, DiaryDAO dao, EmotionDAO emotionDao);
    public void loadDiaries();
    public void searchDiaries(String keyword);
    public void sortDiaries(String sortOrder);
    public void filterByDate(LocalDate startDate, LocalDate endDate);
    public void deleteDiary(int entryId);
}
```

### 2.6 통계 모듈 (statistics 패키지)

현재 구현된 StatisticsView, StatisticsController, StatisticsDAO를 개선하여 완성합니다.

## 3. 데이터베이스 쿼리 설계

### 3.1 UserDAO 쿼리

```sql
-- 회원가입
INSERT INTO user (user_id, user_pw) VALUES (?, ?);

-- 로그인 인증
SELECT user_id FROM user WHERE user_id = ? AND user_pw = ?;

-- 사용자 존재 확인
SELECT COUNT(*) FROM user WHERE user_id = ?;
```

### 3.2 DiaryDAO 쿼리

```sql
-- 일기 생성
INSERT INTO diary (user_id, title, content, stress_level, entry_date)
VALUES (?, ?, ?, ?, ?);

-- 일기 조회 (전체)
SELECT * FROM diary WHERE user_id = ? ORDER BY entry_date DESC;

-- 일기 조회 (날짜 범위)
SELECT * FROM diary 
WHERE user_id = ? AND entry_date BETWEEN ? AND ?
ORDER BY entry_date DESC;

-- 일기 조회 (제목 검색)
SELECT * FROM diary 
WHERE user_id = ? AND title LIKE ?
ORDER BY entry_date DESC;

-- 일기 수정
UPDATE diary 
SET title = ?, content = ?, stress_level = ?
WHERE entry_id = ? AND user_id = ?;

-- 일기 삭제
DELETE FROM diary WHERE entry_id = ? AND user_id = ?;
```

### 3.3 EmotionDAO 쿼리

```sql
-- 감정 생성 (일기당 최대 4개)
INSERT INTO emotion (entry_id, emoji_icon, emotion_level)
VALUES (?, ?, ?);

-- 감정 조회
SELECT * FROM emotion WHERE entry_id = ?;

-- 감정 삭제
DELETE FROM emotion WHERE entry_id = ?;
```

### 3.4 StatisticsDAO 쿼리

```sql
-- 평균 스트레스 (기간)
SELECT AVG(stress_level) AS avgStress 
FROM diary 
WHERE user_id = ? AND entry_date BETWEEN ? AND ?;

-- 감정 데이터 (기간)
SELECT e.emoji_icon, e.emotion_level, d.entry_date
FROM emotion e
JOIN diary d ON e.entry_id = d.entry_id
WHERE d.user_id = ? AND d.entry_date BETWEEN ? AND ?
ORDER BY d.entry_date;

-- 스트레스 데이터 (기간)
SELECT entry_date, stress_level
FROM diary
WHERE user_id = ? AND entry_date BETWEEN ? AND ?
ORDER BY entry_date;
```

## 4. UI/UX 상세 설계

### 4.1 화면 전환 흐름

```
[시작]
  ↓
[로그인 화면]
  ├─ 로그인 성공 → [메인 화면]
  └─ 회원가입 → [회원가입 화면] → [로그인 화면]
  
[메인 화면] (CardLayout)
  ├─ [일기 쓰기 탭]
  ├─ [열람 탭]
  │   └─ 일기 선택 → [상세보기]
  │       ├─ 수정 → [수정 화면]
  │       └─ 삭제 → 확인 다이얼로그
  └─ [통계 탭]
      └─ 주간/월간/연간 선택
```

### 4.2 컴포넌트 배치

#### 로그인 화면
```
┌──────────────────────────────────────┐
│          Emotion Diary 😊             │
│                                       │
│   아이디: [________________]          │
│   비밀번호: [________________]        │
│                                       │
│     [로그인]      [회원가입]          │
└──────────────────────────────────────┘
```

#### 일기 작성 화면
```
┌──────────────────────────────────────┐
│ [일기쓰기] [열람] [통계]              │
├──────────────────────────────────────┤
│ 제목: [_________________________]    │
│                                       │
│ 내용:                                 │
│ ┌─────────────────────────────────┐  │
│ │                                 │  │
│ │                                 │  │
│ └─────────────────────────────────┘  │
│                                       │
│ 스트레스: [==========] 50            │
│                                       │
│ 감정 선택:                            │
│ [😊 기쁨 ▼] [=======] 70  [X]        │
│ [+ 감정 추가] (최대 4개)              │
│                                       │
│          [저장]     [초기화]          │
└──────────────────────────────────────┘
```

#### 일기 열람 화면
```
┌──────────────────────────────────────┐
│ [일기쓰기] [열람] [통계]              │
├──────────────────────────────────────┤
│ 검색: [________] [검색] 정렬:[날짜▼] │
├──────────────────────────────────────┤
│ ┌────────────────┬─────────────────┐ │
│ │ 목록           │ 상세보기        │ │
│ │ □ 2024-11-13  │                 │ │
│ │   행복한 하루  │                 │ │
│ │                │                 │ │
│ │ □ 2024-11-12  │                 │ │
│ │   조금 우울해  │                 │ │
│ └────────────────┴─────────────────┘ │
└──────────────────────────────────────┘
```

#### 통계 화면
```
┌──────────────────────────────────────┐
│ [일기쓰기] [열람] [통계]              │
├──────────────────────────────────────┤
│ [주간▼] [2024년▼] [11월▼] [2주▼]    │
│                                       │
│ 평균 스트레스: 55.5                   │
│                                       │
│ ┌─ 감정 지수 ─────────────────────┐  │
│ │     ▂▃▅▆                        │  │
│ │                                 │  │
│ └─────────────────────────────────┘  │
│                                       │
│ ┌─ 스트레스 추이 ──────────────────┐ │
│ │    ╱╲  ╱                        │  │
│ │   ╱  ╲╱                         │  │
│ └─────────────────────────────────┘  │
└──────────────────────────────────────┘
```

### 4.3 다이얼로그 설계

#### 확인 다이얼로그
- 일기 삭제 확인
- 로그아웃 확인
- 데이터 손실 경고

#### 알림 다이얼로그
- 저장 완료
- 삭제 완료
- 오류 메시지

## 5. 에러 처리

### 5.1 입력 검증
- 빈 필드 체크
- 아이디 중복 체크
- 비밀번호 강도 체크
- 제목/내용 길이 제한

### 5.2 데이터베이스 오류
- 연결 실패 처리
- 쿼리 실패 처리
- 트랜잭션 롤백

### 5.3 사용자 피드백
- 명확한 오류 메시지
- 해결 방법 안내
- 로그 기록

## 6. 보안 고려사항

### 6.1 비밀번호 보안
- **TODO**: 추후 비밀번호 해싱 (SHA-256, BCrypt) 적용
- 현재는 평문 저장 (개발 단계)

### 6.2 SQL Injection 방지
- PreparedStatement 사용
- 모든 쿼리 파라미터화

### 6.3 세션 관리
- 로그아웃 시 세션 정리
- 비정상 종료 시 세션 해제
# 감정 일기장 프로젝트 개요

## 1. 프로젝트 소개

### 1.1 프로젝트 명
**Emotion Diary (감정 일기장)**

### 1.2 목적
우울증 등으로 감정적으로 힘들어하는 사람들에게 작은 위로를 전하고자, 감정을 기록하고 피드백을 받을 수 있는 일기장 프로그램을 개발한다.

### 1.3 핵심 가치
- **감정 인식**: 자신의 감정을 명확히 인식하고 기록
- **자기 성찰**: 감정 패턴을 시각화하여 자기 이해 증진
- **정서적 지원**: 감정 기록을 통한 심리적 안정감 제공

## 2. 기술 스택

### 2.1 개발 환경
- **언어**: Java 17
- **빌드 도구**: Maven
- **IDE**: IntelliJ IDEA / Eclipse

### 2.2 프레임워크 & 라이브러리
- **GUI Framework**: Java Swing
- **Database**: MySQL 8.0.33
- **JDBC**: MySQL Connector/J 8.0.33
- **차트 라이브러리**: JFreeChart 1.5.3
- **테스트**: JUnit 4.13.2

### 2.3 디자인 패턴
- **MVC Pattern**: Model-View-Controller 아키텍처
- **DAO Pattern**: 데이터베이스 접근 계층 분리
- **Singleton Pattern**: 데이터베이스 연결 관리

## 3. 주요 기능

### 3.1 사용자 인증
- 회원가입
- 로그인
- 세션 관리

### 3.2 일기 작성
- 제목 및 내용 입력
- 감정 선택 (최대 4개)
- 각 감정별 수치 입력 (0-100)
- 스트레스 수치 입력 (0-100)
- 작성일시 자동 기록

### 3.3 일기 열람 및 관리
- 날짜별 필터링
- 제목 검색
- 오름차순/내림차순 정렬
- 일기 수정
- 일기 삭제

### 3.4 통계 및 시각화
- 감정 수치 그래프 (막대 차트)
- 스트레스 수치 그래프 (꺾은선 차트)
- 평균 스트레스 수치 표시
- 주간/월간/연간 조회

## 4. UX/UI 디자인 원칙

### 4.1 색상 테마
- **파스텔 블루** (RGB: 230, 240, 255): 메인 배경색 - 차분하고 안정적인 느낌
- **파스텔 옐로우** (RGB: 255, 255, 220): 네비게이션 바 - 따뜻하고 친근한 느낌
- **부드러운 색조**: 감정적 안정감 제공

### 4.2 레이아웃 원칙
- **직관적 네비게이션**: 상단 메뉴바로 모든 기능 접근
- **CardLayout 활용**: 탭 전환으로 깔끔한 화면 구성
- **적절한 여백**: 시각적 편안함 제공
- **일관된 디자인**: 모든 화면에서 동일한 디자인 언어 사용

### 4.3 사용성 원칙
- **최소 클릭**: 주요 기능에 2-3클릭 이내 접근
- **명확한 피드백**: 사용자 액션에 즉각적인 응답
- **오류 방지**: 입력 검증 및 확인 다이얼로그
- **접근성**: 큰 폰트, 명확한 레이블

## 5. 데이터베이스 설계

### 5.1 데이터베이스명
`emotion_diary`

### 5.2 테이블 구조

#### user (사용자)
```sql
CREATE TABLE user (
    user_id VARCHAR(20) PRIMARY KEY,
    user_pw VARCHAR(20) NOT NULL
)
```

#### diary (일기)
```sql
CREATE TABLE diary (
    entry_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL,
    title VARCHAR(50) NOT NULL,
    content TEXT,
    stress_level INTEGER NOT NULL,
    entry_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
)
```

#### emotion (감정)
```sql
CREATE TABLE emotion (
    emotion_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    entry_id INTEGER NOT NULL,
    emotion_level INTEGER NOT NULL,
    emoji_icon VARCHAR(10) NOT NULL,
    FOREIGN KEY (entry_id) REFERENCES diary(entry_id)
)
```

#### question (질문) - 향후 확장용
```sql
CREATE TABLE question (
    question_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    question_text VARCHAR(100) NOT NULL
)
```

### 5.3 데이터 관계
- user (1) --- (N) diary: 한 사용자는 여러 일기를 작성
- diary (1) --- (N) emotion: 한 일기는 최대 4개의 감정 포함

## 6. 프로젝트 구조

```
emotion-diary/
├── src/
│   └── main/
│       └── java/
│           ├── com.diary.emotion/      # 메인 애플리케이션
│           │   ├── AppLauncher.java
│           │   ├── MainApplication.java
│           │   ├── statistics/
│           │   │   ├── StatisticsView.java
│           │   │   ├── StatisticsController.java
│           │   │   └── StatisticsDAO.java
│           │   ├── auth/               # 로그인/회원가입
│           │   ├── write/              # 일기 작성
│           │   └── view/               # 일기 열람
│           └── share/                  # 공통 유틸리티
│               ├── DatabaseUtil.java
│               ├── SessionManager.java
│               └── Constants.java
├── docs/                               # 프로젝트 문서
├── pom.xml
└── README.md
```

## 7. 개발 일정 (예상)

### Phase 1: 기반 구축 (2주)
- 데이터베이스 설계 및 구축 ✅
- 프로젝트 구조 설정 ✅
- 공통 유틸리티 개발

### Phase 2: 핵심 기능 개발 (4주)
- 로그인/회원가입 (1주)
- 일기 작성 (1.5주)
- 일기 열람 및 관리 (1.5주)

### Phase 3: 통계 기능 (2주)
- 통계 View 완성 ✅
- 통계 DAO 완전 구현
- 차트 데이터 연동

### Phase 4: 테스트 및 개선 (1주)
- 통합 테스트
- UX 개선
- 버그 수정

## 8. 현재 진행 상황

### 완료된 작업
- ✅ 데이터베이스 스키마 설계 및 구축
- ✅ 메인 애플리케이션 프레임워크 구축
- ✅ 통계 화면 UI (StatisticsView)
- ✅ MVC 패턴 적용
- ✅ 평균 스트레스 DB 연동 (1단계)

### 진행 중인 작업
- 🔄 통계 차트 DB 연동 완성
- 🔄 일기 작성 화면 설계

### 예정된 작업
- ⏳ 로그인/회원가입 기능
- ⏳ 일기 작성 기능
- ⏳ 일기 열람 및 관리 기능
- ⏳ 통합 테스트

