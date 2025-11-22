package com.diary.emotion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;
import org.jfree.data.category.DefaultCategoryDataset;

public class StatisticsDAO {

    private Connection getConnection() throws Exception {
        return DatabaseManager.getConnection();
    }

    public double getAverageStress(LocalDate startDate, LocalDate endDate) {

        String sql = "SELECT AVG(stress_level) FROM diary WHERE DATE(entry_date) BETWEEN ? AND ?";

        double avgStress = 0.0;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, startDate);
            pstmt.setObject(2, endDate);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    avgStress = rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return avgStress;
    }

    public Map<String, Map<String, Double>> getEmotionData(LocalDate startDate, LocalDate endDate) {

        String sqlCount = "SELECT e.emoji_icon, COUNT(e.emoji_icon) " +
                          "FROM emotion e " +
                          "JOIN diary d ON e.entry_id = d.entry_id " +
                          "WHERE DATE(d.entry_date) BETWEEN ? AND ? " +
                          "GROUP BY e.emoji_icon";

        String sqlValue = "SELECT e.emoji_icon, AVG(e.emotion_level) " +
                          "FROM emotion e " +
                          "JOIN diary d ON e.entry_id = d.entry_id " +
                          "WHERE DATE(d.entry_date) BETWEEN ? AND ? " +
                          "GROUP BY e.emoji_icon";

        Map<String, Map<String, Double>> data = new HashMap<>();
        data.put("횟수", new HashMap<>());
        data.put("수치", new HashMap<>());

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlCount)) {

            pstmt.setObject(1, startDate);
            pstmt.setObject(2, endDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String emojiIcon = rs.getString(1);
                    double count = rs.getDouble(2);
                    data.get("횟수").put(emojiIcon, count);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlValue)) {

            pstmt.setObject(1, startDate);
            pstmt.setObject(2, endDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String emojiIcon = rs.getString(1);
                    double avgLevel = rs.getDouble(2);
                    data.get("수치").put(emojiIcon, avgLevel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public DefaultCategoryDataset getStressData(LocalDate startDate, LocalDate endDate, String mode) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String sql;

        if (mode.equals("주간")) {
            sql = "SELECT DAYOFWEEK(entry_date) AS day_num, AVG(stress_level) FROM diary " +
                  "WHERE DATE(entry_date) BETWEEN ? AND ? " +
                  "GROUP BY day_num " +
                  "ORDER BY FIELD(day_num, 2, 3, 4, 5, 6, 7, 1)";

        } else if (mode.equals("월간")) {
            sql = "SELECT WEEK(entry_date, 3) AS week_num, AVG(stress_level) FROM diary " +
                  "WHERE DATE(entry_date) BETWEEN ? AND ? " +
                  "GROUP BY week_num " +
                  "ORDER BY week_num";

        } else {
            sql = "SELECT MONTH(entry_date) AS month_num, AVG(stress_level) FROM diary " +
                  "WHERE DATE(entry_date) BETWEEN ? AND ? " +
                  "GROUP BY month_num " +
                  "ORDER BY month_num";
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, startDate);
            pstmt.setObject(2, endDate);

            try (ResultSet rs = pstmt.executeQuery()) {

                int firstWeekNum = -1;

                while (rs.next()) {

                    if (mode.equals("주간")) {
                        int dayNum = rs.getInt(1);
                        double avgStress = rs.getDouble(2);
                        String dayName = mapDayOfWeek(dayNum);
                        dataset.setValue(avgStress, "Stress(DAO)", dayName);

                    } else if (mode.equals("월간")) {
                        int weekNum = rs.getInt(1);
                        double avgStress = rs.getDouble(2);

                        if (firstWeekNum == -1) {
                            firstWeekNum = weekNum;
                        }
                        int weekOfMonth = (weekNum - firstWeekNum) + 1;

                        dataset.setValue(avgStress, "Stress(DAO)", weekOfMonth + "주");

                    } else {
                        int monthNum = rs.getInt(1);
                        double avgStress = rs.getDouble(2);
                        dataset.setValue(avgStress, "Stress(DAO)", monthNum + "월");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("스트레스 데이터 조회 중 오류 발생:");
            e.printStackTrace();
            throw new RuntimeException("스트레스 데이터를 조회할 수 없습니다.", e);
        }

        return dataset;
    }

    private String mapDayOfWeek(int dayNum) {
        switch (dayNum) {
            case 1: return "일";
            case 2: return "월";
            case 3: return "화";
            case 4: return "수";
            case 5: return "목";
            case 6: return "금";
            case 7: return "토";
            default: return "?";
        }
    }
}

