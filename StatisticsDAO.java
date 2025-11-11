package com.diary.emotion;

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
import java.util.ArrayList;
// JFreeChart 데이터셋 임포트
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * [수정] 데이터베이스 접근 객체 (Data Access Object)
 * (수정) 캡슐화를 위해 DB 연결 정보가 'private'으로 변경되었습니다.
 * (수정) 'getStressData'의 Mock(가짜) 데이터가 12개월치로 보강되었습니다.
 */
public class StatisticsDAO {

    // --- DB 연결 정보 ---
    // [수정] 'private'으로 변경 (캡슐화)
    // (보안) 이 정보는 실제로는 별도 설정 파일로 분리하는 것이 좋습니다.
    // (TODO) 이 값들을 고객님의 실제 MySQL DB 정보로 변경해야 합니다.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database_name"; // DB이름
    private static final String DB_USER = "your_username"; // DB 아이디
    private static final String DB_PASSWORD = "your_password"; // DB 비밀번호

    /**
     * 'private': 이 클래스 '내부에서만' 사용되는 헬퍼 메소드입니다.
     * 데이터베이스 연결(Connection) 객체를 생성하여 반환합니다.
     * @return Connection 객체
     * @throws SQLException DB 연결 실패 시
     */
    private Connection getConnection() throws SQLException {
        // (DB가 없으므로 이 메소드는 지금 호출되지 않습니다)
        // MySQL JDBC 드라이버 로드 (Maven이 pom.xml에서 자동으로 관리)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found!", e);
        }
        // DB 연결 시도
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * 'public': Controller가 '호출'해야 하는 '공개 API'입니다.
     * (Controller용)
     * 특정 기간의 "평균 스트레스 지수"를 DB에서 계산하여 반환합니다.
     * @param startDate (LocalDate) 조회 시작일
     * @param endDate (LocalDate) 조회 종료일
     * @return (double) 평균 스트레스 지수 (데이터가 없으면 0.0)
     */
    public double getAverageStress(LocalDate startDate, LocalDate endDate) {
        // (TODO) 이 SQL은 'DIARY' 테이블과 'stress_level' 컬럼이 있다는 가정 하에 작성되었습니다.
        // (설계 엑셀.png의 'DIARY' 테이블 'stress' 컬럼을 예시로 함)
        String sql = "SELECT AVG(stress) FROM DIARY WHERE diary_date BETWEEN ? AND ?";
        
        // (임시) 0.0을 반환 (아직 구현되지 않음)
        double avgStress = 0.0; 

        // --- JDBC 실행 코드 (try-with-resources) ---
        // (DB가 없으므로, 이 코드는 DB가 생성될 때까지 주석 처리합니다)
        /*
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // SQL의 첫 번째 '?'에 startDate를 'DATE' 타입으로 설정
            pstmt.setObject(1, startDate);
            // SQL의 두 번째 '?'에 endDate를 'DATE' 타입으로 설정
            pstmt.setObject(2, endDate);

            // SQL 쿼리 실행
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { // 결과가 있다면
                    avgStress = rs.getDouble(1); // 첫 번째 컬럼(AVG(stress)) 값을 가져옴
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // (오류 처리) 실제로는 로깅을 해야 합니다.
        }
        */

        // [수정] (Mocking) DB가 없는 동안, Controller가 잘 작동하는지 테스트하기 위해
        // '가짜 데이터' 55.5를 반환합니다.
        return 55.5; 
        
        // return avgStress; // (원래 코드)
    }

    /**
     * 'public': Controller가 '호출'해야 하는 '공개 API'입니다.
     * (Controller용)
     * 특정 기간의 "감정 지수" (횟수, 수치) 데이터를 DB에서 가져옵니다.
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @return (Map) 감정 데이터 (예: {"횟수": Map<"😢", 10>, "수치": Map<"😢", 20>})
     */
    public Map<String, Map<String, Double>> getEmotionData(LocalDate startDate, LocalDate endDate) {
        // (TODO) 이 메소드는 Controller가 '진짜 데이터'로 감정 차트를 그릴 때 필요합니다.
        // (임시) 빈 Map을 반환합니다.
        Map<String, Map<String, Double>> data = new HashMap<>();
        data.put("횟수", new HashMap<>());
        data.put("수치", new HashMap<>());
        
        // (Mocking) Controller 테스트를 위해 '가짜 데이터'를 추가합니다.
        data.get("횟수").put("😢", 15.0); // 횟수 15
        data.get("횟수").put("🥰", 30.0); // 횟수 30
        data.get("수치").put("😢", 75.0); // 수치 75
        data.get("수치").put("🥰", 20.0); // 수치 20
        
        return data;
    }

    /**
     * 'public': Controller가 '호출'해야 하는 '공개 API'입니다.
     * [수정] (Controller용)
     * 특정 기간의 "스트레스 지수" (요일/주/월별) 데이터를 DB에서 가져옵니다.
     * (수정) DB가 없으므로 Mock 데이터를 7일, 5주, 12개월치로 보강합니다.
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @param mode ("주간", "월간", "연간")
     * @return (DefaultCategoryDataset) JFreeChart가 바로 사용할 수 있는 데이터셋
     */
    public DefaultCategoryDataset getStressData(LocalDate startDate, LocalDate endDate, String mode) {
        // (TODO) 이 메소드는 Controller가 '진짜 데이터'로 스트레스 차트를 그릴 때 필요합니다.
        // (임시) 빈 Dataset을 반환합니다.
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // [수정] (Mocking) Controller 테스트를 위해 '가짜 데이터'를 보강합니다.
        if (mode.equals("주간")) {
           dataset.setValue(80, "Stress(DAO)", "월"); 
           dataset.setValue(60, "Stress(DAO)", "화"); 
           dataset.setValue(90, "Stress(DAO)", "수");
           dataset.setValue(70, "Stress(DAO)", "목"); // (추가)
           dataset.setValue(75, "Stress(DAO)", "금"); // (추가)
           dataset.setValue(60, "Stress(DAO)", "토"); // (추가)
           dataset.setValue(85, "Stress(DAO)", "일"); // (추가)
        } else if (mode.equals("월간")) {
           dataset.setValue(50, "Stress(DAO)", "1주");
           dataset.setValue(65, "Stress(DAO)", "2주");
           dataset.setValue(60, "Stress(DAO)", "3주"); // (추가)
           dataset.setValue(70, "Stress(DAO)", "4주"); // (추가)
           dataset.setValue(55, "Stress(DAO)", "5주"); // (추가)
        } else {
           dataset.setValue(70, "Stress(DAO)", "1월");
           dataset.setValue(40, "Stress(DAO)", "2월");
           dataset.setValue(50, "Stress(DAO)", "3월");  // (추가)
           dataset.setValue(45, "Stress(DAO)", "4월");  // (추가)
           dataset.setValue(60, "Stress(DAO)", "5월");  // (추가)
           dataset.setValue(55, "Stress(DAO)", "6월");  // (추가)
           dataset.setValue(70, "Stress(DAO)", "7월");  // (추가)
           dataset.setValue(65, "Stress(DAO)", "8월");  // (추가)
           dataset.setValue(50, "Stress(DAO)", "9월");  // (추가)
           dataset.setValue(40, "Stress(DAO)", "10월"); // (추가)
           dataset.setValue(45, "Stress(DAO)", "11월"); // (추가)
           dataset.setValue(60, "Stress(DAO)", "12월"); // (추가)
        }
        
        return dataset;
    }
}