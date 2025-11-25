package login;

import javax.swing.*;

import DB.DatabaseManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// [추가됨] 팀원의 메인 화면을 띄우기 위해 임포트
import share.MainView;

public class AuthenticationFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static String loggedInUserId = null;
	
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
  
		private static final long serialVersionUID = 1L;
		AuthenticationFrame authFrame;
        JTextField idField;
        JPasswordField passwordField;
        JButton loginButton, signUpButton;

        public LoginPanel(AuthenticationFrame frame) {
                this.authFrame = frame;
                
                // ⭐ 1. BorderLayout으로 변경하고 배경색 설정
                setLayout(new BorderLayout()); 
                setBackground(PASTEL_YELLOW);
                
                // --- 중앙에 위치할 컴포넌트들을 담을 패널 생성 ---
                JPanel centerPanel = new JPanel(new GridBagLayout()); // GridBagLayout 사용
                centerPanel.setBackground(PASTEL_YELLOW);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 5, 10, 5); // 컴포넌트 간 간격 설정

                // 2. GUI 컴포넌트들을 GBC로 추가

                // 제목
                JLabel titleLabel = new JLabel("로그인");
                titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 2; // 2열 차지
                centerPanel.add(titleLabel, gbc);
                
                // 빈 공간을 위한 패딩
                gbc.gridwidth = 2;
                gbc.gridy = 1;
                centerPanel.add(new JLabel(" "), gbc); // 빈 라벨로 상단 여백 추가

                // ID 라벨 및 필드
                gbc.gridwidth = 1; // 1열로 복구
                gbc.anchor = GridBagConstraints.EAST; // ID: 라벨을 오른쪽(필드쪽)으로 붙임
                gbc.gridx = 0;
                gbc.gridy = 2;
                JLabel idLabel = new JLabel("ID:");
                centerPanel.add(idLabel, gbc);
                
                gbc.anchor = GridBagConstraints.WEST; // 필드를 왼쪽(라벨쪽)으로 붙임
                gbc.gridx = 1;
                gbc.gridy = 2;
                idField = new JTextField(15); // 크기 변경
                centerPanel.add(idField, gbc);

                // Password 라벨 및 필드
                gbc.anchor = GridBagConstraints.EAST;
                gbc.gridx = 0;
                gbc.gridy = 3;
                JLabel pwLabel = new JLabel("Password:");
                centerPanel.add(pwLabel, gbc);
                
                gbc.anchor = GridBagConstraints.WEST;
                gbc.gridx = 1;
                gbc.gridy = 3;
                passwordField = new JPasswordField(15); // 크기 변경
                centerPanel.add(passwordField, gbc);

                // 로그인 버튼
                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL; // 가로로 늘어나게 설정
                loginButton = new JButton("로그인");
                centerPanel.add(loginButton, gbc);
                loginButton.addActionListener(this);

                // 회원가입 버튼
                gbc.gridx = 0;
                gbc.gridy = 5;
                signUpButton = new JButton("회원가입");
                centerPanel.add(signUpButton, gbc);
                signUpButton.addActionListener(this);

                // 3. 전체 패널에 중앙 패널 추가
                add(centerPanel, BorderLayout.CENTER);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton) {
                String id = idField.getText();
                String pw = new String(passwordField.getPassword());

                // [수정됨] 직접 DB를 만지지 않고 매니저를 부름.
                DatabaseManager dbManager = new DatabaseManager();
                boolean isSuccess = dbManager.checkLogin(id, pw);

                if (isSuccess) {
                    JOptionPane.showMessageDialog(this, id + "님 환영합니다!");
                    
                    loggedInUserId = id;
                    
                    // [추가] 팀원의 메인 화면 실행
                    new MainView();
                    
                    // [추가] 로그인 창 닫기
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

		private static final long serialVersionUID = 1L;
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

                // [수정] DB 매니저에게 회원가입 요청
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

		private static final long serialVersionUID = 1L;
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