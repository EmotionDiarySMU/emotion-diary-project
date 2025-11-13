# 상세 TODO 리스트

## 📋 전체 진행 상황
- [ ] Phase 1: 공통 모듈 (0/4)
- [ ] Phase 2: 인증 모듈 (0/4)
- [ ] Phase 3: 일기 작성 모듈 (0/6)
- [ ] Phase 4: 일기 열람 모듈 (0/6)
- [ ] Phase 5: 통계 모듈 완성 (0/3)
- [ ] Phase 6: 통합 및 테스트 (0/5)

---

## 🔥 Phase 1: 공통 모듈 (우선순위: 최고)

### ✅ Task 1.1: Constants 클래스 작성
**파일**: `src/main/java/share/Constants.java`
**예상 시간**: 30분

- [ ] 1.1.1 파일 생성 및 패키지 선언
- [ ] 1.1.2 색상 상수 정의
  ```java
  public static final Color PASTEL_BLUE = new Color(230, 240, 255);
  public static final Color PASTEL_YELLOW = new Color(255, 255, 220);
  public static final Color PASTEL_GREEN = new Color(230, 255, 230);
  public static final Color PASTEL_PINK = new Color(255, 230, 240);
  ```
- [ ] 1.1.3 폰트 상수 정의
  ```java
  public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 18);
  public static final Font SUBTITLE_FONT = new Font("SansSerif", Font.BOLD, 16);
  public static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
  public static final Font SMALL_FONT = new Font("SansSerif", Font.PLAIN, 12);
  ```
- [ ] 1.1.4 크기 상수 정의
  ```java
  public static final Dimension MAIN_WINDOW_SIZE = new Dimension(550, 750);
  public static final Dimension DIALOG_SIZE = new Dimension(400, 300);
  ```
- [ ] 1.1.5 감정 리스트 정의
  ```java
  public static final String[] EMOTIONS = {
      "😊 기쁨", "😢 슬픔", "😠 분노", "😰 불안",
      "😌 평온", "😔 우울", "😖 좌절", "💖 사랑"
  };
  ```
- [ ] 1.1.6 메시지 상수 정의
  ```java
  public static final String MSG_SAVE_SUCCESS = "저장되었습니다.";
  public static final String MSG_DELETE_CONFIRM = "정말 삭제하시겠습니까?";
  ```

**완료 조건**: Constants 클래스가 컴파일되고 다른 클래스에서 import 가능

---

### ✅ Task 1.2: SessionManager 클래스 작성 (싱글톤)
**파일**: `src/main/java/share/SessionManager.java`
**예상 시간**: 45분

- [ ] 1.2.1 파일 생성 및 싱글톤 패턴 구현
  - [ ] private static 인스턴스 변수
  - [ ] private 생성자
  - [ ] public static getInstance() 메소드
- [ ] 1.2.2 멤버 변수 선언
  - [ ] `private String currentUserId`
  - [ ] `private LocalDateTime loginTime`
- [ ] 1.2.3 login 메소드 구현
  ```java
  public void login(String userId) {
      this.currentUserId = userId;
      this.loginTime = LocalDateTime.now();
  }
  ```
- [ ] 1.2.4 logout 메소드 구현
  ```java
  public void logout() {
      this.currentUserId = null;
      this.loginTime = null;
  }
  ```
- [ ] 1.2.5 getCurrentUserId 메소드 구현
- [ ] 1.2.6 isLoggedIn 메소드 구현
  ```java
  public boolean isLoggedIn() {
      return currentUserId != null;
  }
  ```
- [ ] 1.2.7 getLoginDuration 메소드 구현 (선택사항)

**완료 조건**: SessionManager가 싱글톤으로 동작하며 세션 관리 가능

---

### ✅ Task 1.3: DatabaseUtil 개선
**파일**: `src/main/java/share/DatabaseUtil.java` (기존 파일 수정)
**예상 시간**: 30분

- [ ] 1.3.1 getConnection() 메소드 추가
  ```java
  public static Connection getConnection() throws SQLException {
      return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
  }
  ```
- [ ] 1.3.2 closeResources 메소드 추가
  ```java
  public static void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
      if (rs != null) try { rs.close(); } catch (SQLException e) {}
      if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
      if (conn != null) try { conn.close(); } catch (SQLException e) {}
  }
  ```
- [ ] 1.3.3 상수를 private static final로 정리
- [ ] 1.3.4 JavaDoc 주석 추가

**완료 조건**: 다른 DAO에서 DatabaseUtil.getConnection() 사용 가능

---

### ✅ Task 1.4: Model 클래스 작성
**파일**: `src/main/java/com/diary/emotion/model/*.java`
**예상 시간**: 1시간

#### 1.4.1 UserModel.java
- [ ] 패키지 및 클래스 선언
- [ ] 멤버 변수
  - [ ] `private String userId`
  - [ ] `private String userPw`
- [ ] 생성자 (기본, 매개변수)
- [ ] Getter/Setter
- [ ] toString() 오버라이드

#### 1.4.2 EmotionModel.java
- [ ] 패키지 및 클래스 선언
- [ ] 멤버 변수
  - [ ] `private int emotionId`
  - [ ] `private int entryId`
  - [ ] `private String emojiIcon`
  - [ ] `private int emotionLevel`
- [ ] 생성자 (기본, 매개변수)
- [ ] Getter/Setter
- [ ] toString() 오버라이드

#### 1.4.3 DiaryModel.java
- [ ] 패키지 및 클래스 선언
- [ ] 멤버 변수
  - [ ] `private int entryId`
  - [ ] `private String userId`
  - [ ] `private String title`
  - [ ] `private String content`
  - [ ] `private int stressLevel`
  - [ ] `private LocalDateTime entryDate`
  - [ ] `private List<EmotionModel> emotions`
- [ ] 생성자 (기본, 매개변수)
- [ ] Getter/Setter
- [ ] toString() 오버라이드
- [ ] addEmotion, removeEmotion 헬퍼 메소드

**완료 조건**: 3개 Model 클래스 모두 컴파일 성공

---

## 🔐 Phase 2: 인증 모듈 (우선순위: 최고)

