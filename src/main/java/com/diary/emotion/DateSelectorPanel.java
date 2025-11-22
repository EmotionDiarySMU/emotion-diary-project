package com.diary.emotion;

import javax.swing.*;
import java.awt.*;
import java.time.*;

public class DateSelectorPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
	private JComboBox<Object> yearCombo;
    private JComboBox<Object> monthCombo;
    private JComboBox<Object> dayCombo;

    public DateSelectorPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 5));
        initializeComponents();
        addChangeListeners();
    }

    // 🟡 콤보박스 생성, 설정
    private void initializeComponents() {
    	
        // 🔹 연도 콤보박스
        int currentYear = LocalDate.now().getYear();
        
        yearCombo = new JComboBox<>();
        yearCombo.addItem("-"); // 선택 안 된 상태
        for (int i = 0; i < 5; i++) {
            yearCombo.addItem(currentYear - i);
        }

        // 🔹 월 콤보박스 (초기 비활성화)
        monthCombo = new JComboBox<>();
        monthCombo.addItem("-");
        for (int i = 1; i <= 12; i++) {
            monthCombo.addItem(i);
        }
        monthCombo.setEnabled(false);

        // 🔹 일 콤보박스 (초기 비활성화)
        dayCombo = new JComboBox<>();
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
            	// 콤보박스 내부 요소엔 '-'와 날짜들이 있음. '-'는 선택한 날짜가 없음 나타냄. 모든 콤보박스는 활성화/비활성화시 '-'로 초기화됨
                // year 선택 -> month 활성화, day 비활성화
                monthCombo.setEnabled(true);
                monthCombo.setSelectedItem("-");
                dayCombo.removeAllItems();
                dayCombo.addItem("-");
                dayCombo.setEnabled(false);
            } else {
                // year 해제 -> month, day 비활성화
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
    
    // 🟡 선택된 날짜를 YYYY-MM-DD 형식 문자열로 반환 (선택 안 되면 null)
    public String getSelectedDate() {
        int year = getYear();
        int month = getMonth();
        int day = getDay();

        // 연도, 월, 일이 모두 선택되었을 때만 반환
        if (year != -1 && month != -1 && day != -1) {
            return String.format("%04d-%02d-%02d", year, month, day);
        }

        return null;
    }

}
