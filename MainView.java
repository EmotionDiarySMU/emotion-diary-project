package share;

import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

//import view.ViewWindow;
//import write.WriteDiaryGUI;

public class MainView extends JFrame {
	
	CardLayout cardLayout;
	JPanel cardPanel;
	
	public MainView() {
		setTitle("Emotion Diary");
        setSize(550, 700);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
		JButton write = new JButton("쓰기");
		JButton view = new JButton("열람");
		JButton chart = new JButton("통계");
		
		JButton[] buttons = {write, view, chart};
        for (JButton b : buttons) {
            menuBar.add(b);
        }
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        //JPanel writePanel = new WriteDiaryGUI();
        //JPanel viewPanel = new ViewWindow();s
        JPanel chartPanel = new StatisticsView();
        
        //cardPanel.add(writePanel, "write");
        //cardPanel.add(viewPanel, "view");
        cardPanel.add(chartPanel, "chart");
        
        add(cardPanel);

        // 버튼 클릭 시 화면 전환
        write.addActionListener(e -> cardLayout.show(cardPanel, "write"));
        view.addActionListener(e -> cardLayout.show(cardPanel, "view"));
        chart.addActionListener(e -> cardLayout.show(cardPanel, "chart"));

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
