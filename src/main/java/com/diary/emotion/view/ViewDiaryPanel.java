package com.diary.emotion.view;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.*;

import com.diary.emotion.write.WriteDiaryGUI;

public class ViewDiaryPanel extends WriteDiaryGUI {

    private static final long serialVersionUID = 1L;

    JButton editBtn;
    JButton deleteBtn;
    
    protected Color getBackgroundColor() {
        return new Color(255, 218, 185); // salmon
    }

    public ViewDiaryPanel() {

        super();
        
        getBackgroundColor();

        // 질문 라벨 안 보이게
        questionLabel.setVisible(false);

        southPanel.remove(newPostButton);	// 다시쓰기 버튼 삭제
        southPanel.remove(saveButton);		// 저장 버튼 삭제
        southPanel.revalidate();        	// 레이아웃 다시 계산
        southPanel.repaint();           	// 화면 갱신

        // 각 컴포넌트들 수정 불가 상태로 만듦
        titleField.setEditable(false);
        contentArea.setEditable(false);
        for (JTextField tf : valueFields) tf.setEditable(false);
        stressSlider.setFocusable(false);
        stressSlider.setEnabled(false);

        Color bisque = new Color(255, 245, 238);

        // 컴포넌트들 배경색 설정
        titleField.setBackground(bisque);
        contentArea.setBackground(bisque);
        stressValueField.setBackground(bisque);
        
        // for 루프 하나로 3가지 작업 동시 처리
        for (int i = 0; i < 4; i++) {
            
            // 1. 패널 배경색 변경
            slotPanels[i].setBackground(bisque);
            
            // 2. 필드 배경색 변경
            valueFields[i].setBackground(bisque);
            
            // 3. 아이콘 라벨의 마우스 리스너 제거
            for (MouseListener ml : iconLabels[i].getMouseListeners()) {
            	iconLabels[i].removeMouseListener(ml);
            }
        }

        // "저장하기" 버튼 위치에 "수정하기" 버튼 넣음
        deleteBtn = new JButton("삭제");
        southPanel.add(deleteBtn);
        editBtn = new JButton("수정하기");
        southPanel.add(editBtn);

    }
}