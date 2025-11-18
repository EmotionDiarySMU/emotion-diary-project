package com.diary.emotion.view;

import javax.swing.*;
import java.awt.*;

/**
 * 회원가입 화면 View 클래스
 * 신규 사용자 등록을 위한 UI를 제공합니다.
 *
 * 구성 요소:
 * - 앱 제목 라벨
 * - 아이디 입력 필드
 * - 비밀번호 입력 필드
 * - 비밀번호 확인 입력 필드
 * - 회원가입 버튼
 * - 뒤로가기 버튼
 */
public class SignUpView extends JPanel {

    // UI 컴포넌트
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JPasswordField passwordConfirmField;
    private JButton signUpButton;
    private JButton backButton;

    // 파스텔 블루 배경색
    private static final Color PASTEL_BLUE = new Color(230, 240, 255);

    /**
     * SignUpView 생성자
     * UI 컴포넌트를 초기화하고 배치합니다.
     */
    public SignUpView() {
        setLayout(new BorderLayout());
        setBackground(PASTEL_BLUE);

        // 중앙 패널 (입력 필드들)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(PASTEL_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 제목 라벨
        JLabel titleLabel = new JLabel("😊 회원가입");
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

        // 비밀번호 확인 라벨
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel pwConfirmLabel = new JLabel("비밀번호 확인:");
        pwConfirmLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(pwConfirmLabel, gbc);

        // 비밀번호 확인 입력 필드
        passwordConfirmField = new JPasswordField(15);
        passwordConfirmField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1;
        centerPanel.add(passwordConfirmField, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // 하단 패널 (버튼들)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        bottomPanel.setBackground(PASTEL_BLUE);

        // 뒤로가기 버튼 (왼쪽)
        backButton = new JButton("뒤로가기");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setBackground(new Color(211, 211, 211));
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(120, 35));
        bottomPanel.add(backButton);

        // 회원가입 버튼 (오른쪽)
        signUpButton = new JButton("회원가입");
        signUpButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signUpButton.setBackground(new Color(173, 216, 230));
        signUpButton.setFocusPainted(false);
        signUpButton.setPreferredSize(new Dimension(120, 35));
        bottomPanel.add(signUpButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Enter 키로 회원가입
        passwordConfirmField.addActionListener(e -> signUpButton.doClick());
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
     * 사용자가 입력한 비밀번호 확인을 반환합니다.
     *
     * @return 입력된 비밀번호 확인
     */
    public String getPasswordConfirm() {
        return new String(passwordConfirmField.getPassword());
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
     * 뒤로가기 버튼을 반환합니다. (Controller에서 이벤트 리스너 추가용)
     *
     * @return 뒤로가기 버튼
     */
    public JButton getBackButton() {
        return backButton;
    }

    /**
     * 입력 필드를 모두 비웁니다.
     */
    public void clearFields() {
        userIdField.setText("");
        passwordField.setText("");
        passwordConfirmField.setText("");
    }

    /**
     * 아이디 입력 필드에 포커스를 설정합니다.
     */
    public void focusUserId() {
        userIdField.requestFocusInWindow();
    }
}

