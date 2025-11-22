package write;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;


/**
 * 아이콘 1개를 선택하기 위한 팝업 다이얼로그
 */
public class SingleIconChooserDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    // 12개 아이콘 목록
    String[] allIcons = {"😊", "😆", "😍", "😌", "😂", "🤗", "😢", "😠", "😧", "😰", "😅", "😔"};
    String selectedIcon = null; // 사용자가 선택한 아이콘

    JLabel[] allIconLabels; // 메인 GUI의 4개 아이콘 라벨 (중복 검사용)
    String currentIconInSlot = null; // 현재 슬롯의 아이콘 (중복 검사 제외용)
    JButton[] iconButtons = new JButton[12]; // 12개 버튼

    // 🔸 추가된 부분 — 내부에서 선택된 아이콘 위치를 기억
    int selectedIndex = -1; 

    // 🔹 Component 타입으로 변경 — JPanel, JFrame 모두 받을 수 있음
    public SingleIconChooserDialog(Component parent, JLabel[] iconLabels, Color bgColor) {
        // 부모 컴포넌트로부터 JFrame이나 JDialog 찾아서 연결
        super(SwingUtilities.getWindowAncestor(parent), "아이콘 선택", ModalityType.APPLICATION_MODAL);

        this.allIconLabels = iconLabels;
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout(10, 10));

        // 아이콘 버튼 패널
        JPanel iconPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        iconPanel.setBackground(bgColor);

        // 버튼 클릭 시 동작
        ActionListener iconListener = e -> {
            selectedIcon = e.getActionCommand(); // 클릭한 아이콘 저장
            // 🔸 추가된 부분 — 선택 UI 반영
            updateSelectionHighlight();
            setVisible(false); // 팝업 닫기
        };

        // 아이콘 버튼 12개 생성
        for (int i = 0; i < allIcons.length; i++) {
            JButton iconButton = new JButton(allIcons[i]);
            iconButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
            iconButton.setBackground(bgColor);
            iconButton.setBorder(BorderFactory.createEtchedBorder());
            
            // 🔸 추가된 부분 — 선택 시 index 저장
            final int index = i;

            iconButton.addActionListener(e -> {
                selectedIndex = index;             // 선택한 인덱스 저장
                selectedIcon = allIcons[index];    // 선택한 아이콘 저장
                updateSelectionHighlight();        // 하이라이트 적용
                setVisible(false);
            });

            iconButtons[i] = iconButton;
            iconPanel.add(iconButton);
        }

        // 취소 버튼
        JButton cancelButton = new JButton("취소");
        cancelButton.setBackground(bgColor);
        cancelButton.addActionListener(e -> {
            selectedIcon = null; // 선택 안 함
            setVisible(false);
        });

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(bgColor);
        southPanel.add(cancelButton);

        // 다이얼로그 조립
        add(iconPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        setSize(300, 350);
        setLocationRelativeTo(parent);
    }

    // 선택된 아이콘 가져오기
    public String getSelectedIcon() {
        return selectedIcon;
    }

    // 현재 선택 중인 슬롯의 아이콘을 미리 전달받음
    public void setCurrentSlot(int index, String currentIcon) {
        this.currentIconInSlot = currentIcon;

        // 🔸 추가된 부분: 현재 슬롯에 이미 아이콘이 있다면 강조 표시
        selectedIndex = -1; // 초기화

        if (currentIcon != null && !currentIcon.equals("[ ]")) {
            for (int i = 0; i < allIcons.length; i++) {
                if (allIcons[i].equals(currentIcon)) {
                    selectedIndex = i;
                    break;
                }
            }
        }

        // 🔸 추가된 부분 — UI 업데이트
        updateSelectionHighlight();
    }

    // 팝업이 열릴 때 중복 아이콘 비활성화 처리
    @Override
    public void setVisible(boolean b) {
        if (b) {
            selectedIcon = null;

            List<String> usedIcons = new ArrayList<>();
            for (JLabel lbl : allIconLabels) {
                String icon = lbl.getText();
                if (!icon.equals("[ ]") && !icon.equals(this.currentIconInSlot)) {
                    usedIcons.add(icon);
                }
            }

            for (int i = 0; i < iconButtons.length; i++) {
                JButton btn = iconButtons[i];
                btn.setEnabled(!usedIcons.contains(btn.getText()));
            }

            // 🔸 추가된 부분 — 중복 체크 후 하이라이트 다시 적용
            updateSelectionHighlight();
        }
        super.setVisible(b);
    }

    // 🔸 추가된 부분 — 선택된 아이콘 하이라이트 함수
    private void updateSelectionHighlight() {
        for (int i = 0; i < iconButtons.length; i++) {
            JButton btn = iconButtons[i];

            if (i == selectedIndex) {
                btn.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
                btn.setBackground(new Color(255, 235, 200));
            } else {
                btn.setBorder(BorderFactory.createEtchedBorder());
                btn.setBackground(getContentPane().getBackground());
            }
        }
    }
}
