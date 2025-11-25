package com.diary.emotion.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Emotion Diary 애플리케이션의 공통 스타일링 유틸리티 클래스
 * 재사용 가능한 UI 컴포넌트 생성 및 스타일링을 제공합니다.
 */
public class StyleUtils {

    /**
     * 스타일링된 버튼을 생성합니다.
     */
    public static JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(UIFonts.BUTTON);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg, 1, true),
            BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * 기본 스타일의 버튼을 생성합니다.
     */
    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, UIColors.BUTTON_PRIMARY_BG, UIColors.BUTTON_PRIMARY_TEXT);
    }

    /**
     * 스타일링된 텍스트 필드를 생성합니다.
     */
    public static JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(UIFonts.BODY_REGULAR);
        field.setBackground(UIColors.BG_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColors.BORDER_DARK, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    /**
     * 스타일링된 패스워드 필드를 생성합니다.
     */
    public static JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(UIFonts.BODY_REGULAR);
        field.setBackground(UIColors.BG_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIColors.BORDER_DARK, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    /**
     * 스타일링된 텍스트 영역을 생성합니다.
     */
    public static JTextArea createStyledTextArea(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        area.setFont(UIFonts.BODY_REGULAR);
        area.setBackground(UIColors.BG_WHITE);
        area.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    /**
     * 스타일링된 라벨을 생성합니다.
     */
    public static JLabel createStyledLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(UIFonts.BODY_REGULAR);
        label.setForeground(color);
        return label;
    }

    /**
     * 기본 스타일의 라벨을 생성합니다.
     */
    public static JLabel createPrimaryLabel(String text) {
        return createStyledLabel(text, UIColors.TEXT_PRIMARY);
    }

    /**
     * 보조 텍스트 라벨을 생성합니다.
     */
    public static JLabel createSecondaryLabel(String text) {
        return createStyledLabel(text, UIColors.TEXT_SECONDARY);
    }

    /**
     * 에러 메시지 라벨을 생성합니다.
     */
    public static JLabel createErrorLabel(String text) {
        return createStyledLabel(text, UIColors.TEXT_ERROR);
    }

    /**
     * 성공 메시지 라벨을 생성합니다.
     */
    public static JLabel createSuccessLabel(String text) {
        return createStyledLabel(text, UIColors.TEXT_SUCCESS);
    }

    /**
     * 라벨이 있는 컴포넌트를 생성합니다.
     */
    public static JPanel createLabeledComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(UIFonts.BODY_REGULAR);
        label.setForeground(UIColors.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(component);

        return panel;
    }

    /**
     * 카드 스타일의 패널을 생성합니다.
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(UIColors.BG_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIColors.BORDER_LIGHT, 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    /**
     * 둥근 모서리의 버튼을 생성합니다.
     */
    public static JButton createRoundedButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                   RenderingHints.VALUE_ANTIALIAS_ON);

                // 배경 그리기
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // 테두리 그리기
                g2d.setColor(getBackground().darker());
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                super.paintComponent(g);
            }
        };

        btn.setFont(UIFonts.BUTTON);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);  // 기본 배경 제거

        return btn;
    }

    /**
     * 링크 스타일의 버튼을 생성합니다.
     */
    public static JButton createLinkButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UIFonts.BODY_REGULAR);
        btn.setForeground(UIColors.TEXT_LINK);
        btn.setBackground(null);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 호버 효과
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setForeground(UIColors.TEXT_PRIMARY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setForeground(UIColors.TEXT_LINK);
            }
        });

        return btn;
    }
}
