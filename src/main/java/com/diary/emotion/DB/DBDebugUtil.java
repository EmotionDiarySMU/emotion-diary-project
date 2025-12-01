package com.diary.emotion.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * DB 데이터 확인용 디버깅 유틸리티
 */
public class DBDebugUtil {

    /**
     * 특정 사용자의 모든 감정 데이터 조회 및 출력
     */
    public static void printAllEmotions(String userId) {
        String sql = "SELECT d.entry_id, d.title, d.entry_date, e.emoji_icon, e.emotion_level " +
                     "FROM diary d " +
                     "JOIN emotion e ON d.entry_id = e.entry_id " +
                     "WHERE d.user_id = ? " +
                     "ORDER BY d.entry_date DESC, d.entry_id DESC";

        System.out.println("\n========== [DB 감정 데이터 전체 조회] ==========");
        System.out.println("사용자: " + userId);
        System.out.println("================================================");

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                int count = 0;
                int currentEntryId = -1;

                while (rs.next()) {
                    int entryId = rs.getInt("entry_id");
                    String title = rs.getString("title");
                    String entryDate = rs.getString("entry_date");
                    String emoji = rs.getString("emoji_icon");
                    int level = rs.getInt("emotion_level");

                    if (entryId != currentEntryId) {
                        if (currentEntryId != -1) {
                            System.out.println();
                        }
                        System.out.println("일기 #" + entryId + " [" + title + "] - " + entryDate);
                        currentEntryId = entryId;
                    }

                    System.out.println("  └─ " + emoji + " (강도: " + level + ")");
                    count++;
                }

                System.out.println("\n총 감정 데이터: " + count + "건");
                System.out.println("================================================\n");
            }
        } catch (Exception e) {
            System.err.println("DB 조회 오류:");
            e.printStackTrace();
        }
    }

    /**
     * 특정 entry_id의 모든 감정 조회
     */
    public static void printEmotionsForEntry(int entryId) {
        String sql = "SELECT emoji_icon, emotion_level FROM emotion WHERE entry_id = ?";

        System.out.println("\n=== [Entry #" + entryId + " 감정 데이터] ===");

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, entryId);

            try (ResultSet rs = pstmt.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    String emoji = rs.getString("emoji_icon");
                    int level = rs.getInt("emotion_level");
                    System.out.println(emoji + " - 강도: " + level);
                    count++;
                }
                System.out.println("총 " + count + "개 감정");
                System.out.println("================================\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

