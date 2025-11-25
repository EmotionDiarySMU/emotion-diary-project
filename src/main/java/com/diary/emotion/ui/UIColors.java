package com.diary.emotion.ui;

import java.awt.Color;

/**
 * Emotion Diary 애플리케이션의 색상 팔레트 상수 클래스
 */
public class UIColors {

    // 화면별 배경색 (Primary Background Colors)
    public static final Color BG_LOGIN = new Color(252, 255, 159);

    // [요청사항 11] 일기 작성 화면 배경색 변경 (rgba(207, 255, 172))
    public static final Color BG_WRITE = new Color(207, 255, 172);

    public static final Color BG_VIEW = new Color(255, 189, 149);
    public static final Color BG_STATS = new Color(193, 217, 255);

    // 공통 배경색
    public static final Color BG_WHITE = Color.WHITE;
    public static final Color BG_CREAM = new Color(255, 253, 247);
    public static final Color BG_LIGHT_CREAM = new Color(254, 242, 216);

    // Accent Colors (강조 색상)
    public static final Color ACCENT_YELLOW = new Color(252, 238, 100);
    public static final Color ACCENT_GREEN = new Color(100, 200, 100);
    public static final Color ACCENT_ORANGE = new Color(255, 160, 122);
    public static final Color ACCENT_BLUE = new Color(135, 206, 250);

    // 텍스트 색상
    public static final Color TEXT_PRIMARY = new Color(51, 51, 51);
    public static final Color TEXT_SECONDARY = new Color(102, 102, 102);
    public static final Color TEXT_HINT = new Color(153, 153, 153);
    public static final Color TEXT_LINK = new Color(0, 102, 204);
    public static final Color TEXT_WHITE = Color.WHITE;

    // 버튼 색상
    public static final Color BUTTON_PRIMARY_BG = Color.WHITE;
    public static final Color BUTTON_PRIMARY_TEXT = Color.BLACK;
    public static final Color BUTTON_SECONDARY_BG = new Color(224, 224, 224);
    public static final Color BUTTON_SECONDARY_TEXT = new Color(51, 51, 51);

    // 상태 색상
    public static final Color STATE_ERROR = new Color(255, 82, 82);
    public static final Color STATE_SUCCESS = new Color(0, 122, 255);
    public static final Color TEXT_DISABLED = new Color(189, 189, 189);

    // 경계선 색상
    public static final Color BORDER_LIGHT = new Color(230, 230, 230);
    public static final Color BORDER_MEDIUM = new Color(200, 200, 200);
    public static final Color BORDER_DARK = new Color(180, 180, 180);

    // 모드별 색상
    public static final Color VIEW_MODE_BG = new Color(230, 240, 255);
    public static final Color VIEW_MODE_BORDER = new Color(100, 150, 255);
    public static final Color EDIT_MODE_BORDER = new Color(255, 100, 100);

    // 차트 색상
    public static final Color CHART_BAR_PRIMARY = new Color(100, 181, 246);
    public static final Color CHART_BAR_SECONDARY = new Color(255, 200, 100);
    public static final Color CHART_GRID = new Color(220, 220, 220);
    public static final Color CHART_LINE_PRIMARY = new Color(100, 149, 237); // Cornflower Blue

    // TEXT_ERROR 및 TEXT_SUCCESS 추가
    public static final Color TEXT_ERROR = STATE_ERROR;
    public static final Color TEXT_SUCCESS = STATE_SUCCESS;
}