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
    private Timer timer;
    private AuthenticationFrame parent;

    public SignUpSuccessPanel(AuthenticationFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        setBackground(BG_LOGIN);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // 완료 텍스트
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel mainText = new JLabel("회원가입이 완료되었습니다!");
        mainText.setFont(new Font(H2.getName(), Font.BOLD, 28));
        mainText.setForeground(TEXT_PRIMARY);
        mainText.setHorizontalAlignment(SwingConstants.CENTER);
        add(mainText, gbc);

        // 안내 메시지
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        successMessageLabel = new JLabel("5초 후 로그인 화면으로 돌아갑니다.");
        successMessageLabel.setFont(new Font(BODY_LARGE.getName(), Font.PLAIN, 16));
        successMessageLabel.setForeground(new Color(65, 105, 225)); // Royal Blue
        successMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(successMessageLabel, gbc);

        // 로그인 하러 가기 버튼
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        JButton loginButton = ButtonFactory.createCustomButton("로그인 하러 가기", Color.WHITE, Color.BLACK, 150, 40);
        loginButton.addActionListener(e -> {
            timer.stop();
            parent.showPanel("LOGIN");
        });
        add(loginButton, gbc);

        // 타이머 설정 (5초)
        timer = new Timer(5000, e -> {
            parent.showPanel("LOGIN");
            timer.stop();
        });
        timer.setRepeats(false);
    }

    public void setSuccessMessage(String id) {
        timer.restart();
    }
}

