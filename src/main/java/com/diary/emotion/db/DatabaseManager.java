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

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=UTC";
	private static final String DB_ID = "root";
	private static final String DB_PW = "your_password_here";

    public static String loggedInUserId = "test_user";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_ID, DB_PW);
    }

    private static final String Initial_DB_URL = "jdbc:mysql://localhost:3306/?serverTimezone=UTC";

    public static boolean createDatabase() {
	
	    try (Connection conn = DriverManager.getConnection(Initial_DB_URL, DB_ID, DB_PW);
	    		Statement stmt = conn.createStatement()) {
	
	    	ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE 'emotion_diary'");
            if (!rs.next()) {
            	String sql = "CREATE DATABASE emotion_diary CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
		        stmt.executeUpdate(sql);
		        String useEmotion_diaryDB = "USE emotion_diary";
		        stmt.executeUpdate(useEmotion_diaryDB);

	            String createTable_user = """
	                CREATE TABLE user (
	                    user_id VARCHAR(20) PRIMARY KEY,
	                    user_pw VARCHAR(20) NOT NULL
	                )
	            """;
	            stmt.executeUpdate(createTable_user);

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
	    	System.err.println("데이터베이스 생성 중 오류 발생:");
	    	System.err.println("오류 메시지: " + e.getMessage());
	    	e.printStackTrace();
	    	return false;
	    }
	}

    public boolean checkLogin(String id, String pw) {
        String sql = "SELECT user_pw FROM user WHERE user_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_ID, DB_PW);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbPw = rs.getString("user_pw");
                
                return dbPw.equals(pw);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }

    public int registerUser(String id, String pw) {
        String checkSql = "SELECT user_id FROM user WHERE user_id = ?";
        String insertSql = "INSERT INTO user (user_id, user_pw) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_ID, DB_PW)) {
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, id);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return 0;
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, id);
                insertStmt.setString(2, pw);
                insertStmt.executeUpdate();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

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
            pstmtDiary = conn.prepareStatement(sqlInsertDiary, Statement.RETURN_GENERATED_KEYS);
            pstmtDiary.setString(1, loggedInUserId);
            pstmtDiary.setString(2, title);
            pstmtDiary.setString(3, content);
            pstmtDiary.setInt(4, stressLevel);
            pstmtDiary.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            
            int rowsAffected = pstmtDiary.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new Exception("Diary insert failed, no rows affected.");
            }

            rs = pstmtDiary.getGeneratedKeys();

            int entryId;

            if (rs.next()) {
                entryId = rs.getInt(1);
            } else {
                throw new Exception("Diary insert failed, no ID obtained.");
            }
            
            
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
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return false;
            
        } finally {
            
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

    public static List<DiaryEntry> getAllEntries() throws Exception {
        List<DiaryEntry> entries = new ArrayList<>();
        
        String sql = "SELECT entry_id, title, content, stress_level, DATE_FORMAT(entry_date, '%Y-%m-%d %H:%i') AS entry_date FROM diary WHERE user_id = ? ORDER BY entry_id DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	
        	pstmt.setString(1, loggedInUserId);

        	try (ResultSet rs = pstmt.executeQuery()){
	            while (rs.next()) {
	            	DiaryEntry entry = new DiaryEntry();

	                entry.setEntry_id(rs.getInt("entry_id"));
	                entry.setTitle(rs.getString("title"));
	                entry.setContent(rs.getString("content"));
	                entry.setStress_level(rs.getInt("stress_level"));
	                entry.setEntry_date(rs.getString("entry_date"));

	                
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

    public static boolean updateDiaryEntry(
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

            pstmtDiary = conn.prepareStatement(sqlUpdateDiary);
            pstmtDiary.setString(1, title);
            pstmtDiary.setString(2, content);
            pstmtDiary.setInt(3, stressLevel);
            pstmtDiary.setInt(4, entryId);
            pstmtDiary.executeUpdate();

            pstmtEmotion = conn.prepareStatement(sqlDeleteEmotion);
            pstmtEmotion.setInt(1, entryId);
            pstmtEmotion.executeUpdate();

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