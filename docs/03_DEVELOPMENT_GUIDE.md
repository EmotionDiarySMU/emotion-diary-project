# 신규 개발자를 위한 개발 가이드

## 목차
1. [개발 환경 설정](#1-개발-환경-설정)
2. [프로젝트 빌드 및 실행](#2-프로젝트-빌드-및-실행)
3. [데이터베이스 설정](#3-데이터베이스-설정)
4. [코드 구조 이해](#4-코드-구조-이해)
5. [새 기능 개발 가이드](#5-새-기능-개발-가이드)
6. [디버깅 방법](#6-디버깅-방법)
7. [코딩 컨벤션](#7-코딩-컨벤션)
8. [자주 묻는 질문](#8-자주-묻는-질문)

---

## 1. 개발 환경 설정

### 1.1 필수 소프트웨어 설치

#### Java 17
```bash
# macOS (Homebrew 사용)
brew install openjdk@17

# 환경 변수 설정
export JAVA_HOME=/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home
```

#### Maven
```bash
# macOS (Homebrew 사용)
brew install maven

# 설치 확인
mvn -version
```

#### MySQL 8.0.33
```bash
# macOS (Homebrew 사용)
brew install mysql@8.0

# MySQL 서비스 시작
brew services start mysql@8.0

# root 비밀번호 설정
mysql_secure_installation
```

#### IntelliJ IDEA
- [JetBrains 공식 웹사이트](https://www.jetbrains.com/idea/)에서 다운로드
- Community Edition 또는 Ultimate Edition 모두 사용 가능

### 1.2 프로젝트 클론 및 열기

```bash
# 프로젝트 디렉토리로 이동
cd /Users/iee12/IdeaProjects/emotion-diary-project

# IntelliJ IDEA로 프로젝트 열기
# File > Open > emotion-diary-project 폴더 선택
```

### 1.3 Maven 의존성 다운로드

IntelliJ IDEA에서 자동으로 다운로드되지만, 수동으로 할 경우:
```bash
mvn clean install
```

---

## 2. 프로젝트 빌드 및 실행

### 2.1 Maven으로 빌드

```bash
# 프로젝트 루트에서 실행
mvn clean compile
```

### 2.2 애플리케이션 실행

#### 방법 1: IntelliJ IDEA에서 실행
1. `AppLauncher.java` 파일 열기
2. 파일 내의 `main` 메소드 옆의 ▶️ 버튼 클릭
3. "Run 'AppLauncher.main()'" 선택

#### 방법 2: Maven으로 실행
```bash
mvn clean compile exec:java -Dexec.mainClass="com.diary.emotion.AppLauncher"
```

#### 방법 3: 터미널에서 직접 실행
```bash
# 컴파일
javac -cp "target/classes:~/.m2/repository/..." com/diary/emotion/AppLauncher.java

# 실행
java -cp "target/classes:..." com.diary.emotion.AppLauncher
```

### 2.3 구버전 실행 (MainView)

```bash
# Main.java 실행
mvn clean compile exec:java -Dexec.mainClass="share.Main"
```

---

## 3. 데이터베이스 설정

### 3.1 MySQL 접속 정보 확인

프로젝트의 DB 설정:
- **호스트**: localhost
- **포트**: 3306
- **사용자**: root
- **비밀번호**: REMOVED_PASSWORD (실제 환경에 맞게 변경)
- **데이터베이스**: emotion_diary

### 3.2 데이터베이스 초기화

애플리케이션을 처음 실행하면 `DatabaseUtil.createDatabase()`가 자동으로 호출되어:
1. `emotion_diary` 데이터베이스 생성
2. 4개 테이블 생성 (user, diary, emotion, question)

수동으로 초기화하려면:
```java
boolean success = DatabaseUtil.createDatabase();
```

### 3.3 MySQL 워크벤치로 확인

```sql
-- 데이터베이스 확인
SHOW DATABASES;

-- 테이블 확인
USE emotion_diary;
SHOW TABLES;

-- 테이블 구조 확인
DESC user;
DESC diary;
DESC emotion;
DESC question;
```

### 3.4 비밀번호 변경 방법

프로젝트에서 사용하는 모든 DB 연결 정보 변경:

#### DatabaseUtil.java
```java
String pw = "여러분의_비밀번호";
```

#### StatisticsDAO.java
```java
private static final String DB_PASSWORD = "여러분의_비밀번호";
```

---

## 4. 코드 구조 이해

### 4.1 MVC 패턴 적용

```
┌─────────────┐
│    View     │ (Swing GUI)
│  (JPanel)   │
└──────┬──────┘
       │
       ↓
┌─────────────┐
│ Controller  │ (비즈니스 로직)
└──────┬──────┘
       │
       ↓
┌─────────────┐
│    Model    │ (DAO)
│  (Database) │
└─────────────┘
```

### 4.2 패키지 구조

```
com.diary.emotion/
├── AppLauncher.java        # 메인 실행 파일
├── MainApplication.java    # 메인 화면 (JPanel)
└── (추가 클래스)

share/
├── DatabaseUtil.java       # DB 초기화
├── MainView.java          # 구버전 메인 프레임
├── StatisticsView.java    # 통계 화면 (View)
├── StatisticsController.java  # 통계 컨트롤러
└── StatisticsDAO.java     # 통계 데이터 액세스
```

### 4.3 클래스 간 관계

```
AppLauncher (main)
    └─> JFrame 생성
        └─> MainApplication (JPanel)
            ├─> writePanel (일기 쓰기)
            ├─> viewPanel (열람)
            └─> statisticsPanel
                └─> StatisticsView
                    ↔ StatisticsController
                        ↔ StatisticsDAO
                            ↔ MySQL Database
```

---

## 5. 새 기능 개발 가이드

### 5.1 새 화면 추가하기

#### Step 1: View 클래스 생성
```java
package com.diary.emotion.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;

public class WriteDiaryView extends JPanel {
    
    public WriteDiaryView() {
        setLayout(new BorderLayout());
        
        // UI 컴포넌트 추가
        JLabel titleLabel = new JLabel("일기 쓰기");
        add(titleLabel, BorderLayout.NORTH);
        
        // ... 나머지 UI
    }
    
    // Getter/Setter 메소드
}
```

#### Step 2: Controller 클래스 생성
```java
package com.diary.emotion.controller;

import com.diary.emotion.view.WriteDiaryView;
import com.diary.emotion.model.DiaryDAO;

public class WriteDiaryController {
    private WriteDiaryView view;
    private DiaryDAO dao;
    
    public WriteDiaryController(WriteDiaryView view, DiaryDAO dao) {
        this.view = view;
        this.dao = dao;
        addListeners();
    }
    
    private void addListeners() {
        // 이벤트 리스너 연결
    }
}
```

#### Step 3: DAO 클래스 생성
```java
package com.diary.emotion.model;

import java.sql.*;

public class DiaryDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary";
    
    public boolean saveDiary(/* 파라미터 */) {
        String sql = "INSERT INTO diary ...";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, "root", "password");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 파라미터 설정
            pstmt.setString(1, ...);
            
            // 실행
            int rows = pstmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
```

#### Step 4: MainApplication에 통합
```java
// MainApplication.java의 생성자에서

// 새 패널 생성
WriteDiaryView writeDiaryView = new WriteDiaryView();
DiaryDAO diaryDAO = new DiaryDAO();
WriteDiaryController writeDiaryController = new WriteDiaryController(writeDiaryView, diaryDAO);

// 카드 패널에 추가
mainCardPanel.add(writeDiaryView, "write");

// 버튼 이벤트에서 화면 전환
writeButton.addActionListener(e -> mainCardLayout.show(mainCardPanel, "write"));
```

### 5.2 데이터베이스 쿼리 작성

#### SELECT 예시
```java
public List<Diary> getDiariesByDate(LocalDate date) {
    String sql = "SELECT * FROM diary WHERE DATE(entry_date) = ?";
    List<Diary> diaries = new ArrayList<>();
    
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setDate(1, java.sql.Date.valueOf(date));
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Diary diary = new Diary();
                diary.setEntryId(rs.getInt("entry_id"));
                diary.setTitle(rs.getString("title"));
                diary.setContent(rs.getString("content"));
                // ...
                diaries.add(diary);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return diaries;
}
```

#### INSERT 예시
```java
public boolean insertDiary(String userId, String title, String content, int stressLevel) {
    String sql = "INSERT INTO diary (user_id, title, content, stress_level, entry_date) " +
                 "VALUES (?, ?, ?, ?, NOW())";
    
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        pstmt.setString(1, userId);
        pstmt.setString(2, title);
        pstmt.setString(3, content);
        pstmt.setInt(4, stressLevel);
        
        int rows = pstmt.executeUpdate();
        
        if (rows > 0) {
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int entryId = rs.getInt(1);
                    System.out.println("생성된 entry_id: " + entryId);
                }
            }
            return true;
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return false;
}
```

#### UPDATE 예시
```java
public boolean updateDiary(int entryId, String title, String content) {
    String sql = "UPDATE diary SET title = ?, content = ? WHERE entry_id = ?";
    
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, title);
        pstmt.setString(2, content);
        pstmt.setInt(3, entryId);
        
        int rows = pstmt.executeUpdate();
        return rows > 0;
        
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
```

#### DELETE 예시
```java
public boolean deleteDiary(int entryId) {
    String sql = "DELETE FROM diary WHERE entry_id = ?";
    
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, entryId);
        
        int rows = pstmt.executeUpdate();
        return rows > 0;
        
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
```

### 5.3 Swing UI 컴포넌트 활용

#### JTextField (텍스트 입력)
```java
JTextField titleField = new JTextField(20);
String title = titleField.getText();
titleField.setText("새 제목");
```

#### JTextArea (여러 줄 텍스트)
```java
JTextArea contentArea = new JTextArea(10, 30);
contentArea.setLineWrap(true);
contentArea.setWrapStyleWord(true);

JScrollPane scrollPane = new JScrollPane(contentArea);
```

#### JComboBox (드롭다운)
```java
String[] emotions = {"😊 행복", "😆 신남", "😍 설렘"};
JComboBox<String> emotionCombo = new JComboBox<>(emotions);

String selected = (String) emotionCombo.getSelectedItem();
```

#### JButton (버튼)
```java
JButton saveButton = new JButton("저장");
saveButton.addActionListener(e -> {
    // 저장 로직
    saveDiary();
});
```

#### JCheckBox (체크박스)
```java
JCheckBox happyCheck = new JCheckBox("😊 행복");
boolean isSelected = happyCheck.isSelected();
```

#### JSlider (슬라이더)
```java
JSlider stressSlider = new JSlider(0, 100, 50);
stressSlider.setMajorTickSpacing(10);
stressSlider.setPaintTicks(true);
stressSlider.setPaintLabels(true);

int value = stressSlider.getValue();
```

---

## 6. 디버깅 방법

### 6.1 콘솔 로그 활용

```java
// 변수 값 확인
System.out.println("userId: " + userId);
System.out.println("startDate: " + startDate);

// 메소드 호출 추적
System.out.println("[DEBUG] updateAllCharts() 시작");
// ... 로직
System.out.println("[DEBUG] updateAllCharts() 완료");

// 조건문 분기 확인
if (condition) {
    System.out.println("[DEBUG] 조건 true");
} else {
    System.out.println("[DEBUG] 조건 false");
}
```

### 6.2 IntelliJ IDEA 디버거 사용

1. 브레이크포인트 설정: 코드 라인 번호 왼쪽 클릭
2. 디버그 모드 실행: 🐞 버튼 클릭
3. 변수 값 확인: Variables 패널
4. 단계별 실행:
   - **Step Over (F8)**: 다음 줄로
   - **Step Into (F7)**: 메소드 안으로
   - **Step Out (Shift+F8)**: 메소드 밖으로
   - **Resume (F9)**: 다음 브레이크포인트까지

### 6.3 예외 처리 및 로깅

```java
try {
    // 위험한 작업
    int result = riskyOperation();
    System.out.println("성공: " + result);
} catch (SQLException e) {
    System.err.println("DB 오류 발생!");
    System.err.println("메시지: " + e.getMessage());
    e.printStackTrace();
} catch (Exception e) {
    System.err.println("예상치 못한 오류!");
    e.printStackTrace();
}
```

### 6.4 GUI 디버깅

```java
// 컴포넌트 크기 확인
System.out.println("Panel size: " + panel.getSize());

// 컴포넌트 가시성 확인
System.out.println("Panel visible: " + panel.isVisible());

// 레이아웃 확인
System.out.println("Layout: " + panel.getLayout());

// 자식 컴포넌트 개수
System.out.println("Component count: " + panel.getComponentCount());
```

---

## 7. 코딩 컨벤션

### 7.1 네이밍 규칙

#### 클래스명: PascalCase
```java
public class StatisticsView { }
public class DiaryController { }
```

#### 메소드명: camelCase
```java
public void updateChart() { }
public String getUserName() { }
```

#### 변수명: camelCase
```java
private int userId;
private String userName;
```

#### 상수명: UPPER_SNAKE_CASE
```java
private static final String DB_URL = "...";
private static final int MAX_EMOTIONS = 4;
```

### 7.2 주석 작성

#### 클래스 주석
```java
/**
 * [설명] 통계 데이터를 조회하는 DAO 클래스
 * (수정) 2025-11-18: 평균 스트레스 계산 로직 추가
 */
public class StatisticsDAO { }
```

#### 메소드 주석
```java
/**
 * 특정 기간의 평균 스트레스를 계산합니다.
 * 
 * @param userId 사용자 ID
 * @param startDate 조회 시작일
 * @param endDate 조회 종료일
 * @return 평균 스트레스 지수 (0.0 ~ 100.0)
 */
public double getAverageStress(String userId, LocalDate startDate, LocalDate endDate) { }
```

#### 인라인 주석
```java
// (중요) 이 값은 임시 사용자 ID입니다
String userId = "testuser";

// (디버깅) 계산 결과 확인
System.out.println("Result: " + result);

// (TODO) 로그인 기능 완성 후 실제 userId 사용
```

### 7.3 코드 포맷팅

#### 들여쓰기: 4칸 (스페이스)
```java
public void example() {
    if (condition) {
        doSomething();
    }
}
```

#### 중괄호 위치
```java
// 올바른 예시
public void method() {
    // ...
}

// 잘못된 예시
public void method()
{
    // ...
}
```

#### 한 줄에 하나의 문장
```java
// 올바른 예시
int a = 1;
int b = 2;
int c = 3;

// 잘못된 예시
int a = 1; int b = 2; int c = 3;
```

### 7.4 접근 제어자 사용

```java
public class Example {
    // public: 외부에서 접근 필요한 경우
    public void publicMethod() { }
    
    // private: 클래스 내부에서만 사용
    private void helperMethod() { }
    
    // private: 모든 멤버 변수는 기본적으로 private
    private int value;
    
    // public: getter/setter로 접근 제공
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
}
```

---

## 8. 자주 묻는 질문

### Q1: Maven 빌드가 실패합니다
```bash
# 캐시 정리 후 재빌드
mvn clean
mvn install
```

### Q2: MySQL 연결이 안 됩니다
```bash
# MySQL 서비스 상태 확인
brew services list

# MySQL 재시작
brew services restart mysql@8.0

# 연결 테스트
mysql -u root -p
```

### Q3: 한글이 깨져서 나옵니다
```java
// 파일 인코딩을 UTF-8로 설정
// IntelliJ IDEA: File > Settings > Editor > File Encodings
// 모두 UTF-8로 설정
```

### Q4: UI가 제대로 표시되지 않습니다
```java
// EDT에서 실행되는지 확인
SwingUtilities.invokeLater(() -> {
    // GUI 코드
});

// 컴포넌트 갱신
panel.revalidate();
panel.repaint();
```

### Q5: 차트가 비어있습니다
```java
// DAO에서 실제 데이터를 반환하는지 확인
System.out.println("[DEBUG] Dataset: " + dataset);

// Controller에서 update 메소드가 호출되는지 확인
System.out.println("[DEBUG] updateChart() called");
```

### Q6: 데이터베이스에 데이터가 없습니다
```sql
-- 테스트 데이터 삽입
INSERT INTO user (user_id, user_pw) VALUES ('testuser', 'password123');

INSERT INTO diary (user_id, title, content, stress_level, entry_date)
VALUES ('testuser', '테스트 일기', '오늘은 좋은 날', 50, NOW());
```

### Q7: 패키지 구조를 변경하고 싶습니다
```
현재: share, com.diary.emotion 혼용
권장: com.diary.emotion 통일

com.diary.emotion/
├── model/
│   ├── DatabaseUtil.java
│   ├── StatisticsDAO.java
│   └── DiaryDAO.java
├── view/
│   ├── MainApplication.java
│   ├── StatisticsView.java
│   └── WriteDiaryView.java
├── controller/
│   ├── StatisticsController.java
│   └── DiaryController.java
└── AppLauncher.java
```

---

## 9. 다음 단계

### 9.1 학습 순서
1. ✅ 개발 환경 설정
2. ✅ 프로젝트 빌드 및 실행
3. ✅ 데이터베이스 확인
4. ✅ 코드 구조 이해
5. 📝 TODO 리스트 확인 (`04_TODO_LIST.md`)
6. 📝 현재 상태 파악 (`06_CURRENT_STATUS_REPORT.md`)
7. 🔧 미완성 기능 구현 시작

### 9.2 추천 학습 자료
- **Java Swing**: [Oracle Java Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/)
- **JDBC**: [Oracle JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)
- **JFreeChart**: [JFreeChart Documentation](http://www.jfree.org/jfreechart/)
- **Maven**: [Maven Getting Started](https://maven.apache.org/guides/getting-started/)

---

*이 가이드로 개발을 시작하기 충분합니다. 추가 질문이 있다면 `06_CURRENT_STATUS_REPORT.md`를 참조하거나 팀에 문의하세요.*

