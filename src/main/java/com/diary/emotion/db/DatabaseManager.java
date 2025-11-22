package com.diary.emotion.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.diary.emotion.login.AuthenticationFrame;

// ⭐️ 프로그램 실행 중 DB 연결/삽입/조회/수정 등을 관리하는 전용 클래스
public class DatabaseManager {

    // ⭐️ 이 클래스는 'emotion_diary' DB에 바로 연결
    private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=UTC";
	private static final String DB_ID = "root";
	private static final String DB_PW = "quwrof12"; // 비번
	
    // 1. DB 연결을 가져오는 메소드
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_ID, DB_PW);
    }
    
    // 처음 DB 생성을 위한 URL
    private static final String Initial_DB_URL = "jdbc:mysql://localhost:3306/?serverTimezone=UTC";
    
    // DB 생성 메서드
    public static boolean createDatabase() {
	
	    try (Connection conn = DriverManager.getConnection(Initial_DB_URL, DB_ID, DB_PW);
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
	                    title VARCHAR(50),
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
    
    // 1. 로그인 기능: ID와 비번을 받아서 맞으면 true, 틀리면 false 반환
    public boolean checkLogin(String id, String pw) {
        String sql = "SELECT user_pw FROM user WHERE user_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_ID, DB_PW);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbPw = rs.getString("user_pw");
                // 비밀번호가 일치하는지 확인
                return dbPw.equals(pw); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ID가 없거나 오류가 나면 실패로 간주
        return false; 
    }

    // 2. 회원가입 기능: 성공(1), 중복ID(0), 에러(-1) 반환
    public int registerUser(String id, String pw) {
        String checkSql = "SELECT user_id FROM user WHERE user_id = ?";
        String insertSql = "INSERT INTO user (user_id, user_pw) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_ID, DB_PW)) {
            // ID 중복 확인
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, id);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return 0; // 이미 존재하는 ID
                }
            }

            // 회원가입 실행
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, id);
                insertStmt.setString(2, pw);
                insertStmt.executeUpdate();
                return 1; // 성공
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // DB 연결 오류 등
        }
    }

    // 2. 일기 항목을 DB에 삽입하는 메소드
    public static boolean insertDiaryEntry(String title, String content, int stressLevel, 
                                           List<String> emotionIcons, List<Integer> emotionValuesList) {
        
        Connection conn = null;
        PreparedStatement pstmtDiary = null;
        PreparedStatement pstmtEmotion = null;
        ResultSet rs = null;

        String sqlInsertDiary = "INSERT INTO diary (user_id, title, content, stress_level, entry_date) VALUES (?, ?, ?, ?, ?)";
        String sqlInsertEmotion = "INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES (?, ?, ?)";

        try {
            conn = getConnection(); 
            conn.setAutoCommit(false); 
            
            // --- 1단계: 'diary' 테이블에 삽입 ---
            pstmtDiary = conn.prepareStatement(sqlInsertDiary, Statement.RETURN_GENERATED_KEYS);
            pstmtDiary.setString(1, AuthenticationFrame.loggedInUserId);
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
                pstmtEmotion.setInt(2, emotionValuesList.get(i));
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
    
    // 3. 일기 조회 메서드
    public static List<DiaryEntry> getAllEntries() throws Exception {
        List<DiaryEntry> entries = new ArrayList<>();
        // 최신 일기순으로 정렬 (DESC)
        String sql = "SELECT entry_id, title, content, stress_level, DATE_FORMAT(entry_date, '%Y-%m-%d %H:%i') AS entry_date FROM diary WHERE user_id = ? ORDER BY entry_id DESC"; 
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	
        	pstmt.setString(1, AuthenticationFrame.loggedInUserId);
        	
        	try (ResultSet rs = pstmt.executeQuery()){
	            while (rs.next()) {
	            	DiaryEntry entry = new DiaryEntry();

	                entry.setEntry_id(rs.getInt("entry_id"));
	                entry.setTitle(rs.getString("title"));
	                entry.setContent(rs.getString("content"));
	                entry.setStress_level(rs.getInt("stress_level"));
	                entry.setEntry_date(rs.getString("entry_date"));

	                // 감정 최대 4개 가져오기
	                entry.setEmotions(getEmotionsByEntryId(conn, entry.getEntry_id()));

	                entries.add(entry);
	            }
        	}
            
        } catch (SQLException e) {
            System.err.println("일기 조회 중 오류 발생:");
            e.printStackTrace();
        }
        return entries;
    }
    
    public static List<Emotion> getEmotionsByEntryId(Connection conn, int entryId) throws SQLException {

        List<Emotion> list = new ArrayList<>();

        String sql = "SELECT emotion_level, emoji_icon " +
                     "FROM emotion WHERE entry_id = ? " +
                     "ORDER BY emotion_id ASC LIMIT 4";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	
            pstmt.setInt(1, entryId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Emotion em = new Emotion();
                    em.setEmotion_level(rs.getInt("emotion_level"));
                    em.setEmoji_icon(rs.getString("emoji_icon"));
                    list.add(em);
                }
            }
        }
        return list;
    }
    
    // 4. 일기 수정하는 메서드
    public static boolean updateDiaryEntry( // ♦️
            int entryId,
            String title,
            String content,
            int stressLevel,
            List<String> emotionIcons,
            List<Integer> emotionValuesList) {

        Connection conn = null;
        PreparedStatement pstmtDiary = null;
        PreparedStatement pstmtEmotion = null;

        String sqlUpdateDiary = "UPDATE diary SET title = ?, content = ?, stress_level = ? WHERE entry_id = ?";
        String sqlDeleteEmotion = "DELETE FROM emotion WHERE entry_id = ?";
        String sqlInsertEmotion = "INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES (?, ?, ?)";

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // 1. diary 업데이트
            pstmtDiary = conn.prepareStatement(sqlUpdateDiary);
            pstmtDiary.setString(1, title);
            pstmtDiary.setString(2, content);
            pstmtDiary.setInt(3, stressLevel);
            pstmtDiary.setInt(4, entryId);
            pstmtDiary.executeUpdate();

            // 2. 기존 emotion 삭제
            pstmtEmotion = conn.prepareStatement(sqlDeleteEmotion);
            pstmtEmotion.setInt(1, entryId);
            pstmtEmotion.executeUpdate();

            // 3. 새 emotion 삽입
            pstmtEmotion = conn.prepareStatement(sqlInsertEmotion);
            for (int i = 0; i < emotionIcons.size(); i++) {
                pstmtEmotion.setInt(1, entryId);
                pstmtEmotion.setInt(2, emotionValuesList.get(i));
                pstmtEmotion.setString(3, emotionIcons.get(i));
                pstmtEmotion.addBatch();
            }
            pstmtEmotion.executeBatch();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            return false;

        } finally {
            try { if (pstmtDiary != null) pstmtDiary.close(); } catch (Exception e) {}
            try { if (pstmtEmotion != null) pstmtEmotion.close(); } catch (Exception e) {}
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (Exception e) {}
        }
    }


    
}