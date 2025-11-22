package share;

import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import view.ViewWindow;
import chart.MainApplication;

public class MainView extends JFrame {
	
	CardLayout cardLayout;
	JPanel cardPanel;
	
	public MainView() {
		setTitle("Emotion Diary - View & Statistics");
        setSize(550, 700);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
		JButton view = new JButton("열람");
		JButton chart = new JButton("통계");
		
		JButton[] buttons = {view, chart};
        for (JButton b : buttons) {
            menuBar.add(b);
        }
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        JPanel viewPanel = new ViewWindow();
        JPanel chartPanel = new MainApplication();
        
        cardPanel.add(viewPanel, "view");
        cardPanel.add(chartPanel, "chart");
        
        add(cardPanel);

        // 버튼 클릭 시 화면 전환
        view.addActionListener(e -> cardLayout.show(cardPanel, "view"));
        chart.addActionListener(e -> cardLayout.show(cardPanel, "chart"));

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
