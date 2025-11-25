package com.diary.emotion.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * 스크롤바 스타일링 유틸리티 클래스
 * 프로젝트 전체에서 일관된 스크롤바 디자인을 적용하기 위한 유틸리티
 */
public class ScrollBarStyler {

    /**
     * 플랫 스타일 스크롤바 UI
     * 열람 탭의 일기 목록에 사용되는 스크롤 디자인
     */
    public static class FlatScrollBarUI extends BasicScrollBarUI {
        private final Color customThumbColor;
        private final Color customTrackColor;

        public FlatScrollBarUI() {
            this(Color.BLACK, UIColors.BG_LIGHT_CREAM);
        }

        public FlatScrollBarUI(Color thumbColor, Color trackColor) {
            this.customThumbColor = thumbColor;
            this.customTrackColor = trackColor;
        }

        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = customThumbColor;
            this.trackColor = customTrackColor;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            return btn;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(customThumbColor);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                           thumbBounds.width - 4, thumbBounds.height - 4, 10, 10);
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(customTrackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }
    }

    /**
     * JScrollPane에 플랫 스타일 적용
     * @param scrollPane 스타일을 적용할 스크롤 패널
     */
    public static void applyFlatStyle(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new FlatScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new FlatScrollBarUI());
    }

    /**
     * JScrollPane에 커스텀 색상의 플랫 스타일 적용
     * @param scrollPane 스타일을 적용할 스크롤 패널
     * @param thumbColor 스크롤바 썸(thumb) 색상
     * @param trackColor 스크롤바 트랙(track) 색상
     */
    public static void applyFlatStyle(JScrollPane scrollPane, Color thumbColor, Color trackColor) {
        if (scrollPane == null) return;

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();

        if (verticalScrollBar != null) {
            verticalScrollBar.setUI(new FlatScrollBarUI(thumbColor, trackColor));
        }
        if (horizontalScrollBar != null) {
            horizontalScrollBar.setUI(new FlatScrollBarUI(thumbColor, trackColor));
        }
    }

    /**
     * 열람 탭 스타일 스크롤바 적용
     * @param scrollPane 스타일을 적용할 스크롤 패널
     */
    public static void applyViewStyle(JScrollPane scrollPane) {
        applyFlatStyle(scrollPane, Color.BLACK, UIColors.BG_LIGHT_CREAM);
    }

    /**
     * 작성 탭 스타일 스크롤바 적용
     * @param scrollPane 스타일을 적용할 스크롤 패널
     */
    public static void applyWriteStyle(JScrollPane scrollPane) {
        applyFlatStyle(scrollPane, Color.BLACK, UIColors.BG_WRITE);
    }

    /**
     * 통계 탭 스타일 스크롤바 적용
     * @param scrollPane 스타일을 적용할 스크롤 패널
     */
    public static void applyStatsStyle(JScrollPane scrollPane) {
        applyFlatStyle(scrollPane, Color.BLACK, UIColors.BG_STATS);
    }
}