### ✅ Task 2.1: UserDAO 구현
**파일**: `src/main/java/com/diary/emotion/auth/UserDAO.java`
**예상 시간**: 1.5시간

- [ ] 2.1.1 패키지 및 클래스 선언
- [ ] 2.1.2 createUser 메소드 구현
  - [ ] SQL 쿼리 작성: `INSERT INTO user (user_id, user_pw) VALUES (?, ?)`
  - [ ] PreparedStatement 사용
  - [ ] try-with-resources 패턴
  - [ ] 중복 키 예외 처리 (SQLIntegrityConstraintViolationException)
  - [ ] 성공 시 true, 실패 시 false 반환
- [ ] 2.1.3 authenticateUser 메소드 구현
  - [ ] SQL 쿼리: `SELECT user_id FROM user WHERE user_id = ? AND user_pw = ?`
  - [ ] ResultSet 확인
  - [ ] 인증 성공 시 true, 실패 시 false
- [ ] 2.1.4 userExists 메소드 구현
  - [ ] SQL 쿼리: `SELECT COUNT(*) FROM user WHERE user_id = ?`
  - [ ] COUNT 값 확인
- [ ] 2.1.5 deleteUser 메소드 구현 (선택사항)
- [ ] 2.1.6 각 메소드에 JavaDoc 주석 추가
- [ ] 2.1.7 에러 로그 출력 추가

**완료 조건**: UserDAO의 모든 메소드가 DB와 정상 연동

---

### ✅ Task 2.2: AuthView 구현
**파일**: `src/main/java/com/diary/emotion/auth/AuthView.java`
**예상 시간**: 2시간

- [ ] 2.2.1 클래스 선언 (extends JPanel)
- [ ] 2.2.2 멤버 변수 선언
  - [ ] `private CardLayout cardLayout`
  - [ ] `private JPanel cardPanel`
  - [ ] `private JTextField loginIdField, signupIdField`
  - [ ] `private JPasswordField loginPwField, signupPwField, signupPwConfirmField`
  - [ ] `private JButton loginButton, toSignupButton, signupButton, toLoginButton`
- [ ] 2.2.3 생성자 구현
  - [ ] setLayout(BorderLayout)
  - [ ] setBackground(PASTEL_BLUE)
  - [ ] initUI() 호출
- [ ] 2.2.4 createLoginPanel 메소드 구현
  - [ ] GridBagLayout 사용
  - [ ] 상단에 제목 라벨 "Emotion Diary 😊" (TITLE_FONT)
  - [ ] 아이디 입력 필드 (20자 제한)
  - [ ] 비밀번호 입력 필드
  - [ ] 로그인 버튼
  - [ ] "회원가입" 버튼 (카드 전환)
  - [ ] Enter 키 리스너 추가
- [ ] 2.2.5 createSignupPanel 메소드 구현
  - [ ] GridBagLayout 사용
  - [ ] 제목 라벨 "회원가입"
  - [ ] 아이디 입력 필드
  - [ ] 비밀번호 입력 필드
  - [ ] 비밀번호 확인 필드
  - [ ] 회원가입 버튼
  - [ ] "로그인으로 돌아가기" 버튼
- [ ] 2.2.6 initUI 메소드 구현
  - [ ] CardLayout 초기화
  - [ ] 로그인/회원가입 패널 추가
  - [ ] 기본 카드를 "LOGIN"으로 설정
- [ ] 2.2.7 Getter 메소드 추가 (Controller 연동용)
  - [ ] getLoginIdField, getLoginPwField
  - [ ] getSignupIdField, getSignupPwField, getSignupPwConfirmField
  - [ ] getLoginButton, getSignupButton
- [ ] 2.2.8 카드 전환 메소드
  - [ ] showLoginPanel()
  - [ ] showSignupPanel()
- [ ] 2.2.9 입력 필드 초기화 메소드
  - [ ] clearLoginFields()
  - [ ] clearSignupFields()

**완료 조건**: AuthView 패널이 로그인/회원가입 화면 표시, 카드 전환 가능

---

### ✅ Task 2.3: AuthController 구현
**파일**: `src/main/java/com/diary/emotion/auth/AuthController.java`
**예상 시간**: 1.5시간

- [ ] 2.3.1 클래스 선언 및 멤버 변수
  - [ ] `private AuthView view`
  - [ ] `private UserDAO dao`
  - [ ] `private Runnable onLoginSuccess` (콜백)
- [ ] 2.3.2 생성자 구현
  - [ ] View, DAO 저장
  - [ ] 이벤트 리스너 연결
- [ ] 2.3.3 addListeners 메소드 구현
  - [ ] 로그인 버튼 리스너
  - [ ] 회원가입 버튼 리스너
  - [ ] 카드 전환 버튼 리스너
- [ ] 2.3.4 handleLogin 메소드 구현
  - [ ] 입력값 가져오기
  - [ ] validateLoginInput 호출
  - [ ] dao.authenticateUser 호출
  - [ ] 성공 시:
    - [ ] SessionManager.login(userId) 호출
    - [ ] "로그인 성공" 메시지
    - [ ] onLoginSuccess 콜백 실행
  - [ ] 실패 시: "아이디 또는 비밀번호가 잘못되었습니다" 오류
- [ ] 2.3.5 handleSignup 메소드 구현
  - [ ] 입력값 가져오기
  - [ ] validateSignupInput 호출
  - [ ] 비밀번호 일치 확인
  - [ ] dao.userExists 확인 (중복 체크)
  - [ ] dao.createUser 호출
  - [ ] 성공 시: "회원가입 완료" 메시지 + 로그인 화면으로 전환
  - [ ] 실패 시: 적절한 오류 메시지
- [ ] 2.3.6 validateLoginInput 메소드
  - [ ] 아이디 null/공백 체크
  - [ ] 비밀번호 null/공백 체크
  - [ ] 검증 실패 시 false 반환 + 오류 메시지
- [ ] 2.3.7 validateSignupInput 메소드
  - [ ] 아이디 길이 체크 (4-20자)
  - [ ] 비밀번호 길이 체크 (6-20자)
  - [ ] 특수문자 체크 (선택사항)
