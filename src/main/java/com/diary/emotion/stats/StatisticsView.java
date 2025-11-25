package com.diary.emotion.stats;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.time.YearMonth;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import com.diary.emotion.ui.UIColors;
import com.diary.emotion.ui.ComboBoxStyler;

/**
 * 통계 화면 메인 뷰
 * 감정 차트와 스트레스 차트를 표시하고 날짜 선택 기능 제공
 */
public class StatisticsView extends JPanel {

    private JComboBox<String> viewModeSelector;
    private JPanel datePickerCardPanel;
    private CardLayout datePickerCardLayout;

    private JComboBox<String> yearComboW, monthComboW, weekComboW;
    private JComboBox<String> yearComboM, monthComboM;

    private JComboBox<String> yearComboY;

    private JPanel mainChartPanel;

    private LocalDate today;
    private int currentYear;
    private int currentMonth;
    private int currentWeek;

    private static final Font CHART_TITLE_FONT = new Font("SansSerif", Font.BOLD, 14);

    private EmotionChartPanel emotionChartPanel;
    private StressChartPanel stressChartPanel;

    public StatisticsView() {
        setLayout(new BorderLayout());
        setBackground(UIColors.BG_STATS);
        initUI();
    }

    private void initUI() {
        today = LocalDate.now();
        currentYear = today.getYear();
        currentMonth = today.getMonthValue();
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        currentWeek = today.get(weekFields.weekOfMonth());

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        controlPanel.setBackground(UIColors.BG_STATS);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        viewModeSelector = new JComboBox<>(new String[]{"주간", "월간", "연간"});
        ComboBoxStyler.applyStatsComboStyle(viewModeSelector); // 스타일 적용

        viewModeSelector.addActionListener(e -> {
            String selected = (String) viewModeSelector.getSelectedItem();
            datePickerCardLayout.show(datePickerCardPanel, selected);
        });

        controlPanel.add(viewModeSelector);

        datePickerCardLayout = new CardLayout();
        datePickerCardPanel = new JPanel(datePickerCardLayout);
        datePickerCardPanel.setBackground(UIColors.BG_STATS);

        yearComboW = new JComboBox<>();
        monthComboW = new JComboBox<>();
        weekComboW = new JComboBox<>();
        yearComboM = new JComboBox<>();
        monthComboM = new JComboBox<>();
        yearComboY = new JComboBox<>();

        // 모든 콤보박스에 플랫 스타일 적용
        ComboBoxStyler.applyStatsComboStyle(yearComboW);
        ComboBoxStyler.applyStatsComboStyle(monthComboW);
        ComboBoxStyler.applyStatsComboStyle(weekComboW);
        ComboBoxStyler.applyStatsComboStyle(yearComboM);
        ComboBoxStyler.applyStatsComboStyle(monthComboM);
        ComboBoxStyler.applyStatsComboStyle(yearComboY);

        populateYearCombos();
        yearComboW.setSelectedItem(currentYear + "년");
        yearComboM.setSelectedItem(currentYear + "년");
        yearComboY.setSelectedItem(currentYear + "년");
        updateMonthCombos();
        monthComboW.setSelectedItem(String.format("%02d월", currentMonth));
        monthComboM.setSelectedItem(String.format("%02d월", currentMonth));
        updateWeekCombo();
        weekComboW.setSelectedItem(currentWeek + "주");
        yearComboW.addActionListener(e -> updateMonthCombos());
        monthComboW.addActionListener(e -> updateWeekCombo());
        yearComboM.addActionListener(e -> updateMonthCombos());

        JPanel weeklyCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        weeklyCard.setBackground(UIColors.BG_STATS);
        weeklyCard.add(yearComboW);
        weeklyCard.add(monthComboW);
        weeklyCard.add(weekComboW);

        JPanel monthlyCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        monthlyCard.setBackground(UIColors.BG_STATS);
        monthlyCard.add(yearComboM);
        monthlyCard.add(monthComboM);

        JPanel yearlyCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        yearlyCard.setBackground(UIColors.BG_STATS);
        yearlyCard.add(yearComboY);

        datePickerCardPanel.add(weeklyCard, "주간");
        datePickerCardPanel.add(monthlyCard, "월간");
        datePickerCardPanel.add(yearlyCard, "연간");

        // 모드 선택 시 날짜 선택기만 변경 (차트는 StatisticsController에서 업데이트)
        viewModeSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMode = (String) viewModeSelector.getSelectedItem();
                datePickerCardLayout.show(datePickerCardPanel, selectedMode);
            }
        });

        controlPanel.add(datePickerCardPanel);
        datePickerCardLayout.show(datePickerCardPanel, "주간");
        add(controlPanel, BorderLayout.NORTH);
        mainChartPanel = createChartPanel("주간");
        add(mainChartPanel, BorderLayout.CENTER);
    }

    private void populateYearCombos() {
        List<String> years = new ArrayList<>();
        for (int y = 2020; y <= currentYear; y++) {
            years.add(y + "년");
        }
        String[] yearArray = years.toArray(new String[0]);
        yearComboW.setModel(new javax.swing.DefaultComboBoxModel<>(yearArray));
        yearComboM.setModel(new javax.swing.DefaultComboBoxModel<>(yearArray));
        yearComboY.setModel(new javax.swing.DefaultComboBoxModel<>(yearArray));
    }


    private void updateMonthCombos() {
        String selectedYearW = (String) yearComboW.getSelectedItem();
        String selectedYearM = (String) yearComboM.getSelectedItem();
        if (selectedYearW == null || selectedYearM == null) return;
        int yearW = Integer.parseInt(selectedYearW.replace("년", ""));
        int yearM = Integer.parseInt(selectedYearM.replace("년", ""));
        int maxMonthW = (yearW == currentYear) ? currentMonth : 12;
        monthComboW.setModel(createMonthModel(maxMonthW));
        if (monthComboW.getSelectedIndex() == -1) {
            if (yearW == currentYear) {
                monthComboW.setSelectedItem(String.format("%02d월", currentMonth));
            } else {
                monthComboW.setSelectedIndex(0);
            }
        }
        int maxMonthM = (yearM == currentYear) ? currentMonth : 12;
        monthComboM.setModel(createMonthModel(maxMonthM));
        if (monthComboM.getSelectedIndex() == -1) {
            if (yearM == currentYear) {
                monthComboM.setSelectedItem(String.format("%02d월", currentMonth));
            } else {
                monthComboM.setSelectedIndex(0);
            }
        }
    }

    private void updateWeekCombo() {
        String selectedYearW = (String) yearComboW.getSelectedItem();
        String selectedMonthW = (String) monthComboW.getSelectedItem();
        if (selectedYearW == null || selectedMonthW == null) return;
        int year = Integer.parseInt(selectedYearW.replace("년", ""));
        int month = Integer.parseInt(selectedMonthW.replace("월", ""));
        int maxWeek = 5;
        if (year == currentYear && month == currentMonth) {
            maxWeek = currentWeek;
        } else {
            YearMonth ym = YearMonth.of(year, month);
            LocalDate lastDay = ym.atEndOfMonth();
            WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
            maxWeek = lastDay.get(weekFields.weekOfMonth());
        }
        weekComboW.setModel(createWeekModel(maxWeek));
        if (weekComboW.getSelectedIndex() == -1) {
            if (year == currentYear && month == currentMonth) {
                weekComboW.setSelectedItem(currentWeek + "주");
            } else {
                weekComboW.setSelectedIndex(0);
            }
        }
    }

    private javax.swing.DefaultComboBoxModel<String> createMonthModel(int maxMonth) {
        List<String> months = new ArrayList<>();
        for (int m = 1; m <= maxMonth; m++) {
            months.add(String.format("%02d월", m));
        }
        return new javax.swing.DefaultComboBoxModel<>(months.toArray(new String[0]));
    }

    private javax.swing.DefaultComboBoxModel<String> createWeekModel(int maxWeek) {
        List<String> weeks = new ArrayList<>();
        for (int w = 1; w <= maxWeek; w++) {
            weeks.add(w + "주");
        }
        return new javax.swing.DefaultComboBoxModel<>(weeks.toArray(new String[0]));
    }

    private JPanel createChartPanel(String mode) {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(UIColors.BG_STATS);

        // 감정 차트 섹션
        JLabel emotionTitleLabel = new JLabel("<감정 지수>");
        emotionTitleLabel.setFont(CHART_TITLE_FONT);

        emotionChartPanel = new EmotionChartPanel();
        JPanel fakeLegendPanel = EmotionChartPanel.createLegendPanel();

        JPanel emotionHeaderPanel = new JPanel(new GridBagLayout());
        emotionHeaderPanel.setBackground(UIColors.BG_STATS);

        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.gridwidth = 2;
        gbcTitle.anchor = GridBagConstraints.CENTER;
        gbcTitle.weightx = 1.0;
        emotionHeaderPanel.add(emotionTitleLabel, gbcTitle);

        GridBagConstraints gbcLegend = new GridBagConstraints();
        gbcLegend.gridx = 1;
        gbcLegend.gridy = 0;
        gbcLegend.gridwidth = 1;
        gbcLegend.anchor = GridBagConstraints.EAST;
        gbcLegend.weightx = 0.0;
        gbcLegend.insets = new Insets(0, 0, 0, 22);
        emotionHeaderPanel.add(fakeLegendPanel, gbcLegend);
        emotionHeaderPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); // 25 -> 20으로 변경

        // 스트레스 차트 섹션
        JLabel stressTitleLabel = new JLabel("<스트레스 지수>");
        stressTitleLabel.setFont(CHART_TITLE_FONT);
        stressTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        stressTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10)); // 30 -> 25로 변경

        stressChartPanel = new StressChartPanel(mode);

        // 레이아웃 구성
        centerPanel.add(emotionHeaderPanel);
        emotionChartPanel.setPreferredSize(new Dimension(400, 250));
        emotionChartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        emotionChartPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        centerPanel.add(emotionChartPanel);

        centerPanel.add(stressTitleLabel);
        stressChartPanel.setPreferredSize(new Dimension(400, 270));
        stressChartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 270));
        stressChartPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        centerPanel.add(stressChartPanel);

        return centerPanel;
    }


    public void updateStressChart(DefaultCategoryDataset stressDataset) {
        try {
            String mode = (String) viewModeSelector.getSelectedItem();
            stressChartPanel.updateData(stressDataset, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateEmotionChart(Map<String, Map<String, Double>> emotionData) {
        try {
            emotionChartPanel.updateData(emotionData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getter methods for StatisticsController
    public JComboBox<String> getViewModeSelector() {
        return viewModeSelector;
    }

    public JComboBox<String> getYearComboW() {
        return yearComboW;
    }

    public JComboBox<String> getMonthComboW() {
        return monthComboW;
    }

    public JComboBox<String> getWeekComboW() {
        return weekComboW;
    }

    public JComboBox<String> getYearComboM() {
        return yearComboM;
    }

    public JComboBox<String> getMonthComboM() {
        return monthComboM;
    }

    public JComboBox<String> getYearComboY() {
        return yearComboY;
    }

    public JLabel getAvgStressLabel() {
        return stressChartPanel.getAvgStressLabel();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
}