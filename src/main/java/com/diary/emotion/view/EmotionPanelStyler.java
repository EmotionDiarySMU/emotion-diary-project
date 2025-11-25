package com.diary.emotion.view;

import com.diary.emotion.ui.UIColors;

import javax.swing.*;
import java.awt.*;

/**
 * 감정 수치 입력칸 바깥 영역의 배경색을 BG_VIEW로 설정하는 유틸리티 클래스
 * ViewDiaryPanel과 ModifyPanel에서 사용
 */
public class EmotionPanelStyler {

    /**
     * ViewDiaryPanel용 감정 영역 스타일 적용 (감정 칸: BG_LIGHT_CREAM)
     *
     * @param container ViewDiaryPanel 컨테이너
     */
    public static void applyViewEmotionAreaStyle(Container container) {
        if (container == null) {
            return;
        }

        // 재귀적으로 모든 컴포넌트 탐색
        traverseAndStyleEmotionArea(container, UIColors.BG_LIGHT_CREAM);
    }

    /**
     * ModifyPanel용 감정 영역 스타일 적용 (감정 칸: WHITE)
     *
     * @param container ModifyPanel 컨테이너
     */
    public static void applyModifyEmotionAreaStyle(Container container) {
        if (container == null) {
            return;
        }

        // 재귀적으로 모든 컴포넌트 탐색
        traverseAndStyleEmotionArea(container, Color.WHITE);
    }

    /**
     * 컨테이너를 재귀적으로 탐색하여 감정 영역의 배경색을 설정
     *
     * @param container 탐색할 컨테이너
     * @param emotionFieldColor 감정 이모지 칸과 수치 입력 칸의 배경색
     */
    private static void traverseAndStyleEmotionArea(Container container, Color emotionFieldColor) {
        for (Component component : container.getComponents()) {

            // EmotionSlotPanel을 포함하는 패널을 찾아서 배경색 설정
            if (component instanceof JPanel panel) {
                // EmotionSlotPanel을 자식으로 가지고 있는지 확인
                if (containsEmotionSlotPanel(panel)) {
                    // 감정 영역 패널의 배경색을 BG_VIEW로 설정
                    panel.setBackground(UIColors.BG_VIEW);
                    panel.setOpaque(true);
                }

                // EmotionSlotPanel 자체의 배경색도 설정 (클래스 이름으로 확인)
                if (isEmotionSlotPanel(panel)) {
                    panel.setBackground(UIColors.BG_VIEW);
                    panel.setOpaque(true);
                    // EmotionSlotPanel 내부의 모든 하위 패널도 BG_VIEW로 변경
                    styleEmotionSlotPanelChildren(panel, emotionFieldColor);
                }

                // 재귀적으로 하위 컴포넌트 탐색
                traverseAndStyleEmotionArea(panel, emotionFieldColor);
            }
            // Container 타입인 경우 재귀 탐색
            else if (component instanceof Container) {
                traverseAndStyleEmotionArea((Container) component, emotionFieldColor);
            }
        }
    }

    /**
     * EmotionSlotPanel 내부의 모든 하위 컴포넌트의 배경색 설정
     *
     * @param emotionSlotPanel EmotionSlotPanel 인스턴스
     * @param emotionFieldColor 감정 이모지 칸과 수치 입력 칸의 배경색
     */
    private static void styleEmotionSlotPanelChildren(JPanel emotionSlotPanel, Color emotionFieldColor) {
        for (Component component : emotionSlotPanel.getComponents()) {
            // JLabel (감정 이모지 칸)인 경우 지정된 색상으로 설정
            if (component instanceof JLabel label) {
                label.setBackground(emotionFieldColor);
                label.setOpaque(true);
            }
            // JPanel (하위 패널)인 경우
            else if (component instanceof JPanel childPanel) {
                childPanel.setBackground(UIColors.BG_VIEW);
                childPanel.setOpaque(true);
                // 재귀적으로 하위 패널 내부의 컴포넌트 처리
                styleEmotionSlotPanelChildrenRecursive(childPanel, emotionFieldColor);
            } else if (component instanceof Container childContainer) {
                if (childContainer instanceof JComponent jComponent) {
                    jComponent.setBackground(UIColors.BG_VIEW);
                    jComponent.setOpaque(true);
                }
            }
        }
    }

    /**
     * EmotionSlotPanel 하위 패널 내부의 컴포넌트 재귀 처리
     *
     * @param panel 처리할 패널
     * @param emotionFieldColor 감정 수치 입력 칸의 배경색
     */
    private static void styleEmotionSlotPanelChildrenRecursive(JPanel panel, Color emotionFieldColor) {
        for (Component component : panel.getComponents()) {
            // JTextField (감정 수치 입력칸)인 경우 지정된 색상으로 설정
            if (component instanceof JTextField textField) {
                textField.setBackground(emotionFieldColor);
                textField.setOpaque(true);
            }
            // 하위 패널이 더 있는 경우 재귀 처리
            else if (component instanceof JPanel childPanel) {
                childPanel.setBackground(UIColors.BG_VIEW);
                childPanel.setOpaque(true);
                styleEmotionSlotPanelChildrenRecursive(childPanel, emotionFieldColor);
            }
        }
    }

    /**
     * 패널이 EmotionSlotPanel을 자식으로 포함하고 있는지 확인
     *
     * @param panel 확인할 패널
     * @return EmotionSlotPanel을 포함하면 true, 아니면 false
     */
    private static boolean containsEmotionSlotPanel(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (isEmotionSlotPanel(component)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 컴포넌트가 EmotionSlotPanel인지 클래스 이름으로 확인
     *
     * @param component 확인할 컴포넌트
     * @return EmotionSlotPanel이면 true, 아니면 false
     */
    private static boolean isEmotionSlotPanel(Component component) {
        return component.getClass().getSimpleName().equals("EmotionSlotPanel");
    }
}

