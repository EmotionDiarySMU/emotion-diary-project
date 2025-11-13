# 개발 가이드

## 1. 개발 환경 설정

### 1.1 필수 요구사항
- **JDK**: 17 이상
- **Maven**: 3.6 이상
- **MySQL**: 8.0 이상
- **IDE**: IntelliJ IDEA 또는 Eclipse

### 1.2 MySQL 설정
```sql
-- MySQL 서버 실행 후
-- 데이터베이스는 자동 생성됨 (DatabaseUtil.createDatabase())
-- 초기 실행 시 root 계정 비밀번호 확인 필요
```

### 1.3 프로젝트 빌드
```bash
# Maven 의존성 설치
mvn clean install

# 프로젝트 실행
mvn exec:java -Dexec.mainClass="com.diary.emotion.AppLauncher"
```

## 2. 코딩 컨벤션

### 2.1 네이밍 규칙
- **클래스명**: PascalCase (예: `UserModel`, `DiaryDAO`)
- **메소드명**: camelCase (예: `getUserById`, `createDiary`)
- **변수명**: camelCase (예: `userId`, `entryDate`)
- **상수명**: UPPER_SNAKE_CASE (예: `PASTEL_BLUE`, `MAX_EMOTIONS`)
- **패키지명**: 소문자, 점으로 구분 (예: `com.diary.emotion.auth`)

### 2.2 주석 규칙
```java
/**
 * 클래스/메소드 설명 (JavaDoc)
 * @param paramName 파라미터 설명
 * @return 반환값 설명
 */
public ReturnType methodName(ParamType paramName) {
    // 구현 로직 설명
    // TODO: 미구현 기능 표시
}
```

### 2.3 코드 스타일
- **들여쓰기**: 4 스페이스
- **중괄호**: K&R 스타일 (같은 줄에 여는 중괄호)
- **한 줄 길이**: 최대 120자
- **import**: 와일드카드(*) 최소화, 사용하지 않는 import 제거

### 2.4 MVC 패턴 준수
- **View**: UI 로직만, 비즈니스 로직 금지
- **Controller**: View와 Model 중재, 비즈니스 로직
- **Model/DAO**: 데이터 접근만, UI 참조 금지

## 3. 패키지 구조 및 역할

### 3.1 com.diary.emotion (메인 패키지)
```
com.diary.emotion/
├── AppLauncher.java          # 애플리케이션 진입점
├── MainApplication.java      # 메인 화면 (CardLayout 관리)
├── model/                    # 데이터 모델
│   ├── UserModel.java
│   ├── DiaryModel.java
│   └── EmotionModel.java
├── auth/                     # 인증 모듈
│   ├── AuthView.java
│   ├── AuthController.java
│   └── UserDAO.java
├── write/                    # 일기 작성 모듈
│   ├── WriteView.java
│   ├── EmotionSelectorPanel.java
│   ├── EmotionInputPanel.java
│   ├── WriteController.java
│   ├── DiaryDAO.java
│   └── EmotionDAO.java
├── view/                     # 일기 열람 모듈
│   ├── ViewPanel.java
│   ├── DiaryListPanel.java
│   ├── DiaryDetailPanel.java
│   ├── DiaryEditPanel.java
│   └── ViewController.java
└── statistics/               # 통계 모듈
    ├── StatisticsView.java
    ├── StatisticsController.java
    └── StatisticsDAO.java
```

### 3.2 share (공통 패키지)
```
share/
├── DatabaseUtil.java         # DB 연결 유틸리티
├── SessionManager.java       # 세션 관리 (싱글톤)
└── Constants.java            # 상수 정의
```

## 4. 개발 순서 및 우선순위

### 4.1 Phase 1: 공통 모듈 (우선순위: 높음)
1. **Constants 클래스 작성**
   - 색상, 폰트, 크기 상수 정의
   - 감정 리스트 정의

2. **SessionManager 클래스 작성**
   - 싱글톤 패턴 구현
   - 로그인/로그아웃 메소드

3. **DatabaseUtil 개선**
   - `getConnection()` 메소드 추가
   - 리소스 정리 메소드 추가

4. **Model 클래스 작성**
   - UserModel, DiaryModel, EmotionModel
   - Getter/Setter, toString() 구현

