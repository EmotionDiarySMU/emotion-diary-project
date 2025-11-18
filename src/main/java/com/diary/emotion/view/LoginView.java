package com.diary.emotion.view;

import javax.swing.*;
import java.awt.*;

/**
 * 로그인 화면 View 클래스
 * 사용자 인증을 위한 UI를 제공합니다.
 *
 * 구성 요소:
 * - 앱 제목 라벨
 * - 아이디 입력 필드
 * - 비밀번호 입력 필드
 * - 로그인 버튼
 * - 회원가입 버튼
 */
public class LoginView extends JPanel {

    // UI 컴포넌트
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;

    // 파스텔 블루 배경색
    private static final Color PASTEL_BLUE = new Color(230, 240, 255);

    /**
     * LoginView 생성자
     * UI 컴포넌트를 초기화하고 배치합니다.
     */
    public LoginView() {
        setLayout(new BorderLayout());
        setBackground(PASTEL_BLUE);

        // 중앙 패널 (입력 필드들)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(PASTEL_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 제목 라벨
        JLabel titleLabel = new JLabel("😊 감정 일기장");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 30, 10);
        centerPanel.add(titleLabel, gbc);

        // 간격 조정
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 1;

        // 아이디 라벨
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("아이디:");
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(idLabel, gbc);

        // 아이디 입력 필드
        userIdField = new JTextField(15);
        userIdField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1;
        centerPanel.add(userIdField, gbc);

        // 비밀번호 라벨
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel pwLabel = new JLabel("비밀번호:");
        pwLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(pwLabel, gbc);

        // 비밀번호 입력 필드
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1;
        centerPanel.add(passwordField, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // 하단 패널 (버튼들)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        bottomPanel.setBackground(PASTEL_BLUE);

        // 회원가입 버튼 (왼쪽)
        signUpButton = new JButton("회원가입");
        signUpButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signUpButton.setBackground(new Color(255, 228, 196));
        signUpButton.setFocusPainted(false);
        signUpButton.setPreferredSize(new Dimension(120, 35));
        bottomPanel.add(signUpButton);

        // 로그인 버튼 (오른쪽)
        loginButton = new JButton("로그인");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setBackground(new Color(173, 216, 230));
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(120, 35));
        bottomPanel.add(loginButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Enter 키로 로그인
        passwordField.addActionListener(e -> loginButton.doClick());
    }

    /**
     * 사용자가 입력한 아이디를 반환합니다.
     *
     * @return 입력된 아이디
     */
    public String getUserId() {
        return userIdField.getText().trim();
    }

    /**
     * 사용자가 입력한 비밀번호를 반환합니다.
     *
     * @return 입력된 비밀번호
     */
    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    /**
     * 로그인 버튼을 반환합니다. (Controller에서 이벤트 리스너 추가용)
     *
     * @return 로그인 버튼
     */
    public JButton getLoginButton() {
        return loginButton;
    }

    /**
     * 회원가입 버튼을 반환합니다. (Controller에서 이벤트 리스너 추가용)
     *
     * @return 회원가입 버튼
     */
    public JButton getSignUpButton() {
        return signUpButton;
    }

    /**
     * 입력 필드를 모두 비웁니다.
     */
    public void clearFields() {
        userIdField.setText("");
        passwordField.setText("");
    }

    /**
     * 아이디 입력 필드에 포커스를 설정합니다.
     */
    public void focusUserId() {
        userIdField.requestFocusInWindow();
    }
}

