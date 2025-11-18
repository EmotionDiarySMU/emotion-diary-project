package com.diary.emotion.util;

/**
 * 세션 관리 클래스
 * 현재 로그인한 사용자의 정보를 관리합니다.
 *
 * 사용법:
 * - Session.setCurrentUserId("testuser") - 로그인 시
 * - String userId = Session.getCurrentUserId() - 현재 사용자 ID 가져오기
 * - Session.logout() - 로그아웃
 * - boolean loggedIn = Session.isLoggedIn() - 로그인 상태 확인
 */
public class Session {

    // 현재 로그인한 사용자의 ID를 저장하는 정적 변수
    private static String currentUserId = null;

    /**
     * 현재 로그인한 사용자의 ID를 반환합니다.
     *
     * @return 현재 사용자 ID (로그인 안 되어 있으면 null)
     */
    public static String getCurrentUserId() {
        return currentUserId;
    }

    /**
     * 로그인 시 현재 사용자 ID를 설정합니다.
     *
     * @param userId 로그인한 사용자 ID
     */
    public static void setCurrentUserId(String userId) {
        currentUserId = userId;
        System.out.println("[Session] 로그인: " + userId);
    }

    /**
     * 로그아웃 처리를 합니다.
     * 현재 사용자 ID를 null로 설정합니다.
     */
    public static void logout() {
        System.out.println("[Session] 로그아웃: " + currentUserId);
        currentUserId = null;
    }

    /**
     * 현재 로그인 상태를 확인합니다.
     *
     * @return 로그인 상태이면 true, 아니면 false
     */
    public static boolean isLoggedIn() {
        return currentUserId != null;
    }
}

