# 감정 일기 프로젝트 코드 설명서

**작성일**: 2025년 11월 22일  
**대상**: 프로그래밍을 배우는 중학생

---

## 📚 목차

1. [Main.java](#1-mainjava) - 프로그램 시작점
2. [DatabaseManager.java](#2-databasemanagerjava) - 데이터베이스 관리
3. [DiaryEntry.java](#3-diaryentryjava) - 일기 데이터 저장
4. [Emotion.java](#4-emotionjava) - 감정 데이터 저장
5. [MainView.java](#5-mainviewjava) - 메인 화면
6. [SaveQuestion.java](#6-savequestionjava) - 저장 확인 창
7. [StatisticsView.java](#7-statisticsviewjava) - 통계 화면
8. [StatisticsController.java](#8-statisticscontrollerjava) - 통계 로직 처리
9. [StatisticsDAO.java](#9-statisticsdaojava) - 통계 데이터베이스 접근

---

## 1. Main.java

### 역할
프로그램이 처음 시작되는 파일입니다. 컴퓨터를 켜면 가장 먼저 실행되는 프로그램처럼, 이 파일의 `main` 메서드가 프로그램의 시작점입니다.

### 주요 기능

#### 1) 데이터베이스 초기화
```java
boolean success = DatabaseManager.createDatabase();
```
- **무엇을 하나요?** 프로그램이 데이터를 저장할 데이터베이스를 만듭니다.
- **왜 필요한가요?** 일기, 감정, 사용자 정보를 저장하려면 저장 공간이 필요합니다.
- **결과**: 성공하면 `true`, 실패하면 `false`를 반환합니다.

#### 2) 실패 시 안내 메시지 출력
```java
if (!success) {
    System.err.println("데이터베이스 초기화에 실패했습니다.");
    // ... 해결 방법 안내
    return;
}
```
- **무엇을 하나요?** 데이터베이스 생성에 실패하면 에러 메시지를 보여주고 프로그램을 종료합니다.
- **왜 필요한가요?** 사용자가 문제를 해결할 수 있도록 도와줍니다.

#### 3) 통계 화면 띄우기
```java
SwingUtilities.invokeLater(() -> {
    JFrame frame = new JFrame("Emotion Diary");
    // ... 통계 화면 생성
});
```
- **무엇을 하나요?** 통계를 보여주는 창을 만들어서 화면에 표시합니다.
- **SwingUtilities.invokeLater는 뭔가요?** GUI(화면) 작업을 안전하게 실행하기 위한 방법입니다.
- **크기**: 495 x 630 픽셀로 고정되어 있습니다.

### 현재 상태
- 로그인 기능: 아직 구현 안 됨 (주석 처리)
- 통계 화면: 임시로 바로 실행됨
- 나중에 로그인 기능이 추가되면 로그인 후 통계 화면을 볼 수 있게 변경될 예정입니다.

---

## 2. DatabaseManager.java

### 역할
데이터베이스와 관련된 모든 작업을 담당합니다. 데이터베이스는 정보를 저장하는 창고라고 생각하면 됩니다.

### 주요 구성 요소

#### 1) 데이터베이스 연결 정보
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=UTC";
private static final String DB_ID = "root";
private static final String DB_PW = "REMOVED_PASSWORD";
```
- **DB_URL**: 데이터베이스의 주소입니다. `localhost:3306`은 내 컴퓨터의 3306번 포트를 의미합니다.
- **DB_ID**: 데이터베이스 접속 아이디입니다.
- **DB_PW**: 데이터베이스 접속 비밀번호입니다. (Git 푸쉬 전에 꼭 바꿔야 합니다!)

#### 2) 임시 사용자 ID
```java
public static String loggedInUserId = "test_user";
```
- **왜 필요한가요?** 로그인 기능이 구현되기 전까지 임시로 사용하는 사용자 ID입니다.
- 나중에 실제 로그인한 사용자 ID로 교체됩니다.

### 주요 기능

#### 1) 데이터베이스 생성 (createDatabase)
```java
public static boolean createDatabase()
```
**하는 일:**
1. MySQL 서버에 접속합니다.
2. `emotion_diary`라는 이름의 데이터베이스가 있는지 확인합니다.
3. 없으면 새로 만들고 4개의 테이블을 생성합니다:
   - `user` 테이블: 사용자 정보 (아이디, 비밀번호)
   - `diary` 테이블: 일기 내용 (제목, 내용, 스트레스 수준, 날짜)
   - `emotion` 테이블: 감정 정보 (감정 수치, 이모지)
   - `question` 테이블: 질문 목록

**왜 이 순서인가요?**
- `user`를 먼저 만들어야 `diary`가 어느 사용자의 일기인지 알 수 있습니다.
- `diary`를 먼저 만들어야 `emotion`이 어느 일기의 감정인지 알 수 있습니다.

#### 2) 데이터베이스 연결 (getConnection)
```java
public static Connection getConnection() throws Exception
```
- **하는 일**: 데이터베이스에 접속해서 연결을 만듭니다.
- **반환**: 데이터베이스와 소통할 수 있는 `Connection` 객체를 반환합니다.

#### 3) 로그인 확인 (checkLogin)
```java
public boolean checkLogin(String id, String pw)
```
- **하는 일**: 입력한 아이디와 비밀번호가 맞는지 확인합니다.
- **과정**:
  1. 데이터베이스에서 해당 아이디의 비밀번호를 가져옵니다.
  2. 입력한 비밀번호와 비교합니다.
  3. 같으면 `true`, 다르면 `false`를 반환합니다.

#### 4) 회원가입 (registerUser)
```java
public int registerUser(String id, String pw)
```
- **하는 일**: 새로운 사용자를 등록합니다.
- **반환값**:
  - `1`: 회원가입 성공
  - `0`: 이미 존재하는 아이디 (중복)
  - `-1`: 오류 발생

#### 5) 일기 저장 (insertDiaryEntry)
```java
public static boolean insertDiaryEntry(String title, String content, int stressLevel, 
                                        List<String> emotionIcons, List<Integer> emotionValuesList)
```
- **하는 일**: 새 일기를 데이터베이스에 저장합니다.
- **저장하는 정보**:
  - 제목 (title)
  - 내용 (content)
  - 스트레스 수준 (stressLevel)
  - 감정 이모지 목록 (emotionIcons)
  - 감정 수치 목록 (emotionValuesList)
- **특별한 점**: 일기와 감정을 동시에 저장하며, 둘 중 하나라도 실패하면 모두 취소됩니다 (트랜잭션).

#### 6) 일기 목록 조회 (getAllEntries)
```java
public static List<DiaryEntry> getAllEntries() throws Exception
```
- **하는 일**: 현재 로그인한 사용자의 모든 일기를 가져옵니다.
- **반환**: 일기 목록 (최신 순)

#### 7) 감정 정보 조회 (getEmotionsByEntryId)
```java
public static List<Emotion> getEmotionsByEntryId(Connection conn, int entryId)
```
- **하는 일**: 특정 일기에 입력된 감정 정보를 가져옵니다.
- **최대 4개**: 한 일기당 최대 4개의 감정까지 저장할 수 있습니다.

#### 8) 일기 수정 (updateDiaryEntry)
```java
public static boolean updateDiaryEntry(int entryId, String title, String content, 
                                        int stressLevel, List<String> emotionIcons, 
                                        List<Integer> emotionValuesList)
```
- **하는 일**: 기존 일기를 수정합니다.
- **과정**:
  1. 일기의 제목, 내용, 스트레스 수준을 업데이트합니다.
  2. 기존 감정을 모두 삭제합니다.
  3. 새로운 감정을 추가합니다.

### 테이블 구조

#### user 테이블
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| user_id | VARCHAR(20) | 사용자 아이디 (기본키) |
| user_pw | VARCHAR(20) | 사용자 비밀번호 |

#### diary 테이블
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| entry_id | INTEGER | 일기 번호 (자동 증가, 기본키) |
| user_id | VARCHAR(20) | 작성자 아이디 (외래키) |
| title | VARCHAR(50) | 일기 제목 |
| content | TEXT | 일기 내용 |
| stress_level | INTEGER | 스트레스 수준 (0-100) |
| entry_date | DATETIME | 작성 날짜 및 시간 |

#### emotion 테이블
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| emotion_id | INTEGER | 감정 번호 (자동 증가, 기본키) |
| entry_id | INTEGER | 일기 번호 (외래키) |
| emotion_level | INTEGER | 감정 수치 |
| emoji_icon | VARCHAR(10) | 이모지 (😊, 😢 등) |

#### question 테이블
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| question_id | INTEGER | 질문 번호 (자동 증가, 기본키) |
| question_text | VARCHAR(100) | 질문 내용 |

---

## 3. DiaryEntry.java

### 역할
일기 하나의 정보를 담는 클래스입니다. 일기 한 편을 하나의 상자에 담는다고 생각하면 됩니다.

### 포함된 정보

```java
private int entry_id;           // 일기 번호
private String title;           // 제목
private String content;         // 내용
private int stress_level;       // 스트레스 수준 (0-100)
private String entry_date;      // 작성 날짜
private List<Emotion> emotions; // 감정 목록
```

### 주요 메서드

#### Getter 메서드 (정보 가져오기)
```java
public int getEntry_id()        // 일기 번호 가져오기
public String getTitle()        // 제목 가져오기
public String getContent()      // 내용 가져오기
public int getStress_level()    // 스트레스 수준 가져오기
public String getEntry_date()   // 날짜 가져오기
public List<Emotion> getEmotions() // 감정 목록 가져오기
```

#### Setter 메서드 (정보 설정하기)
```java
public void setEntry_id(int entry_id)
public void setTitle(String title)
public void setContent(String content)
public void setStress_level(int stress_level)
public void setEntry_date(String entry_date)
public void setEmotions(List<Emotion> emotions)
```

### 사용 예시
```java
// 새로운 일기 만들기
DiaryEntry diary = new DiaryEntry();
diary.setTitle("오늘의 일기");
diary.setContent("오늘은 즐거운 하루였다.");
diary.setStress_level(30);

// 일기 정보 읽기
String title = diary.getTitle();  // "오늘의 일기"
int stress = diary.getStress_level();  // 30
```

---

## 4. Emotion.java

### 역할
감정 하나의 정보를 담는 클래스입니다. 하나의 감정을 표현하는 작은 상자입니다.

### 포함된 정보

```java
private int emotion_level;    // 감정 강도 (0-100)
private String emoji_icon;    // 이모지 (😊, 😢, 😠 등)
```

### 주요 메서드

#### Getter 메서드
```java
public int getEmotion_level()    // 감정 강도 가져오기
public String getEmoji_icon()    // 이모지 가져오기
```

#### Setter 메서드
```java
public void setEmotion_level(int emotion_level)
public void setEmoji_icon(String emoji_icon)
```

### 사용 예시
```java
// 새로운 감정 만들기
Emotion emotion = new Emotion();
emotion.setEmoji_icon("😊");
emotion.setEmotion_level(80);

// 감정 정보 읽기
String emoji = emotion.getEmoji_icon();  // "😊"
int level = emotion.getEmotion_level();  // 80
```

### 사용 가능한 이모지
프로그램에서 사용하는 12가지 이모지:
- 긍정적 감정: 😊, 😆, 😍, 😌, 😂, 🤗
- 부정적 감정: 😢, 😠, 😰, 😅, 😧, 😔

---

## 5. MainView.java

### 역할
프로그램의 메인 화면을 만드는 클래스입니다. 세 개의 탭(쓰기, 열람, 통계)을 가진 화면입니다.

### 화면 구성

```
┌─────────────────────────────┐
│ [쓰기] [열람] [통계]         │ ← 메뉴 버튼
├─────────────────────────────┤
│                             │
│                             │
│     선택된 탭의 내용        │
│                             │
│                             │
└─────────────────────────────┘
```

### 주요 구성 요소

#### 1) 창 설정
```java
setTitle("Emotion Diary");  // 창 제목
setSize(495, 630);          // 창 크기
setLocationRelativeTo(null); // 화면 중앙에 위치
```

#### 2) 메뉴 버튼
```java
JButton write = new JButton("쓰기");   // 일기 작성
JButton view = new JButton("열람");    // 일기 보기
JButton chart = new JButton("통계");   // 통계 보기
```

#### 3) CardLayout
```java
cardLayout = new CardLayout();
cardPanel = new JPanel(cardLayout);
```
- **CardLayout이란?** 여러 화면을 겹쳐놓고 하나씩 보여주는 방식입니다.
- 카드 뭉치에서 한 장씩 꺼내 보는 것과 같습니다.

#### 4) 세 개의 패널
```java
JPanel writePanel = new JPanel();        // 쓰기 화면 (임시)
JPanel viewPanel = new JPanel();         // 열람 화면 (임시)
StatisticsView chartPanel = new StatisticsView();  // 통계 화면 (구현됨)
```

### 화면 전환 방식

```java
write.addActionListener(e -> cardLayout.show(cardPanel, "write"));
view.addActionListener(e -> cardLayout.show(cardPanel, "view"));
chart.addActionListener(e -> cardLayout.show(cardPanel, "chart"));
```
- **버튼을 누르면** → `cardLayout.show()`가 실행됨
- **해당 화면이 표시됨** → 다른 화면은 숨겨짐

### 창 닫기 처리

```java
addWindowListener(new WindowAdapter() {
    public void windowClosing(WindowEvent e) {
        SaveQuestion.handleWindowClosing(MainView.this, writePanel, true);
    }
});
```
- **X 버튼을 누르면** → `SaveQuestion`이 실행됨
- **저장 여부를 물어봄** → "저장하시겠습니까?"

### 현재 상태
- **쓰기 패널**: 빈 화면 (일기 작성 기능 미구현)
- **열람 패널**: 빈 화면 (일기 열람 기능 미구현)
- **통계 패널**: 완전히 구현됨 (차트 표시)

---

## 6. SaveQuestion.java

### 역할
프로그램을 닫을 때 "저장하시겠습니까?"라고 물어보는 창을 띄우는 클래스입니다.

### 주요 기능

#### handleWindowClosing 메서드
```java
public static boolean handleWindowClosing(JFrame frame, Object panel, boolean exitProgram)
```

**매개변수 설명:**
- `frame`: 현재 열려있는 창
- `panel`: 현재 화면의 패널 (일기 작성 화면 등)
- `exitProgram`: 프로그램을 완전히 종료할지 여부
  - `true`: 프로그램 종료
  - `false`: 창만 닫기

### 작동 방식

#### 1) 수정 사항 확인 (현재 주석 처리됨)
```java
// if (!panel.isModified) {
//     if (exitProgram) System.exit(0);
//     else frame.dispose();
//     return true;
// }
```
- 나중에 일기 작성 기능이 구현되면 활성화됩니다.
- 수정 사항이 없으면 바로 종료합니다.

#### 2) 저장 여부 확인 창 띄우기
```java
int result = JOptionPane.showConfirmDialog(
    frame, 
    "저장되지 않은 변경 사항이 있습니다. 저장하시겠습니까?", 
    "경고", 
    JOptionPane.YES_NO_CANCEL_OPTION
);
```

**사용자의 선택:**
- **예 (YES)**: 저장 후 종료 (현재는 저장 기능 미구현)
- **아니오 (NO)**: 저장하지 않고 종료
- **취소 (CANCEL)**: 종료 취소 (계속 사용)

#### 3) 선택에 따른 처리
```java
switch (result) {
    case JOptionPane.YES_OPTION:
        // panel.saveOrFinish();  // 나중에 구현
        break;
    case JOptionPane.NO_OPTION:
        break;
    case JOptionPane.CANCEL_OPTION:
        return false;  // 종료 취소
}
```

#### 4) 종료 실행
```java
if (exitProgram) System.exit(0);  // 프로그램 종료
else frame.dispose();              // 창만 닫기
```

### 반환값
- `true`: 종료 완료
- `false`: 종료 취소됨

---

## 7. StatisticsView.java

### 역할
통계 화면의 모든 UI(사용자 인터페이스)를 그리는 클래스입니다. 차트, 버튼, 콤보박스 등 화면에 보이는 모든 것을 만듭니다.

### 화면 구성

```
┌─────────────────────────────────────┐
│ [주간▼] [2025년▼] [11월▼] [3주▼]  │ ← 선택 메뉴
├─────────────────────────────────────┤
│ 평균 스트레스 지수: 0.0            │ ← 평균 표시
├─────────────────────────────────────┤
│  ┌───────────────────────────────┐ │
│  │    감정 막대 그래프            │ │ ← 감정 차트
│  └───────────────────────────────┘ │
├─────────────────────────────────────┤
│  ┌───────────────────────────────┐ │
│  │    스트레스 꺾은선 그래프      │ │ ← 스트레스 차트
│  └───────────────────────────────┘ │
└─────────────────────────────────────┘
```

### 주요 구성 요소

#### 1) 색상 설정
```java
private static final Color PASTEL_BLUE = new Color(230, 240, 255);
```
- 연한 파란색 배경을 사용합니다.
- RGB 값: (230, 240, 255)

#### 2) 사용 가능한 이모지
```java
private static final String[] OFFICIAL_EMOTIONS = {
    "😊", "😆", "😍", "😌", "😂", "🤗",
    "😢", "😠", "😰", "😅", "😧", "😔"
};
```
- 총 12개의 이모지를 사용합니다.
- 긍정 6개, 부정 6개

#### 3) 선택 메뉴 (ComboBox)

**조회 모드 선택:**
```java
viewModeSelector = new JComboBox<>(new String[]{"주간", "월간", "연간"});
```

**주간 모드:**
```java
yearComboW  // 년도 선택
monthComboW // 월 선택
weekComboW  // 주 선택
```

**월간 모드:**
```java
yearComboM  // 년도 선택
monthComboM // 월 선택
```

**연간 모드:**
```java
yearComboY  // 년도 선택
```

#### 4) 평균 스트레스 라벨
```java
avgStressLabel = new JLabel();
```
- 선택한 기간의 평균 스트레스 지수를 표시합니다.
- 형식: "평균 스트레스 지수: 45.3"

### 주요 메서드

#### 1) initUI() - 화면 초기화
```java
private void initUI()
```
**하는 일:**
1. 오늘 날짜를 가져옵니다.
2. 콤보박스를 만들고 오늘 날짜로 설정합니다.
3. 년도/월/주 선택 메뉴를 배치합니다.
4. 감정 차트와 스트레스 차트를 만듭니다.
5. 모드 변경 시 화면을 업데이트하는 이벤트를 등록합니다.

#### 2) populateYearCombos() - 년도 목록 만들기
```java
private void populateYearCombos()
```
- 2020년부터 올해까지의 년도 목록을 만듭니다.
- 예: ["2020년", "2021년", "2022년", "2023년", "2024년", "2025년"]

#### 3) updateMonthCombos() - 월 목록 업데이트
```java
private void updateMonthCombos()
```
- 선택한 년도가 올해라면 1월부터 이번 달까지만 표시합니다.
- 예: 2025년 11월이면 ["01월", "02월", ..., "11월"]

#### 4) updateWeekCombo() - 주 목록 업데이트
```java
private void updateWeekCombo()
```
- 선택한 월의 주차 목록을 만듭니다.
- 예: ["1주", "2주", "3주", "4주"]

#### 5) createChartPanel() - 차트 영역 만들기
```java
private JPanel createChartPanel(String mode)
```
**구성:**
- 평균 스트레스 라벨
- 감정 차트 (막대 그래프)
- 스트레스 차트 (꺾은선 그래프)

**모드에 따라 다른 레이블:**
- 주간: X축 = (요일)
- 월간: X축 = (주)
- 연간: X축 = (월)

#### 6) createDemoEmotionBarChart() - 감정 차트 만들기
```java
private JPanel createDemoEmotionBarChart(String mode)
```
**차트 설정:**
- 제목: 없음
- X축: (감정) - 12개 이모지
- Y축: (%) - 0~100
- 막대 색상: 파란색 계열
- 데이터: 빈 상태 (실제 데이터는 Controller가 넣음)

#### 7) createDemoStressLineChart() - 스트레스 차트 만들기
```java
private JPanel createDemoStressLineChart(String mode)
```
**차트 설정:**
- 제목: 없음
- X축: 모드에 따라 (요일), (주), (월)
- Y축: (%) - 0~100
- 선 두께: 2.5픽셀
- 점 모양: 원형 (지름 7픽셀)
- 데이터: 빈 상태

#### 8) updateStressChart() - 스트레스 차트 업데이트
```java
public void updateStressChart(DefaultCategoryDataset dataset)
```
**하는 일:**
1. StatisticsController가 데이터베이스에서 가져온 데이터를 받습니다.
2. 차트의 데이터를 새로운 데이터로 교체합니다.
3. 화면이 자동으로 갱신됩니다.

**데이터 형식:**
- 주간: 월, 화, 수, 목, 금, 토, 일 (7개)
- 월간: 1주, 2주, 3주, 4주 (4~5개)
- 연간: 1월, 2월, ..., 12월 (12개)

#### 9) updateEmotionChart() - 감정 차트 업데이트
```java
public void updateEmotionChart(Map<String, Map<String, Double>> emotionData)
```
**하는 일:**
1. 감정 데이터를 받습니다:
   - "횟수": 각 이모지가 사용된 횟수
   - "수치": 각 이모지의 평균 감정 수치
2. 12개 이모지 각각에 대해:
   - 데이터베이스에 있으면 실제 값 사용
   - 없으면 0으로 설정
3. 차트를 업데이트합니다.

**데이터 구조:**
```java
{
  "횟수": {"😊": 10.0, "😢": 5.0, ...},
  "수치": {"😊": 75.0, "😢": 30.0, ...}
}
```

#### 10) Getter 메서드들
```java
public JComboBox<String> getViewModeSelector()  // 모드 선택기
public JComboBox<String> getYearComboW()        // 주간 년도
public JComboBox<String> getMonthComboW()       // 주간 월
public JComboBox<String> getWeekComboW()        // 주간 주
public JComboBox<String> getYearComboM()        // 월간 년도
public JComboBox<String> getMonthComboM()       // 월간 월
public JComboBox<String> getYearComboY()        // 연간 년도
public JLabel getAvgStressLabel()               // 평균 라벨
```
- Controller가 이 메서드들을 사용해서 선택된 값을 읽습니다.

### 차트 라이브러리: JFreeChart

#### ChartFactory
차트를 쉽게 만들어주는 도구입니다.

```java
// 막대 그래프 만들기
JFreeChart barChart = ChartFactory.createBarChart(
    null,                          // 제목
    "(감정)",                      // X축 라벨
    "(%)",                         // Y축 라벨
    dataset,                       // 데이터
    PlotOrientation.VERTICAL,      // 세로 방향
    false,                         // 범례 없음
    true,                          // 툴팁 있음
    false                          // URL 없음
);

// 꺾은선 그래프 만들기
JFreeChart lineChart = ChartFactory.createLineChart(...);
```

#### CategoryPlot
차트의 세부 설정을 바꾸는 부분입니다.

```java
CategoryPlot plot = chart.getCategoryPlot();
plot.setRangeGridlinesVisible(true);  // 가로 격자선 표시
plot.setRangeGridlinePaint(new Color(220, 220, 220));  // 격자선 색상
```

#### NumberAxis
숫자 축(Y축)을 설정합니다.

```java
NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
yAxis.setRange(0.0, 100.0);              // 범위: 0~100
yAxis.setTickUnit(new NumberTickUnit(10.0));  // 눈금 간격: 10
```

### CardLayout 사용

```java
datePickerCardLayout = new CardLayout();
datePickerCardPanel = new JPanel(datePickerCardLayout);

// 카드 추가
datePickerCardPanel.add(weeklyCard, "주간");
datePickerCardPanel.add(monthlyCard, "월간");
datePickerCardPanel.add(yearlyCard, "연간");

// 카드 보여주기
datePickerCardLayout.show(datePickerCardPanel, "주간");
```
- 모드를 바꾸면 해당하는 날짜 선택 메뉴만 표시됩니다.

---

## 8. StatisticsController.java

### 역할
통계 화면의 로직을 처리하는 클래스입니다. View(화면)와 DAO(데이터베이스) 사이에서 중간 다리 역할을 합니다.

### MVC 패턴에서의 위치

```
View (StatisticsView)
  ↕ 사용자가 버튼 클릭, 데이터 표시
Controller (StatisticsController)  ← 여기
  ↕ 데이터 요청 및 가공
Model/DAO (StatisticsDAO)
  ↕ 데이터베이스 조회
Database (MySQL)
```

### 주요 구성 요소

```java
private StatisticsView view;  // 화면
private StatisticsDAO dao;    // 데이터베이스 접근
```

### 주요 메서드

#### 1) 생성자 - 초기화
```java
public StatisticsController(StatisticsView view, StatisticsDAO dao)
```
**하는 일:**
1. View와 DAO를 저장합니다.
2. 이벤트 리스너를 추가합니다 (버튼 클릭 감지).
3. 모든 차트를 업데이트합니다.

#### 2) addListeners() - 이벤트 등록
```java
private void addListeners()
```
**등록하는 이벤트:**
- 모드 선택이 바뀌면 → 차트 업데이트
- 년도가 바뀌면 → 차트 업데이트
- 월이 바뀌면 → 차트 업데이트
- 주가 바뀌면 → 차트 업데이트

**코드:**
```java
view.getViewModeSelector().addActionListener(e -> updateAllCharts());
view.getYearComboW().addActionListener(e -> updateAllCharts());
// ... 모든 콤보박스에 대해 동일
```

#### 3) updateAllCharts() - 모든 차트 업데이트
```java
private void updateAllCharts()
```
**처리 순서:**

**1단계: 사용자가 선택한 값 읽기**
```java
String mode = (String) view.getViewModeSelector().getSelectedItem();
// "주간", "월간", "연간" 중 하나
```

**2단계: 시작 날짜와 종료 날짜 계산**
```java
LocalDate startDate = getStartDateFromView(mode);
LocalDate endDate = getEndDateFromView(mode);
```

**3단계: 데이터베이스에서 데이터 조회**
```java
double avgStress = dao.getAverageStress(startDate, endDate);
Map<String, Map<String, Double>> emotionData = dao.getEmotionData(startDate, endDate);
DefaultCategoryDataset stressDataset = dao.getStressData(startDate, endDate, mode);
```

**4단계: 화면에 데이터 표시**
```java
view.getAvgStressLabel().setText(
    String.format("<html><center>평균 스트레스 지수<b>:</b> <b>%.1f</b></center></html>", avgStress)
);
view.updateEmotionChart(emotionData);
view.updateStressChart(stressDataset);
```

**에러 처리:**
```java
try {
    // 위의 모든 과정
} catch (Exception e) {
    e.printStackTrace();
    view.showError("데이터를 불러오는 중 오류가 발생했습니다.");
}
```

#### 4) getStartDateFromView() - 시작 날짜 계산
```java
private LocalDate getStartDateFromView(String mode)
```

**주간 모드:**
- 선택한 월의 N번째 주의 월요일을 찾습니다.
- 예: 2025년 11월 3주 → 2025-11-17 (월요일)

**월간 모드:**
- 선택한 월의 1일을 반환합니다.
- 예: 2025년 11월 → 2025-11-01

**연간 모드:**
- 선택한 년도의 1월 1일을 반환합니다.
- 예: 2025년 → 2025-01-01

**계산 방법 (주간):**
```java
int year = Integer.parseInt(yearStr.replace("년", ""));
int month = Integer.parseInt(monthStr.replace("월", ""));
int week = Integer.parseInt(weekStr.replace("주", ""));

YearMonth ym = YearMonth.of(year, month);  // 년월 객체
WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);  // 월요일 시작

LocalDate dayInWeek = ym.atDay(Math.min((week - 1) * 7 + 1, ym.lengthOfMonth()));
LocalDate startDate = dayInWeek.with(weekFields.dayOfWeek(), 1L);  // 그 주의 월요일
```

#### 5) getEndDateFromView() - 종료 날짜 계산
```java
private LocalDate getEndDateFromView(String mode)
```

**주간 모드:**
- 시작 날짜 + 6일 (일요일)
- 월을 넘어가면 그 달의 마지막 날로 조정
- 예: 2025-11-17 (월) → 2025-11-23 (일)

**월간 모드:**
- 선택한 월의 마지막 날
- 예: 2025년 11월 → 2025-11-30

**연간 모드:**
- 선택한 년도의 12월 31일
- 예: 2025년 → 2025-12-31

### 날짜 처리 클래스들

#### LocalDate
날짜를 표현하는 클래스입니다.

```java
LocalDate today = LocalDate.now();           // 오늘
LocalDate specific = LocalDate.of(2025, 11, 22);  // 2025-11-22
LocalDate next = today.plusDays(7);          // 7일 후
```

#### YearMonth
년도와 월을 표현하는 클래스입니다.

```java
YearMonth ym = YearMonth.of(2025, 11);       // 2025년 11월
LocalDate firstDay = ym.atDay(1);            // 2025-11-01
LocalDate lastDay = ym.atEndOfMonth();       // 2025-11-30
int daysInMonth = ym.lengthOfMonth();        // 30
```

#### WeekFields
주차를 계산하는 클래스입니다.

```java
WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
// 월요일을 한 주의 시작으로 설정

int weekOfMonth = date.get(weekFields.weekOfMonth());  // 월의 몇 번째 주인가
```

### 데이터 흐름

```
1. 사용자가 콤보박스 변경
   ↓
2. addListeners()가 감지
   ↓
3. updateAllCharts() 실행
   ↓
4. getStartDateFromView(), getEndDateFromView()로 날짜 계산
   ↓
5. DAO.getAverageStress() 호출 → 평균 스트레스 조회
   ↓
6. DAO.getEmotionData() 호출 → 감정 데이터 조회
   ↓
7. DAO.getStressData() 호출 → 스트레스 데이터 조회
   ↓
8. View.updateEmotionChart(), updateStressChart() 호출
   ↓
9. 화면에 새로운 데이터 표시
```

---

## 9. StatisticsDAO.java

### 역할
데이터베이스에서 통계에 필요한 데이터를 가져오는 클래스입니다. DAO는 Data Access Object의 약자로, 데이터베이스에 접근하는 전담 클래스입니다.

### 주요 메서드

#### 1) getConnection() - 데이터베이스 연결
```java
private Connection getConnection() throws Exception
```
- DatabaseManager를 통해 데이터베이스 연결을 가져옵니다.
- `private`이므로 이 클래스 내부에서만 사용됩니다.

#### 2) getAverageStress() - 평균 스트레스 조회
```java
public double getAverageStress(LocalDate startDate, LocalDate endDate)
```

**SQL 쿼리:**
```sql
SELECT AVG(stress_level) 
FROM diary 
WHERE DATE(entry_date) BETWEEN ? AND ?
```

**의미:**
- `diary` 테이블에서
- `entry_date`가 시작일과 종료일 사이에 있는 일기들의
- `stress_level`의 평균을 계산합니다.

**과정:**
1. 데이터베이스에 연결합니다.
2. SQL 쿼리를 준비합니다.
3. `?`에 시작 날짜와 종료 날짜를 넣습니다.
4. 쿼리를 실행합니다.
5. 결과를 `double` 타입으로 받아옵니다.
6. 연결을 닫습니다.

**반환값:**
- 평균 스트레스 값 (예: 45.5)
- 데이터가 없으면 0.0

#### 3) getEmotionData() - 감정 데이터 조회
```java
public Map<String, Map<String, Double>> getEmotionData(LocalDate startDate, LocalDate endDate)
```

**반환 데이터 구조:**
```java
{
  "횟수": {
    "😊": 10.0,  // 😊가 10번 사용됨
    "😢": 5.0,   // 😢가 5번 사용됨
    ...
  },
  "수치": {
    "😊": 75.0,  // 😊의 평균 감정 수치 75
    "😢": 30.0,  // 😢의 평균 감정 수치 30
    ...
  }
}
```

**SQL 쿼리 1 - 횟수:**
```sql
SELECT e.emoji_icon, COUNT(e.emoji_icon)
FROM emotion e
JOIN diary d ON e.entry_id = d.entry_id
WHERE DATE(d.entry_date) BETWEEN ? AND ?
GROUP BY e.emoji_icon
```

**의미:**
- `emotion` 테이블과 `diary` 테이블을 `entry_id`로 연결
- 기간 내의 감정 데이터만 선택
- 이모지별로 그룹화해서 개수 세기

**SQL 쿼리 2 - 수치:**
```sql
SELECT e.emoji_icon, AVG(e.emotion_level)
FROM emotion e
JOIN diary d ON e.entry_id = d.entry_id
WHERE DATE(d.entry_date) BETWEEN ? AND ?
GROUP BY e.emoji_icon
```

**의미:**
- 횟수 쿼리와 비슷하지만
- 개수 대신 평균 감정 수치를 계산

**과정:**
1. 빈 Map을 2개 만듭니다 ("횟수", "수치").
2. 첫 번째 쿼리로 횟수 데이터를 가져옵니다.
3. 두 번째 쿼리로 수치 데이터를 가져옵니다.
4. 결과를 Map에 저장합니다.
5. 완성된 Map을 반환합니다.

#### 4) getStressData() - 스트레스 데이터 조회
```java
public DefaultCategoryDataset getStressData(LocalDate startDate, LocalDate endDate, String mode)
```

**모드별 다른 SQL 쿼리:**

**주간 모드:**
```sql
SELECT DAYOFWEEK(entry_date) AS day_num, AVG(stress_level)
FROM diary
WHERE DATE(entry_date) BETWEEN ? AND ?
GROUP BY day_num
ORDER BY FIELD(day_num, 2, 3, 4, 5, 6, 7, 1)
```
- `DAYOFWEEK()`: 요일 번호를 반환 (1=일, 2=월, ..., 7=토)
- `GROUP BY day_num`: 요일별로 그룹화
- `ORDER BY FIELD()`: 월, 화, 수, 목, 금, 토, 일 순서로 정렬

**결과:**
```java
dataset.setValue(avgStress, "Stress(DAO)", "월");
dataset.setValue(avgStress, "Stress(DAO)", "화");
// ... 일요일까지
```

**월간 모드:**
```sql
SELECT WEEK(entry_date, 3) AS week_num, AVG(stress_level)
FROM diary
WHERE DATE(entry_date) BETWEEN ? AND ?
GROUP BY week_num
ORDER BY week_num
```
- `WEEK(entry_date, 3)`: 주 번호 계산 (mode 3 = 월요일 시작)
- 첫 번째 주를 1로 만들기 위해 보정

**결과:**
```java
dataset.setValue(avgStress, "Stress(DAO)", "1주");
dataset.setValue(avgStress, "Stress(DAO)", "2주");
// ... 4~5주까지
```

**연간 모드:**
```sql
SELECT MONTH(entry_date) AS month_num, AVG(stress_level)
FROM diary
WHERE DATE(entry_date) BETWEEN ? AND ?
GROUP BY month_num
ORDER BY month_num
```
- `MONTH()`: 월 번호 반환 (1~12)
- 월별로 평균 스트레스 계산

**결과:**
```java
dataset.setValue(avgStress, "Stress(DAO)", "1월");
dataset.setValue(avgStress, "Stress(DAO)", "2월");
// ... 12월까지
```

#### 5) mapDayOfWeek() - 요일 번호를 한글로 변환
```java
private String mapDayOfWeek(int dayNum)
```

**변환 규칙:**
```java
1 → "일"
2 → "월"
3 → "화"
4 → "수"
5 → "목"
6 → "금"
7 → "토"
```

### SQL 주요 함수 설명

#### AVG() - 평균
```sql
AVG(stress_level)  -- 스트레스 수준의 평균
```

#### COUNT() - 개수
```sql
COUNT(emoji_icon)  -- 이모지의 개수
```

#### DATE() - 날짜만 추출
```sql
DATE(entry_date)  -- 2025-11-22 14:30:00 → 2025-11-22
```

#### DAYOFWEEK() - 요일 번호
```sql
DAYOFWEEK('2025-11-22')  -- 7 (토요일)
```

#### WEEK() - 주 번호
```sql
WEEK(entry_date, 3)  -- 월요일 시작 기준 주 번호
```

#### MONTH() - 월 번호
```sql
MONTH('2025-11-22')  -- 11
```

#### GROUP BY - 그룹화
```sql
GROUP BY emoji_icon  -- 이모지별로 묶어서 계산
```

#### JOIN - 테이블 연결
```sql
FROM emotion e
JOIN diary d ON e.entry_id = d.entry_id
-- emotion과 diary를 entry_id로 연결
```

### DefaultCategoryDataset

차트에 데이터를 넣는 그릇입니다.

```java
DefaultCategoryDataset dataset = new DefaultCategoryDataset();

// 데이터 추가
dataset.setValue(45.5, "Stress(DAO)", "월");
//               값     시리즈 이름      카테고리(X축)

dataset.setValue(50.2, "Stress(DAO)", "화");
dataset.setValue(38.7, "Stress(DAO)", "수");
```

**구조:**
- 행(Row/시리즈): "Stress(DAO)"
- 열(Column/카테고리): "월", "화", "수", ...
- 값(Value): 45.5, 50.2, 38.7, ...

### 에러 처리

```java
try {
    // 데이터베이스 작업
} catch (Exception e) {
    e.printStackTrace();  // 에러 내용 출력
    // 빈 값 또는 0.0 반환
}
```

---

## 📌 파일 간 관계도

```
Main.java
  ↓ 생성
DatabaseManager.java (데이터베이스 초기화)
  ↓ 사용
StatisticsView.java
  ↓ 생성
StatisticsController.java
  ↓ 사용
StatisticsDAO.java
  ↓ 조회
DatabaseManager.java
  ↓ 연결
MySQL Database
  ├── user 테이블
  ├── diary 테이블 ← DiaryEntry.java (데이터 형식)
  ├── emotion 테이블 ← Emotion.java (데이터 형식)
  └── question 테이블
```

---

## 🎯 데이터 흐름 예시

### 예시: 주간 통계 조회

**1. 사용자 동작**
- 모드: 주간 선택
- 년도: 2025년 선택
- 월: 11월 선택
- 주: 3주 선택

**2. StatisticsController 처리**
```java
mode = "주간"
startDate = 2025-11-17 (월요일)
endDate = 2025-11-23 (일요일)
```

**3. StatisticsDAO 데이터 조회**

**평균 스트레스:**
```sql
SELECT AVG(stress_level) FROM diary 
WHERE DATE(entry_date) BETWEEN '2025-11-17' AND '2025-11-23'
-- 결과: 45.5
```

**감정 데이터:**
```sql
-- 횟수
SELECT e.emoji_icon, COUNT(e.emoji_icon) ...
-- 결과: 😊: 10개, 😢: 3개, 😠: 2개

-- 수치
SELECT e.emoji_icon, AVG(e.emotion_level) ...
-- 결과: 😊: 75.0, 😢: 30.0, 😠: 80.0
```

**스트레스 데이터:**
```sql
SELECT DAYOFWEEK(entry_date) AS day_num, AVG(stress_level) ...
-- 결과:
-- 월: 40.0
-- 화: 45.0
-- 수: 50.0
-- 목: 48.0
-- 금: 42.0
-- 토: 38.0
-- 일: 55.0
```

**4. StatisticsView 화면 업데이트**
- 평균 스트레스 라벨: "평균 스트레스 지수: 45.5"
- 감정 차트: 😊 막대 10개/75, 😢 막대 3개/30, 😠 막대 2개/80
- 스트레스 차트: 월요일부터 일요일까지 꺾은선 그래프

---

## 💡 핵심 개념 정리

### MVC 패턴
- **Model** (데이터): DiaryEntry, Emotion, StatisticsDAO
- **View** (화면): StatisticsView, MainView
- **Controller** (로직): StatisticsController

### 데이터베이스 연결
1. DriverManager로 연결
2. Connection 객체 받기
3. PreparedStatement로 SQL 실행
4. ResultSet으로 결과 받기
5. 자원 해제 (close)

### Swing GUI
- **JFrame**: 창
- **JPanel**: 패널 (화면 영역)
- **JButton**: 버튼
- **JComboBox**: 선택 메뉴
- **JLabel**: 텍스트 표시

### 날짜 처리
- **LocalDate**: 날짜 (년-월-일)
- **YearMonth**: 년월
- **WeekFields**: 주차 계산

### 차트
- **JFreeChart**: 차트 라이브러리
- **DefaultCategoryDataset**: 데이터 저장
- **CategoryPlot**: 차트 설정

---

## 🔍 추가 학습 자료

### Java 기본
- 클래스와 객체
- 메서드 (static, instance)
- 접근 제어자 (public, private)
- 예외 처리 (try-catch)
- 컬렉션 (List, Map)

### 데이터베이스
- SQL 기본 (SELECT, INSERT, UPDATE, DELETE)
- JOIN 연산
- 집계 함수 (AVG, COUNT, SUM)
- GROUP BY

### GUI 프로그래밍
- Swing 컴포넌트
- 이벤트 처리
- 레이아웃 매니저

---

## ❓ 자주 묻는 질문

### Q1: 왜 Main.java에서 통계 UI만 바로 띄우나요?
**A:** 로그인 기능이 아직 구현되지 않아서 임시로 통계 화면만 테스트하기 위함입니다. 나중에 로그인 기능이 추가되면 로그인 → 메인 화면 → 통계 화면 순서로 변경됩니다.

### Q2: 데이터베이스 비밀번호가 코드에 있는데 안전한가요?
**A:** 안전하지 않습니다! Git에 푸쉬하기 전에 반드시 예시 비밀번호로 바꿔야 합니다. 실무에서는 환경 변수나 설정 파일로 관리합니다.

### Q3: 왜 DAO와 Manager를 분리했나요?
**A:** 역할 분담입니다.
- **DatabaseManager**: 모든 기능에서 사용하는 공통 데이터베이스 작업
- **StatisticsDAO**: 통계 전용 데이터베이스 작업

### Q4: StatisticsController가 왜 필요한가요? View에서 바로 DAO를 호출하면 안 되나요?
**A:** MVC 패턴을 따르기 위함입니다. Controller가 중간에서 데이터를 가공하고 로직을 처리하면 View는 화면만 담당할 수 있어 코드가 깔끔해집니다.

### Q5: SwingUtilities.invokeLater는 왜 사용하나요?
**A:** Swing GUI는 단일 스레드에서 작동해야 안전합니다. `invokeLater`는 GUI 작업을 안전하게 예약하는 방법입니다.

---

**이 문서로 감정 일기 프로젝트의 모든 자바 파일을 이해하셨나요?**

궁금한 점이 있다면 언제든지 물어보세요! 😊

