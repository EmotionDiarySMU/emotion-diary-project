package com.diary.emotion.view;

import javax.swing.*;
import java.awt.*;
import java.time.*;

public class DateSelectorPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JComboBox<Object> yearCombo;
    private JComboBox<Object> monthCombo;
    private JComboBox<Object> dayCombo;
    
    Object preSelected = "-";
    int flag = 0;

    public DateSelectorPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 5));
        initializeComponents();
        addChangeListeners();
    }

    // 🟡 콤보박스 생성, 설정
    private void initializeComponents() {
    	
    	Color salmon = new Color(255, 218, 185);
    	setBackground(salmon);
    	
        // 🔹 연도 콤보박스
        int currentYear = LocalDate.now().getYear();

        yearCombo = new JComboBox<>();
        yearCombo.setEditable(true);
        yearCombo.addItem("-"); // 선택 안 된 상태
        for (int i = 0; i < 5; i++) {
            yearCombo.addItem(currentYear - i);
        }

        // 🔹 월 콤보박스 (초기 비활성화)
        monthCombo = new JComboBox<>();
        monthCombo.setEditable(true);
        monthCombo.addItem("-");
        for (int i = 1; i <= 12; i++) {
            monthCombo.addItem(i);
        }
        monthCombo.setEnabled(false);

        // 🔹 일 콤보박스 (초기 비활성화)
        dayCombo = new JComboBox<>();
        dayCombo.setEditable(true);
        dayCombo.addItem("-");
        dayCombo.setEnabled(false);

        // 🔹 글자 설정 객체
        Font smallFont = new Font("Dialog", Font.PLAIN, 10);

        // 🔹 라벨 생성 후, 폰트 및 크기 설정
        JLabel yearLabel = new JLabel("년");
        JLabel monthLabel = new JLabel("월");
        JLabel dayLabel = new JLabel("일");
        yearLabel.setFont(smallFont);
        monthLabel.setFont(smallFont);
        dayLabel.setFont(smallFont);

        // 🔹 콤보박스 글자 폰트 및 크기 설정
        yearCombo.setFont(smallFont);
        monthCombo.setFont(smallFont);
        dayCombo.setFont(smallFont);

        // 🔹 콤보박스 크기 및 간격 설정
        Dimension comboSize = new Dimension(62, 25);
        yearCombo.setPreferredSize(new Dimension(74, 25));
        monthCombo.setPreferredSize(comboSize);
        dayCombo.setPreferredSize(comboSize);

        this.add(yearCombo);
        this.add(yearLabel);
        this.add(monthCombo);
        this.add(monthLabel);
        this.add(dayCombo);
        this.add(dayLabel);
    }

    // 🟡 날짜 선택 과정 로직 구현
    private void addChangeListeners() {
    	// ♦️ year 선택 리스너
    	yearCombo.addActionListener(e -> {
    	    Object yObj = yearCombo.getSelectedItem();

    	    if (yObj instanceof Integer) {
    	        // 1. 미리 추가된 Integer 항목을 선택했을 때
    	        flag = 1;
    	    }
    	    else if (yObj instanceof String) {
    	        // 2. "-" 선택하거나 사용자가 직접 텍스트 필드에 입력했을 때 (Enter 누른 경우)
    	        String inputYear = ((String) yObj).trim();
    	        
    	        if (inputYear.isEmpty() || inputYear.equals("-")) {
    	        	yearCombo.setSelectedItem("-");
    	        	flag = 0;
    	        }
    	        else {
	    	        try {
	    	            // String을 int로 변환 시도
	    	            int selectedYear = Integer.parseInt(inputYear);
	    	            
	                    int minYear = (int) yearCombo.getItemAt(1); // 최소값
	                    int maxYear = (int) yearCombo.getItemAt(yearCombo.getItemCount() - 1); // 최대값

	                    // 선택한 연도가 최소값보다 작으면 최소값으로 설정
	                    if (selectedYear < minYear) {
	                        yearCombo.setSelectedItem(minYear);
	                    }
	                    // 선택한 연도가 최대값보다 크면 최대값으로 설정
	                    else if (selectedYear > maxYear) {
	                        yearCombo.setSelectedItem(maxYear);
	                    } else {
	                        // 유효한 값이므로 선택 유지
	                        yearCombo.setSelectedItem(selectedYear);
	                    }
	                    flag = 1;
	                    
	    	        } catch (NumberFormatException ex) {
	    	            // 숫자가 아닌 "abcd" 같은 것을 입력했거나, 빈칸일 경우
	    	        	yearCombo.setSelectedItem(preSelected); // 목록에 없으면 강제로 이전 선택 값으로 되돌림
	    	        }
    	        }
    	        preSelected = yearCombo.getSelectedItem();
    	    }

    	    // 3. 공통 로직: selectedYear를 기반으로 month/day 콤보박스 상태 변경
    	    if (flag == 1) {
    	        monthCombo.setEnabled(true);
    	        monthCombo.setSelectedItem("-");
    	        dayCombo.removeAllItems();
    	        dayCombo.addItem("-");
    	        dayCombo.setEnabled(false);
    	    } else {
    	        // year 해제 또는 유효하지 않은 입력 시
    	        monthCombo.setSelectedItem("-");
    	        monthCombo.setEnabled(false);
    	        dayCombo.removeAllItems();
    	        dayCombo.addItem("-");
    	        dayCombo.setEnabled(false);
    	    }
    	});

        // ♦️ month 선택 리스너
        monthCombo.addActionListener(e -> {
            Object yObj = yearCombo.getSelectedItem();
            Object mObj = monthCombo.getSelectedItem();
            // month 선택 -> day 활성화
            if (yObj instanceof Integer && mObj instanceof Integer) {
                updateDayCombo((int) yObj, (int) mObj);
                dayCombo.setEnabled(true);
                // month 선택 안 함 -> day 비활성화
            } else {
                dayCombo.removeAllItems();
                dayCombo.addItem("-");
                dayCombo.setEnabled(false);
            }
        });
    }

    // ♦️ month에 따른 day 수
    private void updateDayCombo(int year, int month) {
        dayCombo.removeAllItems();
        dayCombo.addItem("-");

        YearMonth ym = YearMonth.of(year, month);
        int maxDay = ym.lengthOfMonth();

        for (int i = 1; i <= maxDay; i++) {
            dayCombo.addItem(i);
        }

        dayCombo.setSelectedItem("-");
    }

    // 🟡 선택값 반환 메서드들 (없으면 -1)
    public int getYear() {
        Object y = yearCombo.getSelectedItem();
        return (y instanceof Integer) ? (int) y : -1;
    }

    public int getMonth() {
        Object m = monthCombo.getSelectedItem();
        return (m instanceof Integer) ? (int) m : -1;
    }

    public int getDay() {
        Object d = dayCombo.getSelectedItem();
        return (d instanceof Integer) ? (int) d : -1;
    }

}