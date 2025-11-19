package view;

import javax.swing.*;
import write.WriteDiaryGUI;

public class OpenDiaryViewWindow extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public OpenDiaryViewWindow(String diaryTitle) {
		setTitle(diaryTitle + " 열람");
        setSize(550, 700);
        setLocation(550, 0);
        add(new WriteDiaryGUI());
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }
}
