package share;

import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import view.ViewWindow;
import write.WriteDiaryGUI;

public class MainView extends JFrame {
	private String loggedInUserId;
	private static final long serialVersionUID = 1L;
	
	CardLayout cardLayout;
	JPanel cardPanel;
	
	public MainView(String userId) { // [수정] UserId 받도록함
		this.loggedInUserId = userId; // [수정] 받아온 ID를 저장해둠
		
		// 창 설정
		setTitle("Emotion Diary");
        setSize(550, 700);
        
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
        WriteDiaryGUI writePanel = new WriteDiaryGUI(userId);
        ViewWindow viewPanel = new ViewWindow();
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
                if (writePanel.isModified) {
                    int result = JOptionPane.showConfirmDialog(MainView.this, 
                            "저장되지 않은 변경 사항이 있습니다. 저장하시겠습니까?", 
                            "경고", 
                            JOptionPane.YES_NO_CANCEL_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                    	writePanel.saveButton.doClick();
                        if (writePanel.isModified) return;
                        dispose();
                    } else if (result == JOptionPane.NO_OPTION) {
                    	dispose();
                    }
                } else {
                	dispose();
                }
            }
        });
        
	}
}