- [ ] 2.3.8 showMessage, showError 메소드
  - [ ] JOptionPane 사용
- [ ] 2.3.9 setOnLoginSuccess 메소드
  - [ ] 콜백 설정

**완료 조건**: 로그인/회원가입이 DB와 연동되어 정상 작동

---

### ✅ Task 2.4: AppLauncher 및 MainApplication 수정
**파일**: `src/main/java/com/diary/emotion/AppLauncher.java`
**예상 시간**: 1시간

- [ ] 2.4.1 AppLauncher 수정
  - [ ] frame를 멤버 변수로 선언
  - [ ] 초기 화면을 AuthView로 설정
  - [ ] AuthController 생성 및 콜백 설정
  - [ ] 로그인 성공 콜백에서 MainApplication으로 전환
    ```java
    authController.setOnLoginSuccess(() -> {
        frame.getContentPane().removeAll();
        frame.add(new MainApplication(), BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    });
    ```
- [ ] 2.4.2 MainApplication에 로그아웃 버튼 추가 (선택사항)
  - [ ] 상단 네비게이션에 "로그아웃" 버튼
  - [ ] 클릭 시 SessionManager.logout() + AuthView로 전환

**완료 조건**: 앱 시작 시 로그인 화면 표시, 로그인 성공 시 메인 화면 진입

---

## ✍️ Phase 3: 일기 작성 모듈 (우선순위: 중간)

### ✅ Task 3.1: EmotionInputPanel 구현
**파일**: `src/main/java/com/diary/emotion/write/EmotionInputPanel.java`
**예상 시간**: 1시간

- [ ] 3.1.1 클래스 선언 (extends JPanel)
- [ ] 3.1.2 멤버 변수 선언
  - [ ] `private JComboBox<String> emotionCombo`
  - [ ] `private JSlider levelSlider`
  - [ ] `private JLabel levelValueLabel`
  - [ ] `private JButton removeButton`
- [ ] 3.1.3 생성자 구현
  - [ ] ActionListener removeListener 파라미터
  - [ ] FlowLayout 설정
  - [ ] 배경색 PASTEL_BLUE
  - [ ] initUI() 호출
- [ ] 3.1.4 initUI 메소드
  - [ ] emotionCombo 생성 (Constants.EMOTIONS 사용)
  - [ ] levelSlider 생성 (0-100, 기본값 50)
  - [ ] levelValueLabel 생성 ("50")
  - [ ] removeButton 생성 ("X")
  - [ ] 슬라이더 ChangeListener 추가 (라벨 업데이트)
  - [ ] 모든 컴포넌트 추가
- [ ] 3.1.5 getSelectedEmotion 메소드
  - [ ] 선택된 감정 문자열 반환
- [ ] 3.1.6 getEmotionLevel 메소드
  - [ ] 슬라이더 값 반환
- [ ] 3.1.7 setEmotion 메소드 (수정 시 사용)
  - [ ] 감정 및 레벨 설정

**완료 조건**: EmotionInputPanel이 감정 선택 및 수치 입력 가능

---

### ✅ Task 3.2: EmotionSelectorPanel 구현
**파일**: `src/main/java/com/diary/emotion/write/EmotionSelectorPanel.java`
**예상 시간**: 1.5시간

- [ ] 3.2.1 클래스 선언 (extends JPanel)
- [ ] 3.2.2 멤버 변수
  - [ ] `private List<EmotionInputPanel> emotionPanels`
  - [ ] `private JPanel inputContainer`
  - [ ] `private JButton addButton`
  - [ ] `private static final int MAX_EMOTIONS = 4`
- [ ] 3.2.3 생성자 구현
  - [ ] BoxLayout.Y_AXIS 설정
  - [ ] 배경색 설정
  - [ ] emotionPanels = new ArrayList<>()
  - [ ] initUI() 호출
- [ ] 3.2.4 initUI 메소드
  - [ ] inputContainer 생성 (BoxLayout.Y_AXIS)
  - [ ] addButton 생성 ("+ 감정 추가")
  - [ ] addButton 리스너 추가
  - [ ] 컴포넌트 배치
  - [ ] 초기 감정 패널 1개 추가
- [ ] 3.2.5 addEmotionInput 메소드
  - [ ] MAX_EMOTIONS 체크
  - [ ] EmotionInputPanel 생성 (removeListener 전달)
  - [ ] emotionPanels에 추가
  - [ ] inputContainer에 추가
  - [ ] revalidate(), repaint()
  - [ ] 4개 도달 시 addButton 비활성화
- [ ] 3.2.6 removeEmotionInput 메소드
  - [ ] 파라미터로 받은 패널 제거
  - [ ] emotionPanels에서 제거
  - [ ] inputContainer에서 제거
  - [ ] revalidate(), repaint()
  - [ ] addButton 활성화
  - [ ] 최소 1개는 유지
- [ ] 3.2.7 getEmotions 메소드
  - [ ] List<EmotionModel> 생성
  - [ ] 각 EmotionInputPanel에서 데이터 수집
  - [ ] EmotionModel 객체 생성 후 리스트에 추가
  - [ ] 반환
- [ ] 3.2.8 clear 메소드
  - [ ] 모든 패널 제거 후 1개만 다시 추가
  - [ ] 초기화

**완료 조건**: 감정 패널 동적 추가/삭제, 최대 4개 제한

---

### ✅ Task 3.3: WriteView 구현
**파일**: `src/main/java/com/diary/emotion/write/WriteView.java`
**예상 시간**: 2.5시간

- [ ] 3.3.1 클래스 선언 (extends JPanel)
- [ ] 3.3.2 멤버 변수 선언
  - [ ] `private JTextField titleField`
  - [ ] `private JTextArea contentArea`
  - [ ] `private JSlider stressSlider`
  - [ ] `private JLabel stressValueLabel`
  - [ ] `private EmotionSelectorPanel emotionPanel`
  - [ ] `private JButton saveButton, clearButton`
- [ ] 3.3.3 생성자
  - [ ] BorderLayout 설정
  - [ ] 배경색 설정
  - [ ] initUI() 호출
