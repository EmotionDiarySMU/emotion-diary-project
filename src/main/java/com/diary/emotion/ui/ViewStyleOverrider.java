package com.diary.emotion.ui;

import com.diary.emotion.write.WriteDiaryGUI;
import com.diary.emotion.write.EmotionSlotPanel;
import com.diary.emotion.view.ViewDiaryPanel;
import com.diary.emotion.view.ModifyPanel; // ModifyPanel 클래스 경로 추가

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusListener;

/**
 * [디자인 통제 센터 - 최종 완성판]
 * WriteDiaryGUI 내부의 깊숙한 곳까지 탐색하여 디자인을 강제 적용합니다.
 */
public class ViewStyleOverrider {

    // [공통 사항 1] 테두리: 검정색 1px 고정
    private static final Border BLACK_BORDER = new LineBorder(Color.BLACK, 1);

    // [공통 사항 2] 패딩: 위아래 8px, 좌우 12px
    private static final Border PADDING_BORDER = new EmptyBorder(8, 12, 8, 12);

    // 최종 입력칸 스타일 (검정 테두리 + 패딩)
    private static final Border STYLED_BORDER = new CompoundBorder(BLACK_BORDER, PADDING_BORDER);

    // [배경색]
    // 전체 배경 (연한 살구색)
    private static final Color MAIN_BG_COLOR = UIColors.BG_VIEW;
    // 감정 이모지 칸 주변 배경 (진한 살구색: r255, g160, b122)
    private static final Color EMOTION_AREA_BG = UIColors.BG_VIEW;

    /**
     * 스타일 일괄 적용 진입점 (WriteDiaryGUI용)
     */
    public static void applyStyle(WriteDiaryGUI panel) {
        // 1. 전체 여백 설정 [공통 사항 4]
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setBackground(MAIN_BG_COLOR);

        // 2. 재귀적으로 모든 컴포넌트 탐색 및 스타일링
        updateComponentStyles(panel);

        // 3. 화면 강제 갱신
        panel.revalidate();
        panel.repaint();
    }

    /**
     * 스타일 일괄 적용 진입점 (ViewDiaryPanel용)
     */
    public static void applyStyle(ViewDiaryPanel panel) {
        // ViewDiaryPanel에 특화된 스타일 적용 로직
        panel.setBackground(UIColors.BG_VIEW);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 2. 재귀적으로 모든 컴포넌트 탐색 및 스타일링
        updateComponentStyles(panel);

        // 3. 화면 강제 갱신
        panel.revalidate();
        panel.repaint();
    }

    /**
     * 스타일 일괄 적용 진입점 (ModifyPanel용)
     */
    public static void applyStyle(ModifyPanel panel) {
        if (panel == null) {
            throw new IllegalArgumentException("ModifyPanel cannot be null");
        }
        // ModifyPanel에 특화된 스타일 적용 로직
        panel.setBackground(UIColors.BG_VIEW);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // ModifyPanel 내부 컴포넌트 스타일 적용
        updateComponentStyles(panel);

        // 화면 강제 갱신
        panel.revalidate();
        panel.repaint();

        // 추가 스타일링: 날짜 라벨
        if (panel.dateLabel != null) {
            panel.dateLabel.setFont(UIFonts.LABEL_FONT);
            panel.dateLabel.setForeground(UIColors.TEXT_PRIMARY);
        }
    }

    /**
     * 컴포넌트 전수 조사 및 스타일 변경
     */
    private static void updateComponentStyles(Container container) {
        for (Component c : container.getComponents()) {

            // -------------------------------------------------------
            // 1. 텍스트 필드 (제목, 감정 수치, 스트레스 수치)
            // -------------------------------------------------------
            if (c instanceof JTextField field) {
                removeFocusListeners(field);
                field.setBorder(STYLED_BORDER);
                field.setBackground(Color.WHITE);
                field.setOpaque(true);
                field.setDisabledTextColor(Color.BLACK);
            }

            // -------------------------------------------------------
            // 2. 내용 입력 영역 (JTextArea)
            // -------------------------------------------------------
            else if (c instanceof JTextArea area) {
                removeFocusListeners(area);
                area.setBorder(new EmptyBorder(10, 10, 10, 10));
                area.setDisabledTextColor(Color.BLACK);
                area.setBackground(Color.WHITE);
            }

            // -------------------------------------------------------
            // 3. 스크롤 패널 (내용 입력 칸 틀)
            // -------------------------------------------------------
            else if (c instanceof JScrollPane scroll) {
                scroll.setBorder(BLACK_BORDER);
                scroll.getViewport().setBackground(Color.WHITE);
                Component view = scroll.getViewport().getView();
                if (view instanceof Container containerView) {
                    updateComponentStyles(containerView);
                }
            }

            // -------------------------------------------------------
            // 4. 슬라이더 (스트레스 조절)
            // -------------------------------------------------------
            else if (c instanceof JSlider) {
                c.setBackground(MAIN_BG_COLOR);
            }

            // -------------------------------------------------------
            // 5. 버튼 (필요 시 배경색 적용)
            // -------------------------------------------------------
            else if (c instanceof JButton btn) {
                Container parent = btn.getParent();
                if (parent != null && parent.getBackground() != null) {
                    if (!btn.isOpaque() || !btn.isContentAreaFilled()) {
                        // 버튼 스타일 추가 가능
                    }
                }
            }

            // -------------------------------------------------------
            // 6. EmotionSlotPanel (감정 입력 슬롯) - 특별 처리
            // -------------------------------------------------------
            else if (c instanceof EmotionSlotPanel slotPanel) {
                slotPanel.setBackground(MAIN_BG_COLOR);
                updateComponentStyles(slotPanel);
                JLabel iconLabel = slotPanel.getIconLabel();
                if (iconLabel != null) {
                    iconLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                }
                JTextField valueField = slotPanel.getValueField();
                if (valueField != null) {
                    removeFocusListeners(valueField);
                    valueField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                    valueField.setBackground(Color.WHITE);
                    valueField.setOpaque(true);
                    valueField.setDisabledTextColor(Color.BLACK);
                }
            }

            // -------------------------------------------------------
            // 7. 패널 (배경색 처리)
            // -------------------------------------------------------
            else if (c instanceof JPanel p) {
                if (p.isOpaque()) {
                    if (isEmotionRelatedPanel(p)) {
                        p.setBackground(EMOTION_AREA_BG);
                    } else {
                        p.setBackground(MAIN_BG_COLOR);
                    }
                }
                Border border = p.getBorder();
                if (border instanceof LineBorder lineBorder) {
                    if (!Color.BLACK.equals(lineBorder.getLineColor())) {
                        p.setBorder(null);
                    }
                }
                updateComponentStyles(p);
            }

            // -------------------------------------------------------
            // 8. 기타 Container (재귀 탐색)
            // -------------------------------------------------------
            else if (c instanceof Container containerChild) {
                updateComponentStyles(containerChild);
            }
        }
    }

