package view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ViewWindow extends JPanel{
	private static final long serialVersionUID = 1L;

	public ViewWindow() {
		
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
		
		// 저장된 일기를 보여줄 JList
		String[] items = {"사과", "바나나", "딸기", "포도", "오렌지", "망고"};
		JList<String> diaries = new JList<>(items);
		JScrollPane scrollPane = new JScrollPane(diaries);

		// JList 선택시 해당 일기 열람 창 띄우기
		diaries.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        int index = diaries.locationToIndex(e.getPoint());
		        if (index != -1) {
		            String diaryTitle = diaries.getModel().getElementAt(index);
		            new OpenDiaryViewWindow(diaryTitle);
		        }
		    }
		});
		
		// JList 메인 컨테이너에 추가
		add(scrollPane, BorderLayout.CENTER);
		
	}
}