- [ ] 3.3.4 initUI 메소드
  - [ ] createTitlePanel() - 제목 입력
  - [ ] createContentPanel() - 내용 입력
  - [ ] createStressPanel() - 스트레스 슬라이더
  - [ ] createEmotionPanel() - 감정 선택기
  - [ ] createButtonPanel() - 저장/초기화 버튼
  - [ ] 모든 패널을 메인 패널에 추가 (BoxLayout 또는 GridBagLayout)
- [ ] 3.3.5 createTitlePanel
  - [ ] JLabel "제목:" (LABEL_FONT)
  - [ ] titleField (최대 50자)
  - [ ] FlowLayout 또는 BorderLayout
- [ ] 3.3.6 createContentPanel
  - [ ] JLabel "내용:"
  - [ ] contentArea (JTextArea, 5행 이상)
  - [ ] JScrollPane로 감싸기
  - [ ] 줄바꿈 설정
- [ ] 3.3.7 createStressPanel
  - [ ] JLabel "스트레스 수치:"
  - [ ] stressSlider (0-100, 기본 50)
  - [ ] stressValueLabel ("50")
  - [ ] ChangeListener로 라벨 동기화
- [ ] 3.3.8 createEmotionPanel
  - [ ] JLabel "감정 선택:"
  - [ ] emotionPanel = new EmotionSelectorPanel()
- [ ] 3.3.9 createButtonPanel
  - [ ] saveButton ("저장")
  - [ ] clearButton ("초기화")
  - [ ] FlowLayout.CENTER
- [ ] 3.3.10 getDiaryData 메소드
  - [ ] DiaryModel 객체 생성
  - [ ] 현재 입력값들로 설정
  - [ ] userId는 SessionManager에서 가져오기
  - [ ] entryDate는 LocalDateTime.now()
  - [ ] emotions는 emotionPanel.getEmotions()
  - [ ] 반환
- [ ] 3.3.11 clearForm 메소드
  - [ ] 모든 입력 필드 초기화
  - [ ] emotionPanel.clear()
- [ ] 3.3.12 Getter 메소드들 (Controller용)
  - [ ] getSaveButton, getClearButton

**완료 조건**: WriteView가 모든 입력 요소 표시, 데이터 수집 가능

---

### ✅ Task 3.4: DiaryDAO 구현
**파일**: `src/main/java/com/diary/emotion/write/DiaryDAO.java`
**예상 시간**: 2시간

- [ ] 3.4.1 클래스 선언
- [ ] 3.4.2 createDiary 메소드
  - [ ] SQL: `INSERT INTO diary (user_id, title, content, stress_level, entry_date) VALUES (?, ?, ?, ?, ?)`
  - [ ] PreparedStatement.RETURN_GENERATED_KEYS 사용
  - [ ] 생성된 entry_id 반환
  - [ ] 실패 시 -1 반환
- [ ] 3.4.3 updateDiary 메소드
  - [ ] SQL: `UPDATE diary SET title=?, content=?, stress_level=? WHERE entry_id=? AND user_id=?`
  - [ ] 성공 여부 반환
- [ ] 3.4.4 deleteDiary 메소드
  - [ ] SQL: `DELETE FROM diary WHERE entry_id=? AND user_id=?`
  - [ ] CASCADE로 emotion도 자동 삭제되도록 (또는 수동 삭제)
- [ ] 3.4.5 getDiaryById 메소드
  - [ ] SQL: `SELECT * FROM diary WHERE entry_id=?`
  - [ ] DiaryModel 객체 생성 및 반환
  - [ ] null 체크
- [ ] 3.4.6 getDiariesByUser 메소드
  - [ ] SQL: `SELECT * FROM diary WHERE user_id=? ORDER BY entry_date DESC`
  - [ ] List<DiaryModel> 반환
- [ ] 3.4.7 getDiariesByDateRange 메소드
  - [ ] SQL: `SELECT * FROM diary WHERE user_id=? AND entry_date BETWEEN ? AND ? ORDER BY entry_date DESC`
  - [ ] LocalDate를 java.sql.Date로 변환
- [ ] 3.4.8 searchDiariesByTitle 메소드
  - [ ] SQL: `SELECT * FROM diary WHERE user_id=? AND title LIKE ? ORDER BY entry_date DESC`
  - [ ] LIKE 패턴: `%keyword%`
- [ ] 3.4.9 에러 처리 및 로그 추가

**완료 조건**: DiaryDAO의 모든 CRUD 메소드가 DB 연동

---

### ✅ Task 3.5: EmotionDAO 구현
**파일**: `src/main/java/com/diary/emotion/write/EmotionDAO.java`
**예상 시간**: 1.5시간

- [ ] 3.5.1 클래스 선언
- [ ] 3.5.2 createEmotions 메소드
  - [ ] SQL: `INSERT INTO emotion (entry_id, emoji_icon, emotion_level) VALUES (?, ?, ?)`
  - [ ] List<EmotionModel>를 받아서 반복 실행
  - [ ] Batch Insert 사용 (선택사항)
  - [ ] 트랜잭션 처리 (Connection 파라미터로 받기)
- [ ] 3.5.3 getEmotionsByEntryId 메소드
  - [ ] SQL: `SELECT * FROM emotion WHERE entry_id=?`
  - [ ] List<EmotionModel> 반환
- [ ] 3.5.4 deleteEmotionsByEntryId 메소드
  - [ ] SQL: `DELETE FROM emotion WHERE entry_id=?`
  - [ ] 일기 삭제 시 또는 수정 시 사용
- [ ] 3.5.5 updateEmotions 메소드 (편의 메소드)
  - [ ] 기존 감정 삭제 후 새로 삽입
  - [ ] 트랜잭션 처리

**완료 조건**: EmotionDAO가 감정 데이터 CRUD 가능

---

### ✅ Task 3.6: WriteController 구현 및 통합
**파일**: `src/main/java/com/diary/emotion/write/WriteController.java`
**예상 시간**: 2시간

