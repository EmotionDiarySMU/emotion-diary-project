package com.diary.emotion.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

/**
 * 콤보박스 스타일링 유틸리티 클래스
 * 프로젝트 전체에서 일관된 콤보박스 디자인을 적용하기 위한 클래스
 */
public class ComboBoxStyler {

    /**
     * 날짜 선택용 플랫 스타일 콤보박스 적용
     */
    public static void applyDateComboStyle(JComboBox<?> comboBox, int width) {
        comboBox.setFont(UIFonts.BODY_REGULAR);
        comboBox.setBackground(UIColors.BG_LIGHT_CREAM);
        comboBox.setForeground(Color.BLACK);
        comboBox.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        comboBox.setPreferredSize(new Dimension(width, 28));
        comboBox.setUI(new FlatComboBoxUI(UIColors.BG_LIGHT_CREAM));
        comboBox.setRenderer(new DateComboRenderer());
    }

    /**
     * 통계 화면용 콤보박스 스타일 적용
     */
    public static void applyStatsComboStyle(JComboBox<String> comboBox) {
        comboBox.setFont(UIFonts.BODY_REGULAR.deriveFont(12f));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(UIColors.TEXT_PRIMARY);
        comboBox.setOpaque(true); // 불투명 설정
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1), // 검정색 테두리로 변경
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        comboBox.setFocusable(false);
        comboBox.setUI(new StatsComboBoxUI());
        comboBox.setRenderer(new StatsComboRenderer());
    }

    /**
     * 검색 화면용 콤보박스 스타일 적용 (배경색 유지)
     */
    public static void applySearchComboStyle(JComboBox<Object> comboBox) {
        comboBox.setFont(UIFonts.BODY_REGULAR.deriveFont(12f));
        comboBox.setBackground(UIColors.BG_LIGHT_CREAM);
        comboBox.setOpaque(true);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.DARK_GRAY, 1),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        comboBox.setUI(new SearchComboBoxUI());
        comboBox.setRenderer(new SearchComboRenderer());
    }

    // ==================== 내부 UI 클래스 ====================

    /**
     * 날짜 선택용 플랫 콤보박스 UI
     */
    static class FlatComboBoxUI extends BasicComboBoxUI {
        private final Color backgroundColor;

        public FlatComboBoxUI(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        @Override
        protected JButton createArrowButton() {
            JButton btn = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(backgroundColor);
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    int w = getWidth();
                    int h = getHeight();
                    int size = 5;
                    int x = (w - size) / 2;
                    int y = (h - size) / 2 + 1;

                    g2.setColor(Color.DARK_GRAY);
                    int[] xPoints = {x, x + size, x + size / 2};
                    int[] yPoints = {y, y, y + size - 1};
                    g2.fillPolygon(xPoints, yPoints, 3);
                    g2.dispose();
                }
            };
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
            btn.setPreferredSize(new Dimension(15, 20));
            return btn;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(backgroundColor);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = (BasicComboPopup) super.createPopup();
            popup.setBorder(new LineBorder(Color.DARK_GRAY, 1));
            // 팝업의 스크롤 패널에 스타일 적용 (나중에 적용되도록 invokeLater 사용)
            SwingUtilities.invokeLater(() -> {
                JScrollPane scrollPane = getScrollPane(popup);
                if (scrollPane != null) {
                    ScrollBarStyler.applyViewStyle(scrollPane);
                }
            });
            return popup;
        }

        private JScrollPane getScrollPane(BasicComboPopup popup) {
            for (java.awt.Component comp : popup.getComponents()) {
                if (comp instanceof JScrollPane) {
                    return (JScrollPane) comp;
                }
            }
            return null;
        }
    }

    /**
     * 통계 화면용 콤보박스 UI
     */
    static class StatsComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            BasicArrowButton button = new BasicArrowButton(
                    BasicArrowButton.SOUTH,
                    Color.WHITE,        // 배경색
                    Color.LIGHT_GRAY,   // 그림자
                    Color.BLACK,        // 화살표 색상 (검정색)
                    Color.WHITE         // 하이라이트
            );
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setBackground(Color.WHITE);
            return button;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            // 텍스트 영역 배경을 완전히 흰색으로 칠함
            g.setColor(Color.WHITE);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = (BasicComboPopup) super.createPopup();
            // 팝업의 스크롤 패널에 스타일 적용 (나중에 적용되도록 invokeLater 사용)
            SwingUtilities.invokeLater(() -> {
                JScrollPane scrollPane = getScrollPane(popup);
                if (scrollPane != null) {
                    ScrollBarStyler.applyStatsStyle(scrollPane);
                }
            });
            return popup;
        }

        private JScrollPane getScrollPane(BasicComboPopup popup) {
            for (java.awt.Component comp : popup.getComponents()) {
                if (comp instanceof JScrollPane) {
                    return (JScrollPane) comp;
                }
            }
            return null;
        }
    }

    /**
     * 검색 화면용 콤보박스 UI
     */
    static class SearchComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            BasicArrowButton button = new BasicArrowButton(
                    BasicArrowButton.SOUTH,
                    UIColors.BG_LIGHT_CREAM,
                    Color.LIGHT_GRAY,
                    Color.BLACK,
                    UIColors.BG_LIGHT_CREAM
            );
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setBackground(UIColors.BG_LIGHT_CREAM);
            return button;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(UIColors.BG_LIGHT_CREAM);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = (BasicComboPopup) super.createPopup();
            // 팝업의 스크롤 패널에 스타일 적용 (나중에 적용되도록 invokeLater 사용)
            SwingUtilities.invokeLater(() -> {
                JScrollPane scrollPane = getScrollPane(popup);
                if (scrollPane != null) {
                    ScrollBarStyler.applyViewStyle(scrollPane);
                }
            });
            return popup;
        }

        private JScrollPane getScrollPane(BasicComboPopup popup) {
            for (java.awt.Component comp : popup.getComponents()) {
                if (comp instanceof JScrollPane) {
                    return (JScrollPane) comp;
                }
            }
            return null;
        }
    }

    // ==================== 렌더러 클래스 ====================

    /**
     * 날짜 선택용 콤보박스 렌더러
     */
    static class DateComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            if (isSelected) {
                setBackground(UIColors.BG_STATS);
                setForeground(Color.BLACK);
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            return this;
        }
    }

    /**
     * 통계 화면용 콤보박스 렌더러
     */
    static class StatsComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            renderer.setFont(UIFonts.BODY_REGULAR.deriveFont(12f));
            renderer.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
            renderer.setOpaque(true); // 완전히 불투명하게 설정

            if (isSelected) {
                renderer.setBackground(new Color(230, 240, 255));
                renderer.setForeground(Color.BLACK);
            } else {
                renderer.setBackground(Color.WHITE);
                renderer.setForeground(Color.BLACK);
            }
            return renderer;
        }

        @Override
        protected void paintComponent(Graphics g) {
            // 배경을 먼저 완전히 흰색으로 칠함
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());

            // 텍스트 렌더링 (안티앨리어싱 off)
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            super.paintComponent(g);
        }
    }

    /**
     * 검색 화면용 콤보박스 렌더러
     */
    static class SearchComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            renderer.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
            if (isSelected) {
                renderer.setBackground(new Color(230, 240, 255));
                renderer.setForeground(Color.BLACK);
            } else {
                renderer.setBackground(Color.WHITE);
                renderer.setForeground(Color.BLACK);
            }
            return renderer;
        }
    }
}

