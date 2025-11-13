import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

// ⭐️ 프로그램 실행 중 DB 연결/삽입/조회 등을 관리하는 전용 클래스
public class DatabaseManager {

    // ⭐️ 이 클래스는 'emotion_diary' DB에 바로 연결
    private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=UTC";
	private static final String DB_ID = "root";
	private static final String DB_PW = "dldkwls0514@"; // 비번
    // 1. DB 연결을 가져오는 메소드
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_ID, DB_PW);
    }

    // 2. 일기 항목을 DB에 삽입하는 메소드
    public static boolean insertDiaryEntry(String title, String content, int stressLevel, 
                                           List<String> emotionIcons, List<String> emotionLevels) {
        
        Connection conn = null;
        PreparedStatement pstmtDiary = null;
        PreparedStatement pstmtEmotion = null;
        ResultSet rs = null;

        String userId = "default"; 

        String sqlInsertDiary = "INSERT INTO diary (user_id, title, content, stress_level, entry_date) VALUES (?, ?, ?, ?, ?)";
        String sqlInsertEmotion = "INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES (?, ?, ?)";

        try {
            conn = getConnection(); 
            conn.setAutoCommit(false); 
            
            // --- 1단계: 'diary' 테이블에 삽입 ---
            pstmtDiary = conn.prepareStatement(sqlInsertDiary, Statement.RETURN_GENERATED_KEYS);
            pstmtDiary.setString(1, userId);
            pstmtDiary.setString(2, title);
            pstmtDiary.setString(3, content);
            pstmtDiary.setInt(4, stressLevel);
            pstmtDiary.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            
            int rowsAffected = pstmtDiary.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new Exception("Diary insert failed, no rows affected.");
            }

            // --- 2단계: 방금 삽입된 'diary'의 entry_id 가져오기 ---
            rs = pstmtDiary.getGeneratedKeys();
            int entryId;
            if (rs.next()) {
                entryId = rs.getInt(1); 
            } else {
                throw new Exception("Diary insert failed, no ID obtained.");
            }
            
            // --- 3단계: 'emotion' 테이블에 삽입 ---
            pstmtEmotion = conn.prepareStatement(sqlInsertEmotion);
            
            for (int i = 0; i < emotionIcons.size(); i++) {
                pstmtEmotion.setInt(1, entryId);
                pstmtEmotion.setInt(2, Integer.parseInt(emotionLevels.get(i)));
                pstmtEmotion.setString(3, emotionIcons.get(i));
                pstmtEmotion.addBatch();
            }
            pstmtEmotion.executeBatch(); 

            // --- 4단계: 최종 반영 ---
            conn.commit();
            return true; // 성공!

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // 실패 시 되돌리기
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return false; // 실패
            
        } finally {
            // --- 5단계: 리소스 정리 ---
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pstmtDiary != null) pstmtDiary.close(); } catch (Exception e) {}
            try { if (pstmtEmotion != null) pstmtEmotion.close(); } catch (Exception e) {}
            try { 
                if (conn != null) {
                    conn.setAutoCommit(true); 
                    conn.close(); 
                }
            } catch (Exception e) {}
        }
    }
}