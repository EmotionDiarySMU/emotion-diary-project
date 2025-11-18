package com.diary.emotion;

// Java Swing(GUI) 라이브러리 임포트
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;

// MVC 패턴 임포트
import com.diary.emotion.view.LoginView;
import com.diary.emotion.view.SignUpView;
import com.diary.emotion.view.MainApplication;
import com.diary.emotion.controller.LoginController;
import com.diary.emotion.controller.SignUpController;
import com.diary.emotion.model.UserDAO;
import com.diary.emotion.model.DatabaseUtil;

/**
 * 감정 일기장 프로젝트 메인 실행기(Launcher) 클래스
 *
 * 주요 변경사항:
 * - CardLayout을 사용하여 LoginView, SignUpView, MainApplication 간 전환
 * - 데이터베이스 자동 초기화
 * - MVC 패턴 적용
 */
public class AppLauncher {
    public static void main(String[] args) {
        // Swing GUI 작업은 항상 Event Dispatch Thread(EDT)에서 실행되도록 보장합니다.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // OS의 Look&Feel 적용
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                // 데이터베이스 초기화
                System.out.println("=== 데이터베이스 초기화 시작 ===");
                boolean dbInitSuccess = DatabaseUtil.createDatabase();
                if (dbInitSuccess) {
                    System.out.println("=== 데이터베이스 초기화 완료 ===");
                } else {
                    System.err.println("=== 데이터베이스 초기화 실패 ===");
                }

                // JFrame 생성
                JFrame frame = new JFrame("Emotion Diary 😊");
                frame.setSize(550, 750);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);

                // CardLayout을 사용하여 화면 전환
                CardLayout cardLayout = new CardLayout();
                JPanel rootPanel = new JPanel(cardLayout);

                // 1. LoginView 생성
                LoginView loginView = new LoginView();
                UserDAO userDAO = new UserDAO();
                LoginController loginController = new LoginController(loginView, userDAO);

                // 2. SignUpView 생성
                SignUpView signUpView = new SignUpView();
                SignUpController signUpController = new SignUpController(signUpView, userDAO);

                // 3. MainApplication 생성
                MainApplication mainApp = new MainApplication();

                // 4. rootPanel에 추가
                rootPanel.add(loginView, "login");
                rootPanel.add(signUpView, "signup");
                rootPanel.add(mainApp, "main");

                // 5. 화면 전환 콜백 설정

                // 로그인 성공 시 -> MainApplication으로 전환
                loginController.setOnLoginSuccess(() -> {
                    cardLayout.show(rootPanel, "main");
                });

                // 로그인 화면에서 회원가입 버튼 클릭 시 -> SignUpView로 전환
                loginController.setOnSignUpRequest(() -> {
                    signUpView.clearFields();
                    cardLayout.show(rootPanel, "signup");
                    signUpView.focusUserId();
                });

                // 회원가입 성공 시 -> LoginView로 전환
                signUpController.setOnSignUpSuccess(() -> {
                    loginView.clearFields();
                    cardLayout.show(rootPanel, "login");
                    loginView.focusUserId();
                });

                // 회원가입 화면에서 뒤로가기 버튼 클릭 시 -> LoginView로 전환
                signUpController.setOnBackRequest(() -> {
                    loginView.clearFields();
                    cardLayout.show(rootPanel, "login");
                    loginView.focusUserId();
                });

                // 6. 프레임에 rootPanel 추가
                frame.add(rootPanel, BorderLayout.CENTER);

                // 7. 처음에는 LoginView 표시
                cardLayout.show(rootPanel, "login");

                // 8. 프레임 표시
                frame.setVisible(true);

                // 9. LoginView의 아이디 필드에 포커스 설정
                loginView.focusUserId();

                System.out.println("=== 애플리케이션 시작 완료 ===");
            }
        });
    }
}

