
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.awt.event.*;
import java.util.*; 
import javax.swing.*;

/*
 * 아이콘 1개를 선택하기 위한 팝업 다이얼로그
 */
public class SingleIconChooserDialog extends JDialog {
    private static final long serialVersionUID = 1L;
	// 12개 아이콘 목록
    String[] allIcons = {"😊", "😆", "😍", "😌", "😂","🤗","😢", "😠", "😧", "😰", "😅","😔"}; //일단 임시
    String selectedIcon = null; // 사용자가 최종 선택한 아이콘

    JLabel[] allIconLabels; // 메인 GUI의 4개 아이콘 라벨 (중복 검사용)
    String currentIconInSlot = null; // 현재 클릭한 슬롯의 아이콘 (중복 검사 제외용)
    JButton[] iconButtons = new JButton[12]; // 12개 버튼 (활성화/비활성화용)

    public SingleIconChooserDialog(JFrame parent, JLabel[] iconLabels, Color bgColor) {
        super(parent, "아이콘 선택", true);
        this.allIconLabels = iconLabels;
        
        getContentPane().setBackground(bgColor); // 노란색 배경
        setLayout(new BorderLayout(10, 10));

        JPanel iconPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        iconPanel.setBackground(bgColor); // 노란색 배경
        
        // 아이콘 버튼 클릭 시 공통 리스너
        ActionListener iconListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedIcon = e.getActionCommand(); // 클릭한 아이콘 저장
                setVisible(false); // 팝업 닫기
            }
        };

        for (int i = 0; i < allIcons.length; i++) {
            JButton iconButton = new JButton(allIcons[i]);
            iconButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
            iconButton.setBackground(bgColor); // 노란색 버튼
            iconButton.setBorder(BorderFactory.createEtchedBorder()); 
            iconButton.addActionListener(iconListener);
            iconButtons[i] = iconButton; // 배열에 버튼 저장
            iconPanel.add(iconButton);
        }

        JButton cancelButton = new JButton("취소");
        cancelButton.setBackground(bgColor); // 노란색 버튼
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(bgColor); // 노란색 배경
        southPanel.add(cancelButton);
        
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedIcon = null; // 선택 없음
                setVisible(false); // 팝업 닫기
            }
        });
        
        add(iconPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        
        setSize(300, 350); // 고정 크기
        setLocationRelativeTo(parent);
    }

    // 메인 GUI가 선택된 아이콘을 가져가는 메소드
    public String getSelectedIcon() {
        return selectedIcon;
    }
    
    // 팝업창이 뜨기 직전, 메인 GUI가 현재 슬롯의 아이콘을 알려주는 메소드
    public void setCurrentSlot(int index, String currentIcon) {
        this.currentIconInSlot = currentIcon;
    }
    
    // 팝업창이 보일 때마다(setVisible(true)) 호출됨
    @Override
    public void setVisible(boolean b) {
        if (b) {
            selectedIcon = null; // 결과값 초기화
            
            // 1. "다른" 슬롯에서 이미 사용 중인 아이콘 목록 생성
            List<String> usedIcons = new ArrayList<>();
            for (int i = 0; i < allIconLabels.length; i++) {
                String icon = allIconLabels[i].getText();
                // (빈 슬롯 아니고) && (내가 지금 클릭한 슬롯의 아이콘도 아닐 때)
                if (!icon.equals("[ ]") && !icon.equals(this.currentIconInSlot)) {
                    usedIcons.add(icon);
                }
            }
            
            // 2. 12개 버튼을 순회하며 중복 검사
            for (JButton btn : iconButtons) {
                if (usedIcons.contains(btn.getText())) {
                    btn.setEnabled(false); // 이미 쓰였으면 비활성화
                } else {
                    btn.setEnabled(true); // 아니면 활성화
                }
            }
        }
        super.setVisible(b);
    }
}