- [ ] 3.6.1 클래스 선언 및 멤버 변수
  - [ ] `private WriteView view`
  - [ ] `private DiaryDAO diaryDao`
  - [ ] `private EmotionDAO emotionDao`
- [ ] 3.6.2 생성자
  - [ ] View, DAO 저장
  - [ ] addListeners() 호출
- [ ] 3.6.3 addListeners 메소드
  - [ ] saveButton 리스너
  - [ ] clearButton 리스너
- [ ] 3.6.4 handleSave 메소드
  - [ ] view.getDiaryData() 호출
  - [ ] validateDiary() 호출
  - [ ] saveDiaryWithEmotions() 호출
  - [ ] 성공 시: 성공 메시지 + clearForm()
  - [ ] 실패 시: 오류 메시지
- [ ] 3.6.5 saveDiaryWithEmotions 메소드
  - [ ] Connection 가져오기
  - [ ] setAutoCommit(false)
  - [ ] diaryDao.createDiary() 호출 → entryId 획득
  - [ ] 각 emotion의 entryId 설정
  - [ ] emotionDao.createEmotions() 호출
  - [ ] commit()
  - [ ] 예외 발생 시 rollback()
  - [ ] finally에서 리소스 정리
- [ ] 3.6.6 validateDiary 메소드
  - [ ] 제목 null/공백 체크
  - [ ] 제목 길이 체크 (최대 50자)
  - [ ] 내용 null 체크 (공백 허용)
  - [ ] 감정 최소 1개 체크
  - [ ] 검증 실패 시 오류 메시지 + false 반환
- [ ] 3.6.7 handleClear 메소드
  - [ ] 확인 다이얼로그 표시
  - [ ] 확인 시 view.clearForm()
- [ ] 3.6.8 showMessage, showError 메소드

**완료 조건**: 일기 작성 + 저장이 DB에 정상 저장됨

---

### ✅ Task 3.7: MainApplication에 WriteView 통합
**파일**: `src/main/java/com/diary/emotion/MainApplication.java`
**예상 시간**: 30분

- [ ] 3.7.1 임시 writePanel 제거
- [ ] 3.7.2 WriteView, WriteController 생성
  ```java
  WriteView writeView = new WriteView();
  DiaryDAO diaryDao = new DiaryDAO();
  EmotionDAO emotionDao = new EmotionDAO();
  WriteController writeController = new WriteController(writeView, diaryDao, emotionDao);
  ```
- [ ] 3.7.3 mainCardPanel에 추가
  ```java
  mainCardPanel.add(writeView, "WRITE");
  ```
- [ ] 3.7.4 실행 및 테스트
  - [ ] 일기 쓰기 탭 클릭 시 WriteView 표시
  - [ ] 저장 버튼 클릭 시 DB 저장 확인

**완료 조건**: 메인 화면에서 일기 작성 기능 사용 가능

---

## 📖 Phase 4: 일기 열람 모듈 (우선순위: 중간)

### ✅ Task 4.1: DiaryListPanel 구현
**파일**: `src/main/java/com/diary/emotion/view/DiaryListPanel.java`
**예상 시간**: 2시간

- [ ] 4.1.1 클래스 선언 (extends JPanel)
- [ ] 4.1.2 멤버 변수
  - [ ] `private JList<DiaryModel> diaryList`
  - [ ] `private DefaultListModel<DiaryModel> listModel`
  - [ ] `private JScrollPane scrollPane`
- [ ] 4.1.3 생성자
  - [ ] BorderLayout 설정
  - [ ] initUI() 호출
- [ ] 4.1.4 initUI 메소드
  - [ ] listModel 생성
  - [ ] diaryList 생성 (listModel 사용)
  - [ ] 커스텀 ListCellRenderer 설정
  - [ ] scrollPane로 감싸기
  - [ ] 추가
- [ ] 4.1.5 커스텀 ListCellRenderer 구현
  - [ ] JPanel 기반 셀 렌더러
  - [ ] 날짜 (작은 폰트, 회색)
  - [ ] 제목 (큰 폰트, 굵게)
  - [ ] 감정 아이콘들 표시
  - [ ] 스트레스 수치 표시 (선택사항)
  - [ ] 선택 시 배경색 변경
- [ ] 4.1.6 setDiaries 메소드
  - [ ] listModel.clear()
  - [ ] 각 DiaryModel을 listModel에 추가
- [ ] 4.1.7 addSelectionListener 메소드
  - [ ] diaryList.addListSelectionListener()
- [ ] 4.1.8 getSelectedDiary 메소드
  - [ ] diaryList.getSelectedValue() 반환
- [ ] 4.1.9 clearSelection 메소드

**완료 조건**: 일기 목록이 리스트로 표시, 선택 가능

---

### ✅ Task 4.2: DiaryDetailPanel 구현
**파일**: `src/main/java/com/diary/emotion/view/DiaryDetailPanel.java`
**예상 시간**: 2시간

- [ ] 4.2.1 클래스 선언 (extends JPanel)
- [ ] 4.2.2 멤버 변수
  - [ ] `private JLabel titleLabel, dateLabel, stressLabel`
  - [ ] `private JTextArea contentArea`
  - [ ] `private JPanel emotionDisplayPanel`
  - [ ] `private JButton editButton, deleteButton, backButton`
  - [ ] `private DiaryModel currentDiary`
- [ ] 4.2.3 생성자
  - [ ] BorderLayout 설정
  - [ ] initUI() 호출
- [ ] 4.2.4 initUI 메소드
  - [ ] 상단: 제목 + 날짜
  - [ ] 중앙: 내용 (JScrollPane)
  - [ ] 감정 표시 패널
  - [ ] 스트레스 표시
  - [ ] 하단: 버튼 패널
- [ ] 4.2.5 setDiary 메소드
  - [ ] currentDiary 저장
  - [ ] titleLabel.setText()
  - [ ] dateLabel.setText() (포맷: yyyy-MM-dd HH:mm)
  - [ ] contentArea.setText()
  - [ ] stressLabel.setText()
  - [ ] emotionDisplayPanel 업데이트
- [ ] 4.2.6 createEmotionDisplayPanel
  - [ ] FlowLayout
  - [ ] 각 EmotionModel을 작은 패널로 표시
  - [ ] 이모지 + 수치 표시
