# API 및 클래스 레퍼런스

## 목차
1. [Model 계층](#1-model-계층)
2. [View 계층](#2-view-계층)
3. [Controller 계층](#3-controller-계층)
4. [Utility 계층](#4-utility-계층)

---

## 1. Model 계층

### 1.1 DatabaseUtil 클래스

**패키지**: `share`  
**역할**: 데이터베이스 초기화 및 연결 관리

#### 필드
```java
private static final String url = "jdbc:mysql://localhost:3306/?serverTimezone=UTC"
private static final String id = "root"
private static final String pw = "U9Bsi7sj1*"
```

#### 메소드

##### createDatabase()
```java
public static boolean createDatabase()
```
- **설명**: emotion_diary 데이터베이스 생성 및 초기 테이블 구성
- **반환값**: `boolean` - 성공 시 true, 실패 시 false
- **동작**:
  1. 데이터베이스 존재 여부 확인
  2. 없으면 `emotion_diary` 데이터베이스 생성
  3. 문자셋을 UTF8MB4로 설정 (이모지 지원)
  4. 4개의 테이블 생성 (user, diary, emotion, question)

**사용 예시**:
```java
boolean success = DatabaseUtil.createDatabase();
if (success) {
    System.out.println("데이터베이스 초기화 성공");
}
```

---

### 1.2 StatisticsDAO 클래스

**패키지**: `share`  
**역할**: 통계 데이터 조회를 위한 데이터 액세스 객체

#### 필드
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=UTC"
private static final String DB_USER = "root"
private static final String DB_PASSWORD = "U9Bsi7sj1*"
```

#### 메소드

##### getConnection()
```java
private Connection getConnection() throws SQLException
```
- **설명**: 데이터베이스 연결 객체 반환
- **접근 제한**: private (내부 사용)
- **예외**: SQLException

##### getAverageStress()
```java
public double getAverageStress(String userId, LocalDate startDate, LocalDate endDate)
```
- **설명**: 특정 기간의 평균 스트레스 지수 계산
- **파라미터**:
  - `userId`: 사용자 ID
  - `startDate`: 조회 시작일
  - `endDate`: 조회 종료일
- **반환값**: `double` - 평균 스트레스 지수 (데이터 없으면 0.0)
- **SQL 쿼리**:
```sql
SELECT AVG(stress_level) AS avgStress 
FROM diary 
WHERE user_id = ? AND entry_date BETWEEN ? AND ?
```

##### getEmotionData()
```java
public Map<String, Map<String, Double>> getEmotionData(String userId, LocalDate startDate, LocalDate endDate)
```
- **설명**: 특정 기간의 감정 데이터 조회
- **파라미터**:
  - `userId`: 사용자 ID
  - `startDate`: 조회 시작일
  - `endDate`: 조회 종료일
- **반환값**: `Map<String, Map<String, Double>>`
  - 외부 Map 키: "긍정", "부정"
  - 내부 Map 키: 감정 이름 (예: "행복", "슬픔")
  - 내부 Map 값: 감정 수치 평균

##### getStressData()
```java
public DefaultCategoryDataset getStressData(String userId, LocalDate startDate, LocalDate endDate, String mode)
```
- **설명**: 특정 기간의 스트레스 데이터 조회 (차트용)
- **파라미터**:
  - `userId`: 사용자 ID
  - `startDate`: 조회 시작일
  - `endDate`: 조회 종료일
  - `mode`: 조회 모드 ("주간", "월간", "연간")
- **반환값**: `DefaultCategoryDataset` - JFreeChart용 데이터셋

---

## 2. View 계층

### 2.1 MainApplication 클래스

**패키지**: `com.diary.emotion`  
**부모 클래스**: `JPanel`  
**역할**: 메인 애플리케이션 화면 컨테이너

#### 상수 필드
```java
private static final Color PASTEL_BLUE = new Color(230, 240, 255)
private static final Color PASTEL_YELLOW = new Color(255, 255, 220)
```

#### 멤버 필드
```java
private CardLayout mainCardLayout          // 카드 레이아웃 관리자
private JPanel mainCardPanel               // 카드 패널 컨테이너
private JPanel statisticsPanel             // 통계 탭 패널
private JPanel writePanel                  // 일기 쓰기 탭 패널
private JPanel viewPanel                   // 열람 탭 패널
```

#### 생성자
```java
public MainApplication()
```
- **설명**: MainApplication 패널 초기화
- **동작**:
  1. BorderLayout 설정
  2. 파스텔 블루 배경색 적용
  3. 상단 네비게이션 패널 생성
  4. 중앙 카드 패널 생성
  5. 3개 탭 패널 추가 (일기 쓰기, 열람, 통계)
  6. 버튼 이벤트 리스너 연결

---

### 2.2 StatisticsView 클래스

**패키지**: `share`  
**부모 클래스**: `JPanel`  
**역할**: 통계 화면 UI 구성 및 차트 표시

#### 상수 필드
```java
private static final Color PASTEL_BLUE = new Color(230, 240, 255)
private static final String[] YEARS = {"2020년", "2021년", "2022년", "2023년", "2024년", "2025년"}
private static final String[] MONTHS = {"01월", "02월", ..., "12월"}
private static final String[] WEEKS = {"1주", "2주", "3주", "4주", "5주"}
private static final Font AXIS_LABEL_FONT = new Font("SansSerif", Font.PLAIN, 11)
private static final Font CHART_TITLE_FONT = new Font("SansSerif", Font.BOLD, 16)
```

#### 멤버 필드
```java
private JLabel avgStressLabel              // 평균 스트레스 지수 라벨
private JComboBox<String> viewModeSelector // 주간/월간/연간 선택 콤보박스
private JPanel datePickerCardPanel         // 날짜 선택기 카드 패널
private CardLayout datePickerCardLayout    // 날짜 선택기 레이아웃
private JComboBox<String> yearComboW, monthComboW, weekComboW  // 주간 선택
private JComboBox<String> yearComboM, monthComboM              // 월간 선택
private JComboBox<String> yearComboY                           // 연간 선택
private JPanel mainChartPanel              // 차트 영역 메인 패널
```

#### 생성자
```java
public StatisticsView()
```
- **설명**: 통계 화면 UI 초기화

#### 주요 메소드

##### updateEmotionChart()
```java
public void updateEmotionChart(Map<String, Map<String, Double>> emotionData)
```
- **설명**: 감정 통계 차트 갱신
- **파라미터**: `emotionData` - 감정별 데이터 맵

##### updateStressChart()
```java
public void updateStressChart(DefaultCategoryDataset dataset)
```
- **설명**: 스트레스 지수 차트 갱신
- **파라미터**: `dataset` - JFreeChart 데이터셋

##### updateAverageStressLabel()
```java
public void updateAverageStressLabel(double avgStress)
```
- **설명**: 평균 스트레스 지수 라벨 갱신
- **파라미터**: `avgStress` - 평균 스트레스 값

##### Getter 메소드
```java
public JComboBox<String> getViewModeSelector()
public JComboBox<String> getYearComboW()
public JComboBox<String> getMonthComboW()
public JComboBox<String> getWeekComboW()
public JComboBox<String> getYearComboM()
public JComboBox<String> getMonthComboM()
public JComboBox<String> getYearComboY()
```
- **설명**: Controller가 View의 콤보박스에 접근하기 위한 getter

---

### 2.3 MainView 클래스

**패키지**: `share`  
**부모 클래스**: `JFrame`  
**역할**: 메인 프레임 (구버전, MainApplication과 병행 사용 중)

#### 멤버 필드
```java
CardLayout cardLayout
JPanel cardPanel
```

#### 생성자
```java
public MainView()
```
- **설명**: 메인 프레임 초기화 (550x700 크기)

---

## 3. Controller 계층

### 3.1 StatisticsController 클래스

**패키지**: `share`  
**역할**: 통계 화면의 비즈니스 로직 처리 및 View-Model 연결

#### 상수 필드
```java
private static final String TEMP_USER_ID = "testuser"
```
- **설명**: 로그인 기능 구현 전 임시 사용자 ID

#### 멤버 필드
```java
private StatisticsView view    // 제어할 View 객체
private StatisticsDAO dao      // 데이터를 요청할 DAO 객체
```

#### 생성자
```java
public StatisticsController(StatisticsView view, StatisticsDAO dao)
```
- **설명**: Controller 초기화 및 이벤트 리스너 연결
- **파라미터**:
  - `view`: StatisticsView 인스턴스
  - `dao`: StatisticsDAO 인스턴스
- **동작**:
  1. View와 DAO 저장
  2. 모든 콤보박스에 리스너 연결
  3. 초기 차트 데이터 갱신

#### 주요 메소드

##### addListeners()
```java
private void addListeners()
```
- **설명**: View의 모든 콤보박스에 이벤트 리스너 연결
- **접근 제한**: private

##### updateAllCharts()
```java
private void updateAllCharts()
```
- **설명**: 콤보박스 변경 시 모든 차트 갱신
- **접근 제한**: private
- **동작**:
  1. 현재 선택된 모드(주간/월간/연간) 읽기
  2. 시작일/종료일 계산
  3. DAO에서 데이터 조회
  4. View 업데이트

##### getStartDateFromView()
```java
private LocalDate getStartDateFromView(String mode)
```
- **설명**: View에서 선택된 날짜를 기반으로 시작일 계산
- **파라미터**: `mode` - "주간", "월간", "연간"
- **반환값**: `LocalDate` - 계산된 시작일
- **접근 제한**: private

##### getEndDateFromView()
```java
private LocalDate getEndDateFromView(String mode)
```
- **설명**: View에서 선택된 날짜를 기반으로 종료일 계산
- **파라미터**: `mode` - "주간", "월간", "연간"
- **반환값**: `LocalDate` - 계산된 종료일
- **접근 제한**: private

---

## 4. Utility 계층

### 4.1 AppLauncher 클래스

**패키지**: `com.diary.emotion`  
**역할**: 애플리케이션 실행 진입점

#### main 메소드
```java
public static void main(String[] args)
```
- **설명**: 프로그램의 시작점
- **동작**:
  1. Swing GUI를 EDT(Event Dispatch Thread)에서 실행
  2. 시스템 Look&Feel 적용
  3. JFrame 생성 (550x750)
  4. MainApplication 패널 생성 및 추가
  5. 화면 표시

**사용 예시**:
```bash
# Maven으로 실행
mvn clean compile exec:java -Dexec.mainClass="com.diary.emotion.AppLauncher"
```

---

### 4.2 Main 클래스

**패키지**: `share`  
**역할**: 구버전 실행 진입점 (MainView용)

#### main 메소드
```java
public static void main(String[] args)
```
- **설명**: 데이터베이스 초기화 및 MainView 실행
- **동작**:
  1. DatabaseUtil.createDatabase() 호출
  2. MainView 인스턴스 생성

---

## 5. 데이터 모델

### 5.1 감정 데이터 구조

#### Map<String, Map<String, Double>> emotionData
- **외부 Map**:
  - 키: "긍정" 또는 "부정"
  - 값: 감정별 데이터 맵
- **내부 Map**:
  - 키: 감정 이름 ("행복", "슬픔" 등)
  - 값: 감정 수치 (0.0 ~ 100.0)

**예시**:
```json
{
  "긍정": {
    "행복": 75.5,
    "신남": 60.0,
    "설렘": 80.2
  },
  "부정": {
    "슬픔": 30.0,
    "분노": 20.5
  }
}
```

---

## 6. 이벤트 처리

### 6.1 ActionListener 패턴

모든 버튼 클릭 및 콤보박스 선택 이벤트는 ActionListener를 통해 처리됩니다.

**예시**:
```java
button.addActionListener(new ActionListener() {
    @Override
    public void actionEvent(ActionEvent e) {
        // 이벤트 처리 로직
    }
});

// 람다식 사용
button.addActionListener(e -> {
    // 이벤트 처리 로직
});
```

---

## 7. 차트 라이브러리 (JFreeChart)

### 7.1 주요 클래스

#### ChartFactory
- `createBarChart()`: 막대 차트 생성
- `createLineChart()`: 꺾은선 차트 생성

#### DefaultCategoryDataset
```java
DefaultCategoryDataset dataset = new DefaultCategoryDataset();
dataset.addValue(75.5, "Series", "Category");
```

#### ChartPanel
```java
ChartPanel chartPanel = new ChartPanel(chart);
```

---

## 8. 예외 처리

### 8.1 SQLException
데이터베이스 관련 모든 메소드에서 SQLException 처리:
```java
try {
    // DB 작업
} catch (SQLException e) {
    System.err.println("DB 오류: " + e.getMessage());
    e.printStackTrace();
}
```

### 8.2 NullPointerException
콤보박스 선택값 확인:
```java
String mode = (String) viewModeSelector.getSelectedItem();
if (mode == null) return; // 초기화 중이면 중단
```

---

## 9. 스레드 안전성

### 9.1 SwingUtilities.invokeLater()
모든 GUI 작업은 EDT에서 실행:
```java
SwingUtilities.invokeLater(() -> {
    // GUI 작업
});
```

---

## 10. 디버깅

### 10.1 콘솔 출력
주요 지점에 디버깅 메시지 출력:
```java
System.out.println("[DAO] 평균 스트레스: " + avgStress);
System.out.println("Mode: " + mode + ", Start: " + startDate);
```

---

*이 문서는 코드의 구조와 API를 이해하는 데 도움을 줍니다. 실제 사용 예시는 `03_DEVELOPMENT_GUIDE.md`를 참조하세요.*

