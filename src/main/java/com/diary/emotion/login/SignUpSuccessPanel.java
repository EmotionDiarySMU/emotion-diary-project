package com.diary.emotion.login;

import javax.swing.*;

import java.awt.*;

import com.diary.emotion.ui.ButtonFactory;
import static com.diary.emotion.ui.UIColors.*;
import static com.diary.emotion.ui.UIFonts.*;

/**
 * 회원가입 완료 패널
 * 회원가입 성공 시 표시되는 화면 (5초 후 자동으로 로그인 화면으로 이동)
 */
public class SignUpSuccessPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel successMessageLabel;
    private JLabel userWelcomeLabel;
    private Timer timer;
    private AuthenticationFrame parent;

    public SignUpSuccessPanel(AuthenticationFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        setBackground(BG_LOGIN);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // 회원가입 성공 메시지 (고정)
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        successMessageLabel = new JLabel("🎉 회원가입 성공! 🎉");
        successMessageLabel.setFont(new Font(H2.getName(), Font.BOLD, 28));
        successMessageLabel.setForeground(TEXT_PRIMARY);
        successMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(successMessageLabel, gbc);

        // 사용자 환영 메시지 (동적으로 변경됨)
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        userWelcomeLabel = new JLabel("");
        userWelcomeLabel.setFont(new Font(H2.getName(), Font.BOLD, 24));
        userWelcomeLabel.setForeground(TEXT_PRIMARY);
        userWelcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(userWelcomeLabel, gbc);

        // 완료 텍스트
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 100, 0);
        JLabel mainText = new JLabel("");
        mainText.setFont(new Font(H2.getName(), Font.PLAIN, 18));
        mainText.setForeground(TEXT_PRIMARY);
        mainText.setHorizontalAlignment(SwingConstants.CENTER);
        add(mainText, gbc);

        // 로그인 하러 가기 버튼
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        JButton loginButton = ButtonFactory.createCustomButton("로그인 하러 가기 (5초 후 자동)", Color.WHITE, Color.BLACK, 200, 40);
        loginButton.addActionListener(e -> {
            timer.stop();
            parent.getLoginPanel().clearFields();
            parent.showPanel("LOGIN");
        });
        add(loginButton, gbc);

        // 타이머 설정 (5초)
        timer = new Timer(5000, e -> {
            parent.getLoginPanel().clearFields();
            parent.showPanel("LOGIN");
            timer.stop();
        });
        timer.setRepeats(false);
    }

    public void setSuccessMessage(String id) {
        userWelcomeLabel.setText(id + " 님, 가입을 축하합니다!");
        timer.restart();
    }
}

