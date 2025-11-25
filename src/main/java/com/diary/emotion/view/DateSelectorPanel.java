package com.diary.emotion.view;

import com.diary.emotion.ui.ComboBoxStyler;
import com.diary.emotion.ui.UIColors;
import com.diary.emotion.ui.UIFonts;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * 날짜 선택 패널
 * 연도, 월, 일을 선택할 수 있는 콤보박스를 제공합니다.
 */
public class DateSelectorPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JComboBox<Object> yearCombo;
    private JComboBox<Object> monthCombo;
    private JComboBox<Object> dayCombo;

    public DateSelectorPanel() {
        setBackground(UIColors.BG_VIEW);
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        initializeComponents();
        addChangeListeners();
    }

    private void initializeComponents() {
        int currentYear = LocalDate.now().getYear();

        yearCombo = createCombo(currentYear, 5, true);
        monthCombo = createCombo(12, 1, false);
        dayCombo = createCombo(0, 0, false);

        ComboBoxStyler.applyDateComboStyle(yearCombo, 75);
        ComboBoxStyler.applyDateComboStyle(monthCombo, 60);
        ComboBoxStyler.applyDateComboStyle(dayCombo, 60);

        add(yearCombo);
        add(createLabel("년 "));
        add(monthCombo);
        add(createLabel("월 "));
        add(dayCombo);
        add(createLabel("일"));
    }

    private JComboBox<Object> createCombo(int limit, int start, boolean enabled) {
        JComboBox<Object> combo = new JComboBox<>();
        combo.addItem("-");

        if (limit > 0) {
            if (start > 1) { // 연도인 경우
                for (int i = 0; i < limit; i++) {
                    combo.addItem(limit - i);
                }
            } else { // 월인 경우
                for (int i = 1; i <= limit; i++) {
                    combo.addItem(i);
                }
            }
        }

        combo.setEnabled(enabled);
        return combo;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIFonts.BODY_REGULAR);
        label.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        return label;
    }

    private void addChangeListeners() {
        yearCombo.addActionListener(e -> {
            Object selected = yearCombo.getSelectedItem();
            if (selected instanceof Integer) {
                monthCombo.setEnabled(true);
            } else {
                monthCombo.setSelectedItem("-");
                monthCombo.setEnabled(false);
                dayCombo.setSelectedItem("-");
                dayCombo.setEnabled(false);
            }
        });

        monthCombo.addActionListener(e -> {
            Object yObj = yearCombo.getSelectedItem();
            Object mObj = monthCombo.getSelectedItem();
            if (yObj instanceof Integer && mObj instanceof Integer) {
                updateDayCombo((int) yObj, (int) mObj);
                dayCombo.setEnabled(true);
            } else {
                dayCombo.removeAllItems();
                dayCombo.addItem("-");
                dayCombo.setEnabled(false);
            }
        });
    }

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

    public JComboBox<Object> getYearCombo() {
        return yearCombo;
    }

    public JComboBox<Object> getMonthCombo() {
        return monthCombo;
    }

    public JComboBox<Object> getDayCombo() {
        return dayCombo;
    }

    /**
     * 검색 화면용 스타일 적용 (배경색 유지)
     */
    public void applyFlatComboStyleWithoutColor(JComboBox<Object> comboBox) {
        ComboBoxStyler.applySearchComboStyle(comboBox);
    }
}
