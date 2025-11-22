package com.diary.emotion.view;

import java.awt.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.diary.emotion.db.DiaryEntry;
import com.diary.emotion.db.DatabaseManager;

public class SearchDiaryPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private static ExtraWindow currentWindow = null;
	
	public static DefaultListModel<String> diaryModel;
	public static List<DiaryEntry> diaryEntries = new ArrayList<>();

	public SearchDiaryPanel() {
		
		setLayout(new BorderLayout());
		
		// 제목 검색
		JPanel titlePn = new JPanel();
		JLabel titleLb = new JLabel("제목: ");
		JTextField titleFd = new JTextField(30);
		titlePn.add(titleLb);
		titlePn.add(titleFd);
		
		// 날짜 검색
		JPanel datePn = new JPanel();
		JLabel dateLb = new JLabel("날짜: ");
		datePn.add(dateLb);
		DateSelectorPanel firstDS = new DateSelectorPanel();
		DateSelectorPanel secondDS = new DateSelectorPanel();
		datePn.add(firstDS);
		datePn.add(new JLabel("~"));
		datePn.add(secondDS);
		
		// 검색 버튼
		JPanel serchBtWrapPn = new JPanel(); 
		JButton serchBt = new JButton("검색");
		serchBtWrapPn.add(serchBt); // FlowLayout이 버튼의 선호 크기 유지
		
		// 정렬 버튼
		JPanel array = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton latest = new JButton("최신순");
		JButton oldest = new JButton("오래된순");
		array.add(latest);
		array.add(oldest);
		
		// {제목 검색, 날짜 검색, 검색 버튼, 정렬 버튼} 묶음
		JPanel searching = new JPanel(new GridLayout(4,1));
		searching.add(titlePn);
		searching.add(datePn);
		searching.add(serchBtWrapPn);
		searching.add(array);	
		
		// 묶음을 메인 컨테이너에 추가
		add(searching, BorderLayout.NORTH);

		diaryModel = new DefaultListModel<>();
		JList<String> diaries = new JList<>(diaryModel);
		JScrollPane scrollPane = new JScrollPane(diaries);

		// JList 선택시 해당 일기 열람 창 띄우기
		diaries.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        int index = diaries.getSelectedIndex();
		        DiaryEntry entry = diaryEntries.get(index);
		        
		        // 같은 창이면 아무것도 하지 않고 리턴
		        if (currentWindow != null && currentWindow.entryId == entry.getEntry_id()) return;
		        
		        // 이미 열린 창이 있으면 닫기
                if (currentWindow != null) {
                    currentWindow.dispose();
                }
                
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(SearchDiaryPanel.this);
                
                if (parentFrame == null) return; 

                // 부모 창의 현재 위치(좌표)와 너비를 얻습니다.
                int parentX = parentFrame.getX();
                int parentY = parentFrame.getY();
                int parentWidth = parentFrame.getWidth();
                
                // 새 창을 부모 창의 오른쪽 끝 바로 옆에 배치할 X, Y 좌표 계산
                int newX = parentX + parentWidth;
                int newY = parentY;
                
                // 4. 새 창을 열고 계산된 위치에 배치합니다.
                currentWindow = new ExtraWindow(entry); // ExtraWindow 생성
                currentWindow.setLocation(newX, newY); // 위치 적용 (바로 옆)
                
                // X로 닫았을 때 null 처리
                currentWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        currentWindow = null;
                    }
                });
            }
        });
		
		// JList 메인 컨테이너에 추가
		add(scrollPane, BorderLayout.CENTER);
		
		refreshDiaryList();
	}
	
	public static void refreshDiaryList() {
        try {
        	diaryEntries = DatabaseManager.getAllEntries();
            diaryModel.clear(); // 기존 항목 제거
            for (DiaryEntry e : diaryEntries) {
                diaryModel.addElement(e.getTitle() + " (" + e.getEntry_date() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


