package com.diary.emotion.model;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * 일기 데이터 접근 객체 (Data Access Object)
 * diary 및 emotion 테이블과 상호작용하는 모든 데이터베이스 쿼리를 담당합니다.
 *
 * 주요 기능:
 * - 일기 저장 (saveDiary)
 * - 감정 저장 (saveEmotion)
 * - 일기 조회 (getDiariesByUserId, getDiaryById 등)
 * - 일기 수정 (updateDiary)
 * - 일기 삭제 (deleteDiary)
 */
public class DiaryDAO {

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
     * 일기 저장 메소드
     * diary 테이블에 새로운 일기를 추가합니다.
     *
     * @param userId 사용자 ID
     * @param title 일기 제목
     * @param content 일기 내용
     * @param stressLevel 스트레스 수치 (0~100)
     * @param entryDate 작성 날짜 및 시간
     * @return 저장된 일기의 entry_id (실패 시 -1)
     */
    public int saveDiary(String userId, String title, String content, int stressLevel, LocalDateTime entryDate) {
        String sql = "INSERT INTO diary (user_id, title, content, stress_level, entry_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, title);
            pstmt.setString(3, content);
            pstmt.setInt(4, stressLevel);
            pstmt.setTimestamp(5, Timestamp.valueOf(entryDate));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // 자동 생성된 entry_id 가져오기
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int entryId = rs.getInt(1);
                        System.out.println("[DiaryDAO] 일기 저장 성공 - entry_id: " + entryId);
                        return entryId;
                    }
                }
            }

            System.err.println("[DiaryDAO] 일기 저장 실패");
            return -1;

        } catch (SQLException e) {
            System.err.println("[DiaryDAO] 일기 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 감정 저장 메소드
     * emotion 테이블에 감정 데이터를 추가합니다.
     *
     * @param entryId 일기 ID (diary 테이블의 entry_id)
     * @param emotionLevel 감정 수치 (0~100)
     * @param emojiIcon 이모지 아이콘 (예: "😊")
     * @return 저장 성공 여부
     */
    public boolean saveEmotion(int entryId, int emotionLevel, String emojiIcon) {
        String sql = "INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, entryId);
            pstmt.setInt(2, emotionLevel);
            pstmt.setString(3, emojiIcon);

            int affectedRows = pstmt.executeUpdate();
            boolean success = affectedRows > 0;

            if (success) {
                System.out.println("[DiaryDAO] 감정 저장 성공 - entry_id: " + entryId + ", emoji: " + emojiIcon);
            } else {
                System.err.println("[DiaryDAO] 감정 저장 실패");
            }

            return success;

        } catch (SQLException e) {
            System.err.println("[DiaryDAO] 감정 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 일기와 감정을 트랜잭션으로 저장하는 메소드
     * 일기 저장 실패 시 감정도 저장되지 않도록 보장합니다.
     *
     * @param userId 사용자 ID
     * @param title 일기 제목
     * @param content 일기 내용
     * @param stressLevel 스트레스 수치
     * @param entryDate 작성 날짜
     * @param emotions 감정 데이터 배열 [{emoji: "😊", level: 80}, ...]
     * @return 저장 성공 여부
     */
    public boolean saveDiaryWithEmotions(String userId, String title, String content,
                                         int stressLevel, LocalDateTime entryDate,
                                         EmotionData[] emotions) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 1. 일기 저장
            String diarySql = "INSERT INTO diary (user_id, title, content, stress_level, entry_date) VALUES (?, ?, ?, ?, ?)";
            int entryId = -1;

            try (PreparedStatement pstmt = conn.prepareStatement(diarySql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, userId);
                pstmt.setString(2, title);
                pstmt.setString(3, content);
                pstmt.setInt(4, stressLevel);
                pstmt.setTimestamp(5, Timestamp.valueOf(entryDate));

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            entryId = rs.getInt(1);
                        }
                    }
                }
            }

            if (entryId == -1) {
                conn.rollback();
                System.err.println("[DiaryDAO] 일기 저장 실패 - 트랜잭션 롤백");
                return false;
            }

            // 2. 감정 저장 (최대 4개)
            String emotionSql = "INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(emotionSql)) {
                for (EmotionData emotion : emotions) {
                    pstmt.setInt(1, entryId);
                    pstmt.setInt(2, emotion.level);
                    pstmt.setString(3, emotion.emoji);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            conn.commit(); // 트랜잭션 커밋
            System.out.println("[DiaryDAO] 일기 및 감정 저장 성공 (트랜잭션) - entry_id: " + entryId);
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("[DiaryDAO] 트랜잭션 롤백 완료");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("[DiaryDAO] 일기/감정 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 감정 데이터를 담는 내부 클래스
     */
    public static class EmotionData {
        public String emoji;
        public int level;

        public EmotionData(String emoji, int level) {
            this.emoji = emoji;
            this.level = level;
        }
    }

    /**
     * 일기 데이터를 담는 클래스
     */
    public static class DiaryEntry {
        public int entryId;
        public String userId;
        public String title;
        public String content;
        public int stressLevel;
        public LocalDateTime entryDate;
        public EmotionData[] emotions;

        public DiaryEntry(int entryId, String userId, String title, String content,
                         int stressLevel, LocalDateTime entryDate) {
            this.entryId = entryId;
            this.userId = userId;
            this.title = title;
            this.content = content;
            this.stressLevel = stressLevel;
            this.entryDate = entryDate;
        }
    }

    /**
     * 사용자별 모든 일기 목록 조회
     *
     * @param userId 사용자 ID
     * @return 일기 목록 배열
     */
    public DiaryEntry[] getDiariesByUserId(String userId) {
        String sql = "SELECT entry_id, user_id, title, content, stress_level, entry_date " +
                    "FROM diary WHERE user_id = ? ORDER BY entry_date DESC";

        java.util.List<DiaryEntry> diaries = new java.util.ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DiaryEntry entry = new DiaryEntry(
                        rs.getInt("entry_id"),
                        rs.getString("user_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("stress_level"),
                        rs.getTimestamp("entry_date").toLocalDateTime()
                    );
                    // 감정 데이터 로드
                    entry.emotions = getEmotionsByEntryId(entry.entryId);
                    diaries.add(entry);
                }
            }

            System.out.println("[DiaryDAO] 일기 목록 조회 성공 - 총 " + diaries.size() + "개");
            return diaries.toArray(new DiaryEntry[0]);

        } catch (SQLException e) {
            System.err.println("[DiaryDAO] 일기 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return new DiaryEntry[0];
        }
    }

    /**
     * 제목으로 일기 검색
     *
     * @param userId 사용자 ID
     * @param keyword 검색 키워드
     * @return 검색된 일기 목록
     */
    public DiaryEntry[] searchByTitle(String userId, String keyword) {
        String sql = "SELECT entry_id, user_id, title, content, stress_level, entry_date " +
                    "FROM diary WHERE user_id = ? AND title LIKE ? ORDER BY entry_date DESC";

        java.util.List<DiaryEntry> diaries = new java.util.ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DiaryEntry entry = new DiaryEntry(
                        rs.getInt("entry_id"),
                        rs.getString("user_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("stress_level"),
                        rs.getTimestamp("entry_date").toLocalDateTime()
                    );
                    entry.emotions = getEmotionsByEntryId(entry.entryId);
                    diaries.add(entry);
                }
            }

            System.out.println("[DiaryDAO] 제목 검색 성공 - 총 " + diaries.size() + "개");
            return diaries.toArray(new DiaryEntry[0]);

        } catch (SQLException e) {
            System.err.println("[DiaryDAO] 제목 검색 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return new DiaryEntry[0];
        }
    }

    /**
     * 날짜 범위로 일기 검색
     *
     * @param userId 사용자 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 검색된 일기 목록
     */
    public DiaryEntry[] searchByDate(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT entry_id, user_id, title, content, stress_level, entry_date " +
                    "FROM diary WHERE user_id = ? AND entry_date BETWEEN ? AND ? ORDER BY entry_date DESC";

        java.util.List<DiaryEntry> diaries = new java.util.ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setTimestamp(2, Timestamp.valueOf(startDate));
            pstmt.setTimestamp(3, Timestamp.valueOf(endDate));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DiaryEntry entry = new DiaryEntry(
                        rs.getInt("entry_id"),
                        rs.getString("user_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("stress_level"),
                        rs.getTimestamp("entry_date").toLocalDateTime()
                    );
                    entry.emotions = getEmotionsByEntryId(entry.entryId);
                    diaries.add(entry);
                }
            }

            System.out.println("[DiaryDAO] 날짜 검색 성공 - 총 " + diaries.size() + "개");
            return diaries.toArray(new DiaryEntry[0]);

        } catch (SQLException e) {
            System.err.println("[DiaryDAO] 날짜 검색 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return new DiaryEntry[0];
        }
    }

    /**
     * 특정 일기 조회
     *
     * @param entryId 일기 ID
     * @return 일기 데이터 (없으면 null)
     */
    public DiaryEntry getDiaryById(int entryId) {
        String sql = "SELECT entry_id, user_id, title, content, stress_level, entry_date " +
                    "FROM diary WHERE entry_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, entryId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    DiaryEntry entry = new DiaryEntry(
                        rs.getInt("entry_id"),
                        rs.getString("user_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("stress_level"),
                        rs.getTimestamp("entry_date").toLocalDateTime()
                    );
                    entry.emotions = getEmotionsByEntryId(entry.entryId);
                    System.out.println("[DiaryDAO] 일기 조회 성공 - entry_id: " + entryId);
                    return entry;
                }
            }

            System.err.println("[DiaryDAO] 일기를 찾을 수 없음 - entry_id: " + entryId);
            return null;

        } catch (SQLException e) {
            System.err.println("[DiaryDAO] 일기 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 일기 수정
     *
     * @param entryId 일기 ID
     * @param title 새 제목
     * @param content 새 내용
     * @param stressLevel 새 스트레스 수치
     * @return 수정 성공 여부
     */
    public boolean updateDiary(int entryId, String title, String content, int stressLevel) {
        String sql = "UPDATE diary SET title = ?, content = ?, stress_level = ? WHERE entry_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, stressLevel);
            pstmt.setInt(4, entryId);

            int affectedRows = pstmt.executeUpdate();
            boolean success = affectedRows > 0;

            if (success) {
                System.out.println("[DiaryDAO] 일기 수정 성공 - entry_id: " + entryId);
            } else {
                System.err.println("[DiaryDAO] 일기 수정 실패 - entry_id: " + entryId);
            }

            return success;

        } catch (SQLException e) {
            System.err.println("[DiaryDAO] 일기 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 일기 삭제
     *
     * @param entryId 일기 ID
     * @return 삭제 성공 여부
     */
    public boolean deleteDiary(int entryId) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // 1. 감정 데이터 먼저 삭제 (외래키 제약)
            String emotionSql = "DELETE FROM emotion WHERE entry_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(emotionSql)) {
                pstmt.setInt(1, entryId);
                pstmt.executeUpdate();
            }

            // 2. 일기 삭제
            String diarySql = "DELETE FROM diary WHERE entry_id = ?";
            int affectedRows;
            try (PreparedStatement pstmt = conn.prepareStatement(diarySql)) {
                pstmt.setInt(1, entryId);
                affectedRows = pstmt.executeUpdate();
            }

            conn.commit();
            boolean success = affectedRows > 0;

            if (success) {
                System.out.println("[DiaryDAO] 일기 삭제 성공 - entry_id: " + entryId);
            } else {
                System.err.println("[DiaryDAO] 일기 삭제 실패 - entry_id: " + entryId);
            }

            return success;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("[DiaryDAO] 일기 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 일기별 감정 조회
     *
     * @param entryId 일기 ID
     * @return 감정 데이터 배열
     */
    public EmotionData[] getEmotionsByEntryId(int entryId) {
        String sql = "SELECT emoji_icon, emotion_level FROM emotion WHERE entry_id = ?";
        java.util.List<EmotionData> emotions = new java.util.ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, entryId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    emotions.add(new EmotionData(
                        rs.getString("emoji_icon"),
                        rs.getInt("emotion_level")
                    ));
                }
            }

            return emotions.toArray(new EmotionData[0]);

        } catch (SQLException e) {
            System.err.println("[DiaryDAO] 감정 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return new EmotionData[0];
        }
    }

    /**
     * 일기의 감정 데이터 모두 삭제
     *
     * @param entryId 일기 ID
     * @return 삭제 성공 여부
     */
    public boolean deleteEmotionsByEntryId(int entryId) {
        String sql = "DELETE FROM emotion WHERE entry_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, entryId);
            pstmt.executeUpdate();
            System.out.println("[DiaryDAO] 감정 삭제 성공 - entry_id: " + entryId);
            return true;

        } catch (SQLException e) {
            System.err.println("[DiaryDAO] 감정 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 일기 수정 시 감정도 함께 업데이트
     *
     * @param entryId 일기 ID
     * @param title 새 제목
     * @param content 새 내용
     * @param stressLevel 새 스트레스 수치
     * @param emotions 새 감정 데이터
     * @return 수정 성공 여부
     */
    public boolean updateDiaryWithEmotions(int entryId, String title, String content,
                                          int stressLevel, EmotionData[] emotions) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // 1. 일기 업데이트
            String diarySql = "UPDATE diary SET title = ?, content = ?, stress_level = ? WHERE entry_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(diarySql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, content);
                pstmt.setInt(3, stressLevel);
                pstmt.setInt(4, entryId);
                pstmt.executeUpdate();
            }

            // 2. 기존 감정 삭제
            String deleteSql = "DELETE FROM emotion WHERE entry_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, entryId);
                pstmt.executeUpdate();
            }

            // 3. 새 감정 추가
            String emotionSql = "INSERT INTO emotion (entry_id, emotion_level, emoji_icon) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(emotionSql)) {
                for (EmotionData emotion : emotions) {
                    pstmt.setInt(1, entryId);
                    pstmt.setInt(2, emotion.level);
                    pstmt.setString(3, emotion.emoji);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            conn.commit();
            System.out.println("[DiaryDAO] 일기 및 감정 수정 성공 - entry_id: " + entryId);
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("[DiaryDAO] 일기 및 감정 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