- [ ] 4.2.7 Getter 메소드들
  - [ ] getEditButton, getDeleteButton, getBackButton
  - [ ] getCurrentDiary

**완료 조건**: 선택한 일기의 상세 정보 표시

---

### ✅ Task 4.3: DiaryEditPanel 구현
**파일**: `src/main/java/com/diary/emotion/view/DiaryEditPanel.java`
**예상 시간**: 1.5시간

- [ ] 4.3.1 WriteView를 상속하거나 유사하게 구현
- [ ] 4.3.2 setDiary 메소드 추가
  - [ ] 기존 DiaryModel 데이터를 폼에 로드
  - [ ] 제목, 내용, 스트레스, 감정 모두 설정
- [ ] 4.3.3 getUpdatedDiary 메소드
  - [ ] 수정된 데이터를 DiaryModel로 반환
  - [ ] entry_id는 유지
- [ ] 4.3.4 저장 버튼을 "수정" 버튼으로 변경
- [ ] 4.3.5 취소 버튼 추가

**완료 조건**: 기존 일기를 수정 가능

---

### ✅ Task 4.4: ViewPanel 구현
**파일**: `src/main/java/com/diary/emotion/view/ViewPanel.java`
**예상 시간**: 2.5시간

- [ ] 4.4.1 클래스 선언 (extends JPanel)
- [ ] 4.4.2 멤버 변수
  - [ ] `private CardLayout cardLayout`
  - [ ] `private JPanel cardPanel`
  - [ ] `private JPanel filterPanel`
  - [ ] `private JTextField searchField`
  - [ ] `private JComboBox<String> sortCombo`
  - [ ] `private JButton searchButton, resetButton`
  - [ ] `private DiaryListPanel listPanel`
  - [ ] `private DiaryDetailPanel detailPanel`
  - [ ] `private DiaryEditPanel editPanel`
- [ ] 4.4.3 생성자
  - [ ] BorderLayout 설정
  - [ ] initUI() 호출
- [ ] 4.4.4 initUI 메소드
  - [ ] createFilterPanel() → NORTH
  - [ ] createCardPanel() → CENTER
- [ ] 4.4.5 createFilterPanel
  - [ ] searchField (제목 검색)
  - [ ] searchButton ("검색")
  - [ ] sortCombo ("날짜 내림차순", "날짜 오름차순", "제목")
  - [ ] resetButton ("전체 보기")
  - [ ] FlowLayout 배치
- [ ] 4.4.6 createCardPanel
  - [ ] CardLayout 생성
  - [ ] listPanel 생성 및 추가 ("LIST")
  - [ ] detailPanel 생성 및 추가 ("DETAIL")
  - [ ] editPanel 생성 및 추가 ("EDIT")
  - [ ] 기본 카드: "LIST"
- [ ] 4.4.7 Getter 메소드들
  - [ ] getSearchField, getSearchButton, getSortCombo, getResetButton
  - [ ] getListPanel, getDetailPanel, getEditPanel
- [ ] 4.4.8 화면 전환 메소드들
  - [ ] showListView()
  - [ ] showDetailView()
  - [ ] showEditView()
- [ ] 4.4.9 refreshDiaryList 메소드 (public)
  - [ ] Controller가 호출할 수 있도록

**완료 조건**: ViewPanel이 목록/상세/수정 화면을 CardLayout으로 전환

---

### ✅ Task 4.5: ViewController 구현
**파일**: `src/main/java/com/diary/emotion/view/ViewController.java`
**예상 시간**: 2.5시간

- [ ] 4.5.1 클래스 선언 및 멤버 변수
  - [ ] `private ViewPanel view`
  - [ ] `private DiaryDAO diaryDao`
  - [ ] `private EmotionDAO emotionDao`
  - [ ] `private List<DiaryModel> allDiaries`
  - [ ] `private List<DiaryModel> filteredDiaries`
- [ ] 4.5.2 생성자
  - [ ] View, DAO 저장
  - [ ] addListeners() 호출
  - [ ] loadAllDiaries() 호출
- [ ] 4.5.3 addListeners 메소드
  - [ ] searchButton 리스너
  - [ ] resetButton 리스너
  - [ ] sortCombo 리스너
  - [ ] listPanel 선택 리스너 (일기 선택 시 상세보기)
  - [ ] detailPanel.backButton 리스너
  - [ ] detailPanel.editButton 리스너
  - [ ] detailPanel.deleteButton 리스너
  - [ ] editPanel 저장/취소 버튼 리스너
- [ ] 4.5.4 loadAllDiaries 메소드
  - [ ] userId = SessionManager.getCurrentUserId()
  - [ ] allDiaries = diaryDao.getDiariesByUser(userId)
  - [ ] 각 DiaryModel에 대해 emotions 로드
    ```java
    for (DiaryModel diary : allDiaries) {
        List<EmotionModel> emotions = emotionDao.getEmotionsByEntryId(diary.getEntryId());
        diary.setEmotions(emotions);
    }
    ```
  - [ ] filteredDiaries = new ArrayList<>(allDiaries)
  - [ ] updateListView()
- [ ] 4.5.5 handleSearch 메소드
  - [ ] searchField에서 키워드 가져오기
  - [ ] allDiaries에서 제목에 키워드 포함된 것만 필터링
  - [ ] filteredDiaries 업데이트
  - [ ] updateListView()
- [ ] 4.5.6 handleReset 메소드
  - [ ] filteredDiaries = new ArrayList<>(allDiaries)
  - [ ] searchField.clear()
  - [ ] updateListView()
- [ ] 4.5.7 handleSort 메소드
  - [ ] sortCombo 선택값에 따라
  - [ ] filteredDiaries를 정렬
  - [ ] Collections.sort() 사용
  - [ ] updateListView()
- [ ] 4.5.8 handleDiarySelected 메소드
  - [ ] listPanel.getSelectedDiary()
  - [ ] detailPanel.setDiary()
  - [ ] view.showDetailView()
