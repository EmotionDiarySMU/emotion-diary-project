package com.diary.emotion;

// Java Swing(GUI) 라이브러리 임포트
import javax.swing.JButton; // 버튼 컴포넌트
import javax.swing.JFrame; // 창(Window)의 기본 프레임
import javax.swing.JPanel; // 컴포넌트들을 담는 패널(컨테이너)
import javax.swing.SwingUtilities; // Swing 작업을 스레드 안전하게 처리하는 유틸리티
import javax.swing.JLabel; // 텍스트 라벨 컴포넌트
import javax.swing.UIManager; // Java의 Look and Feel(테마)을 관리

import java.awt.BorderLayout; // 패널을 동/서/남/북/중앙으로 배치하는 레이아웃
import java.awt.CardLayout; // 여러 패널을 카드처럼 겹쳐놓고 바꿔 보여주는 레이아웃
import java.awt.FlowLayout; // 컴포넌트를 왼쪽에서 오른쪽으로, 줄바꿈하며 배치하는 레이아웃
import java.awt.Color; // 색상(RGB)을 정의하기 위한 클래스
import java.awt.event.ActionEvent; // 버튼 클릭 등 '이벤트'가 발생했음을 알리는 객체
import java.awt.event.ActionListener; // '이벤트'가 발생했을 때 동작을 정의하는 인터페이스

/**
 * [수정] 감정 일기장 메인 애플리케이션 클래스 (JFrame)
 * (수정) 캡슐화를 위해 모든 멤버 변수가 'private'으로 변경되었습니다.
 * (수정) 상단 네비게이션 버튼이 '왼쪽 정렬'로 변경되었습니다.
 */
public class MainApplication extends JFrame {

    // (디자인) 앱 전체에서 사용할 파스텔 톤의 파란색 배경을 상수로 정의합니다.
    // (final: 변경 불가능한 상수)
    private static final Color PASTEL_BLUE = new Color(230, 240, 255);
    // (디자인) 상단 네비게이션 바에서 사용할 파스텔 톤의 노란색 배경을 상수로 정의합니다.
    private static final Color PASTEL_YELLOW = new Color(255, 255, 220);

    // [수정] 'private'으로 변경 (캡슐화)
    // 메인 화면의 "일기 쓰기", "열람", "통계" 탭을 전환하기 위한 카드 레이아웃 매니저입니다.
    private CardLayout mainCardLayout;
    // [수정] 'private'으로 변경 (캡슐화)
    // mainCardLayout에 의해 관리될, 여러 '카드(패널)'들을 담는 부모 패널입니다.
    private JPanel mainCardPanel;

    // [수정] 'private'으로 변경 (캡슐화)
    // "통계" 탭의 화면을 담당하는 (StatisticsView 클래스로 만든) 패널입니다.
    private JPanel statisticsPanel; 
    // [수정] 'private'으로 변경 (캡슐화)
    // "일기 쓰기" 탭의 화면을 담당하는 (임시) 패널입니다.
    private JPanel writePanel;      
    // [수정] 'private'으로 변경 (캡슐화)
    // "열람" 탭의 화면을 담당하는 (임시) 패널입니다.
    private JPanel viewPanel;       