### 4.2 Phase 2: 인증 모듈 (우선순위: 높음)
1. **UserDAO 구현**
   - createUser, authenticateUser, userExists

2. **AuthView 구현**
   - 로그인 패널, 회원가입 패널
   - CardLayout으로 전환

3. **AuthController 구현**
   - 입력 검증, 로그인/회원가입 로직
   - SessionManager 연동

4. **AppLauncher 수정**
   - 로그인 성공 시 MainApplication 표시
   - 실패 시 AuthView 유지

### 4.3 Phase 3: 일기 작성 모듈 (우선순위: 중간)
1. **EmotionInputPanel 구현**
   - 감정 선택 콤보박스
   - 수치 입력 슬라이더
   - 삭제 버튼

2. **EmotionSelectorPanel 구현**
   - 동적 EmotionInputPanel 추가/삭제
   - 최대 4개 제한

3. **WriteView 구현**
   - 제목, 내용, 스트레스 입력
   - EmotionSelectorPanel 통합
   - 저장/초기화 버튼

4. **DiaryDAO & EmotionDAO 구현**
   - CRUD 메소드 구현
   - 트랜잭션 처리

5. **WriteController 구현**
   - 입력 검증
   - DAO 호출 및 저장

6. **MainApplication 통합**
   - writePanel을 WriteView로 교체

### 4.4 Phase 4: 일기 열람 모듈 (우선순위: 중간)
1. **DiaryListPanel 구현**
   - JList로 일기 목록 표시
   - 커스텀 렌더러 (날짜, 제목, 감정 아이콘)

2. **DiaryDetailPanel 구현**
   - 일기 상세 정보 표시
   - 수정/삭제/뒤로가기 버튼

3. **DiaryEditPanel 구현**
   - WriteView와 유사하지만 기존 데이터 로드
   - 업데이트 로직

4. **ViewPanel 구현**
   - 검색, 정렬 필터
   - CardLayout으로 목록/상세/수정 전환

5. **ViewController 구현**
   - 목록 로드, 검색, 정렬, 삭제
   - DiaryDAO, EmotionDAO 연동

6. **MainApplication 통합**
   - viewPanel을 ViewPanel로 교체

### 4.5 Phase 5: 통계 모듈 완성 (우선순위: 낮음)
1. **StatisticsDAO 완성**
   - getEmotionData 실제 쿼리 구현
   - getStressData 실제 쿼리 구현

2. **StatisticsController 개선**
   - SessionManager 연동 (임시 userId 제거)

3. **통합 테스트**
   - 실제 데이터로 차트 표시 확인

## 5. 데이터베이스 연동 가이드

### 5.1 Connection 사용 패턴
```java
// 권장 패턴: try-with-resources
public List<DiaryModel> getDiaries(String userId) {
    List<DiaryModel> diaries = new ArrayList<>();
    String sql = "SELECT * FROM diary WHERE user_id = ?";
    
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, userId);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                DiaryModel diary = new DiaryModel();
                diary.setEntryId(rs.getInt("entry_id"));
                diary.setTitle(rs.getString("title"));
                // ...
                diaries.add(diary);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // 사용자에게 오류 메시지 표시
    }
    
    return diaries;
}
```

### 5.2 트랜잭션 처리
```java
// 일기 + 감정 저장 (원자성 보장)
public boolean saveDiaryWithEmotions(DiaryModel diary) {
    Connection conn = null;
    try {
        conn = DatabaseUtil.getConnection();
        conn.setAutoCommit(false); // 트랜잭션 시작
        
        // 1. 일기 저장
        int entryId = insertDiary(conn, diary);
        
        // 2. 감정 저장
        insertEmotions(conn, entryId, diary.getEmotions());
        
        conn.commit(); // 커밋
        return true;
        
    } catch (SQLException e) {
        if (conn != null) {
            try {
                conn.rollback(); // 롤백
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        e.printStackTrace();
        return false;
        
    } finally {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
```

### 5.3 날짜 처리
```java
// LocalDateTime <-> java.sql.Timestamp 변환
LocalDateTime now = LocalDateTime.now();
Timestamp timestamp = Timestamp.valueOf(now);

// DB에서 읽기
Timestamp ts = rs.getTimestamp("entry_date");
LocalDateTime dateTime = ts.toLocalDateTime();
```

