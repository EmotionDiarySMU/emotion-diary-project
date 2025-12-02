package com.diary.emotion.share;

import com.diary.emotion.DB.DatabaseManager;
import com.diary.emotion.login.AuthenticationFrame; // 로그인 화면으로 돌아가기 위해 필요
import com.diary.emotion.view.SearchDiaryPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class DeleteAccountDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPasswordField passwordField;
    private MainView parent; // 메인 창을 참조하기 위한 필드

    /**
     * 계정 삭제 확인 다이얼로그 생성자
     */
    public DeleteAccountDialog(MainView parent) {
        super(parent, "비밀번호 재확인", true); // 모달 다이얼로그 설정 (true)
        this.parent = parent;
        
        setLayout(new BorderLayout(10, 10));
        setSize(300, 150);
        setLocationRelativeTo(parent); // 메인 창 중앙에 표시

        // 1. 메시지 및 입력 필드 패널
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JLabel messageLabel = new JLabel("계정 삭제를 위해 비밀번호를 다시 입력하세요.");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        passwordField = new JPasswordField(15);
        
        inputPanel.add(messageLabel);
        inputPanel.add(passwordField);

        // 2. 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("확인");
        JButton cancelButton = new JButton("취소");
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        // 3. 컴포넌트 추가
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 4. 이벤트 리스너 설정
        confirmButton.addActionListener(e -> attemptDeleteAccount());
        cancelButton.addActionListener(e -> dispose()); // 다이얼로그 닫기

        // 5. 엔터 키로 확인 버튼 누르기
        JRootPane rootPane = this.getRootPane();
        rootPane.setDefaultButton(confirmButton);
        
        // 6. 닫기 버튼(X) 처리: 메인뷰의 닫기 로직을 따라가지 않고 바로 닫도록 처리
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        setVisible(true);
    }
    
    /**
     * 비밀번호를 검증하고 계정 삭제를 시도하는 메서드
     */
    private void attemptDeleteAccount() {
        // 비밀번호 필드에서 문자 배열을 가져와 String으로 변환
        String inputPassword = new String(passwordField.getPassword());
        
        if (inputPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1단계: DatabaseManager의 정적 메서드를 호출하여 계정 삭제 시도
        boolean success = DatabaseManager.deleteUserAccount(inputPassword);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "계정이 성공적으로 삭제되었습니다.\n프로그램을 종료합니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            
            // 2단계: 메인 창(MainView)과 열려있는 모든 ExtraWindow를 닫기
            dispose(); // 현재 다이얼로그 닫기
            
            // 열람 창에서 열었던 모든 수정 다이얼로그 닫기
            for (Window win : new HashSet<>(parent.viewPanel.openWindows)) {
                win.dispose();
            }

            // 3단계: 메인 창 닫고 로그인 화면으로 이동
            parent.dispose();
            
            // 로그인 화면 다시 띄우기 (AuthenticationFrame의 인스턴스를 새로 생성하거나 가져와서 표시)
            new AuthenticationFrame(); 
            
        } else {
            // 실패 메시지 (비밀번호 불일치, DB 오류 등)
            JOptionPane.showMessageDialog(this, "계정 삭제에 실패했습니다.\n비밀번호를 확인해주세요.", "실패", JOptionPane.ERROR_MESSAGE);
            passwordField.setText(""); // 비밀번호 필드 초기화
            passwordField.requestFocusInWindow();
        }
    }
}
