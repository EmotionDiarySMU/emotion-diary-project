package com.diary.emotion.write;

import com.diary.emotion.ui.UIFonts;
import com.diary.emotion.ui.ButtonFactory;

import static com.diary.emotion.ui.UIFonts.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    private final Color LIGHT_YELLOW = new Color(255, 255, 224); // #FFFFE0 - 팝업 배경색

    // 🔹 Component 타입으로 변경 — JPanel, JFrame 모두 받을 수 있음
    public SingleIconChooserDialog(JFrame parent, String currentSelection, List<String> usedIcons) {
        super(parent, "감정 선택", true); // modal 다이얼로그로 설정
        Color PASTEL_YELLOW = new Color(254, 255, 216);

        // [수정 1] GridLayout 간격을 5 -> 15로 늘림 (여백 10 추가효과)
        JPanel panel = new JPanel(new GridLayout(4, 3, 15, 15));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30)); // 좌우 패딩 20 -> 30 (너비 증가)
        panel.setBackground(PASTEL_YELLOW);

        this.selectedIcon = currentSelection;
        this.currentIconInSlot = currentSelection; // 현재 슬롯의 아이콘 저장

        // 아이콘 버튼 생성
        for (int i = 0; i < allIcons.length; i++) {
            String icon = allIcons[i];

            // 둥근 모서리 버튼으로 변경
            JButton btn = new JButton(icon) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // 배경 그리기 (둥근 모서리)
                    g2d.setColor(getBackground());
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                    g2d.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setFont(UIFonts.EMOJI);
            btn.setBackground(PASTEL_YELLOW);
            btn.setPreferredSize(new Dimension(70, 60)); // 버튼 크기 지정 (너비 70, 높이 60)
            btn.setFocusPainted(false);
            btn.setBorder(new ButtonFactory.RoundedBorder(Color.BLACK, 1, 10));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setContentAreaFilled(false);
            btn.setOpaque(false); // 들뜸 방지

            // 이미 선택된 아이콘 인덱스 찾기
            if (icon.equals(currentSelection)) {
                selectedIndex = i;
            }

            // 사용 중인 아이콘 비활성화 (단, 현재 슬롯의 아이콘은 제외)
            if (usedIcons.contains(icon) && !icon.equals(currentSelection)) {
                btn.setEnabled(false);
            }

            int finalI = i;
            btn.addActionListener(e -> {
                // 비활성화된 버튼은 클릭 무시 (Enabled false면 이벤트 안오지만 방어코드)
                if (!btn.isEnabled()) return;

                selectedIcon = icon;
                selectedIndex = finalI;
                updateSelectionHighlight();

                // 약간의 딜레이 후 닫기 (시각적 피드백 위함) or 바로 닫기
                dispose();
            });

            iconButtons[i] = btn;
            panel.add(btn);
        }

        // 초기 하이라이트 적용
        updateSelectionHighlight();

        add(panel, BorderLayout.CENTER);

        // 하단 취소 버튼
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(PASTEL_YELLOW);
        bottomPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // 둥근 모서리 버튼으로 변경
        JButton cancelBtn = new JButton("취소") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 배경 그리기 (둥근 모서리)
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.dispose();
                super.paintComponent(g);
            }
        };
        cancelBtn.setPreferredSize(new Dimension(80, 35));
        cancelBtn.setFont(UIFonts.BUTTON);
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(new ButtonFactory.RoundedBorder(Color.DARK_GRAY, 1, 10));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setOpaque(false); // 들뜸 방지
        cancelBtn.addActionListener(e -> {
            selectedIcon = null; // 취소 시 선택 없음
            dispose();
        });

        bottomPanel.add(cancelBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
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

            // allIconLabels가 null일 경우 초기화
            if (allIconLabels == null) {
                allIconLabels = new JLabel[0];
            }

            // 다른 슬롯에서 사용 중인 아이콘 목록을 만듦
            List<String> usedIcons = new ArrayList<>();
            for (JLabel lbl : allIconLabels) {
                String icon = lbl.getText();
                if (!icon.equals("[ ]") && !icon.equals(this.currentIconInSlot)) {
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
        // 배경색 설정
        Color defaultBg = new Color(254, 255, 216);
        Color selectedBg = new Color(254, 255, 216);

        for (int i = 0; i < iconButtons.length; i++) {
            JButton btn = iconButtons[i];

            if (i == selectedIndex) {
                // 선택된 상태
                btn.setBackground(selectedBg);
            } else if (!btn.isEnabled()) {
                // 비활성화 상태
                btn.setBackground(new Color(240, 240, 240)); // 회색
            } else {
                // 기본 상태
                btn.setBackground(defaultBg);
            }
        }
    }
}