## 6. UI 개발 가이드

### 6.1 공통 스타일 적용
```java
// Constants에서 가져오기
import static share.Constants.*;

JPanel panel = new JPanel();
panel.setBackground(PASTEL_BLUE);

JLabel label = new JLabel("제목");
label.setFont(TITLE_FONT);
```

### 6.2 슬라이더 + 라벨 동기화
```java
JSlider slider = new JSlider(0, 100, 50);
JLabel valueLabel = new JLabel("50");

slider.addChangeListener(e -> {
    int value = slider.getValue();
    valueLabel.setText(String.valueOf(value));
});
```

### 6.3 확인 다이얼로그
```java
int result = JOptionPane.showConfirmDialog(
    this,
    "정말 삭제하시겠습니까?",
    "삭제 확인",
    JOptionPane.YES_NO_OPTION,
    JOptionPane.WARNING_MESSAGE
);

if (result == JOptionPane.YES_OPTION) {
    // 삭제 실행
}
```

### 6.4 알림 다이얼로그
```java
// 성공
JOptionPane.showMessageDialog(
    this,
    "일기가 저장되었습니다.",
    "저장 완료",
    JOptionPane.INFORMATION_MESSAGE
);

// 오류
JOptionPane.showMessageDialog(
    this,
    "저장에 실패했습니다.",
    "오류",
    JOptionPane.ERROR_MESSAGE
);
```

## 7. 테스트 가이드

### 7.1 단위 테스트
- 각 DAO 메소드별 테스트 케이스 작성
- 정상 케이스 + 예외 케이스 모두 테스트

### 7.2 통합 테스트
- 전체 플로우 테스트 (로그인 → 작성 → 조회 → 수정 → 삭제)
- 실제 DB 사용

### 7.3 UI 테스트
- 모든 버튼 클릭 테스트
- 잘못된 입력 시나리오 테스트
- 화면 전환 테스트

## 8. 디버깅 팁

### 8.1 로그 출력
```java
// 개발 중에는 System.out.println 사용
System.out.println("[DEBUG] userId: " + userId);
System.out.println("[DEBUG] diary saved: " + entryId);

// 추후 Logger로 교체 가능
```

### 8.2 SQL 쿼리 확인
```java
// PreparedStatement 실행 전 쿼리 출력
System.out.println("[SQL] " + pstmt.toString());
```

### 8.3 예외 스택 추적
```java
catch (SQLException e) {
    e.printStackTrace(); // 전체 스택 출력
    System.err.println("Error: " + e.getMessage());
}
```

## 9. 주의사항

### 9.1 리소스 관리
- **반드시** try-with-resources 사용
- Connection, Statement, ResultSet 누수 방지

### 9.2 NULL 체크
```java
// 사용자 입력은 항상 NULL 체크
if (title == null || title.trim().isEmpty()) {
    showError("제목을 입력해주세요.");
    return;
}
```

### 9.3 SQL Injection 방지
```java
// ❌ 나쁜 예
String sql = "SELECT * FROM user WHERE user_id = '" + userId + "'";

// ✅ 좋은 예
String sql = "SELECT * FROM user WHERE user_id = ?";
pstmt.setString(1, userId);
```

### 9.4 UI 스레드 안전성
```java
// DB 작업은 백그라운드 스레드에서
SwingWorker<List<DiaryModel>, Void> worker = new SwingWorker<>() {
    @Override
    protected List<DiaryModel> doInBackground() {
        return dao.getDiaries(userId);
    }
    
    @Override
    protected void done() {
        try {
            List<DiaryModel> diaries = get();
            updateUI(diaries); // UI 업데이트는 EDT에서
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
};
worker.execute();
```

## 10. 최적화 가이드

### 10.1 Connection Pool (선택사항)
- 현재는 단일 사용자이므로 불필요
- 추후 다중 사용자 지원 시 HikariCP 고려

### 10.2 쿼리 최적화
- 필요한 컬럼만 SELECT
- 인덱스 활용 (user_id, entry_date)
- LIMIT 사용으로 불필요한 데이터 로드 방지

### 10.3 UI 반응성
- 긴 작업은 SwingWorker 사용
- 진행 표시기 표시 (ProgressBar)

