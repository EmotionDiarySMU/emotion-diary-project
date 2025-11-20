package com.diary.emotion;

// Java Swing(GUI) 라이브러리 임포트
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane; // 팝업 메시지 창
import javax.swing.SwingUtilities; // Swing 스레드 유틸리티
import javax.swing.UIManager; // Look and Feel(테마) 관리
import javax.swing.Timer; // 일정 시간 후 동작을 위한 타이머
import javax.swing.SwingConstants; // 라벨 정렬을 위한 상수

import java.awt.CardLayout; // 패널 전환을 위한 카드 레이아웃
import java.awt.Color; // 색상
import java.awt.Font; // 폰트
import java.awt.event.ActionEvent; // 이벤트 객체
import java.awt.event.ActionListener; // 이벤트 리스너
import java.util.HashMap; // "간단 DB"로 사용할 해시맵

/**
 * 로그인 및 회원가입을 처리하는 메인 프레임(창) 클래스입니다.
 * CardLayout을 사용하여 로그인, 회원가입, 성공 패널을 전환합니다.
 */
public class AuthenticationFrame extends JFrame {

    // (디자인) 다이어그램의 노란색 배경을 참고한 색상
    public static final Color PASTEL_YELLOW = new Color(255, 255, 220);

    // (DB) 사용자 정보를 저장할 간단한 인-메모리 데이터베이스 (ID, Password)
    public static HashMap<String, String> userDatabase;

    // 패널들을 전환하기 위한 CardLayout
    public CardLayout cardLayout;
    // CardLayout이 적용될 메인 패널
    public JPanel mainPanel;

