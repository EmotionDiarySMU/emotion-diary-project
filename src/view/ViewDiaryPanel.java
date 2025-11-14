package view;

import java.awt.event.MouseListener;

import javax.swing.*;

import write.WriteDiaryGUI;

public class ViewDiaryPanel extends WriteDiaryGUI {
	
    private static final long serialVersionUID = 1L;
    
    JButton editBtn;

	public ViewDiaryPanel() {
		
        super();
        
        // 질문 라벨 안 보이게
        questionLabel.setVisible(false);
        
        southPanel.remove(saveButton);	// 저장 버튼 삭제
        southPanel.revalidate();        // 레이아웃 다시 계산
        southPanel.repaint();           // 화면 갱신
        
        // 각 컴포넌트들 수정 불가 상태로 만듦
        titleField.setEditable(false);
        contentArea.setEditable(false);
        for (JTextField tf : valueFields) tf.setEditable(false);
        stressSlider.setFocusable(false);
        stressSlider.setEnabled(false);
        
        // 아이콘 선택 라벨 수정 불가 상태로 만들기 위해 마우스리스너 제거
        for (JLabel icon : iconLabels) {
            for (MouseListener ml : icon.getMouseListeners()) { // 우리 코드에선 각 라벨들에 마우스리스너가 1개만 붙어있지만 안전하게 모든 마우스리스너를 찾아서 제거해 주게끔 했다
                icon.removeMouseListener(ml); // 기존 클릭 이벤트 제거
            }
        }
        
        // "저장하기" 버튼 위치에 "수정하기" 버튼 넣음
        editBtn = new JButton("수정하기");
        southPanel.add(editBtn);

    }
}

