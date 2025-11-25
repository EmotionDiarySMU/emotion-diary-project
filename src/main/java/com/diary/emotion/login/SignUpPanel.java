package com.diary.emotion.login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.diary.emotion.ui.*;
import com.diary.emotion.db.DatabaseManager;

import java.awt.*;

import static com.diary.emotion.ui.UIColors.*;
import static com.diary.emotion.ui.UIFonts.*;

/**
 * 회원가입 패널
 * 새로운 사용자 계정을 등록하는 화면
 */
public class SignUpPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField idField;
    private JPasswordField pwField;
    private JLabel errorLabel;
    private AuthenticationFrame parent;

    public SignUpPanel(AuthenticationFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(BG_LOGIN);

        // 중앙: 입력 폼
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
        JLabel titleLabel = new JLabel("회원가입");
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
        gbc.insets = new Insets(0, 0, 5, 0);
        centerPanel.add(pwField, gbc);

        // 에러 메시지
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font(BODY_SMALL.getName(), Font.PLAIN, 12));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 10, 0);
        centerPanel.add(errorLabel, gbc);

        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_LOGIN);
        bottomPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        // 왼쪽: 뒤로 버튼
        JPanel leftBtnContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftBtnContainer.setBackground(BG_LOGIN);

        JButton backBtn = ButtonFactory.createCustomButton("뒤로", BG_WHITE, TEXT_SECONDARY);
        backBtn.addActionListener(e -> {
            clearFields();
            parent.showPanel("LOGIN");
        });
        leftBtnContainer.add(backBtn);

        // 중앙: 가입하기 버튼
        JPanel centerBtnContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerBtnContainer.setBackground(BG_LOGIN);

        JButton joinBtn = ButtonFactory.createCustomButton("가입하기", BUTTON_PRIMARY_BG, BUTTON_PRIMARY_TEXT);
        joinBtn.addActionListener(e -> handleSignUp());
        centerBtnContainer.add(joinBtn);

        // 오른쪽: 더미
        JPanel rightDummy = new JPanel();
        rightDummy.setPreferredSize(new Dimension(75, 28));
        rightDummy.setBackground(BG_LOGIN);

        bottomPanel.add(leftBtnContainer, BorderLayout.WEST);
        bottomPanel.add(centerBtnContainer, BorderLayout.CENTER);
        bottomPanel.add(rightDummy, BorderLayout.EAST);

        return bottomPanel;
    }

    private void handleSignUp() {
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword()).trim();

        if (id.isEmpty() || pw.isEmpty()) {
            setErrorMessage("아이디와 비밀번호를 모두 입력해주세요.");
            return;
        }

        boolean success = DatabaseManager.registerUser(id, pw);
        if (success) {
            parent.showSuccess(id);
        } else {
            setErrorMessage("이미 존재하는 아이디이거나 오류가 발생했습니다.");
        }
    }

    public void setErrorMessage(String msg) {
        errorLabel.setText(msg);
    }

    public void clearFields() {
        idField.setText("");
        pwField.setText("");
        errorLabel.setText("");
    }
}

