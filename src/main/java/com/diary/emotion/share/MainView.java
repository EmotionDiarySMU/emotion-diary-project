package com.diary.emotion.share;

import java.awt.CardLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;

import com.diary.emotion.view.ExtraWindow;
import com.diary.emotion.view.SearchDiaryPanel;
import com.diary.emotion.write.WriteDiaryGUI;
import com.diary.emotion.stats.StatisticsView;
import com.diary.emotion.login.AuthenticationFrame;
import com.diary.emotion.stats.StatisticsController;
import com.diary.emotion.stats.StatisticsDAO;

public class MainView extends JFrame {
	
	// ⭐ 싱글톤
	// ⭐ 정적(static) 필드로 자기 자신의 인스턴스 선언
    private static MainView instance;

    // ⭐️ 정적(static) 접근 메서드
    public static MainView getInstance() {
        return instance;
    }
    
    // ⭐️ 통계 갱신을 외부에 제공하는 메서드
    public void refreshStatistics() {
            statisticsController.refreshCharts();
    }
	
	private static final long serialVersionUID = 1L;
	
	CardLayout cardLayout;
	JPanel cardPanel;
	
	int flag;
	
	public WriteDiaryGUI writePanel;
	public SearchDiaryPanel viewPanel;

	public StatisticsView statisticsView;
	public StatisticsDAO statisticsDAO;
	public StatisticsController statisticsController;
	
	// AuthenticationFrame을 참조할 필드
    private AuthenticationFrame authFrame; 

    public MainView(AuthenticationFrame authFrame) {
        
        this.authFrame = authFrame; // AuthenticationFrame 객체 참조 저장
        instance = this;
		
		// 창 설정
		setTitle("Emotion Diary");
        setSize(495, 630);
        setMinimumSize(new java.awt.Dimension(495, 630));
        setLocationRelativeTo(null);
        
        // 메뉴바 생성 후 창에 추가
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        // 버튼 생성
		JButton write = new JButton("쓰기");
		JButton view = new JButton("열람");
		JButton chart = new JButton("통계");
		
		// 버튼을 메뉴바에 추가
		JButton[] leftButtons = {write, view, chart};
        for (JButton b : leftButtons) {
            menuBar.add(b);
        }
        
        menuBar.add(Box.createHorizontalGlue());
        
        // 메뉴와 메뉴아이템 생성
        JMenu account = new JMenu("사용자: " + AuthenticationFrame.loggedInUserId);
        JMenuItem logoutButton = new JMenuItem("로그아웃");
        JMenuItem deleteAccount = new JMenuItem("계정삭제");
        
        // 메뉴에 메뉴아이템 추가
        account.add(logoutButton);
        account.add(deleteAccount);
        
        // 메뉴를 메뉴바에 추가
        menuBar.add(account);
        
        // cardLayout 패널 생성
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // 쓰기, 열람, 통계 패널 생성
        writePanel = new WriteDiaryGUI();
        viewPanel = new SearchDiaryPanel();

        // 통계 패널 초기화 (MVC 패턴)
        statisticsView = new StatisticsView();
        statisticsDAO = new StatisticsDAO();
        statisticsController = new StatisticsController(statisticsView, statisticsDAO);

        // 각 3개의 패널을 cardLayout 패널에 추가
        cardPanel.add(writePanel, "write");
        cardPanel.add(viewPanel, "view");
        cardPanel.add(statisticsView, "chart");

        // cardLayout 패널 창에 추가
        add(cardPanel);

        // 버튼 클릭 시 화면 전환 리스너
        write.addActionListener(e -> cardLayout.show(cardPanel, "write"));
        view.addActionListener(e -> cardLayout.show(cardPanel, "view"));
        chart.addActionListener(e -> {
            cardLayout.show(cardPanel, "chart");
            // 통계 탭 클릭 시 차트 강제 새로고침
            statisticsController.refreshCharts();
        });
        
        // 로그아웃 버튼 리스너
        logoutButton.addActionListener(e -> logout());
        
        // 삭제 버튼 리스너
        deleteAccount.addActionListener(e -> {
            
            // 1. 사용자에게 계정 삭제를 다시 한 번 확인할 경고 창 표시
            int result = JOptionPane.showConfirmDialog(
                MainView.this,
                "계정을 삭제하시겠습니까?\n모든 데이터가 영구히 삭제됩니다.", // 메시지
                "경고: 계정 삭제 확인", // 제목
                JOptionPane.YES_NO_OPTION, // 옵션: 예, 아니오
                JOptionPane.WARNING_MESSAGE // 아이콘: 경고 아이콘
            );

            // 2. 사용자의 선택에 따른 작동
            switch (result) {
	            case JOptionPane.YES_OPTION:
	            	new DeleteAccountDialog(MainView.this); // 새 다이얼로그 띄우기
	                break;
	            case JOptionPane.NO_OPTION:
	                break;
            }
        });

        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        
        // 닫기전에 "저장하시겠습니까?" 창 띄우기
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		flag = 0;
        		
    			for (ExtraWindow win : new HashSet<>(viewPanel.openWindows)) {
    				flag = SaveQuestion.handleWindowClosing(win, win.modifyPanel, 2);
    				if (flag == 1) return;
    			}
        		SaveQuestion.handleWindowClosing(MainView.this, writePanel, 1);
            }
        });

	}
    
    private void logout() {
    	flag = 0;
        
        // 저장 안 된 일기 수정들 정리
        for (ExtraWindow win : new HashSet<>(viewPanel.openWindows)) {
            flag = SaveQuestion.handleWindowClosing(win, win.modifyPanel, 2);
            if (flag == 1) return; // 사용자가 취소했을 경우 로그아웃 중단
        }
        
        // 저장 안 된 일기 쓰기 정리
        int flag2 = SaveQuestion.handleWindowClosing(MainView.this, writePanel, 2);
        
        if (flag2 == 1) return; // 사용자가 취소했을 경우 로그아웃 중단
        
        authFrame.clearLoginFields();
        
        // 로그인 정보 초기화
        AuthenticationFrame.loggedInUserId = null;
        
        // 로그인 화면(AuthenticationFrame) 다시 보이기
        authFrame.setVisible(true);
        authFrame.showPanel("LOGIN");
    }
}
	
	