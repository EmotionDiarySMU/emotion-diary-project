package com.diary.emotion.login;

import javax.swing.*;

import com.diary.emotion.db.DatabaseManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// [추가됨] 팀원의 메인 화면을 띄우기 위해 임포트
import com.diary.emotion.share.MainView;

public class AuthenticationFrame extends JFrame {

	public static String loggedInUserId = null; // 🔸
	
    public static final Color PASTEL_YELLOW = new Color(255, 255, 220);
  
    // [삭제됨] HashMap 관련 코드 삭제

    public CardLayout cardLayout;
    public JPanel mainPanel;

    public AuthenticationFrame() {
        setTitle("Emotion Diary");
        setSize(495, 630);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        LoginPanel loginPanel = new LoginPanel(this);
        SignUpPanel signUpPanel = new SignUpPanel(this);
        SignUpSuccessPanel successPanel = new SignUpSuccessPanel(this);

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(signUpPanel, "SIGNUP");
        mainPanel.add(successPanel, "SUCCESS");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
        
        setVisible(true);
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    // =========================================================
    // 1. 로그인 패널 
    // =========================================================
    public class LoginPanel extends JPanel implements ActionListener {
        AuthenticationFrame authFrame;
        JTextField idField;
        JPasswordField passwordField;
        JButton loginButton, signUpButton;

        public LoginPanel(AuthenticationFrame frame) {
            this.authFrame = frame;
            setLayout(null);
            setBackground(PASTEL_YELLOW);

            // (GUI 디자인 코드는 기존과 동일)
            JLabel titleLabel = new JLabel("로그인");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            titleLabel.setBounds(225, 150, 100, 30);
            
            JLabel idLabel = new JLabel("ID:");
            idLabel.setBounds(150, 230, 80, 30);
            idField = new JTextField(20);
            idField.setBounds(240, 230, 160, 30);
            
            JLabel pwLabel = new JLabel("Password:");
            pwLabel.setBounds(150, 280, 80, 30);
            passwordField = new JPasswordField(20);
            passwordField.setBounds(240, 280, 160, 30);

            loginButton = new JButton("로그인");
            loginButton.setBounds(150, 360, 250, 40);
            loginButton.addActionListener(this);

            signUpButton = new JButton("회원가입");
            signUpButton.setBounds(150, 420, 250, 40);
            signUpButton.addActionListener(this);
            
            add(titleLabel); add(idLabel); add(idField);
            add(pwLabel); add(passwordField);
            add(loginButton); add(signUpButton);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton) {
                String id = idField.getText();
                String pw = new String(passwordField.getPassword());

                // [수정됨] 직접 DB를 만지지 않고 매니저를 부릅니다.
                DatabaseManager dbManager = new DatabaseManager();
                boolean isSuccess = dbManager.checkLogin(id, pw);

                if (isSuccess) {
                    JOptionPane.showMessageDialog(this, id + "님 환영합니다!");
                    
                    loggedInUserId = id; // 🔸
                    
                    // [추가됨] 팀원의 메인 화면 실행
                    new MainView();
                    
                    // [추가됨] 로그인 창 닫기
                    authFrame.dispose(); 
                } else {
                    JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 틀렸습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                }

            } else if (e.getSource() == signUpButton) {
                authFrame.showPanel("SIGNUP");
            }
        }
    }

    // =========================================================
    // 2. 회원가입 패널 (DB 로직 분리)
    // =========================================================
    public class SignUpPanel extends JPanel implements ActionListener {
        AuthenticationFrame authFrame;
        JTextField idField;
        JPasswordField passwordField, confirmPasswordField;
        JButton signUpButton, backButton;

