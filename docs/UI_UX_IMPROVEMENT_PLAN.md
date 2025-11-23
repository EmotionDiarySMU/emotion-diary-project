# 🎨 Emotion Diary - UI/UX 개선 계획서

> **작성일**: 2025년 11월 23일  
> **목적**: 기존 기능을 유지하면서 사용자 경험을 극대화하는 디자인 개선

---

## 📋 목차

1. [현재 상태 분석](#1-현재-상태-분석)
2. [디자인 철학](#2-디자인-철학)
3. [색상 팔레트 개선](#3-색상-팔레트-개선)
4. [컴포넌트별 개선 계획](#4-컴포넌트별-개선-계획)
5. [폰트 및 타이포그래피](#5-폰트-및-타이포그래피)
6. [애니메이션 및 전환 효과](#6-애니메이션-및-전환-효과)
7. [반응형 레이아웃](#7-반응형-레이아웃)
8. [접근성 개선](#8-접근성-개선)
9. [구현 우선순위](#9-구현-우선순위)

---

## 1. 현재 상태 분석

### ✅ 잘 작동하는 기능들
- ✓ 로그인/회원가입 시스템
- ✓ 일기 작성/수정/삭제 (CRUD)
- ✓ 감정 이모지 선택 (12개 이모지 지원)
- ✓ 스트레스 레벨 측정 (슬라이더)
- ✓ 일기 검색 (제목, 날짜 기반)
- ✓ 통계 시각화 (주간/월간/연간)
- ✓ 실시간 데이터베이스 동기화

### 🎨 개선 필요 영역
- 색상 일관성 및 현대적인 디자인
- 버튼 및 입력 필드의 시각적 피드백
- 공간 활용 및 여백 최적화
- 아이콘 및 이모지 표현 향상
- 차트 및 그래프 디자인 개선

---

## 2. 디자인 철학

### 🌟 핵심 원칙

#### 2.1 감정 중심 디자인 (Emotion-First Design)
- **부드러운 곡선**: 날카로운 모서리 대신 둥근 모서리(border-radius) 사용
- **편안한 색상**: 파스텔 톤으로 심리적 안정감 제공
- **감정 시각화**: 이모지와 색상을 통해 감정 상태 직관적 표현

#### 2.2 미니멀리즘 (Minimalism)
- **여백의 미**: 충분한 패딩과 마진으로 숨 쉴 수 있는 공간
- **명확한 계층**: 정보의 우선순위를 시각적으로 구분
- **불필요한 요소 제거**: 꼭 필요한 UI 요소만 유지

#### 2.3 일관성 (Consistency)
- **통일된 컬러 시스템**: 모든 화면에서 동일한 색상 규칙 적용
- **일관된 컴포넌트**: 버튼, 입력 필드 등 동일한 스타일 유지
- **예측 가능한 인터랙션**: 사용자가 기대하는 방식으로 작동

---

## 3. 색상 팔레트 개선

### 🎨 새로운 색상 시스템

#### 3.1 Primary Colors (주요 색상)
```java
// 메인 브랜드 색상 - 따뜻하고 편안한 느낌
public static final Color PRIMARY_SOFT_CORAL = new Color(255, 183, 178);    // #FFB7B2
public static final Color PRIMARY_PEACH = new Color(255, 214, 186);         // #FFD6BA
public static final Color PRIMARY_LAVENDER = new Color(221, 212, 255);      // #DDD4FF

// 배경 색상 - 부드러운 파스텔
public static final Color BG_CREAM = new Color(255, 253, 247);              // #FFFDF7
public static final Color BG_LIGHT_BLUE = new Color(240, 247, 255);         // #F0F7FF
public static final Color BG_MINT = new Color(240, 255, 244);               // #F0FFF4
```

#### 3.2 Emotion Colors (감정별 색상)
```java
// 긍정 감정 (따뜻한 톤)
public static final Color EMOTION_JOY = new Color(255, 235, 156);           // 기쁨 - 노란색
public static final Color EMOTION_LOVE = new Color(255, 204, 229);          // 사랑 - 분홍색
public static final Color EMOTION_PEACE = new Color(204, 242, 255);         // 평온 - 하늘색

// 부정 감정 (차가운 톤)
public static final Color EMOTION_SAD = new Color(189, 207, 255);           // 슬픔 - 파란색
public static final Color EMOTION_ANGER = new Color(255, 171, 145);         // 분노 - 주황색
public static final Color EMOTION_ANXIETY = new Color(207, 207, 207);       // 불안 - 회색
```

#### 3.3 UI Element Colors
```java
// 버튼 색상
public static final Color BUTTON_PRIMARY = new Color(156, 163, 255);        // #9CA3FF
public static final Color BUTTON_PRIMARY_HOVER = new Color(138, 146, 255);  // #8A92FF
public static final Color BUTTON_SECONDARY = new Color(229, 229, 229);      // #E5E5E5
public static final Color BUTTON_DANGER = new Color(255, 153, 153);         // #FF9999

// 텍스트 색상
public static final Color TEXT_PRIMARY = new Color(51, 51, 51);             // #333333
public static final Color TEXT_SECONDARY = new Color(119, 119, 119);        // #777777
public static final Color TEXT_DISABLED = new Color(189, 189, 189);         // #BDBDBD

// 경계선 색상
public static final Color BORDER_LIGHT = new Color(230, 230, 230);          // #E6E6E6
public static final Color BORDER_MEDIUM = new Color(204, 204, 204);         // #CCCCCC
public static final Color BORDER_FOCUS = new Color(156, 163, 255);          // #9CA3FF
```

#### 3.4 Chart Colors (차트 색상)
```java
// 스트레스 레벨 그라데이션
public static final Color STRESS_LOW = new Color(144, 238, 144);            // 낮음 - 연두색
public static final Color STRESS_MEDIUM = new Color(255, 218, 121);         // 보통 - 노란색
public static final Color STRESS_HIGH = new Color(255, 145, 145);           // 높음 - 빨간색
```

### 🌈 화면별 색상 매핑

| 화면 | 배경색 | 강조색 |
|------|--------|--------|
| 로그인/회원가입 | `BG_CREAM` | `PRIMARY_LAVENDER` |
| 일기 쓰기 | `BG_LIGHT_BLUE` | `PRIMARY_SOFT_CORAL` |
| 일기 열람 | `BG_MINT` | `PRIMARY_PEACH` |
| 통계 | `BG_LIGHT_BLUE` | `BUTTON_PRIMARY` |

---

## 4. 컴포넌트별 개선 계획

### 4.1 로그인/회원가입 화면 (AuthenticationFrame)

#### 현재 문제점
- 기본 Swing 스타일로 다소 구식
- 입력 필드와 버튼의 시각적 구분 부족
- 여백 및 간격 최적화 필요

#### 개선 사항
```java
// ✨ 1. 카드형 레이아웃
- 로그인/회원가입 패널을 카드처럼 중앙에 배치
- 그림자 효과로 입체감 부여
- 둥근 모서리 (border-radius: 20px)

// ✨ 2. 입력 필드 개선
- 아이콘 추가 (👤 사용자, 🔒 비밀번호)
- 포커스 시 테두리 색상 변경 (BORDER_FOCUS)
- 플레이스홀더 텍스트 개선

// ✨ 3. 버튼 스타일링
- 그라데이션 배경
- 호버 시 색상 변경 및 약간의 확대 효과
- 클릭 시 눌리는 애니메이션

// ✨ 4. 환영 메시지
- 로고나 일러스트 추가
- "당신의 감정을 기록하세요" 같은 캐치프레이즈
```

#### 코드 예시
```java
// 입력 필드 스타일링
JTextField userField = new JTextField(20);
userField.setBorder(BorderFactory.createCompoundBorder(
    BorderFactory.createLineBorder(BORDER_LIGHT, 2, true), // 둥근 테두리
    BorderFactory.createEmptyBorder(12, 15, 12, 15)        // 내부 패딩
));
userField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
userField.setBackground(Color.WHITE);

// 포커스 리스너로 테두리 색상 변경
userField.addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
        userField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_FOCUS, 2, true),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
    }
    
    @Override
    public void focusLost(FocusEvent e) {
        userField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_LIGHT, 2, true),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
    }
});

// 버튼 스타일링
JButton loginBtn = new JButton("로그인");
loginBtn.setBackground(BUTTON_PRIMARY);
loginBtn.setForeground(Color.WHITE);
loginBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
loginBtn.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
loginBtn.setFocusPainted(false);
loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

// 호버 효과
loginBtn.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseEntered(MouseEvent e) {
        loginBtn.setBackground(BUTTON_PRIMARY_HOVER);
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        loginBtn.setBackground(BUTTON_PRIMARY);
    }
});
```

---

### 4.2 메인 뷰 (MainView) - 탭 네비게이션

#### 현재 문제점
- 기본 메뉴바 스타일이 단조로움
- 현재 선택된 탭의 시각적 표시 부족

#### 개선 사항
```java
// ✨ 1. 탭 버튼 디자인
- 아이콘 추가 (✍️ 쓰기, 📖 열람, 📊 통계)
- 선택된 탭은 밑줄 또는 배경색으로 강조
- 호버 시 부드러운 색상 전환

// ✨ 2. 상단 헤더 영역
- 프로그램 제목 "Emotion Diary" 중앙 배치
- 로그인된 사용자 이름 표시
- 로그아웃 버튼 우측 상단 배치
```

#### 코드 예시
```java
// 커스텀 탭 버튼
class CustomTabButton extends JButton {
    private boolean isSelected = false;
    
    public CustomTabButton(String text, String icon) {
        super(icon + " " + text);
        setFont(new Font("맑은 고딕", Font.BOLD, 14));
        setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        setBackground(Color.WHITE);
        setForeground(TEXT_SECONDARY);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isSelected) {
                    setBackground(new Color(245, 245, 245));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!isSelected) {
                    setBackground(Color.WHITE);
                }
            }
        });
    }
    
    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected) {
            setBackground(PRIMARY_LAVENDER);
            setForeground(TEXT_PRIMARY);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 30, 12, 30),
                BorderFactory.createMatteBorder(0, 0, 3, 0, BUTTON_PRIMARY) // 밑줄
            ));
        } else {
            setBackground(Color.WHITE);
            setForeground(TEXT_SECONDARY);
            setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        }
    }
}
```

---

### 4.3 일기 작성 화면 (WriteDiaryGUI)

#### 현재 문제점
- 텍스트 영역이 다소 답답함
- 이모지 선택 UI가 작고 클릭하기 어려움
- 스트레스 슬라이더가 시각적으로 단조로움

#### 개선 사항
```java
// ✨ 1. 제목 입력 필드
- 더 큰 폰트 사이즈 (18px)
- 플레이스홀더: "오늘의 제목을 입력하세요..."
- 하단 구분선 (밑줄 스타일)

// ✨ 2. 내용 입력 영역
- 충분한 패딩 (20px)
- 라인 하이라이트 (현재 줄 배경색 변경)
- 글자 수 카운터 (예: 1,234 / 5,000 자)

// ✨ 3. 이모지 선택 영역
- 카드형 레이아웃 (4개 슬롯)
- 각 슬롯에 레이블 추가 ("감정 1", "감정 2"...)
- 선택된 이모지는 크게 표시 (48px)
- 호버 시 확대 효과

// ✨ 4. 스트레스 슬라이더
- 색상 그라데이션 (녹색 → 노란색 → 빨간색)
- 숫자 표시 위치 개선
- 범위별 아이콘 표시 (😌 → 😐 → 😰)
```

#### 코드 예시
```java
// 제목 입력 필드 - 밑줄 스타일
JTextField titleField = new JTextField();
titleField.setFont(new Font("맑은 고딕", Font.BOLD, 18));
titleField.setBorder(BorderFactory.createCompoundBorder(
    BorderFactory.createEmptyBorder(10, 5, 10, 5),
    BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_MEDIUM)
));
titleField.setBackground(BG_LIGHT_BLUE);

// 내용 입력 영역 - 패딩 개선
JTextArea contentArea = new JTextArea(15, 40);
contentArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
contentArea.setLineWrap(true);
contentArea.setWrapStyleWord(true);
contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
contentArea.setBackground(Color.WHITE);

// 글자 수 카운터
JLabel charCountLabel = new JLabel("0 / 5,000 자");
charCountLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
charCountLabel.setForeground(TEXT_SECONDARY);

contentArea.getDocument().addDocumentListener(new DocumentListener() {
    private void updateCount() {
        int count = contentArea.getText().length();
        charCountLabel.setText(String.format("%,d / 5,000 자", count));
        
        if (count > 4500) {
            charCountLabel.setForeground(BUTTON_DANGER);
        } else {
            charCountLabel.setForeground(TEXT_SECONDARY);
        }
    }
    
    public void insertUpdate(DocumentEvent e) { updateCount(); }
    public void removeUpdate(DocumentEvent e) { updateCount(); }
    public void changedUpdate(DocumentEvent e) { updateCount(); }
});

// 커스텀 슬라이더 UI
class GradientSliderUI extends BasicSliderUI {
    public GradientSliderUI(JSlider slider) {
        super(slider);
    }
    
    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        Rectangle trackBounds = trackRect;
        
        // 그라데이션 생성 (녹색 → 노란색 → 빨간색)
        GradientPaint gradient = new GradientPaint(
            trackBounds.x, 0, STRESS_LOW,
            trackBounds.x + trackBounds.width, 0, STRESS_HIGH
        );
        
        g2d.setPaint(gradient);
        g2d.fillRoundRect(trackBounds.x, trackBounds.y + 5, 
                          trackBounds.width, 10, 10, 10);
    }
}

JSlider stressSlider = new JSlider(0, 100, 50);
stressSlider.setUI(new GradientSliderUI(stressSlider));
```

---

### 4.4 이모지 선택 다이얼로그 (SingleIconChooserDialog)

#### 현재 문제점
- 이모지가 작아서 선택하기 어려움
- 이미 선택된 이모지 표시가 불명확

#### 개선 사항
```java
// ✨ 1. 이모지 크기 증가
- 36px → 48px로 확대
- 버튼 크기도 비례해서 증가

// ✨ 2. 선택 상태 시각화
- 선택된 이모지: 파란색 테두리 + 배경색
- 비활성 이모지: 회색 처리 + 반투명
- 호버 시: 약간 확대 + 그림자

// ✨ 3. 카테고리 구분
- 긍정 감정 / 부정 감정으로 그룹핑
- 구분선 또는 라벨로 표시

// ✨ 4. 애니메이션
- 다이얼로그 열릴 때 페이드인 효과
- 이모지 클릭 시 살짝 튀는 애니메이션
```

#### 코드 예시
```java
// 이모지 버튼 생성
for (int i = 0; i < allIcons.length; i++) {
    JButton iconButton = new JButton(allIcons[i]);
    iconButton.setFont(new Font("Apple Color Emoji", Font.PLAIN, 48));
    iconButton.setPreferredSize(new Dimension(80, 80));
    iconButton.setBackground(Color.WHITE);
    iconButton.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_LIGHT, 2, true),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));
    iconButton.setFocusPainted(false);
    iconButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    final int index = i;
    
    // 호버 효과
    iconButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (iconButton.isEnabled()) {
                iconButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_FOCUS, 3, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                iconButton.setBackground(new Color(240, 247, 255));
            }
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            if (iconButton.isEnabled() && selectedIndex != index) {
                iconButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_LIGHT, 2, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                iconButton.setBackground(Color.WHITE);
            }
        }
    });
    
    iconButtons[i] = iconButton;
}
```

---

### 4.5 일기 검색 화면 (SearchDiaryPanel)

#### 현재 문제점
- 검색 필터가 복잡해 보임
- 일기 목록이 단순한 텍스트로만 표시

#### 개선 사항
```java
// ✨ 1. 검색 바 디자인
- 통합 검색 바 (제목 + 날짜)
- 검색 아이콘 (🔍) 추가
- 검색 버튼 크기 및 색상 개선

// ✨ 2. 날짜 선택기
- 캘린더 UI로 개선
- 범위 선택 시 시각적 표시

// ✨ 3. 일기 목록 카드화
- 각 일기를 카드 형태로 표시
- 썸네일: 이모지 미리보기
- 제목 + 날짜 + 스트레스 레벨 표시
- 호버 시 약간 떠오르는 효과 (elevation)

// ✨ 4. 빈 상태 처리
- 검색 결과 없을 때 친근한 메시지
- "아직 일기가 없어요. 첫 일기를 작성해보세요!" 같은 안내
```

#### 코드 예시
```java
// 일기 카드 렌더러
class DiaryCardRenderer extends JPanel implements ListCellRenderer<String> {
    private JLabel titleLabel;
    private JLabel dateLabel;
    private JLabel emojiLabel;
    private JPanel stressBar;
    
    public DiaryCardRenderer() {
        setLayout(new BorderLayout(10, 5));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_LIGHT, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // 이모지 영역
        emojiLabel = new JLabel();
        emojiLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 36));
        emojiLabel.setPreferredSize(new Dimension(60, 60));
        
        // 텍스트 영역
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        titleLabel = new JLabel();
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        
        dateLabel = new JLabel();
        dateLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        dateLabel.setForeground(TEXT_SECONDARY);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(dateLabel);
        
        // 스트레스 바
        stressBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                     RenderingHints.VALUE_ANTIALIAS_ON);
                // 스트레스 레벨에 따라 색상 결정
                // ... 구현
            }
        };
        stressBar.setPreferredSize(new Dimension(5, 60));
        
        add(emojiLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
        add(stressBar, BorderLayout.EAST);
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends String> list,
                                                  String value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        // DiaryEntry 데이터로 라벨 업데이트
        // ...
        
        if (isSelected) {
            setBackground(PRIMARY_LAVENDER);
        } else {
            setBackground(Color.WHITE);
        }
        
        return this;
    }
}
```

---

### 4.6 통계 화면 (StatisticsView)

#### 현재 문제점
- 차트 색상이 단조로움
- 날짜 선택 UI가 복잡함

#### 개선 사항
```java
// ✨ 1. 차트 디자인 개선
- 바 차트: 둥근 모서리, 그라데이션
- 선 차트: 부드러운 곡선, 그림자 효과
- 배경 그리드: 연한 회색, 파선

// ✨ 2. 통계 카드
- 평균 스트레스, 가장 많은 감정 등을 카드로 표시
- 아이콘 + 숫자 + 설명

// ✨ 3. 기간 선택 개선
- 탭 형태로 변경 (주간 | 월간 | 연간)
- 선택된 탭은 밑줄 강조

// ✨ 4. 감정 분포 시각화
- 파이 차트 또는 도넛 차트
- 각 감정별 이모지 + 퍼센트 표시
```

#### 코드 예시
```java
// 통계 카드 컴포넌트
class StatCard extends JPanel {
    public StatCard(String icon, String value, String label) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_LIGHT, 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        textLabel.setForeground(TEXT_SECONDARY);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        add(iconLabel);
        add(Box.createVerticalStrut(10));
        add(valueLabel);
        add(Box.createVerticalStrut(5));
        add(textLabel);
    }
}

// 사용 예시
JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
statsPanel.setOpaque(false);
statsPanel.add(new StatCard("📊", "45.2", "평균 스트레스"));
statsPanel.add(new StatCard("😊", "28", "긍정 감정 횟수"));
statsPanel.add(new StatCard("🔥", "7", "연속 작성 일수"));
```

---

## 5. 폰트 및 타이포그래피

### 📝 폰트 시스템

#### 5.1 기본 폰트
```java
// 한글 - 맑은 고딕 (Windows), Apple SD Gothic Neo (macOS)
public static final Font FONT_REGULAR = new Font("맑은 고딕", Font.PLAIN, 14);
public static final Font FONT_BOLD = new Font("맑은 고딕", Font.BOLD, 14);

// 이모지 - 시스템 이모지 폰트
public static final Font FONT_EMOJI = new Font("Apple Color Emoji", Font.PLAIN, 24);

// 숫자 - Roboto 또는 맑은 고딕
public static final Font FONT_NUMBER = new Font("맑은 고딕", Font.PLAIN, 16);
```

#### 5.2 타이포그래피 스케일
```java
// 헤더
public static final Font H1 = new Font("맑은 고딕", Font.BOLD, 32);  // 메인 타이틀
public static final Font H2 = new Font("맑은 고딕", Font.BOLD, 24);  // 섹션 타이틀
public static final Font H3 = new Font("맑은 고딕", Font.BOLD, 18);  // 서브 타이틀

// 본문
public static final Font BODY_LARGE = new Font("맑은 고딕", Font.PLAIN, 16);
public static final Font BODY_REGULAR = new Font("맑은 고딕", Font.PLAIN, 14);
public static final Font BODY_SMALL = new Font("맑은 고딕", Font.PLAIN, 12);

// 버튼
public static final Font BUTTON = new Font("맑은 고딕", Font.BOLD, 15);

// 캡션
public static final Font CAPTION = new Font("맑은 고딕", Font.PLAIN, 11);
```

#### 5.3 라인 높이 및 자간
```java
// 본문 텍스트 라인 높이는 폰트 크기의 1.5배
// JTextArea의 경우 setLineSpacing()으로 조정 불가하므로
// HTML 렌더링 사용 고려

JLabel label = new JLabel("<html><body style='line-height: 1.5;'>" +
                          "여러 줄의<br>텍스트<br>내용</body></html>");
```

---

## 6. 애니메이션 및 전환 효과

### 🎬 부드러운 전환 효과

#### 6.1 페이드 인/아웃
```java
// 패널 전환 시 페이드 효과
public class FadeTransition {
    public static void fadeIn(JComponent component, int duration) {
        Timer timer = new Timer(10, null);
        final float[] opacity = {0.0f};
        
        timer.addActionListener(e -> {
            opacity[0] += 0.05f;
            if (opacity[0] >= 1.0f) {
                opacity[0] = 1.0f;
                timer.stop();
            }
            component.repaint();
        });
        
        timer.start();
    }
    
    public static void fadeOut(JComponent component, int duration) {
        // 유사하게 구현
    }
}
```

#### 6.2 스무스 스크롤
```java
// JScrollPane에 부드러운 스크롤 적용
public class SmoothScroll extends MouseWheelListener {
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        JScrollPane scrollPane = (JScrollPane) e.getSource();
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        
        int rotation = e.getWheelRotation();
        int currentValue = scrollBar.getValue();
        int targetValue = currentValue + (rotation * 30);
        
        // 애니메이션으로 스크롤
        animateScroll(scrollBar, currentValue, targetValue, 200);
    }
    
    private void animateScroll(JScrollBar bar, int start, int end, int duration) {
        Timer timer = new Timer(10, null);
        long startTime = System.currentTimeMillis();
        
        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1.0f, elapsed / (float) duration);
            
            // Ease-out 함수
            float eased = 1 - (float) Math.pow(1 - progress, 3);
            int value = (int) (start + (end - start) * eased);
            
            bar.setValue(value);
            
            if (progress >= 1.0f) {
                timer.stop();
            }
        });
        
        timer.start();
    }
}
```

#### 6.3 버튼 클릭 효과 (Ripple Effect)
```java
// Material Design 스타일 리플 효과
class RippleButton extends JButton {
    private Point clickPoint;
    private float rippleRadius = 0;
    private Timer rippleTimer;
    
    public RippleButton(String text) {
        super(text);
        setContentAreaFilled(false);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickPoint = e.getPoint();
                rippleRadius = 0;
                
                if (rippleTimer != null && rippleTimer.isRunning()) {
                    rippleTimer.stop();
                }
                
                rippleTimer = new Timer(10, evt -> {
                    rippleRadius += 5;
                    if (rippleRadius > getWidth()) {
                        rippleTimer.stop();
                        rippleRadius = 0;
                    }
                    repaint();
                });
                rippleTimer.start();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 배경 그리기
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        
        // 리플 효과 그리기
        if (rippleRadius > 0 && clickPoint != null) {
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.fillOval((int) (clickPoint.x - rippleRadius),
                        (int) (clickPoint.y - rippleRadius),
                        (int) (rippleRadius * 2),
                        (int) (rippleRadius * 2));
        }
        
        super.paintComponent(g);
    }
}
```

#### 6.4 카드 호버 효과
```java
// 일기 카드에 호버 시 살짝 떠오르는 효과
class HoverCard extends JPanel {
    private boolean isHovered = false;
    private float elevation = 0;
    
    public HoverCard() {
        addMouseListener(new MouseAdapter() {
            Timer timer;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                animateElevation(0, 8);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                animateElevation(8, 0);
            }
            
            private void animateElevation(float from, float to) {
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                
                timer = new Timer(10, evt -> {
                    if (isHovered && elevation < to) {
                        elevation += 0.5f;
                        if (elevation >= to) {
                            elevation = to;
                            timer.stop();
                        }
                    } else if (!isHovered && elevation > to) {
                        elevation -= 0.5f;
                        if (elevation <= to) {
                            elevation = to;
                            timer.stop();
                        }
                    }
                    repaint();
                });
                timer.start();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 그림자 그리기 (elevation에 따라 크기 변경)
        int shadowSize = (int) elevation;
        g2d.setColor(new Color(0, 0, 0, 20));
        for (int i = 0; i < shadowSize; i++) {
            g2d.fillRoundRect(i, i, getWidth() - i * 2, getHeight() - i * 2, 15, 15);
        }
        
        // 배경 그리기
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        
        super.paintComponent(g);
    }
}
```

---

## 7. 반응형 레이아웃

### 📐 창 크기 대응

#### 7.1 최소/최대 크기 설정
```java
// MainView 최소/최대 크기 설정
setMinimumSize(new Dimension(400, 500));
setMaximumSize(new Dimension(800, 900));
setPreferredSize(new Dimension(495, 630));
```

#### 7.2 GridBagLayout 활용
```java
// 유연한 레이아웃을 위해 GridBagLayout 사용
GridBagLayout layout = new GridBagLayout();
GridBagConstraints gbc = new GridBagConstraints();

// 컴포넌트 추가 시 가중치 설정
gbc.weightx = 1.0;  // 가로 방향 확장
gbc.weighty = 1.0;  // 세로 방향 확장
gbc.fill = GridBagConstraints.BOTH;
```

#### 7.3 ComponentListener로 동적 조정
```java
// 창 크기 변경 시 레이아웃 조정
addComponentListener(new ComponentAdapter() {
    @Override
    public void componentResized(ComponentEvent e) {
        int width = getWidth();
        
        // 너비에 따라 폰트 크기 조정
        if (width < 500) {
            titleLabel.setFont(H2);
        } else {
            titleLabel.setFont(H1);
        }
        
        revalidate();
        repaint();
    }
});
```

---

## 8. 접근성 개선

### ♿ 모든 사용자를 위한 디자인

#### 8.1 색상 대비
```java
// WCAG 2.1 AA 기준 준수 (대비율 4.5:1 이상)
// 텍스트와 배경 색상 대비 확인
public static boolean checkContrast(Color fg, Color bg) {
    double l1 = getRelativeLuminance(fg);
    double l2 = getRelativeLuminance(bg);
    
    double contrast = (Math.max(l1, l2) + 0.05) / (Math.min(l1, l2) + 0.05);
    return contrast >= 4.5;
}

private static double getRelativeLuminance(Color color) {
    double r = color.getRed() / 255.0;
    double g = color.getGreen() / 255.0;
    double b = color.getBlue() / 255.0;
    
    r = (r <= 0.03928) ? r / 12.92 : Math.pow((r + 0.055) / 1.055, 2.4);
    g = (g <= 0.03928) ? g / 12.92 : Math.pow((g + 0.055) / 1.055, 2.4);
    b = (b <= 0.03928) ? b / 12.92 : Math.pow((b + 0.055) / 1.055, 2.4);
    
    return 0.2126 * r + 0.7152 * g + 0.0722 * b;
}
```

#### 8.2 키보드 네비게이션
```java
// 탭 키로 모든 컴포넌트 접근 가능하도록
setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
setFocusCycleRoot(true);

// 엔터 키로 버튼 클릭
button.setMnemonic(KeyEvent.VK_ENTER);

// 단축키 설정
KeyStroke saveKey = KeyStroke.getKeyStroke(KeyEvent.VK_S, 
                                           InputEvent.CTRL_DOWN_MASK);
saveButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
          .put(saveKey, "save");
saveButton.getActionMap().put("save", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // 저장 로직
    }
});
```

#### 8.3 툴팁 및 레이블
```java
// 모든 입력 필드에 툴팁 추가
titleField.setToolTipText("일기 제목을 입력하세요 (최대 50자)");
contentArea.setToolTipText("오늘의 이야기를 자유롭게 적어보세요");

// 아이콘 버튼에 설명 추가
deleteButton.setToolTipText("일기 삭제 (Ctrl+D)");
```

---

## 9. 구현 우선순위

### 🚀 Phase 1: 기본 스타일링 (1-2주)
**목표**: 색상 및 폰트 일관성 확보

- [ ] `UIConstants.java` 생성 - 모든 색상, 폰트 상수 정의
- [ ] `StyleUtil.java` 생성 - 공통 스타일 적용 유틸리티
- [ ] 로그인/회원가입 화면 리디자인
- [ ] 메인 뷰 탭 네비게이션 개선
- [ ] 버튼 스타일 통일

### 🎨 Phase 2: 컴포넌트 개선 (2-3주)
**목표**: 주요 UI 컴포넌트 업그레이드

- [ ] 일기 작성 화면 레이아웃 개선
- [ ] 이모지 선택 다이얼로그 리디자인
- [ ] 스트레스 슬라이더 커스텀 UI
- [ ] 입력 필드 포커스 효과
- [ ] 일기 카드 렌더러 구현

### ✨ Phase 3: 인터랙션 강화 (1-2주)
**목표**: 사용자 경험 향상

- [ ] 버튼 호버 효과
- [ ] 리플 클릭 효과
- [ ] 카드 호버 애니메이션
- [ ] 페이드 전환 효과
- [ ] 스무스 스크롤

### 📊 Phase 4: 통계 시각화 (1-2주)
**목표**: 데이터를 아름답게 표현

- [ ] 차트 색상 및 스타일 개선
- [ ] 통계 카드 컴포넌트
- [ ] 기간 선택 UI 개선
- [ ] 감정 분포 시각화

### 🔧 Phase 5: 최적화 및 마무리 (1주)
**목표**: 성능 및 접근성

- [ ] 반응형 레이아웃 조정
- [ ] 키보드 네비게이션 개선
- [ ] 색상 대비 검증
- [ ] 최종 테스트 및 버그 수정

---

## 📚 참고 자료

### 디자인 영감
- **Notion**: 미니멀하고 깔끔한 UI
- **Apple Notes**: 직관적인 일기 작성 인터페이스
- **Daylio**: 감정 트래킹 앱 참고
- **Material Design**: 구글의 디자인 가이드라인

### 색상 도구
- [Coolors.co](https://coolors.co/) - 색상 팔레트 생성
- [Adobe Color](https://color.adobe.com/) - 색상 조합 도구
- [WebAIM Contrast Checker](https://webaim.org/resources/contrastchecker/) - 대비 검사

### Swing UI 라이브러리
- **FlatLaf** - 현대적인 Look & Feel
  ```xml
  <dependency>
      <groupId>com.formdev</groupId>
      <artifactId>flatlaf</artifactId>
      <version>3.2.5</version>
  </dependency>
  ```
  
- **JFreeChart** - 이미 사용 중인 차트 라이브러리
- **SwingX** - 추가 컴포넌트 (필요 시)

---

## 🎯 예상 효과

### 사용자 경험 개선
- ⬆️ **사용 만족도 향상**: 시각적으로 더 매력적인 UI
- ⬆️ **직관성 증가**: 명확한 시각적 계층 구조
- ⬆️ **편의성 향상**: 더 나은 인터랙션과 피드백

### 브랜드 가치
- 🎨 **전문성**: 세련된 디자인으로 신뢰감 형성
- 💡 **차별화**: 다른 일기 앱과의 차별점 확보
- 🏆 **경쟁력**: 상업용 앱 수준의 완성도

---

## ✅ 체크리스트

### 개발 전 준비
- [ ] 디자인 시스템 문서 검토
- [ ] 색상 팔레트 최종 결정
- [ ] 폰트 라이선스 확인
- [ ] FlatLaf 또는 다른 Look & Feel 라이브러리 선택

### 개발 중
- [ ] 각 Phase별 브랜치 생성
- [ ] 코드 리뷰 프로세스 수립
- [ ] UI 테스트 진행
- [ ] 사용자 피드백 수집

### 개발 후
- [ ] 최종 디자인 검수
- [ ] 성능 테스트
- [ ] 접근성 검증
- [ ] 문서화 업데이트

---

## 📞 문의 및 피드백

이 계획서는 살아있는 문서입니다. 개발 과정에서 새로운 아이디어나 개선사항이 있다면 언제든 추가/수정해주세요.

**마지막 업데이트**: 2025년 11월 23일

