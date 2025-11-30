package com.diary.emotion.DB;

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
    private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useUnicode=true";
	private static final String DB_ID = "root";
	private static final String DB_PW = "password"; // 비번
	
    // 1. DB 연결을 가져오는 메소드
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_ID, DB_PW);
    }
    
    // 처음 DB 생성을 위한 URL
    private static final String Initial_DB_URL = "jdbc:mysql://localhost:3306/?serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useUnicode=true";

    // DB 생성 메서드
    public static boolean createDatabase() {
	
    	try (Connection conn = DriverManager.getConnection(Initial_DB_URL, DB_ID, DB_PW);
    		     Statement stmt = conn.createStatement();
    		     ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE 'emotion_diary'")) {

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
	                    modify_date DATETIME,
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
                    emoji_icon VARCHAR(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
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
	    	System.err.println("일기 삭제 중 오류 발생:");
	    	e.printStackTrace(); // 오류 콘솔에 출력 (디버깅용)
	    	
	    	return false;
	    }
	}
    
    // 1. 로그인 기능: ID와 비번을 받아서 맞으면 true, 틀리면 false 반환
    public boolean checkLogin(String id, String pw) {
        String sql = "SELECT user_pw FROM user WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_ID, DB_PW);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return pw.equals(rs.getString("user_pw"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) return 0; 
                }
            }

            // 회원가입
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
            pstmtDiary.setString(2, title.trim()); // 앞 뒤 공백 제거
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
    public static List<DiaryEntry> getEntries(String title, Timestamp start, Timestamp end) {
        List<DiaryEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM diary WHERE user_id = ?";

        if (!title.isEmpty()) sql += " AND title LIKE ?";
        if (start != null) sql += " AND entry_date >= ?";
        if (end != null) sql += " AND entry_date <= ?";

        sql += " ORDER BY entry_id DESC";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int index = 1;
            pstmt.setString(index++, AuthenticationFrame.loggedInUserId);

            if (!title.isEmpty()) pstmt.setString(index++, "%" + title + "%");
            if (start != null) pstmt.setTimestamp(index++, start);
            if (end != null) pstmt.setTimestamp(index++, end);

            try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                DiaryEntry entry = new DiaryEntry();
	                entry.setEntry_id(rs.getInt("entry_id"));
	                entry.setTitle(rs.getString("title"));
	                entry.setContent(rs.getString("content"));
	                entry.setStress_level(rs.getInt("stress_level"));
	                entry.setEntry_date(rs.getTimestamp("entry_date"));
	                entry.setModify_date(rs.getTimestamp("modify_date"));
	                entry.setEmotions(getEmotionsByEntryId(conn, entry.getEntry_id()));
	                list.add(entry);
	            }
            }
	    } catch (Exception e) {
	    	System.err.println("일기 조회 중 오류 발생:");
	        e.printStackTrace();
	        return list;
	    }
        return list;
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
    public static boolean updateDiaryEntry(
            int entryId,
            String title,
            String content,
            int stressLevel,
            Timestamp modifyDate,
            List<String> emotionIcons,
            List<Integer> emotionValuesList) {

        String sqlUpdateDiary =
                "UPDATE diary SET title = ?, content = ?, stress_level = ?, modify_date = ? WHERE entry_id = ?";
        String sqlDeleteEmotion =
                "DELETE FROM emotion WHERE entry_id = ?";
        String sqlInsertEmotion =
                "INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES (?, ?, ?)";

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false); // 자동커밋 꺼두기

            // 1. diary 업데이트
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateDiary)) {
                pstmt.setString(1, title);
                pstmt.setString(2, content);
                pstmt.setInt(3, stressLevel);
                pstmt.setTimestamp(4, modifyDate);
                pstmt.setInt(5, entryId);
                pstmt.executeUpdate();
            }

            // 2. 기존 emotion 삭제
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteEmotion)) {
                pstmt.setInt(1, entryId);
                pstmt.executeUpdate();
            }

            // 3. 새 emotion 삽입
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertEmotion)) {
                for (int i = 0; i < emotionIcons.size(); i++) {
                    pstmt.setInt(1, entryId);
                    pstmt.setInt(2, emotionValuesList.get(i));
                    pstmt.setString(3, emotionIcons.get(i));
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
        	System.err.println("일기 수정 중 오류 발생:");
            e.printStackTrace();
            return false;
        }
    }


    // 5. 일기 삭제하는 메서드
    public static boolean deleteEntry(int entryId) {
        String sqlDeleteEmotion = "DELETE FROM emotion WHERE entry_id = ?";
        String sqlDeleteDiary = "DELETE FROM diary WHERE entry_id = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // 감정 먼저 삭제
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteEmotion)) {
                pstmt.setInt(1, entryId);
                pstmt.executeUpdate();
            }

            // 일기 삭제
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDiary)) {
                pstmt.setInt(1, entryId);
                pstmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
        	System.err.println("일기 삭제 중 오류 발생:");
            e.printStackTrace();
            return false;
        }
    }
    

    // 6. 로그인한 사용자의 가장 오래된 일기 연도를 가져오는 메서드 (일기가 없으면 현재 연도를 반환)
    public static int getOldestDiaryYear() {
        int currentYear = LocalDateTime.now().getYear();
        
        // entry_date를 오름차순으로 정렬하여 가장 첫 번째 일기의 날짜를 가져옵니다.
        String sql = "SELECT entry_date FROM diary WHERE user_id = ? ORDER BY entry_date ASC LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 사용자 ID 설정
            pstmt.setString(1, AuthenticationFrame.loggedInUserId); 

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 가장 오래된 entry_date를 Timestamp로 가져와서
                    Timestamp oldestTimestamp = rs.getTimestamp("entry_date");
                    // LocalDateTime으로 변환 후 연도만 반환
                    return oldestTimestamp.toLocalDateTime().getYear();
                }
            }
            
            // 일기가 하나도 없을 경우 (ResultSet이 비어있음)
            return currentYear;

        } catch (Exception e) {
            System.err.println("가장 오래된 일기 연도 조회 중 오류 발생:");
            e.printStackTrace();
            // 오류 발생 시에도 기본값으로 현재 연도 반환
            return currentYear;
        }
    }
}
