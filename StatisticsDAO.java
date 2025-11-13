package share;

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
    private static final String DB_PASSWORD = "REMOVED_PASSWORD";

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
     * [1단계 테스트] 기간 내의 감정 데이터를 DB에서 가져와 Map을 생성합니다.
     * (차트가 의도적으로 비워지도록, 지금은 빈 맵을 반환합니다.)
     */
    public Map<String, Map<String, Double>> getEmotionData(String userId, LocalDate startDate, LocalDate endDate) {
        
        System.out.println("[DAO 1단계 테스트] 감정 차트 비우기 (의도적)");
        
        // [수정] Mock 데이터 제거. 빈 맵을 반환하여 차트를 비웁니다.
        Map<String, Map<String, Double>> data = new HashMap<>();
        data.put("긍정", new HashMap<>());
        data.put("부정", new HashMap<>());
        return data;
    }

    /**
     * [1단계 테스트] 기간 내의 스트레스 데이터를 DB에서 가져와 Line Chart용 Dataset을 생성합니다.
     * (차트가 의도적으로 비워지도록, 지금은 빈 데이터셋을 반환합니다.)
     */
    public DefaultCategoryDataset getStressData(String userId, LocalDate startDate, LocalDate endDate, String mode) {
        
        System.out.println("[DAO 1단계 테스트] 스트레스 차트 비우기 (의도적)");

        // [수정] Mock 데이터 제거. 빈 데이터셋을 반환하여 차트를 비웁니다.
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        return dataset;
    }
}