    /**
     * [스마트 감지] 감정 이모지나 감정 수치 입력칸을 포함하는 패널인지 확인
     */
    private static boolean isEmotionRelatedPanel(JPanel p) {
        // EmotionSlotPanel의 부모 패널인지 확인
        for (Component child : p.getComponents()) {
            if (child instanceof EmotionSlotPanel) {
                return true;
            }
        }

        // 감정 관련 컴포넌트 감지 (기존 로직 유지)
        for (Component child : p.getComponents()) {
            // 이모지 라벨 감지 (폰트가 크거나 내용이 짧음)
            if (child instanceof JLabel) {
                JLabel label = (JLabel) child;
                String text = label.getText();
                Font f = child.getFont();

                // 이모지 폰트 감지 또는 큰 폰트 감지
                if (f != null && (f.getSize() > 18 || "Apple Color Emoji".equals(f.getName()))) {
                    return true;
                }

                // 이모지 문자 감지 (유니코드 범위 체크)
                if (text != null && !text.isEmpty() && containsEmoji(text)) {
                    return true;
                }
            }

            // 감정 수치 입력칸 감지 (작은 텍스트필드)
            if (child instanceof JTextField) {
                JTextField field = (JTextField) child;
                Dimension prefSize = field.getPreferredSize();
                if (prefSize != null && prefSize.width < 60) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 문자열에 이모지가 포함되어 있는지 확인
     */
    private static boolean containsEmoji(String text) {
        for (char c : text.toCharArray()) {
            // 이모지는 일반적으로 U+1F300 이상의 유니코드 범위에 있음
            if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
                return true;
            }
            // 일반적인 이모지 범위 체크
            int codePoint = c;
            if (codePoint >= 0x1F300 && codePoint <= 0x1F9FF) {
                return true;
            }
        }
        return false;
    }

    /**
     * FocusListener 강제 제거 (파란 테두리 방지)
     */
    private static void removeFocusListeners(JComponent c) {
        for (FocusListener fl : c.getFocusListeners()) {
            c.removeFocusListener(fl);
        }
    }

    /**
     * [공통 사항 7] 날짜 라벨 추가 ([2025-11-24])
     */
    public static void addDateLabel(JPanel panel, String dateString) {
        // titleField를 찾아서 그 부모에 날짜 라벨 추가
        JTextField titleField = findTitleField(panel);
        if (titleField == null) return;

        Container parent = titleField.getParent();
        if (parent != null) {
            // 중복 추가 방지
            for (Component c : parent.getComponents()) {
                if ("DateLabel".equals(c.getName())) {
                    parent.remove(c);
                }
            }

            JLabel dateLabel = new JLabel("[" + dateString + "]");
            dateLabel.setName("DateLabel"); // 식별 태그
            dateLabel.setFont(UIFonts.BODY_REGULAR);
            dateLabel.setForeground(Color.BLACK);
            dateLabel.setBorder(new EmptyBorder(0, 0, 5, 0)); // 제목과의 간격

            // 제목 필드가 있는 패널의 맨 앞에 추가
            if (parent instanceof JPanel) {
                try {
                    parent.add(dateLabel, 0);
                } catch (Exception e) {
                    parent.add(dateLabel);
                }
            }
            parent.revalidate();
            parent.repaint();
        }
    }

    /**
     * 패널에서 titleField를 찾는 헬퍼 메서드
     */
    private static JTextField findTitleField(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JTextField) {
                JTextField field = (JTextField) c;
                // 제목 필드는 보통 첫 번째 또는 크기가 큰 텍스트 필드
                Dimension size = field.getPreferredSize();
                if (size != null && size.width > 50) {
                    return field;
                }
            }
            if (c instanceof Container) {
                JTextField found = findTitleField((Container) c);
                if (found != null) return found;
            }
        }
        return null;
    }
}
