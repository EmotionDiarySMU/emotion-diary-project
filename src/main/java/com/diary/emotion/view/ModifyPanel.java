package com.diary.emotion.view;

import java.awt.Color;

import javax.swing.JButton;

import com.diary.emotion.write.WriteDiaryGUI;

public class ModifyPanel extends WriteDiaryGUI {
	
	protected Color getBackgroundColor() {
        return new Color(255, 218, 185); // salmon
    }

    private static final long serialVersionUID = 1L;

    JButton fineditBtn;
    JButton cancelBtn;

    public ModifyPanel() {
    	
        super();
        
        getBackgroundColor();
        
        questionLabel.setVisible(false);

        southPanel.remove(newPostButton);	// 다시쓰기 버튼 삭제
        southPanel.remove(saveButton);		// 저장 버튼 삭제
        southPanel.revalidate();        	// 레이아웃 다시 계산
        southPanel.repaint();           	// 화면 갱신

        cancelBtn = new JButton("취소");
        fineditBtn = new JButton("수정완료");
        southPanel.add(cancelBtn);
        southPanel.add(fineditBtn);
    }

    // SaveQuestion 클래스에서 쓸 것
    public void saveOrFinish() {
        fineditBtn.doClick();
    }
}