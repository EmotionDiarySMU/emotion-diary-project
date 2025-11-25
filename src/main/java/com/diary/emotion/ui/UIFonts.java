package com.diary.emotion.ui;

import java.awt.*;

/**
 * Emotion Diary 애플리케이션의 폰트 상수 클래스
 * 타이포그래피 스케일을 제공합니다.
 */
public class UIFonts {

    // 기본 폰트 - SansSerif 우선, 없으면 Dialog
    private static final String PRIMARY_FONT = "SansSerif";
    private static final String FALLBACK_FONT = "Dialog";  // 시스템 기본 폰트

    // 타이포그래피 스케일
    public static final Font H1 = createFont(Font.BOLD, 28);      // 메인 타이틀
    public static final Font H2 = createFont(Font.BOLD, 24);      // 섹션 타이틀
    public static final Font H3 = createFont(Font.BOLD, 18);      // 서브 타이틀

    // 본문
    public static final Font BODY_LARGE = createFont(Font.PLAIN, 16);
    public static final Font BODY_REGULAR = createFont(Font.PLAIN, 14);
    public static final Font BODY_SMALL = createFont(Font.PLAIN, 12);

    // 버튼
    public static final Font BUTTON = createFont(Font.PLAIN, 14);  // 버튼은 크지 않음

    // 이모지 폰트
    public static final Font EMOJI = new Font("Apple Color Emoji", Font.PLAIN, 24);

    // 라벨 폰트
    public static final Font LABEL_FONT = createFont(Font.PLAIN, 14);
    public static final Font DATE_LABEL_FONT = createFont(Font.BOLD, 16); // 날짜 라벨용 폰트

    /**
     * 지정된 스타일과 크기로 폰트를 생성합니다.
     * 기본 폰트가 시스템에 없으면 대체 폰트를 사용합니다.
     */
    private static Font createFont(int style, int size) {
        Font font;
        try {
            font = new Font(PRIMARY_FONT, style & (Font.PLAIN | Font.BOLD | Font.ITALIC), size);
            // 폰트가 실제로 로드되는지 확인
            if (font.getFamily().equals("Dialog")) {
                // PRIMARY_FONT이 로드되지 않았으므로 FALLBACK 사용
                font = new Font(FALLBACK_FONT, style & (Font.PLAIN | Font.BOLD | Font.ITALIC), size);
            }
        } catch (Exception e) {
            // 예외 발생 시 FALLBACK 사용
            font = new Font(FALLBACK_FONT, style & (Font.PLAIN | Font.BOLD | Font.ITALIC), size);
        }
        return font;
    }
}
