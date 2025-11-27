package com.diary.emotion.share;

import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;

import com.diary.emotion.view.ExtraWindow;
import com.diary.emotion.view.SearchDiaryPanel;
import com.diary.emotion.write.WriteDiaryGUI;

public class MainView extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	CardLayout cardLayout;
	JPanel cardPanel;
	
	int flag;
	
	public MainView() {
		// 창 설정
		setTitle("Emotion Diary");
        setSize(495, 630);
        setLocationRelativeTo(null);
        
        // 메뉴바 생성 후 창에 추가
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        // 버튼 생성
		JButton write = new JButton("쓰기");
		JButton view = new JButton("열람");
		JButton chart = new JButton("통계");
		
		// 버튼 메뉴바에 추가
		JButton[] buttons = {write, view, chart};
        for (JButton b : buttons) {
            menuBar.add(b);
        }
        
        // cardLayout 패널 생성
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // 쓰기, 열람, 통계 패널 생성
        WriteDiaryGUI writePanel = new WriteDiaryGUI();
        SearchDiaryPanel viewPanel = new SearchDiaryPanel();
        JPanel chartPanel = new JPanel();
        
        // 각 3개의 패널을 cardLayout 패널에 추가
        cardPanel.add(writePanel, "write");
        cardPanel.add(viewPanel, "view");
        cardPanel.add(chartPanel, "chart");
        
        // cardLayout 패널 창에 추가
        add(cardPanel);

        // 버튼 클릭 시 화면 전환
        write.addActionListener(e -> cardLayout.show(cardPanel, "write"));
        view.addActionListener(e -> cardLayout.show(cardPanel, "view"));
        chart.addActionListener(e -> cardLayout.show(cardPanel, "chart"));

        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        
        // 닫기전에 "저장하시겠습니까?" 창 띄우기
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		flag = 0;
        		
    			for (ExtraWindow win : new HashSet<>(SearchDiaryPanel.openWindows)) {
    				flag = SaveQuestion.handleWindowClosing(win, win.modifyPanel, 2);
    				if (flag == 1) return;
    			}
        		SaveQuestion.handleWindowClosing(MainView.this, writePanel, 1);
            }
        });
        
	}
}
	
	