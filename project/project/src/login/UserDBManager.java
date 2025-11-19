package login;

import java.sql.*;

// [새로 작성함] DB 관련 작업(로그인, 회원가입)을 전담하는 관리 클래스
public class UserDBManager {

    // 팀원의 DatabaseUtil과 동일한 DB 연결 정보
    private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=UTC";
    private static final String DB_ID = "root";
    private static final String DB_PW = "dldkwls0514@"; //비번

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
}