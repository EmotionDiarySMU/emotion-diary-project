package com.diary.emotion;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Color;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.time.YearMonth;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import org.jfree.data.category.DefaultCategoryDataset;

import java.util.Map;

public class StatisticsView extends JPanel {

    private static final Color PASTEL_BLUE = new Color(230, 240, 255);

    private JLabel avgStressLabel;
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

    private static final Font AXIS_LABEL_FONT = new Font("SansSerif", Font.PLAIN, 11);

    private static final Font CHART_TITLE_FONT = new Font("SansSerif", Font.BOLD, 16);

    private static final String[] OFFICIAL_EMOTIONS = {
        "😊", "😆", "😍", "😌", "😂", "🤗",
        "😢", "😠", "😰", "😅", "😧", "😔"
    };

    public StatisticsView() {
        setLayout(new BorderLayout());
        setBackground(PASTEL_BLUE);
        initUI();
    }
    private void initUI() {
        today = LocalDate.now();
        currentYear = today.getYear();
        currentMonth = today.getMonthValue();
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        currentWeek = today.get(weekFields.weekOfMonth());
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        controlPanel.setBackground(PASTEL_BLUE);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 0));
        viewModeSelector = new JComboBox<>(new String[]{"주간", "월간", "연간"});
        controlPanel.add(viewModeSelector);
        datePickerCardLayout = new CardLayout();
        datePickerCardPanel = new JPanel(datePickerCardLayout);
        datePickerCardPanel.setOpaque(false);
        yearComboW = new JComboBox<>();
        monthComboW = new JComboBox<>();
        weekComboW = new JComboBox<>();
        yearComboM = new JComboBox<>();
        monthComboM = new JComboBox<>();
        yearComboY = new JComboBox<>();
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
        weeklyCard.setBackground(PASTEL_BLUE);
        weeklyCard.add(yearComboW);
        weeklyCard.add(monthComboW);
        weeklyCard.add(weekComboW);
        JPanel monthlyCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        monthlyCard.setBackground(PASTEL_BLUE);
        monthlyCard.add(yearComboM);
        monthlyCard.add(monthComboM);
        JPanel yearlyCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        yearlyCard.setBackground(PASTEL_BLUE);
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
        centerPanel.setBackground(PASTEL_BLUE);
        JLabel emotionTitleLabel = new JLabel("<감정 지수>");
        emotionTitleLabel.setFont(CHART_TITLE_FONT);
        JPanel emotionChartPanel = createDemoEmotionBarChart(mode);
        JPanel fakeLegendPanel = createCustomLegendPanel();
        JPanel emotionHeaderPanel = new JPanel(new GridBagLayout());
        emotionHeaderPanel.setBackground(PASTEL_BLUE);
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
        emotionHeaderPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        JLabel stressTitleLabel = new JLabel("<스트레스 지수>");
        stressTitleLabel.setFont(CHART_TITLE_FONT);
        stressTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        stressTitleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 5, 10));
        JPanel stressChartPanel = createDemoStressLineChart(mode);
        centerPanel.add(emotionHeaderPanel);
        emotionChartPanel.setPreferredSize(new Dimension(400, 260));
        emotionChartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        emotionChartPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        centerPanel.add(emotionChartPanel);
        centerPanel.add(stressTitleLabel);
        stressChartPanel.setPreferredSize(new Dimension(400, 260));
        stressChartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        stressChartPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        centerPanel.add(stressChartPanel);
        avgStressLabel = new JLabel(getAvgStressText(mode), SwingConstants.CENTER);
        avgStressLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        avgStressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avgStressLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        centerPanel.add(avgStressLabel);
        return centerPanel;
    }
    private String getAvgStressText(String mode) {
        
        double avgStress = 0.0;
        return String.format("<html><center>평균 스트레스 지수<b>:</b> <b>%.1f</b></center></html>", avgStress);
    }
    private JPanel createDemoStressLineChart(String mode) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        

        String xAxisLabel = "(요일)";
        if(mode.equals("주간")) {
            xAxisLabel = "(요일)";
        } else if(mode.equals("월간")) {
            xAxisLabel = "(주)";
        } else if(mode.equals("연간")) {
            xAxisLabel = "(월)";
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
            null,
            xAxisLabel,
            "(%)",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        lineChart.setBackgroundPaint(PASTEL_BLUE);
        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.getDomainAxis().setLabelFont(AXIS_LABEL_FONT);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setRange(0.0, 100.0);
        yAxis.setTickUnit(new NumberTickUnit(10.0));
        yAxis.setLabelAngle(Math.PI / 2.0);
        yAxis.setLabelFont(AXIS_LABEL_FONT);
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesStroke(0, new java.awt.BasicStroke(2.5f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-3.5, -3.5, 7.0, 7.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setSeriesOutlinePaint(0, renderer.getSeriesPaint(0));
        renderer.setUseFillPaint(true);
        renderer.setUseOutlinePaint(true);
        plot.setBackgroundPaint(Color.white);
        plot.setOutlineVisible(false);
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setBackground(PASTEL_BLUE);
        return chartPanel;
    }
    private JPanel createDemoEmotionBarChart(String mode) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // 12개 이모지를 모두 0으로 초기화하여 표시
        for (String emotion : OFFICIAL_EMOTIONS) {
            dataset.setValue(0.0, "횟수", emotion);
            dataset.setValue(0.0, "수치", emotion);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
            null,
            "(감정)",
            "(%)",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        barChart.setBackgroundPaint(PASTEL_BLUE);
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.getDomainAxis().setLabelFont(AXIS_LABEL_FONT);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setRange(0.0, 100.0);
        yAxis.setTickUnit(new NumberTickUnit(10.0));
        yAxis.setLabelAngle(Math.PI / 2.0);
        yAxis.setLabelFont(AXIS_LABEL_FONT);
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setSeriesPaint(0, new Color(100, 150, 255));
        renderer.setSeriesPaint(1, new Color(190, 220, 255));
        renderer.setItemMargin(0.1);
        plot.getDomainAxis().setCategoryMargin(0.3);
        plot.setBackgroundPaint(Color.white);
        plot.setOutlineVisible(false);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setBackground(PASTEL_BLUE);
        return chartPanel;
    }
    private JPanel createCustomLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.setOpaque(true);
        legendPanel.add(createLegendItem(new Color(100, 150, 255), "횟수"));
        legendPanel.add(Box.createHorizontalStrut(5));
        legendPanel.add(createLegendItem(new Color(190, 220, 255), "수치"));
        legendPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return legendPanel;
    }
    private JPanel createLegendItem(Color color, String text) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setOpaque(true);
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(10, 10));
        colorBox.setBackground(color);
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        itemPanel.add(colorBox);
        itemPanel.add(textLabel);
        return itemPanel;
    }
    public void updateStressChart(DefaultCategoryDataset stressDataset) {
        try {
            Component chartComponent = mainChartPanel.getComponent(3);
            if (chartComponent instanceof ChartPanel) {
                ChartPanel chartPanel = (ChartPanel) chartComponent;
                JFreeChart chart = chartPanel.getChart();
                CategoryPlot plot = chart.getCategoryPlot();
                plot.setDataset(stressDataset);
                String mode = (String) viewModeSelector.getSelectedItem();
                String xAxisLabel = "(요일)";
                if (mode != null) {
                    if (mode.equals("주간")) {
                        xAxisLabel = "(요일)";
                    } else if (mode.equals("월간")) {
                        xAxisLabel = "(주)";
                    } else if (mode.equals("연간")) {
                        xAxisLabel = "(월)";
                    }
                }
                plot.getDomainAxis().setLabel(xAxisLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateEmotionChart(Map<String, Map<String, Double>> emotionData) {
        try {
            Component chartComponent = mainChartPanel.getComponent(1);
            if (chartComponent instanceof ChartPanel) {
                ChartPanel chartPanel = (ChartPanel) chartComponent;
                JFreeChart chart = chartPanel.getChart();
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                Map<String, Double> counts = emotionData.get("횟수");
                Map<String, Double> values = emotionData.get("수치");
                boolean hasData = (counts != null && !counts.isEmpty()) ||
                                 (values != null && !values.isEmpty());
                String[] emotions = OFFICIAL_EMOTIONS;

                for (String emotion : emotions) {
                    double countValue = (counts != null) ? counts.getOrDefault(emotion, 0.0) : 0.0;
                    double valueValue = (values != null) ? values.getOrDefault(emotion, 0.0) : 0.0;
                    dataset.setValue(countValue, "횟수", emotion);
                    dataset.setValue(valueValue, "수치", emotion);
                }

                CategoryPlot plot = chart.getCategoryPlot();
                plot.setDataset(dataset);
                if (!hasData) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public JComboBox<String> getViewModeSelector() {
        return viewModeSelector;
    }
    public JComboBox<String> getYearComboW() { return yearComboW; }
    public JComboBox<String> getMonthComboW() { return monthComboW; }
    public JComboBox<String> getWeekComboW() { return weekComboW; }
    public JComboBox<String> getYearComboM() { return yearComboM; }
    public JComboBox<String> getMonthComboM() {
        return monthComboM;
    }
    public JComboBox<String> getYearComboY() { return yearComboY; }
    public JLabel getAvgStressLabel() {
        return avgStressLabel;
    }
    public void setMainChartPanel(JPanel newPanel) {
        if (mainChartPanel != null) {
            remove(mainChartPanel);
        }
        mainChartPanel = newPanel;
        add(mainChartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "오류",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public void showWarning(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "경고",
            JOptionPane.WARNING_MESSAGE
        );
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "알림",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}