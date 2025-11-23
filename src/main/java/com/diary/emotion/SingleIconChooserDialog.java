package com.diary.emotion;

import java.awt.*;
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
    
    // 🔹 (회색!!!!!!!!) 추가된 부분 — 비활성화된 아이콘의 배경색
    private final Color DISABLED_BG_COLOR = new Color(240, 240, 240); // 연한 회색

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

        // 아이콘 버튼 12개 생성
        for (int i = 0; i < allIcons.length; i++) {
            JButton iconButton = new JButton(allIcons[i]);
            iconButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
            iconButton.setBackground(bgColor);
            iconButton.setBorder(BorderFactory.createEtchedBorder());
            
            // 🔸 추가된 부분 — 선택 시 index 저장
            final int index = i;

            iconButton.addActionListener(e -> {
                // 버튼이 비활성화(다른 곳에서 사용 중)된 경우 선택하지 않음
                if (!iconButton.isEnabled()) {
                    return; 
                }
                
                selectedIndex = index;      // 선택한 인덱스 저장
                selectedIcon = allIcons[index];     // 선택한 아이콘 저장
                updateSelectionHighlight();      // 하이라이트 적용
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
        // 중복 체크는 setVisible(true)에서 수행되므로 여기서는 하이라이트만 준비
        // updateSelectionHighlight(); // setVisible에서 호출되므로 주석 처리하거나 제거 가능
    }

    // 팝업이 열릴 때 중복 아이콘 비활성화 처리
    @Override
    public void setVisible(boolean b) {
        if (b) {
            selectedIcon = null;
            
         // 다른 슬롯에서 사용 중인 아이콘 목록을 만듦 
            List<String> usedIcons = new ArrayList<>();
            for (JLabel lbl : allIconLabels) {
                String icon = lbl.getText();
                if (!icon.equals("[ ]") && !icon.equals(this.currentIconInSlot)) {  // 현재 슬롯의 아이콘과 "[ ]"는 제외하고 목록에 추가
                    usedIcons.add(icon);
                }
            }

            for (int i = 0; i < iconButtons.length; i++) { // 모든 버튼을 순회하며 비활성화 처리
                JButton btn = iconButtons[i];
                boolean isUsed = usedIcons.contains(btn.getText());
                btn.setEnabled(!isUsed); // 비활성화
                
                // 🔹 추가된 부분 — 비활성화된 아이콘에 시각적 표시 추가
                // updateSelectionHighlight()에서 처리되도록 이동
            }

            // 🔸 추가된 부분 — 중복 체크 후 하이라이트 다시 적용
            updateSelectionHighlight();
        }
        super.setVisible(b);
    }

    // 🔸 수정된 부분 — 선택된 아이콘 하이라이트 함수 (비활성화 상태도 반영)
    private void updateSelectionHighlight() {
        Color defaultBg = getContentPane().getBackground();
        Color selectedBg = new Color(255, 235, 200);
        Color selectedBorder = Color.ORANGE;
        
        for (int i = 0; i < iconButtons.length; i++) {
            JButton btn = iconButtons[i];
            
            // 1. 현재 슬롯의 아이콘 (선택 강조)
            if (i == selectedIndex) {
                btn.setBorder(BorderFactory.createLineBorder(selectedBorder, 3));
                btn.setBackground(selectedBg);
                btn.setForeground(Color.BLACK); // 선택된 것은 명확하게
            } 
            // 2. 다른 슬롯에서 이미 사용 중인 아이콘 (비활성화 강조)
            else if (!btn.isEnabled()) {  // 버튼 클릭 방지!!
                btn.setBorder(BorderFactory.createEtchedBorder()); // 테두리는 기본
                btn.setBackground(DISABLED_BG_COLOR); // 회색 배경 (회색!!!!!!!!)
                btn.setForeground(Color.GRAY); // 글자도 회색으로 (회색!!!!!!!!) > !btn.isEnabled()가 트루인지 잘 보기
            }
            // 3. 선택 가능한 기본 아이콘
            else {
                btn.setBorder(BorderFactory.createEtchedBorder());
                btn.setBackground(defaultBg);
                btn.setForeground(Color.BLACK); // 기본 글자색
            }
        }
    }
}