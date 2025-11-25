package com.diary.emotion.ui;

import javax.swing.*;

/**
 * Emotion Diary 애플리케이션의 UI 상수 클래스
 * 창 크기, 제목 등의 공통 설정을 제공합니다.
 */
public class UIConstants {

    // 창 설정
    public static final String APP_TITLE = "Emotion Diary";
    public static final int WINDOW_WIDTH = 495;
    public static final int WINDOW_HEIGHT = 630;

    // 모든 JFrame에서 사용
    public static void setupFrame(JFrame frame) {
        frame.setTitle(APP_TITLE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);  // 크기 고정
        frame.setLocationRelativeTo(null);  // 화면 중앙 배치
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // 모든 JDialog에서 사용
    public static void setupDialog(JDialog dialog, JFrame parent) {
        dialog.setTitle(APP_TITLE);
        dialog.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        dialog.setResizable(false);  // 크기 고정
        dialog.setLocationRelativeTo(parent);  // 부모 창 중앙 배치
        dialog.setModal(true);  // 모달 다이얼로그
    }
}
