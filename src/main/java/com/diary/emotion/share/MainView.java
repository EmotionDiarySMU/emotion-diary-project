package com.diary.emotion.share;

import com.diary.emotion.stats.StatisticsController;
import com.diary.emotion.stats.StatisticsDAO;
import com.diary.emotion.stats.StatisticsView;

import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainView extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	CardLayout cardLayout;
	JPanel cardPanel;
	
	public MainView() {
		setTitle("Emotion Diary");
        setSize(495, 630);
        setLocationRelativeTo(null);
        
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
        
        JPanel writePanel = new JPanel();
        JPanel viewPanel = new JPanel();

        StatisticsView chartPanel = new StatisticsView();
        StatisticsDAO dao = new StatisticsDAO();
        new StatisticsController(chartPanel, dao);

        cardPanel.add(writePanel, "write");
        cardPanel.add(viewPanel, "view");
        cardPanel.add(chartPanel, "chart");
        
        add(cardPanel);

        write.addActionListener(e -> cardLayout.show(cardPanel, "write"));
        view.addActionListener(e -> cardLayout.show(cardPanel, "view"));
        chart.addActionListener(e -> cardLayout.show(cardPanel, "chart"));

        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		SaveQuestion.handleWindowClosing(MainView.this, writePanel, true);
            }
        });
        
	}
}