- [ ] 4.5.9 handleEdit 메소드
  - [ ] currentDiary 가져오기
  - [ ] editPanel.setDiary()
  - [ ] view.showEditView()
- [ ] 4.5.10 handleUpdate 메소드
  - [ ] editPanel.getUpdatedDiary()
  - [ ] validateDiary()
  - [ ] updateDiaryWithEmotions() (트랜잭션)
  - [ ] 성공 시: loadAllDiaries() + showListView()
- [ ] 4.5.11 handleDelete 메소드
  - [ ] 확인 다이얼로그
  - [ ] emotionDao.deleteEmotionsByEntryId()
  - [ ] diaryDao.deleteDiary()
  - [ ] 성공 시: loadAllDiaries() + showListView()
- [ ] 4.5.12 updateListView 메소드
  - [ ] view.getListPanel().setDiaries(filteredDiaries)
- [ ] 4.5.13 updateDiaryWithEmotions (트랜잭션)
  - [ ] diaryDao.updateDiary()
  - [ ] emotionDao.deleteEmotionsByEntryId()
  - [ ] emotionDao.createEmotions()

**완료 조건**: 일기 열람, 검색, 정렬, 수정, 삭제 모두 동작

---

### ✅ Task 4.6: MainApplication에 ViewPanel 통합
**파일**: `src/main/java/com/diary/emotion/MainApplication.java`
**예상 시간**: 30분

- [ ] 4.6.1 임시 viewPanel 제거
- [ ] 4.6.2 ViewPanel, ViewController 생성
  ```java
  ViewPanel viewPanel = new ViewPanel();
  DiaryDAO diaryDao = new DiaryDAO(); // 재사용 또는 새로 생성
  EmotionDAO emotionDao = new EmotionDAO();
  ViewController viewController = new ViewController(viewPanel, diaryDao, emotionDao);
  ```
- [ ] 4.6.3 mainCardPanel에 추가
  ```java
  mainCardPanel.add(viewPanel, "VIEW");
  ```
- [ ] 4.6.4 실행 및 테스트

**완료 조건**: 메인 화면에서 일기 열람 기능 사용 가능

---

## 📊 Phase 5: 통계 모듈 완성 (우선순위: 낮음)

### ✅ Task 5.1: StatisticsDAO 완성
**파일**: `src/main/java/share/StatisticsDAO.java` (기존 파일 수정)
**예상 시간**: 2.5시간

- [ ] 5.1.1 getEmotionData 메소드 실제 구현
  - [ ] SQL 쿼리 작성
    ```sql
    SELECT e.emoji_icon, e.emotion_level, d.entry_date
    FROM emotion e
    JOIN diary d ON e.entry_id = d.entry_id
    WHERE d.user_id = ? AND d.entry_date BETWEEN ? AND ?
    ORDER BY d.entry_date
    ```
  - [ ] ResultSet에서 데이터 읽기
  - [ ] 날짜별로 감정을 그룹화
  - [ ] Map<String, Map<String, Double>> 형식으로 변환
  - [ ] "긍정" / "부정" 카테고리 분류 로직
  - [ ] 반환
- [ ] 5.1.2 getStressData 메소드 실제 구현
  - [ ] SQL 쿼리
    ```sql
    SELECT entry_date, stress_level
    FROM diary
    WHERE user_id = ? AND entry_date BETWEEN ? AND ?
    ORDER BY entry_date
    ```
  - [ ] 날짜별로 스트레스 수치 집계
  - [ ] mode에 따라 X축 라벨 변환 (요일/날짜/월)
  - [ ] DefaultCategoryDataset 생성 및 반환
- [ ] 5.1.3 Mock 데이터 제거
- [ ] 5.1.4 에러 처리 강화
- [ ] 5.1.5 디버그 로그 추가

**완료 조건**: 실제 DB 데이터로 차트 표시

---

### ✅ Task 5.2: StatisticsController 개선
**파일**: `src/main/java/share/StatisticsController.java` (기존 파일 수정)
**예상 시간**: 30분

- [ ] 5.2.1 TEMP_USER_ID 제거
- [ ] 5.2.2 updateAllCharts에서 SessionManager 사용
  ```java
  String currentUserId = SessionManager.getInstance().getCurrentUserId();
  if (currentUserId == null) {
      showError("로그인이 필요합니다.");
      return;
  }
  ```
- [ ] 5.2.3 에러 메시지 표시 메소드 추가

**완료 조건**: SessionManager와 연동되어 로그인한 사용자의 통계 표시

---

### ✅ Task 5.3: 통계 모듈 통합 테스트
**예상 시간**: 1시간

- [ ] 5.3.1 실제 일기 데이터 생성 (테스트용)
  - [ ] 여러 날짜에 걸쳐 일기 작성
  - [ ] 다양한 감정 및 스트레스 수치
- [ ] 5.3.2 통계 탭에서 차트 확인
  - [ ] 주간 차트
  - [ ] 월간 차트
  - [ ] 연간 차트
- [ ] 5.3.3 평균 스트레스 수치 확인
- [ ] 5.3.4 버그 수정

**완료 조건**: 모든 통계 차트가 실제 데이터로 정상 표시

---

## 🧪 Phase 6: 통합 및 테스트 (우선순위: 높음)

### ✅ Task 6.1: 전체 플로우 테스트
**예상 시간**: 2시간

- [ ] 6.1.1 회원가입 → 로그인 플로우
  - [ ] 회원가입 성공
  - [ ] 중복 아이디 테스트
  - [ ] 로그인 성공/실패
- [ ] 6.1.2 일기 작성 플로우
  - [ ] 일기 저장
  - [ ] 감정 추가/삭제
  - [ ] 입력 검증 테스트
- [ ] 6.1.3 일기 열람 플로우
  - [ ] 목록 조회
  - [ ] 상세 보기
  - [ ] 검색 기능
  - [ ] 정렬 기능
- [ ] 6.1.4 일기 수정/삭제 플로우
  - [ ] 수정 후 저장
  - [ ] 삭제 확인
- [ ] 6.1.5 통계 플로우
  - [ ] 주간/월간/연간 전환
  - [ ] 차트 데이터 확인

