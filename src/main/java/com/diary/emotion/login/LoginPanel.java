package com.diary.emotion.login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.diary.emotion.ui.*;
import com.diary.emotion.db.DatabaseManager;
import com.diary.emotion.main.MainView;

import java.awt.*;

import static com.diary.emotion.ui.UIColors.*;
import static com.diary.emotion.ui.UIFonts.*;

/**
 * 로그인 패널
 * 사용자 아이디와 비밀번호를 입력받아 로그인 처리
 */
public class LoginPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField idField;
    private JPasswordField pwField;
    private AuthenticationFrame parent;

    public LoginPanel(AuthenticationFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(BG_LOGIN);

        // 중앙: 로고 및 입력 폼
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // 하단: 버튼 패널
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BG_LOGIN);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.NONE;

        // 타이틀
        JLabel titleLabel = new JLabel("로그인");
        titleLabel.setFont(H1);
        titleLabel.setForeground(TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        centerPanel.add(titleLabel, gbc);

        // ID 라벨
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(BODY_REGULAR);
        idLabel.setForeground(TEXT_SECONDARY);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 2, 0);
        centerPanel.add(idLabel, gbc);

        // ID 입력창
        idField = StyleUtils.createStyledTextField();
        idField.setPreferredSize(new Dimension(280, 30));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        centerPanel.add(idField, gbc);

        // PW 라벨
        JLabel pwLabel = new JLabel("PW:");
        pwLabel.setFont(BODY_REGULAR);
        pwLabel.setForeground(TEXT_SECONDARY);
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 2, 0);
        centerPanel.add(pwLabel, gbc);

        // PW 입력창
        pwField = StyleUtils.createStyledPasswordField();
        pwField.setPreferredSize(new Dimension(280, 30));
        pwField.setFont(BODY_REGULAR);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 10, 0);
        centerPanel.add(pwField, gbc);

        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_LOGIN);
        bottomPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        // 왼쪽: 회원가입 버튼
        JPanel leftBtnContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftBtnContainer.setBackground(BG_LOGIN);

        JButton signUpBtn = ButtonFactory.createCustomButton("회원가입", BG_WHITE, TEXT_PRIMARY);
        signUpBtn.addActionListener(e -> {
            parent.getSignUpPanel().clearFields();
            parent.showPanel("SIGNUP");
        });
        leftBtnContainer.add(signUpBtn);

        // 중앙: 로그인 버튼
        JPanel centerBtnContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerBtnContainer.setBackground(BG_LOGIN);

        JButton loginBtn = ButtonFactory.createCustomButton("로그인", BUTTON_PRIMARY_BG, BUTTON_PRIMARY_TEXT);
        loginBtn.addActionListener(e -> handleLogin());
        centerBtnContainer.add(loginBtn);

        // 오른쪽: 더미
        JPanel rightDummy = new JPanel();
        rightDummy.setPreferredSize(new Dimension(75, 28));
        rightDummy.setBackground(BG_LOGIN);

        bottomPanel.add(leftBtnContainer, BorderLayout.WEST);
        bottomPanel.add(centerBtnContainer, BorderLayout.CENTER);
        bottomPanel.add(rightDummy, BorderLayout.EAST);

        return bottomPanel;
    }

    private void handleLogin() {
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword()).trim();

        if (id.isEmpty() || pw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 입력해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (DatabaseManager.checkLogin(id, pw)) {
            AuthenticationFrame.loggedInUserId = id;
            new MainView().setVisible(true);
            parent.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 일치하지 않습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
        }
    }
}

