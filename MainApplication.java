package com.diary.emotion;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Emotion Diary의 메인 애플리케이션 프레임입니다.
 * 로그인 성공 후 일기 작성 및 조회 기능을 제공합니다.
 * (모든 클래스, 메서드, 필드가 public으로 설정됨)
 */
public class MainApplication extends JFrame {

    // AuthenticationFrame에서 사용된 동일한 배경색
    public static final Color PASTEL_YELLOW = new Color(255, 255, 220);
    
    // (public으로 변경)
    public static final int WIDTH = 550;
    public static final int HEIGHT = 700;
    
    public JPanel mainPanel;
    public JLabel welcomeLabel;

    /**
     * 메인 애플리케이션 생성자
     * @param loggedInUser 현재 로그인한 사용자 ID
     */
    public MainApplication(String loggedInUser) {
        // JFrame 기본 설정
        setTitle("Emotion Diary - " + loggedInUser + "님의 공간");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙 배치

        // 메인 패널 설정 (public으로 변경)
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(PASTEL_YELLOW);

        // 환영 메시지 라벨 (public으로 변경)
        welcomeLabel = new JLabel("환영합니다! 오늘 당신의 감정은 무엇인가요?", JLabel.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.DARK_GRAY);
        welcomeLabel.setPreferredSize(new Dimension(WIDTH, 100));

        // TODO: 여기에 일기 작성, 달력, 조회 등의 컴포넌트 추가

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        
        // 프레임에 메인 패널 추가
        add(mainPanel);
    }

    /**
     * 메인 애플리케이션을 실행하는 정적 메소드
     * (AuthenticationFrame의 로그인 성공 로직에서 호출됨)
     * @param username 로그인에 성공한 사용자 ID
     */
    public static void startMainApp(String username) {
        SwingUtilities.invokeLater(() -> {
            try {
                // OS 기본 테마 적용
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 메인 애플리케이션 창을 생성하고 표시
            MainApplication app = new MainApplication(username);
            app.setVisible(true);
        });
    }

    /**
     * MainApplication을 단독으로 실행하기 위한 main 메소드 (선택 사항)
     */
    public static void main(String[] args) {
        // 테스트용으로 "Guest" 사용자로 앱 시작
        startMainApp("Guest"); 
    }
}