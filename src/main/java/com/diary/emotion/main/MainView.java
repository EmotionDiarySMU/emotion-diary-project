package com.diary.emotion.main;

import java.awt.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

import com.diary.emotion.ui.*;
import com.diary.emotion.write.WriteDiaryGUI;
import com.diary.emotion.write.SaveQuestion;
import com.diary.emotion.view.SearchDiaryPanel;
import com.diary.emotion.view.ExtraWindow;
import com.diary.emotion.stats.StatisticsController;
import com.diary.emotion.stats.StatisticsView;
import com.diary.emotion.stats.StatisticsDAO;

import static com.diary.emotion.ui.UIColors.*;
import static com.diary.emotion.ui.UIFonts.*;

/**
 * 메인 화면
 * - 일기 쓰기, 열람, 통계 탭 전환
 * - 각 탭별 테마 색상 적용
 */
public class MainView extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	CardLayout cardLayout;
	JPanel cardPanel;
	
	// 탭 버튼들
	JButton writeTab, viewTab, chartTab;

	int flag;
	
	public static StatisticsController statisticsController;

	// SearchDiaryPanel 인스턴스
	public static SearchDiaryPanel searchDiaryPanel;

	// WriteDiaryGUI 인스턴스 (탭 전환 시 저장 확인용)
	private WriteDiaryGUI writePanel;

	public MainView() {
		// UIConstants로 창 설정
		UIConstants.setupFrame(this);

        // 상단 탭 패널 생성
        JPanel tabPanel = createTabPanel();
        add(tabPanel, BorderLayout.NORTH);

        // cardLayout 패널 생성
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // 쓰기, 열람, 통계 패널 생성
        writePanel = new WriteDiaryGUI();
        searchDiaryPanel = new SearchDiaryPanel();
        StatisticsView chartPanel = new StatisticsView();

        // 통계 컨트롤러 초기화
        StatisticsDAO statisticsDAO = new StatisticsDAO();
        statisticsController = new StatisticsController(chartPanel, statisticsDAO);

        // 각 3개의 패널을 cardLayout 패널에 추가
        cardPanel.add(writePanel, "write");
        cardPanel.add(searchDiaryPanel, "view");
        cardPanel.add(chartPanel, "chart");
        
        // cardLayout 패널 창에 추가
        add(cardPanel, BorderLayout.CENTER);

        // 기본으로 일기 쓰기 탭 활성화
        showWritePanel();

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

	/**
	 * 상단 탭 패널 생성
	 */
	private JPanel createTabPanel() {
		// 탭 버튼들을 왼쪽 정렬로 배치
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // FlowLayout.LEFT로 설정
        panel.setBackground(Color.WHITE); // 탭 패널 배경색
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)); // 하단 검정 테두리

        // 일기 쓰기 탭
        writeTab = createTabButton("일기 쓰기");
        writeTab.addActionListener(e -> showWritePanel());

        // 열람 탭
        viewTab = createTabButton("열람");
        viewTab.addActionListener(e -> showViewPanel());

        // 통계 탭
        chartTab = createTabButton("통계");
        chartTab.addActionListener(e -> showChartPanel());

        panel.add(writeTab);
        panel.add(viewTab);
        panel.add(chartTab);

        return panel;
	}

	/**
	 * 탭 버튼 생성
	 */
	private JButton createTabButton(String text) {
		JButton btn = new JButton(text);
		btn.setFont(BODY_REGULAR);
		btn.setForeground(TEXT_SECONDARY);
		btn.setBackground(Color.WHITE); // 비활성 탭 배경색
		btn.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK), // 검정 테두리 (상, 좌, 우)
			BorderFactory.createEmptyBorder(12, 25, 12, 25) // 내부 패딩
		));
		btn.setFocusPainted(false);
		btn.setOpaque(true); // 배경색이 보이도록 설정
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// 호버 효과
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!btn.getFont().isBold()) {  // 비활성 탭만
					btn.setBackground(new Color(245, 245, 245)); // 연한 회색
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!btn.getFont().isBold()) {  // 비활성 탭만
					btn.setBackground(Color.WHITE);
				}
			}
		});

		return btn;
	}

	/**
	 * 활성 탭 설정
	 */
	private void setActiveTab(JButton activeBtn, Color bgColor, Color accentColor) {
		// 모든 탭 초기화 (비활성 상태로)
		writeTab.setFont(BODY_REGULAR);
		writeTab.setForeground(TEXT_SECONDARY);
		writeTab.setBackground(Color.WHITE); // 비활성 탭 배경
		writeTab.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK), // 검정 테두리
			BorderFactory.createEmptyBorder(12, 25, 12, 25)
		));

		viewTab.setFont(BODY_REGULAR);
		viewTab.setForeground(TEXT_SECONDARY);
		viewTab.setBackground(Color.WHITE); // 비활성 탭 배경
		viewTab.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK), // 검정 테두리
			BorderFactory.createEmptyBorder(12, 25, 12, 25)
		));

		chartTab.setFont(BODY_REGULAR);
		chartTab.setForeground(TEXT_SECONDARY);
		chartTab.setBackground(Color.WHITE); // 비활성 탭 배경
		chartTab.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK), // 검정 테두리
			BorderFactory.createEmptyBorder(12, 25, 12, 25)
		));

		// 선택된 탭 강조
		activeBtn.setFont(new Font(BODY_REGULAR.getName(), Font.BOLD, BODY_REGULAR.getSize()));
		activeBtn.setForeground(TEXT_PRIMARY);
		activeBtn.setBackground(bgColor);
		activeBtn.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK), // 검정 테두리
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(12, 25, 9, 25),
				BorderFactory.createMatteBorder(0, 0, 3, 0, accentColor) // 하단 강조색 테두리
			)
		));
	}

	/**
	 * 일기 쓰기 패널 표시
	 */
	private void showWritePanel() {
		setActiveTab(writeTab, BG_WRITE, ACCENT_GREEN);
		cardLayout.show(cardPanel, "write");
	}

	/**
	 * 열람 패널 표시
	 */
	private void showViewPanel() {
		// 일기 쓰기 탭에서 수정된 내용이 있으면 저장 확인
		if (writePanel.isModified) {
			int result = JOptionPane.showConfirmDialog(
				this,
				"저장되지 않은 변경 사항이 있습니다. 저장하시겠습니까?",
				"경고",
				JOptionPane.YES_NO_CANCEL_OPTION
			);

			if (result == JOptionPane.YES_OPTION) {
				writePanel.saveOrFinish();
				// 저장 후에도 isModified가 true면 저장 실패한 것이므로 탭 전환 취소
				if (writePanel.isModified) {
					return;
				}
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return; // 탭 전환 취소
			} else if (result == JOptionPane.NO_OPTION) {
				// 요구사항 7: No 클릭 시 작성 중인 일기 내용 모두 초기화
				writePanel.clearAllFields();
			}
		}

		setActiveTab(viewTab, BG_VIEW, ACCENT_ORANGE);
		cardLayout.show(cardPanel, "view");
	}

	/**
	 * 통계 패널 표시
	 */
	private void showChartPanel() {
		// 일기 쓰기 탭에서 수정된 내용이 있으면 저장 확인
		if (writePanel.isModified) {
			int result = JOptionPane.showConfirmDialog(
				this,
				"저장되지 않은 변경 사항이 있습니다. 저장하시겠습니까?",
				"경고",
				JOptionPane.YES_NO_CANCEL_OPTION
			);

			if (result == JOptionPane.YES_OPTION) {
				writePanel.saveOrFinish();
				// 저장 후에도 isModified가 true면 저장 실패한 것이므로 탭 전환 취소
				if (writePanel.isModified) {
					return;
				}
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return; // 탭 전환 취소
			} else if (result == JOptionPane.NO_OPTION) {
				// 요구사항 7: No 클릭 시 작성 중인 일기 내용 모두 초기화
				writePanel.clearAllFields();
			}
		}

		setActiveTab(chartTab, BG_STATS, ACCENT_BLUE);
		cardLayout.show(cardPanel, "chart");
		// 통계 탭으로 전환할 때마다 데이터 새로고침
		if (statisticsController != null) {
			statisticsController.refresh();
		}
	}
}
