package com.diary.emotion.model;

import java.sql.*;

/**
 * 사용자 데이터 접근 객체 (Data Access Object)
 * user 테이블과 상호작용하는 모든 데이터베이스 쿼리를 담당합니다.
 *
 * 주요 기능:
 * - 로그인 검증 (authenticateUser)
 * - 회원가입 (registerUser)
 * - 아이디 중복 확인 (userExists)
 */
public class UserDAO {

    // 데이터베이스 연결 정보
    private static final String DB_URL = "jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "REMOVED_PASSWORD";

    /**
     * 데이터베이스 연결을 가져오는 헬퍼 메소드
     *
     * @return Connection 객체
     * @throws SQLException DB 연결 실패 시
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * 로그인 검증 메소드
     * 입력된 아이디와 비밀번호가 데이터베이스에 일치하는지 확인합니다.
     *
     * @param userId 사용자 입력 아이디
     * @param password 사용자 입력 비밀번호
     * @return 로그인 성공 시 true, 실패 시 false
     */
    public boolean authenticateUser(String userId, String password) {
        String sql = "SELECT user_pw FROM user WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("user_pw");
                    boolean isMatch = storedPassword.equals(password);

                    if (isMatch) {
                        System.out.println("[UserDAO] 로그인 성공: " + userId);
                    } else {
                        System.out.println("[UserDAO] 로그인 실패: 비밀번호 불일치");
                    }

                    return isMatch;
                } else {
                    System.out.println("[UserDAO] 로그인 실패: 존재하지 않는 아이디");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] 로그인 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 회원가입 메소드
     * 새로운 사용자를 user 테이블에 추가합니다.
     *
     * @param userId 신규 사용자 아이디
     * @param password 신규 사용자 비밀번호
     * @return 회원가입 성공 시 true, 실패 시 false
     */
    public boolean registerUser(String userId, String password) {
        // 먼저 아이디 중복 확인
        if (userExists(userId)) {
            System.out.println("[UserDAO] 회원가입 실패: 이미 존재하는 아이디");
            return false;
        }

        String sql = "INSERT INTO user (user_id, user_pw) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            int rows = pstmt.executeUpdate();
            boolean success = rows > 0;

            if (success) {
                System.out.println("[UserDAO] 회원가입 성공: " + userId);
            } else {
                System.out.println("[UserDAO] 회원가입 실패: 데이터 삽입 실패");
            }

            return success;

        } catch (SQLException e) {
            System.err.println("[UserDAO] 회원가입 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 아이디 중복 확인 메소드
     * 입력된 아이디가 이미 데이터베이스에 존재하는지 확인합니다.
     *
     * @param userId 확인할 아이디
     * @return 아이디가 존재하면 true, 존재하지 않으면 false
     */
    public boolean userExists(String userId) {
        String sql = "SELECT COUNT(*) FROM user WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    boolean exists = count > 0;

                    System.out.println("[UserDAO] 아이디 중복 확인 (" + userId + "): " + (exists ? "존재" : "사용 가능"));

                    return exists;
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] 아이디 중복 확인 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}