**완료 조건**: 모든 기능이 끊김 없이 연결되어 동작

---

### ✅ Task 6.2: 예외 상황 테스트
**예상 시간**: 1.5시간

- [ ] 6.2.1 DB 연결 실패 시나리오
  - [ ] MySQL 서버 중지 후 앱 실행
  - [ ] 적절한 오류 메시지 표시 확인
- [ ] 6.2.2 세션 만료 시나리오
  - [ ] 로그아웃 후 기능 접근
- [ ] 6.2.3 잘못된 입력 테스트
  - [ ] 빈 제목, 긴 제목
  - [ ] 특수문자 입력
  - [ ] SQL Injection 시도
- [ ] 6.2.4 동시성 테스트 (선택사항)
  - [ ] 여러 창에서 동시 작업

**완료 조건**: 모든 예외 상황에서 앱이 크래시하지 않음

---

### ✅ Task 6.3: UI/UX 개선
**예상 시간**: 2시간

- [ ] 6.3.1 일관된 색상/폰트 적용
  - [ ] 모든 패널에 Constants 사용
  - [ ] 통일된 디자인 언어
- [ ] 6.3.2 여백 및 간격 조정
  - [ ] 적절한 padding/margin
  - [ ] 컴포넌트 간 간격 통일
- [ ] 6.3.3 반응형 레이아웃
  - [ ] 창 크기 변경 시 레이아웃 유지
- [ ] 6.3.4 로딩 인디케이터 추가
  - [ ] DB 작업 시 ProgressBar 표시
  - [ ] SwingWorker 사용
- [ ] 6.3.5 키보드 단축키
  - [ ] Enter 키로 로그인
  - [ ] Ctrl+S로 저장 (선택사항)
- [ ] 6.3.6 툴팁 추가
  - [ ] 버튼 hover 시 설명 표시

**완료 조건**: 사용자 경험이 직관적이고 편안함

---

### ✅ Task 6.4: 코드 리팩토링
**예상 시간**: 2시간

- [ ] 6.4.1 중복 코드 제거
  - [ ] 공통 메소드 추출
  - [ ] 유틸리티 클래스 활용
- [ ] 6.4.2 네이밍 통일
  - [ ] 변수/메소드명 일관성 확인
  - [ ] 오타 수정
- [ ] 6.4.3 주석 정리
  - [ ] JavaDoc 추가
  - [ ] 불필요한 주석 제거
- [ ] 6.4.4 패키지 구조 정리
  - [ ] 파일 위치 재확인
  - [ ] import 정리
- [ ] 6.4.5 성능 최적화
  - [ ] 불필요한 객체 생성 제거
  - [ ] 효율적인 자료구조 사용

**완료 조건**: 코드가 깔끔하고 유지보수 가능

---

### ✅ Task 6.5: 문서화 및 README 작성
**예상 시간**: 1.5시간

- [ ] 6.5.1 README.md 업데이트
  - [ ] 프로젝트 소개
  - [ ] 설치 방법
  - [ ] 실행 방법
  - [ ] 주요 기능 스크린샷
  - [ ] 기술 스택
  - [ ] 라이선스 (선택사항)
- [ ] 6.5.2 JavaDoc 생성
  - [ ] `mvn javadoc:javadoc` 실행
  - [ ] 생성된 문서 확인
- [ ] 6.5.3 사용자 매뉴얼 작성 (선택사항)
  - [ ] docs/USER_MANUAL.md
  - [ ] 각 기능 사용법 설명
- [ ] 6.5.4 개발자 가이드 업데이트
  - [ ] docs/DEVELOPER_GUIDE.md
  - [ ] 프로젝트 구조 설명
  - [ ] 빌드 및 배포 방법

**완료 조건**: 프로젝트가 완전히 문서화됨

---

## 🎯 선택적 개선 사항 (여유가 있다면)

### 💡 추가 기능
- [ ] 비밀번호 해싱 (BCrypt)
- [ ] 프로필 사진 업로드
- [ ] 일기 백업/복원 기능
- [ ] 테마 변경 (다크 모드)
- [ ] 일기 내보내기 (PDF, TXT)
- [ ] 알림 기능 (일기 작성 리마인더)
- [ ] 감정 패턴 분석 리포트

### 🔧 기술적 개선
- [ ] Connection Pool (HikariCP)
- [ ] Logger 프레임워크 (SLF4J + Logback)
- [ ] 단위 테스트 (JUnit)
- [ ] 비밀번호 찾기 기능
- [ ] 설정 파일 외부화 (properties)

---

## 📝 진행 상황 체크리스트

### 현재 완료된 작업
- [x] 데이터베이스 스키마 설계
- [x] DatabaseUtil.createDatabase()
- [x] MainApplication 프레임워크
- [x] StatisticsView UI
- [x] StatisticsController (임시 userId)
- [x] StatisticsDAO (평균 스트레스만 구현)

### 다음 우선순위 작업
1. **Phase 1: 공통 모듈** - 모든 모듈의 기반
2. **Phase 2: 인증 모듈** - 사용자 구분 필수
3. **Phase 3: 일기 작성** - 핵심 기능
4. **Phase 4: 일기 열람** - 핵심 기능
5. **Phase 5: 통계 완성** - 부가 기능
6. **Phase 6: 통합 테스트** - 품질 보증

---

## 🎓 개발 팁

### 개발 순서
1. Model → DAO → Controller → View 순서로 개발 (Bottom-up)
2. 또는 View → Controller → DAO 순서로 프로토타입 개발 (Top-down)
3. 한 기능씩 완전히 완성 후 다음 기능으로 이동

### 디버깅 전략
- 각 단계마다 콘솔 출력으로 확인
- DB에 실제로 저장되었는지 MySQL Workbench로 확인
- 작은 단위로 테스트 (메소드 단위)

### Git 사용 권장
```bash
git init
git add .
git commit -m "Initial commit"

# 각 Phase 완료 시마다 커밋
git commit -m "Phase 1: 공통 모듈 완성"
```

---

**총 예상 개발 시간**: 약 40-50시간
**권장 개발 기간**: 2-3주 (하루 2-3시간 작업 시)