    /**
     * MainApplication 생성자
     * 이 클래스의 객체가 생성될 때(new MainApplication()) 자동으로 호출되어 GUI를 초기화합니다.
     */
    public MainApplication() {
        // 1. JFrame(창)의 기본 속성 설정
        
        // 애플리케이션 창의 제목 표시줄에 "Emotion Diary ☺️" 텍스트를 설정합니다.
        setTitle("Emotion Diary ☺️");
        // 창의 기본 크기를 설정합니다. (가로 550px, 세로 750px)
        setSize(550, 750); 
        // 창의 'X' 버튼을 눌렀을 때 프로그램이 완전히 종료되도록 설정합니다.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 창이 화면 중앙에 나타나도록 위치를 설정합니다.
        setLocationRelativeTo(null); 
        // 이 창(JFrame)의 전체 레이아웃을 BorderLayout (동/서/남/북/중앙)으로 설정합니다.
        setLayout(new BorderLayout());

        // (디자인) 메인 창의 기본 배경색(컨텐츠 영역)을 파스텔 블루로 설정합니다.
        getContentPane().setBackground(PASTEL_BLUE);

        
        // 2. 상단 네비게이션 버튼 패널 (BorderLayout.NORTH)
        
        // [수정] FlowLayout: 컴포넌트를 '중앙(CENTER)' -> '왼쪽(LEFT)' 정렬로 변경합니다.
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        // (디자인) 상단 네비게이션 바(navPanel)의 배경색을 파스텔 톤의 노란색으로 설정합니다.
        navPanel.setBackground(PASTEL_YELLOW);
        
        // 3개의 메인 네비게이션 버튼을 생성합니다.
        JButton writeButton = new JButton("일기 쓰기");
        JButton viewButton = new JButton("열람");
        JButton statsButton = new JButton("통계");

        // 네비게이션 패널(navPanel)에 3개의 버튼을 순서대로 추가합니다.
        navPanel.add(writeButton);
        navPanel.add(viewButton);
        navPanel.add(statsButton);

        
        // 3. 중앙 메인 컨텐츠 패널 (BorderLayout.CENTER)
        
        // CardLayout 매니저 객체를 생성합니다.
        mainCardLayout = new CardLayout();
        // CardLayout을 사용하는 메인 패널(mainCardPanel)을 생성합니다.
        mainCardPanel = new JPanel(mainCardLayout);
        // (디자인) mainCardPanel을 투명하게 설정하여, 부모(JFrame)의 파스텔 블루 배경색이 보이도록 합니다.
        mainCardPanel.setOpaque(false); 

        
        // 3-1. "일기 쓰기" 탭에 해당하는 패널 생성 (임시)
        writePanel = new JPanel();
        // 임시 텍스트 라벨을 추가합니다.
        writePanel.add(new JLabel("일기 쓰기 화면 (구현 예정)"));
        // (디자인) '일기 쓰기' 패널의 배경색을 파스텔 블루로 설정합니다.
        writePanel.setBackground(PASTEL_BLUE); 

        // 3-2. "열람" 탭에 해당하는 패널 생성 (임시)
        viewPanel = new JPanel();
        // 임시 텍스트 라벨을 추가합니다.
        viewPanel.add(new JLabel("일기 열람 화면 (구현 예정)"));
        // (디자인) '열람' 패널의 배경색을 파스텔 블루로 설정합니다.
        viewPanel.setBackground(PASTEL_BLUE);

        // 3-3. "통계" 탭에 해당하는 패널 생성 (MVC 패턴 적용)
        
        // --- MVC 컴포넌트 생성 및 연결 ---
        
        // (1) View(화면) 객체 생성
        StatisticsView statisticsView = new StatisticsView();
        
        // (2) DAO(DB담당) 객체 생성
        // (주의) 이 DAO는 StatisticsDAO.java에 DB 연결 정보가 올바르게 입력되어야 동작합니다.
        StatisticsDAO statisticsDAO = new StatisticsDAO();
        
        // (3) Controller(두뇌) 객체 생성 (View와 DAO를 연결)
        // (중요) Controller가 생성되자마자 View의 '가짜 데이터'를 '진짜 데이터'(DAO의 Mock)로 갱신합니다.
        StatisticsController statisticsController = new StatisticsController(statisticsView, statisticsDAO);

        // (4) 메인 창에 표시될 'statisticsPanel'은 View 객체 자체입니다.
        statisticsPanel = statisticsView; 
        
        // --- [연결 완료] ---
        

        
        // 4. 메인 CardLayout 패널에 3개의 탭 패널을 "이름표"와 함께 추가
        
        // 'mainCardPanel'에 "WRITE"라는 이름표로 '일기 쓰기' 패널을 추가합니다.
        mainCardPanel.add(writePanel, "WRITE");
        // 'mainCardPanel'에 "VIEW"라는 이름표로 '열람' 패널을 추가합니다.
        mainCardPanel.add(viewPanel, "VIEW");
        // 'mainCardPanel'에 "STATS"라는 이름표로 '통계' 패널을 추가합니다.
        mainCardPanel.add(statisticsPanel, "STATS"); 

        
        // 5. 메인 프레임(JFrame)에 네비게이션 패널과 메인 패널을 배치
        
        // 상단 네비게이션 패널을 창의 "NORTH"(북쪽) 영역에 추가합니다.
        add(navPanel, BorderLayout.NORTH);
        // 중앙 메인 패널을 창의 "CENTER"(중앙) 영역에 추가합니다.
        add(mainCardPanel, BorderLayout.CENTER);

        
        // 6. 네비게이션 버튼 3개에 대한 클릭 이벤트 리스너(동작) 설정
        
        // "일기 쓰기" 버튼에 클릭 이벤트 리스너(동작)를 추가합니다.
        writeButton.addActionListener(new ActionListener() {
            @Override // ActionListener 인터페이스의 actionPerformed 메소드를 구현(Override)합니다.
            public void actionPerformed(ActionEvent e) {
                // 'mainCardPanel'에게 "WRITE"라는 이름의 카드를 보여달라고 지시합니다.
                mainCardLayout.show(mainCardPanel, "WRITE");
            }
        });

        // "열람" 버튼에 클릭 이벤트 리스너(동작)를 추가합니다.
        viewButton.addActionListener(new ActionListener() {
            @Override // actionPerformed 메소드를 구현합니다.
            public void actionPerformed(ActionEvent e) {
                // 'mainCardPanel'에게 "VIEW"라는 이름의 카드를 보여달라고 지시합니다.
                mainCardLayout.show(mainCardPanel, "VIEW");
            }
        });

        // "통계" 버튼에 클릭 이벤트 리스너(동작)를 추가합니다.
        statsButton.addActionListener(new ActionListener() {
            @Override // actionPerformed 메소드를 구현합니다.
            public void actionPerformed(ActionEvent e) {
                // 'mainCardPanel'에게 "STATS"라는 이름의 카드를 보여달라고 지시합니다.
                mainCardLayout.show(mainCardPanel, "STATS");
            }
        });

        // 프로그램이 처음 켜졌을 때 기본으로 "통계" 탭을 보여줍니다.
        mainCardLayout.show(mainCardPanel, "STATS");
    }

    /**
     * 이 프로그램을 실행하기 위한 메인 메소드 (Entry Point)
     */
    public static void main(String[] args) {
        // Swing GUI 작업은 항상 Event Dispatch Thread(EDT)에서 실행되도록 보장합니다.
        SwingUtilities.invokeLater(new Runnable() {
            @Override // Runnable 인터페이스의 run 메소드를 구현합니다.
            public void run() {
                try {
                    // (디자인) Java의 기본 Look&Feel 대신, 현재 OS(Mac/Windows)의 테마를 따르도록 설정합니다.
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Look&Feel 설정에 실패해도 프로그램은 계속 실행되어야 하므로, 오류만 출력합니다.
                    e.printStackTrace();
                }
                
                // MainApplication 클래스의 새 인스턴스(객체)를 생성합니다. (이때 생성자가 호출됩니다)
                MainApplication app = new MainApplication();
                // 생성된 창(app)을 화면에 보이도록 설정합니다.
                app.setVisible(true);
            }
        });
    }
}