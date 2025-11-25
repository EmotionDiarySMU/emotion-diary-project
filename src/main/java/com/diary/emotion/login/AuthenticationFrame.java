package com.diary.emotion.login;

import javax.swing.*;
import com.diary.emotion.ui.UIConstants;
import java.awt.*;
import java.io.Serial;

/**
 * 인증 프레임 (로그인 및 회원가입 화면 전환)
 * 로그인, 회원가입, 회원가입 완료 화면을 CardLayout으로 관리
 */
public class AuthenticationFrame extends JFrame {

    @Serial
    private static final long serialVersionUID = 1L;
    public static String loggedInUserId = null;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private LoginPanel loginPanel;
    private SignUpPanel signUpPanel;
    private SignUpSuccessPanel successPanel;

    public AuthenticationFrame() {
        UIConstants.setupFrame(this);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        signUpPanel = new SignUpPanel(this);
        successPanel = new SignUpSuccessPanel(this);

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(signUpPanel, "SIGNUP");
        mainPanel.add(successPanel, "SUCCESS");

        add(mainPanel);
        setVisible(true);
    }

    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    public void showSuccess(String id) {
        successPanel.setSuccessMessage(id);
        showPanel("SUCCESS");
    }

    public SignUpPanel getSignUpPanel() {
        return signUpPanel;
    }
}

