package view;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import share.SaveQuestion;

public class ExtraWindow extends JFrame{
	
	private static final long serialVersionUID = 1L;

	CardLayout cardLayout;
	JPanel cardPanel;
	
	public ExtraWindow(String diaryTitle) {
        
		setTitle(diaryTitle);
        setSize(550, 700);
        setLocation(550, 0);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        ViewDiaryPanel viewPanel = new ViewDiaryPanel();
        ModifyPanel modifyPanel = new ModifyPanel();
        
        
        cardPanel.add(viewPanel, "view");
        cardPanel.add(modifyPanel, "modify");
        
        add(cardPanel);
        
        viewPanel.editBtn.addActionListener(e -> cardLayout.show(cardPanel, "modify"));
        modifyPanel.saveButton.addActionListener(e -> {
            if (!modifyPanel.isModified) cardLayout.show(cardPanel, "view");
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
