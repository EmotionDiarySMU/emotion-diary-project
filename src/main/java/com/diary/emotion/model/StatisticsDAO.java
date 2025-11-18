package com.diary.emotion.model;

// Java SQL(JDBC) 라이브러리 임포트
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// Java 8+ 날짜/시간 라이브러리 임포트 (DB의 DATE 타입과 연동)
import java.time.LocalDate;
// Java 데이터 구조 (Map, List 등) 임포트
import java.util.Map;
import java.util.HashMap;
import java.util.List;
// JFreeChart 데이터셋 임포트
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * [수정] 데이터베이스 접근 객체 (Data Access Object)
 * (수정) 1단계 테스트: '평균 스트레스' 외 모든 Mock 데이터를 제거합니다.
 * (수정) 1단계 테스트: DB 연결 정보를 DatabaseUtil과 일치시킵니다.
 */
public class StatisticsDAO {

    // --- DB 연결 정보 ---
    // [수정] DatabaseUtil.java와 동일한 실제 DB 정보로 변경
    private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "U9Bsi7sj1*";

    /**
     * 데이터베이스 커넥션을 가져오는 헬퍼 메소드
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * [1단계 테스트] 기간 내의 평균 스트레스 지수를 DB에서 계산합니다.
     * (이 메소드만 실제 쿼리를 실행합니다.)
     * @param userId (String) Controller로부터 전달받은 사용자 ID
     * @param startDate (LocalDate) 조회 시작일
     * @param endDate (LocalDate) 조회 종료일
     * @return (double) 계산된 평균 스트레스 지수 (데이터 없으면 0.0)
     */
    public double getAverageStress(String userId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT AVG(stress_level) AS avgStress FROM diary " +
                     "WHERE user_id = ? AND entry_date BETWEEN ? AND ?";
        
        double averageStress = 0.0;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId); 
            pstmt.setDate(2, java.sql.Date.valueOf(startDate));
            pstmt.setDate(3, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    averageStress = rs.getDouble("avgStress");
                }
            }
        } catch (SQLException e) {
            System.err.println("평균 스트레스 계산 중 DB 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        
        // (디버깅) 콘솔에 실제 DB에서 가져온 값을 출력합니다.
        System.out.println("[DAO 1단계 테스트] DB 평균 스트레스: " + averageStress);
        
        return averageStress; // [수정] Mock 데이터(55.5) 대신 실제 DB 값 반환
    }

    /**
     * 기간 내의 감정 데이터를 DB에서 가져와 Map을 생성합니다.
     *
     * @param userId 사용자 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 감정별 평균 수치를 담은 Map (긍정/부정 분류)
     */
    public Map<String, Map<String, Double>> getEmotionData(String userId, LocalDate startDate, LocalDate endDate) {
        
        // 감정 분류 맵
        Map<String, String> emotionCategory = new HashMap<>();
        emotionCategory.put("😊", "긍정");
        emotionCategory.put("😆", "긍정");
        emotionCategory.put("😍", "긍정");
        emotionCategory.put("😌", "긍정");
        emotionCategory.put("😂", "긍정");
        emotionCategory.put("🤗", "긍정");
        emotionCategory.put("😢", "부정");
        emotionCategory.put("😠", "부정");
        emotionCategory.put("😰", "부정");
        emotionCategory.put("😅", "부정");
        emotionCategory.put("😧", "부정");
        emotionCategory.put("😔", "부정");

        // 결과 맵 초기화
        Map<String, Map<String, Double>> data = new HashMap<>();
        data.put("긍정", new HashMap<>());
        data.put("부정", new HashMap<>());

        // SQL: 기간 내 모든 감정 데이터 조회
        String sql = "SELECT e.emoji_icon, AVG(e.emotion_level) AS avg_level " +
                    "FROM emotion e " +
                    "JOIN diary d ON e.entry_id = d.entry_id " +
                    "WHERE d.user_id = ? AND d.entry_date BETWEEN ? AND ? " +
                    "GROUP BY e.emoji_icon";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(startDate));
            pstmt.setDate(3, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String emoji = rs.getString("emoji_icon");
                    double avgLevel = rs.getDouble("avg_level");

                    String category = emotionCategory.get(emoji);
                    if (category != null) {
                        data.get(category).put(emoji, avgLevel);
                    }
                }
            }

            System.out.println("[DAO] 감정 데이터 조회 완료 - 긍정: " + data.get("긍정").size() +
                             ", 부정: " + data.get("부정").size());

        } catch (SQLException e) {
            System.err.println("감정 데이터 조회 중 DB 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    /**
     * 기간 내의 스트레스 데이터를 DB에서 가져와 Line Chart용 Dataset을 생성합니다.
     *
     * @param userId 사용자 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @param mode "주간", "월간", "연간"
     * @return JFreeChart용 DefaultCategoryDataset
     */
    public DefaultCategoryDataset getStressData(String userId, LocalDate startDate, LocalDate endDate, String mode) {
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // SQL: 기간 내 일별 평균 스트레스 조회
        String sql;

        if ("주간".equals(mode)) {
            // 주간: 일별 데이터
            sql = "SELECT DATE(entry_date) AS date, AVG(stress_level) AS avg_stress " +
                 "FROM diary " +
                 "WHERE user_id = ? AND entry_date BETWEEN ? AND ? " +
                 "GROUP BY DATE(entry_date) " +
                 "ORDER BY DATE(entry_date)";
        } else if ("월간".equals(mode)) {
            // 월간: 일별 데이터
            sql = "SELECT DATE(entry_date) AS date, AVG(stress_level) AS avg_stress " +
                 "FROM diary " +
                 "WHERE user_id = ? AND entry_date BETWEEN ? AND ? " +
                 "GROUP BY DATE(entry_date) " +
                 "ORDER BY DATE(entry_date)";
        } else {
            // 연간: 월별 데이터
            sql = "SELECT YEAR(entry_date) AS year, MONTH(entry_date) AS month, AVG(stress_level) AS avg_stress " +
                 "FROM diary " +
                 "WHERE user_id = ? AND entry_date BETWEEN ? AND ? " +
                 "GROUP BY YEAR(entry_date), MONTH(entry_date) " +
                 "ORDER BY YEAR(entry_date), MONTH(entry_date)";
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(startDate));
            pstmt.setDate(3, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = pstmt.executeQuery()) {
                int count = 0;

                if ("연간".equals(mode)) {
                    // 연간 모드: 월별 표시
                    while (rs.next()) {
                        int month = rs.getInt("month");
                        double avgStress = rs.getDouble("avg_stress");
                        dataset.addValue(avgStress, "스트레스", month + "월");
                        count++;
                    }
                } else {
                    // 주간/월간 모드: 일별 표시
                    while (rs.next()) {
                        String date = rs.getString("date");
                        double avgStress = rs.getDouble("avg_stress");

                        // 날짜 포맷: MM-dd
                        String label = date.substring(5); // YYYY-MM-DD -> MM-DD
                        dataset.addValue(avgStress, "스트레스", label);
                        count++;
                    }
                }

                System.out.println("[DAO] 스트레스 데이터 조회 완료 - " + count + "개 데이터");
            }

        } catch (SQLException e) {
            System.err.println("스트레스 데이터 조회 중 DB 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return dataset;
    }
}