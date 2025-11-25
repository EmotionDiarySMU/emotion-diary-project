package com.diary.emotion.ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

import static com.diary.emotion.ui.UIFonts.BODY_REGULAR;

/**
 * 버튼 생성 팩토리 클래스
 * 프로젝트 전체에서 일관된 버튼 디자인을 적용하기 위한 유틸리티
 */
public class ButtonFactory {

    /**
     * 커스텀 스타일 버튼 생성 (둥근 모서리)
     * @param text 버튼 텍스트
     * @param bg 배경색
     * @param fg 글자색
     * @return 스타일이 적용된 JButton
     */
    public static JButton createCustomButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 배경 그리기 (둥근 모서리)
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(80, 30));
        btn.setFont(new Font(BODY_REGULAR.getName(), Font.PLAIN, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(new RoundedBorder(Color.BLACK, 1, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false); // 들뜸 방지
        return btn;
    }

    /**
     * 기본 크기의 커스텀 버튼 생성
     * @param text 버튼 텍스트
     * @param bg 배경색
     * @param fg 글자색
     * @param width 버튼 너비
     * @param height 버튼 높이
     * @return 스타일이 적용된 JButton
     */
    public static JButton createCustomButton(String text, Color bg, Color fg, int width, int height) {
        JButton btn = createCustomButton(text, bg, fg);
        btn.setPreferredSize(new Dimension(width, height));
        return btn;
    }

    /**
     * 둥근 모서리 버튼 생성 (일기 작성 화면용)
     * @param text 버튼 텍스트
     * @return 스타일이 적용된 JButton
     */
    public static JButton createRoundedButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 배경 그리기 (둥근 모서리)
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(new RoundedBorder(Color.BLACK, 1, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false); // 들뜸 방지
        return btn;
    }

    /**
     * 둥근 모서리 테두리 클래스
     */
    public static class RoundedBorder extends AbstractBorder {
        private Color color;
        private int thickness;
        private int radius;

        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 테두리만 그리기 (배경은 paintComponent에서 처리)
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness + 2, thickness + 2, thickness + 2, thickness + 2);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = thickness + 2;
            return insets;
        }
    }
}
