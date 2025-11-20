import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseUtil {
	public static boolean createDatabase() {
		String url = "jdbc:mysql://localhost:3306/?serverTimezone=UTC"; // JDBC 연결 URL (MySQL, 로컬, 3306, UTC)
		String id = "root"; //데이터베이스 root id 
		String pw = " "; //패스워드(수정하세요)
	
	    try (Connection conn = DriverManager.getConnection(url, id, pw);
	        Statement stmt = conn.createStatement()) {
	
	    	ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE 'emotion_diary'"); // 해당 DB가 존재하는가
            if (!rs.next()) { // 해당 DB가 없으면 실행
            	
            	// DB 생성
		        String sql = "CREATE DATABASE emotion_diary CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"; // 이모지 저장 및 대소문자 구분 없는 유니코드 문자셋 설정
		        stmt.executeUpdate(sql);
		        
		        // 해당 DB 사용
		        String useEmotion_diaryDB = "USE emotion_diary";
	            stmt.executeUpdate(useEmotion_diaryDB);
	
	            // user 테이블 생성
	            String createTable_user = """
	                CREATE TABLE user (
	                    user_id VARCHAR(20) PRIMARY KEY,
	                    user_pw VARCHAR(20) NOT NULL
	                )
	            """;
	            stmt.executeUpdate(createTable_user);
	            
	            // diary 테이블 생성
	            String createTable_diary = """
	                CREATE TABLE diary (
	                    entry_id INTEGER AUTO_INCREMENT PRIMARY KEY,
	                    user_id VARCHAR(20) NOT NULL,
	                    title VARCHAR(50) NOT NULL,
	                    content TEXT,
	                    stress_level INTEGER NOT NULL,
	                    entry_date DATETIME NOT NULL,
	                    FOREIGN KEY (user_id) REFERENCES user(user_id)
	                )
	            """;
	            stmt.executeUpdate(createTable_diary);
	            
	            // emotion 테이블 생성
	            String createTable_emotion = """
	                CREATE TABLE emotion (
	                    emotion_id INTEGER AUTO_INCREMENT PRIMARY KEY,
	                    entry_id INTEGER NOT NULL,
	                    emotion_level INTEGER NOT NULL,
	                    emoji_icon VARCHAR(10) NOT NULL,
	                    FOREIGN KEY (entry_id) REFERENCES diary(entry_id)
	                )
	            """;
	            stmt.executeUpdate(createTable_emotion);
	                
	            // question 테이블 생성
	            String createTable_question = """
	                CREATE TABLE question (
	                    question_id INTEGER AUTO_INCREMENT PRIMARY KEY,
	                    question_text VARCHAR(100) NOT NULL
	                )
	            """;
	            stmt.executeUpdate(createTable_question);
            }

	        return true;
	        
	    } catch (Exception e) {
//	    	e.printStackTrace(); // 오류 콘솔에 출력 (디버깅용)
	    	
	    	return false;
	    }
	}
}

