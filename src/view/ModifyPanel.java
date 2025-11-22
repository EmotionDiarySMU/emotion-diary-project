package view;

import javax.swing.JButton;

import write.WriteDiaryGUI;

public class ModifyPanel extends WriteDiaryGUI {
	
    private static final long serialVersionUID = 1L;
    
    JButton fineditBtn;
    JButton cancelBtn;

	public ModifyPanel() {
        super();
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