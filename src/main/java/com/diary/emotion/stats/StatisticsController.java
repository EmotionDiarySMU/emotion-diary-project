package com.diary.emotion.stats;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.Map;
import org.jfree.data.category.DefaultCategoryDataset;

public class StatisticsController {

    private StatisticsView view;
    private StatisticsDAO dao;

    public StatisticsController(StatisticsView view, StatisticsDAO dao) {
        this.view = view;
        this.dao = dao;

        addListeners();
        updateAllCharts();
    }

    private void addListeners() {
        view.getViewModeSelector().addActionListener(e -> updateAllCharts());

        view.getYearComboW().addActionListener(e -> updateAllCharts());
        view.getMonthComboW().addActionListener(e -> updateAllCharts());
        view.getWeekComboW().addActionListener(e -> updateAllCharts());

        view.getYearComboM().addActionListener(e -> updateAllCharts());
        view.getMonthComboM().addActionListener(e -> updateAllCharts());

        view.getYearComboY().addActionListener(e -> updateAllCharts());
    }

    private void updateAllCharts() {
        try {
            String mode = (String) view.getViewModeSelector().getSelectedItem();

            if (mode == null) return;

            LocalDate startDate = getStartDateFromView(mode);
            LocalDate endDate = getEndDateFromView(mode);

            double avgStress = dao.getAverageStress(startDate, endDate);

            Map<String, Map<String, Double>> emotionData = dao.getEmotionData(startDate, endDate);

            DefaultCategoryDataset stressDataset = dao.getStressData(startDate, endDate, mode);

            view.getAvgStressLabel().setText(
                String.format("<html><center>평균 스트레스 지수<b>:</b> <b>%.1f</b></center></html>", avgStress)
            );

            view.updateEmotionChart(emotionData);
            view.updateStressChart(stressDataset);

        } catch (Exception e) {
            e.printStackTrace();
            view.showError("데이터를 불러오는 중 오류가 발생했습니다.\n" +
                          "데이터베이스 연결 상태를 확인해주세요.\n\n" +
                          "오류: " + e.getMessage());
        }
    }

    private LocalDate getStartDateFromView(String mode) {

        if (mode.equals("주간")) {
            String yearStr = (String) view.getYearComboW().getSelectedItem();
            String monthStr = (String) view.getMonthComboW().getSelectedItem();
            String weekStr = (String) view.getWeekComboW().getSelectedItem();

            if (yearStr == null || monthStr == null || weekStr == null) return LocalDate.now();

            int year = Integer.parseInt(yearStr.replace("년", ""));
            int month = Integer.parseInt(monthStr.replace("월", ""));
            int week = Integer.parseInt(weekStr.replace("주", ""));

            YearMonth ym = YearMonth.of(year, month);

            WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);

            LocalDate dayInWeek = ym.atDay(Math.min((week - 1) * 7 + 1, ym.lengthOfMonth()));
            LocalDate startDate = dayInWeek.with(weekFields.dayOfWeek(), 1L);

            if (startDate.getMonthValue() != month) {
                return ym.atDay(1);
            }
            return startDate;

        } else if (mode.equals("월간")) {
            String yearStr = (String) view.getYearComboM().getSelectedItem();
            String monthStr = (String) view.getMonthComboM().getSelectedItem();

            if (yearStr == null || monthStr == null) return LocalDate.now();

            int year = Integer.parseInt(yearStr.replace("년", ""));
            int month = Integer.parseInt(monthStr.replace("월", ""));

            return YearMonth.of(year, month).atDay(1);

        } else if (mode.equals("연간")) {
            String yearStr = (String) view.getYearComboY().getSelectedItem();

            if (yearStr == null) return LocalDate.now();

            int year = Integer.parseInt(yearStr.replace("년", ""));

            return LocalDate.of(year, 1, 1);
        }

        return LocalDate.now();
    }

    private LocalDate getEndDateFromView(String mode) {

        if (mode.equals("주간")) {
            String yearStr = (String) view.getYearComboW().getSelectedItem();
            String monthStr = (String) view.getMonthComboW().getSelectedItem();
            String weekStr = (String) view.getWeekComboW().getSelectedItem();

            if (yearStr == null || monthStr == null || weekStr == null) return LocalDate.now();

            int year = Integer.parseInt(yearStr.replace("년", ""));
            int month = Integer.parseInt(monthStr.replace("월", ""));
            int week = Integer.parseInt(weekStr.replace("주", ""));

            YearMonth ym = YearMonth.of(year, month);

            WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);

            LocalDate dayInWeek = ym.atDay(Math.min((week - 1) * 7 + 1, ym.lengthOfMonth()));
            LocalDate startDate = dayInWeek.with(weekFields.dayOfWeek(), 1L);

            if (startDate.getMonthValue() != month) {
                startDate = ym.atDay(1);
            }

            LocalDate endDate = startDate.plusDays(6);

            if (endDate.getMonthValue() != month) {
                return ym.atEndOfMonth();
            }
            return endDate;

        } else if (mode.equals("월간")) {
            String yearStr = (String) view.getYearComboM().getSelectedItem();
            String monthStr = (String) view.getMonthComboM().getSelectedItem();

            if (yearStr == null || monthStr == null) return LocalDate.now();

            int year = Integer.parseInt(yearStr.replace("년", ""));
            int month = Integer.parseInt(monthStr.replace("월", ""));

            return YearMonth.of(year, month).atEndOfMonth();

        } else if (mode.equals("연간")) {
            String yearStr = (String) view.getYearComboY().getSelectedItem();

            if (yearStr == null) return LocalDate.now();

            int year = Integer.parseInt(yearStr.replace("년", ""));

            return LocalDate.of(year, 12, 31);
        }

        return LocalDate.now();
    }

}
