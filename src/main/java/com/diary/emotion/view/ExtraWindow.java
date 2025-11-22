package com.diary.emotion.view;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.diary.emotion.db.DiaryEntry;
import com.diary.emotion.share.SaveQuestion;

public class ExtraWindow extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	public ViewDiaryPanel viewPanel;
	public ModifyPanel modifyPanel;
	
	public int entryId;

	CardLayout cardLayout;
	JPanel cardPanel;
	
	public ExtraWindow(DiaryEntry entry) {
		
		this.entryId = entry.getEntry_id();
        
		setTitle("[ " + entry.getTitle() + " ]" + "   " + entry.getEntry_date());
        setSize(495, 630);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        viewPanel = new ViewDiaryPanel();
        modifyPanel = new ModifyPanel();
        
        viewPanel.fillEntry(entry);
        modifyPanel.fillEntry(entry);
        
        
        cardPanel.add(viewPanel, "view");
        cardPanel.add(modifyPanel, "modify");
        
        add(cardPanel);
        
        // 수정하기 버튼 누르면 modify 창 띄움
        viewPanel.editBtn.addActionListener(e -> cardLayout.show(cardPanel, "modify"));
        
        // 수정 완료 버튼 → DB 저장 후 view 창 띄움
        modifyPanel.fineditBtn.addActionListener(e -> {
        	
            for(int i=0;i<4;i++) modifyPanel.validateAndSaveEmotionValue(i); // 감정 수치 검증
            modifyPanel.validateAndSaveStressValue(); // 스트레스 수치 검증
            
            // 2. 감정 아이콘과 값 리스트 생성
            List<String> emotionIcons = new ArrayList<>();
            List<Integer> emotionValuesList = new ArrayList<>();
            for (int i = 0; i < modifyPanel.iconLabels.length; i++) {
                emotionIcons.add(modifyPanel.iconLabels[i].getText());
                emotionValuesList.add(modifyPanel.emotionValues[i]);
            }
            
            try{
                boolean success = DB.DatabaseManager.updateDiaryEntry(
                    entry.getEntry_id(),
                    modifyPanel.titleField.getText(),
                    modifyPanel.contentArea.getText(),
                    modifyPanel.stressSlider.getValue(),
                    emotionIcons,
                    emotionValuesList
                );
                if(success){
                	// entry 객체에 수정된 값 반영
                    entry.setTitle(modifyPanel.titleField.getText());
                    entry.setContent(modifyPanel.contentArea.getText());
                    entry.setStress_level(modifyPanel.stressSlider.getValue());

                    List<DB.Emotion> newEmotions = new ArrayList<>();
                    for (int i = 0; i < emotionIcons.size(); i++) {
                        DB.Emotion em = new DB.Emotion();
                        em.setEmoji_icon(emotionIcons.get(i));
                        em.setEmotion_level(emotionValuesList.get(i));
                        newEmotions.add(em);
                    }
                    
                    entry.setEmotions(newEmotions);
                    
                    JOptionPane.showMessageDialog(modifyPanel,"수정 완료");
                    
                    modifyPanel.isModified=false;
                    
                    viewPanel.fillEntry(entry); // viewPanel에 수정된 값 반영
                    
                    SearchDiaryPanel.refreshDiaryList(); // 목록 패널도 갱신
                    
                    cardLayout.show(cardPanel,"view");
                }else{
                    JOptionPane.showMessageDialog(modifyPanel,"DB 수정 실패","오류",JOptionPane.ERROR_MESSAGE);
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        });
        
        // 취소 버튼
        modifyPanel.cancelBtn.addActionListener(e -> {
        	// 1. 수정된 내용 모두 원래 entry 값으로 되돌리기
            modifyPanel.fillEntry(entry);

            // 2. 수정 플래그 false (창 닫을 때 저장 물어보지 않도록)
            modifyPanel.isModified = false;

            // 3. view 화면으로 전환
            cardLayout.show(cardPanel, "view");
        });
        
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // 닫기전에 "저장하시겠습니까?" 창 띄우기
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		SaveQuestion.handleWindowClosing(ExtraWindow.this, modifyPanel, false);
            }
        });

        
    }
}