        public SignUpPanel(AuthenticationFrame frame) {
            this.authFrame = frame;
            setLayout(null);
            setBackground(PASTEL_YELLOW);

            // (GUI 디자인 코드 동일)
            JLabel titleLabel = new JLabel("회원가입");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            titleLabel.setBounds(210, 100, 150, 30);

            JLabel idLabel = new JLabel("ID:");
            idLabel.setBounds(150, 180, 80, 30);
            idField = new JTextField(20);
            idField.setBounds(240, 180, 160, 30);

            JLabel pwLabel = new JLabel("Password:");
            pwLabel.setBounds(150, 230, 80, 30);
            passwordField = new JPasswordField(20);
            passwordField.setBounds(240, 230, 160, 30);

            JLabel confirmPwLabel = new JLabel("Confirm PW:");
            confirmPwLabel.setBounds(150, 280, 80, 30);
            confirmPasswordField = new JPasswordField(20);
            confirmPasswordField.setBounds(240, 280, 160, 30);
            
            signUpButton = new JButton("가입하기");
            signUpButton.setBounds(150, 360, 250, 40);
            signUpButton.addActionListener(this);

            backButton = new JButton("취소");
            backButton.setBounds(150, 420, 250, 40);
            backButton.addActionListener(this);

            add(titleLabel); add(idLabel); add(idField);
            add(pwLabel); add(passwordField);
            add(confirmPwLabel); add(confirmPasswordField);
            add(signUpButton); add(backButton);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == signUpButton) {
                String id = idField.getText().trim();
                String pw = new String(passwordField.getPassword());
                String confirmPw = new String(confirmPasswordField.getPassword());

                if (id.isEmpty() || pw.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "ID와 비밀번호를 입력해주세요.");
                    return;
                }
                if (!pw.equals(confirmPw)) {
                    JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
                    return;
                }

                // [수정됨] DB 매니저에게 회원가입 요청
                DatabaseManager dbManager = new DatabaseManager();
                int result = dbManager.registerUser(id, pw);

                if (result == 1) {
                    // 성공 시 성공 패널로 이동
                    SignUpSuccessPanel successPanel = (SignUpSuccessPanel) authFrame.mainPanel.getComponent(2);
                    successPanel.setSuccessMessage(id);
                    authFrame.showPanel("SUCCESS");
                } else if (result == 0) {
                    JOptionPane.showMessageDialog(this, "이미 존재하는 ID입니다.", "가입 실패", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "시스템 오류가 발생했습니다.", "에러", JOptionPane.ERROR_MESSAGE);
                }

            } else if (e.getSource() == backButton) {
                authFrame.showPanel("LOGIN");
            }
        }
    }

    // =========================================================
    // 3. 성공 화면 (변경사항 거의 없음)
    // =========================================================
    public class SignUpSuccessPanel extends JPanel implements ActionListener {
        AuthenticationFrame authFrame;
        JLabel successMessageLabel;
        JButton goToLoginButton;
        Timer timer;

        public SignUpSuccessPanel(AuthenticationFrame frame) {
            this.authFrame = frame;
            setLayout(null);
            setBackground(PASTEL_YELLOW);

            JLabel titleLabel = new JLabel("🎉 회원가입 성공! 🎉");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
            titleLabel.setBounds(120, 150, 350, 40);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            successMessageLabel = new JLabel("환영합니다!");
            successMessageLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            successMessageLabel.setBounds(120, 220, 350, 30);
            successMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            goToLoginButton = new JButton("로그인 하러 가기 (5초 후 자동)");
            goToLoginButton.setBounds(150, 350, 250, 40);
            goToLoginButton.addActionListener(this);
            
            timer = new Timer(5000, e -> {
                authFrame.showPanel("LOGIN");
                timer.stop();
            });
            timer.setRepeats(false);
            
            add(titleLabel); add(successMessageLabel); add(goToLoginButton);
        }
        
        public void setSuccessMessage(String id) {
            successMessageLabel.setText(id + "님, 가입을 축하합니다!");
            timer.restart();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == goToLoginButton) {
                timer.stop();
                authFrame.showPanel("LOGIN");
            }
        }
    }
    
}