    /**
     * AuthenticationFrame 생성자
     * GUI 컴포넌트들을 초기화하고 프레임을 설정합니다.
     */
    public AuthenticationFrame() {
        // 1. "간단 DB" 초기화
        userDatabase = new HashMap<>();
        // (테스트용 계정)
        userDatabase.put("test", "1234");

        // 2. JFrame(창) 기본 속성 설정
        setTitle("Emotion Diary"); 
        // 요청하신 크기 (가로 550, 세로 700) 설정
        setSize(550, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치

        // 3. CardLayout 및 메인 패널 설정
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 4. 각 화면(Panel) 클래스 인스턴스화
        // 'this' (AuthenticationFrame 자신)를 전달하여 패널들이 메인 프레임의
        // showPanel 메소드를 호출(화면 전환)할 수 있도록 합니다.
        LoginPanel loginPanel = new LoginPanel(this);
        SignUpPanel signUpPanel = new SignUpPanel(this);
        SignUpSuccessPanel successPanel = new SignUpSuccessPanel(this);

        // 5. 메인 패널에 각 화면 추가
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(signUpPanel, "SIGNUP");
        mainPanel.add(successPanel, "SUCCESS");

        // 6. JFrame에 메인 패널 추가
        add(mainPanel);

        // 7. 기본으로 보여줄 화면 설정
        cardLayout.show(mainPanel, "LOGIN");
    }

    /**
     * CardLayout의 화면을 전환하는 공용 메소드
     * @param panelName "LOGIN", "SIGNUP", "SUCCESS" 등
     */
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    /**
     * 프로그램을 실행하기 위한 main 메소드 (Entry Point)
     */
    public static void main(String[] args) {
        // Swing GUI 작업은 항상 Event Dispatch Thread(EDT)에서 실행되도록 보장합니다.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // (디자인) OS 기본 테마 적용 (MainApplication.java 참고)
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                // 로그인/회원가입 창 인스턴스 생성 및 표시
                AuthenticationFrame authFrame = new AuthenticationFrame();
                authFrame.setVisible(true);
            }
        });
    }

    // --- 내부 클래스로 각 화면(Panel) 구현 ---

    /**
     * 1. 로그인 화면 (JPanel)
     */
    public class LoginPanel extends JPanel implements ActionListener {
        
        public AuthenticationFrame authFrame;
        public JTextField idField;
        public JPasswordField passwordField;
        public JButton loginButton;
        public JButton signUpButton;

        public LoginPanel(AuthenticationFrame frame) {
            this.authFrame = frame;
            // 레이아웃 매니저를 null로 설정 (절대 좌표)
            setLayout(null);
            setBackground(PASTEL_YELLOW); // 배경색 설정

            // "로그인" 타이틀 라벨
            JLabel titleLabel = new JLabel("로그인");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            titleLabel.setBounds(225, 150, 100, 30); // (x, y, width, height)
            
            // ID 라벨 및 텍스트 필드
            JLabel idLabel = new JLabel("ID:");
            idLabel.setBounds(150, 230, 80, 30);
            idField = new JTextField(20);
            idField.setBounds(240, 230, 160, 30);
            
            // Password 라벨 및 텍스트 필드
            JLabel pwLabel = new JLabel("Password:");
            pwLabel.setBounds(150, 280, 80, 30);
            passwordField = new JPasswordField(20);
            passwordField.setBounds(240, 280, 160, 30);

            // 로그인 버튼
            loginButton = new JButton("로그인");
            loginButton.setBounds(150, 360, 250, 40);
            loginButton.addActionListener(this); // 이벤트 리스너 등록

            // 회원가입 버튼
            signUpButton = new JButton("회원가입");
            signUpButton.setBounds(150, 420, 250, 40);
            signUpButton.addActionListener(this); // 이벤트 리스너 등록
            
            // 컴포넌트 추가
            add(titleLabel);
            add(idLabel);
            add(idField);
            add(pwLabel);
            add(passwordField);
            add(loginButton);
            add(signUpButton);
        }

        /**
         * 버튼 클릭 이벤트 처리
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton) {
                String id = idField.getText();
                // 비밀번호 필드의 텍스트를 char 배열로 가져와 String으로 변환
                String password = new String(passwordField.getPassword()); 
                
                // 1. ID가 DB에 존재하는지 확인
                if (userDatabase.containsKey(id)) {
                    // 2. 비밀번호 일치 확인
                    if (userDatabase.get(id).equals(password)) {
                        // 로그인 성공
                        JOptionPane.showMessageDialog(this, "로그인 성공! 환영합니다, " + id + "님!", "성공", JOptionPane.INFORMATION_MESSAGE);
                        
                        // TODO: 이후 'Emotion Diary'의 메인 화면으로 이동하는 로직 추가

                    } else {
                        // 비밀번호 불일치
                        JOptionPane.showMessageDialog(this, "비밀번호가 올바르지 않습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // ID 없음
                    JOptionPane.showMessageDialog(this, "존재하지 않는 사용자 ID입니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                }
                
            } else if (e.getSource() == signUpButton) {
                // 회원가입 화면으로 전환
                authFrame.showPanel("SIGNUP");
            }
        }
    }

    /**
     * 2. 회원가입 화면 (JPanel)
     */
    public class SignUpPanel extends JPanel implements ActionListener {

        public AuthenticationFrame authFrame;
        public JTextField idField;
        public JPasswordField passwordField;
        public JPasswordField confirmPasswordField;
        public JButton signUpButton;
        public JButton backButton;

        public SignUpPanel(AuthenticationFrame frame) {
            this.authFrame = frame;
            setLayout(null);
            setBackground(PASTEL_YELLOW);

            // "회원가입" 타이틀 라벨
            JLabel titleLabel = new JLabel("회원가입");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            titleLabel.setBounds(210, 100, 150, 30);

            // ID 라벨 및 텍스트 필드
            JLabel idLabel = new JLabel("ID:");
            idLabel.setBounds(150, 180, 80, 30);
            idField = new JTextField(20);
            idField.setBounds(240, 180, 160, 30);

            // Password 라벨 및 텍스트 필드
            JLabel pwLabel = new JLabel("Password:");
            pwLabel.setBounds(150, 230, 80, 30);
            passwordField = new JPasswordField(20);
            passwordField.setBounds(240, 230, 160, 30);

            // Password 확인 라벨 및 텍스트 필드
            JLabel confirmPwLabel = new JLabel("Confirm PW:");
            confirmPwLabel.setBounds(150, 280, 80, 30);
            confirmPasswordField = new JPasswordField(20);
            confirmPasswordField.setBounds(240, 280, 160, 30);
            
            // 회원가입 완료 버튼
            signUpButton = new JButton("회원가입 완료");
            signUpButton.setBounds(150, 360, 250, 40);
            signUpButton.addActionListener(this);

            // 뒤로가기 버튼
            backButton = new JButton("로그인 화면으로");
            backButton.setBounds(150, 420, 250, 40);
            backButton.addActionListener(this);

            // 컴포넌트 추가
            add(titleLabel);
            add(idLabel);
            add(idField);
            add(pwLabel);
            add(passwordField);
            add(confirmPwLabel);
            add(confirmPasswordField);
            add(signUpButton);
            add(backButton);
        }

        /**
         * 버튼 클릭 이벤트 처리
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == signUpButton) {
                String id = idField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (id.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "모든 필드를 채워주세요.", "가입 실패", JOptionPane.WARNING_MESSAGE);
                } else if (userDatabase.containsKey(id)) {
                    JOptionPane.showMessageDialog(this, "이미 존재하는 ID입니다.", "가TSS 실패", JOptionPane.WARNING_MESSAGE);
                } else if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", "가입 실패", JOptionPane.WARNING_MESSAGE);
                } else {
                    // 회원가입 성공 처리
                    userDatabase.put(id, password);
                    
                    // 성공 화면으로 전환 (ID 전달)
                    SignUpSuccessPanel successPanel = (SignUpSuccessPanel) authFrame.mainPanel.getComponent(2);
                    successPanel.setSuccessMessage(id); 
                    authFrame.showPanel("SUCCESS");
                }
            } else if (e.getSource() == backButton) {
                // 로그인 화면으로 전환
                authFrame.showPanel("LOGIN");
            }
        }
    }

    /**
     * 3. 회원가입 성공 화면 (JPanel)
     */
    public class SignUpSuccessPanel extends JPanel implements ActionListener {

        public AuthenticationFrame authFrame;
        public JLabel successMessageLabel;
        public JButton goToLoginButton;
        public Timer timer; // 자동 전환을 위한 타이머

        public SignUpSuccessPanel(AuthenticationFrame frame) {
            this.authFrame = frame;
            setLayout(null);
            setBackground(PASTEL_YELLOW);

            // "가입 성공" 타이틀 라벨
            JLabel titleLabel = new JLabel("🎉 회원가입 성공! 🎉");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
            titleLabel.setBounds(150, 150, 300, 40);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            // 성공 메시지 라벨 (가변적인 내용을 담을 곳)
            successMessageLabel = new JLabel("...님, 환영합니다!");
            successMessageLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            successMessageLabel.setBounds(150, 220, 300, 30);
            successMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            // 로그인 버튼
            goToLoginButton = new JButton("로그인 화면으로 이동 (5초 후 자동 전환)");
            goToLoginButton.setBounds(150, 350, 250, 40);
            goToLoginButton.addActionListener(this);
            
            // 타이머 설정 (5초 = 5000ms)
            timer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 5초 후 로그인 화면으로 전환
                    authFrame.showPanel("LOGIN");
                    timer.stop(); // 타이머 중지
                }
            });
            timer.setRepeats(false); // 한 번만 실행되도록 설정
            
            // 컴포넌트 추가
            add(titleLabel);
            add(successMessageLabel);
            add(goToLoginButton);
        }
        
        /**
         * 성공 메시지 설정 및 타이머 시작
         * @param id 가입에 성공한 사용자 ID
         */
        public void setSuccessMessage(String id) {
            successMessageLabel.setText("<html><center><b>" + id + "</b>님,<br>Emotion Diary에 오신 것을 환영합니다!</center></html>");
            // 화면이 보일 때마다 타이머를 재시작
            timer.restart();
        }

        /**
         * 버튼 클릭 이벤트 처리
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == goToLoginButton) {
                // 수동으로 로그인 화면으로 전환
                timer.stop(); // 자동 전환 타이머 중지
                authFrame.showPanel("LOGIN");
            }
        }
    }
}