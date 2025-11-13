package com.diary.emotion;

// Java Swing(GUI) 라이브러리 임포트
import javax.swing.JFrame; // 창(Window)의 기본 프레임
import javax.swing.JPanel; // 컴포넌트들을 담는 패널(컨테이너)
import javax.swing.SwingUtilities; // Swing 작업을 스레드 안전하게 처리하는 유틸리티
import javax.swing.UIManager; // Java의 Look and Feel(테마)을 관리
import java.awt.BorderLayout; // 패널을 동/서/남/북/중앙으로 배치하는 레이아웃

/**
 * [V40] 감정 일기장 프로젝트 메인 실행기(Launcher) 클래스
 * 이 클래스는 이 프로젝트의 유일한 'main' 메소드를 가집니다.
 * [V41] (설명) 이 main 메소드는 'new MainApplication()' (생성자)을 호출하여
 * 생성자가 GUI를 만들도록(initUI) 하고, 완성된 JPanel을 JFrame에 추가합니다.
 */
public class AppLauncher {
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
                
                // 1. JFrame(창)의 기본 속성 설정
                JFrame frame = new JFrame("Emotion Diary️");
                frame.setSize(550, 750); // 창의 기본 크기
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 'X' 버튼
                frame.setLocationRelativeTo(null); // 화면 중앙 배치
                frame.setLayout(new BorderLayout()); // (중요) 프레임의 레이아웃 설정
                
                // 2. MainApplication (JPanel) 부품 생성
                // [V41] (핵심) 'new'를 통해 MainApplication의 '생성자'를 호출합니다.
                //       (이때 생성자가 'initUI'를 실행하여 GUI 패널을 완성합니다.)
                MainApplication mainPanel = new MainApplication();
                
                // 3. JFrame(창)에 MainApplication (JPanel) 부품을 추가
                // (BorderLayout.CENTER: 패널이 창 전체를 꽉 채우도록 함)
                frame.add(mainPanel, BorderLayout.CENTER);
                
                // 4. 생성된 창(frame)을 화면에 보이도록 설정합니다.
                frame.setVisible(true);
            }
        });
    }
}