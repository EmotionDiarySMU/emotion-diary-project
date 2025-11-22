package share;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import view.ViewWindow;

public class MainView extends JFrame {
	
	public MainView() {
		setTitle("Emotion Diary - View");
        setSize(550, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 일기 열람 패널만 표시
        JPanel viewPanel = new ViewWindow();
        add(viewPanel, BorderLayout.CENTER);

        setVisible(true);
	}
}
