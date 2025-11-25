package view;

import java.awt.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;

import DB.DiaryEntry;
import DB.DatabaseManager;

public class SearchDiaryPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public static Set<ExtraWindow> openWindows = new HashSet<>();
	
	public static DefaultListModel<String> diaryModel;
	public static List<DiaryEntry> diaryEntries = new ArrayList<>();
	
	static JTextField titleFd;
	
	static DateSelectorPanel firstDS;
	static DateSelectorPanel secondDS;

	public SearchDiaryPanel() {
		
		setLayout(new BorderLayout());
		
		// 제목 검색
		JPanel titlePn = new JPanel();
		JLabel titleLb = new JLabel("제목: ");
		titleFd = new JTextField(30);
		titlePn.add(titleLb);
		titlePn.add(titleFd);
		
		
		// 날짜 검색
		JPanel datePn = new JPanel();
		datePn.setLayout(new BoxLayout(datePn, BoxLayout.Y_AXIS));
		
		// 1줄: "날짜:" + 첫 번째 DateSelectorPanel
		JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
		firstRow.add(new JLabel("날짜: "));
		firstDS = new DateSelectorPanel();
		firstRow.add(firstDS);
		
		// 2줄: "~" + 두 번째 DateSelectorPanel
		JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
		secondRow.add(new JLabel("~"));
		secondDS = new DateSelectorPanel();
		secondRow.add(secondDS);
		
		// 패널에 두 줄 추가
		datePn.add(firstRow);
		datePn.add(secondRow);
		
		
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
		
		latest.addActionListener(e -> {
		    diaryEntries.sort((a, b) -> b.getEntry_id() - a.getEntry_id()); // 내림차순
		    refreshDiaryModel(false); // JList 갱신
		});

		// 오래된순 버튼 클릭 시
		oldest.addActionListener(e -> {
		    diaryEntries.sort((a, b) -> a.getEntry_id() - b.getEntry_id()); // 오름차순
		    refreshDiaryModel(false); // JList 갱신
		});
		
		
		// {제목 검색, 날짜 검색, 검색 버튼, 정렬 버튼} 묶음
		JPanel searching = new JPanel();
        searching.setLayout(new BoxLayout(searching, BoxLayout.Y_AXIS));
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
		        
		        // 이미 같은 entry 창이 열려 있는지 검사
	            for (ExtraWindow w : openWindows) {
	                if (w.entryId == entry.getEntry_id()) {
	                    w.toFront();
	                    return;
	                }
	            }
	            
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(SearchDiaryPanel.this);
                if (parentFrame == null) return; 

                // 부모 창의 현재 위치(좌표)와 너비
                int parentX = parentFrame.getX();
                int parentY = parentFrame.getY();
                int parentWidth = parentFrame.getWidth();
                
                // 새 창을 부모 창의 오른쪽 끝 바로 옆에 배치할 X, Y 좌표 계산
                int newX = parentX + parentWidth;
                int newY = parentY;
                
                // 4. 새 창을 열고 계산된 위치에 배치, openWindows 셋에 추가
                ExtraWindow win = new ExtraWindow(entry);
                win.setLocation(newX, newY);

                openWindows.add(win);

                win.addWindowListener(new WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        openWindows.remove(win);
                    }
                });
            }
        });
		
		
		serchBt.addActionListener(e -> {
		    refreshDiaryModel(true); // 모델 갱신
		});


		
		// JList 메인 컨테이너에 추가
		add(scrollPane, BorderLayout.CENTER);
		
		refreshDiaryModel(true);
	}
	
	public static Timestamp getStartTimestamp() {
	    int year = firstDS.getYear();
	    int month = firstDS.getMonth();
	    int day = firstDS.getDay();

	    if (year == -1) return null;

	    if (month == -1) month = 1;
	    if (day == -1) day = 1;

	    LocalDate date = LocalDate.of(year, month, day);
	    return Timestamp.valueOf(date.atStartOfDay());
	}

	public static Timestamp getEndTimestamp() {
	    int year = secondDS.getYear();
	    int month = secondDS.getMonth();
	    int day = secondDS.getDay();

	    if (year == -1) return null;

	    if (month == -1) month = 12;
	    if (day == -1) day = YearMonth.of(year, month).lengthOfMonth();

	    LocalDate date = LocalDate.of(year, month, day);
	    return Timestamp.valueOf(date.atTime(23, 59, 59));
	}

	
	
	public static void refreshDiaryModel(boolean fromDB) {
		if (fromDB) {
			String keyword = titleFd.getText().trim();
		    Timestamp start = getStartTimestamp();
		    Timestamp end = getEndTimestamp();

		    diaryEntries = DatabaseManager.getEntries(keyword, start, end);
	    }
        diaryModel.clear(); // 기존 항목 제거
        for (DiaryEntry e : diaryEntries) {
        	String entryText = e.getTitle() + " (작성일: " + e.getEntry_date().toLocalDateTime().toLocalDate().toString() + ")";
            
            if (e.getModify_date() != null) {
                entryText += " (수정일: " + e.getModify_date().toLocalDateTime().toLocalDate().toString() + ")";
            }
            
            diaryModel.addElement(entryText);
        }
    }
	
}


