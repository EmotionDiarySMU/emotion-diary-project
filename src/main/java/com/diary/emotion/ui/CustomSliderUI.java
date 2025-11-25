package com.diary.emotion.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

/**
 * 커스텀 슬라이더 UI
 * 스트레스 수치 입력용 슬라이더의 디자인을 정의
 */
public class CustomSliderUI extends BasicSliderUI {
    private static final Color TRACK_COLOR = new Color(101, 67, 33);
    private static final Color THUMB_COLOR = Color.WHITE;
    private static final Color THUMB_BORDER_COLOR = Color.BLACK;

    public CustomSliderUI(JSlider slider) {
        super(slider);
    }

    /**
     * 트랙 영역을 아래로 이동시켜 눈금과 맞닿게 조정
     */
    @Override
    protected void calculateTrackRect() {
        super.calculateTrackRect();
        // 트랙을 아래로 8픽셀 이동하여 눈금과 맞닿게 함
        trackRect.y += 8;
    }

    /**
     * 눈금(틱) 높이 조정
     */
    @Override
    protected void calculateTickRect() {
        super.calculateTickRect();
        // 눈금 위치 조정 (필요시 값 변경)
        tickRect.y -= 4; // 양수: 위로 이동, 음수: 아래로 이동
    }

    /**
     * 숫자(레이블) 높이 조정
     */
    @Override
    protected void calculateLabelRect() {
        super.calculateLabelRect();
        // 숫자 위치 조정 (필요시 값 변경)
        labelRect.y -= 4; // 양수: 위로 이동, 음수: 아래로 이동
    }

    /**
     * 그리기 시작 전에 배경을 완전히 지워서 잔상 방지
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g.create();

        // 1. 전체 영역을 배경색으로 완전히 지움 (잔상 제거)
        g2d.setColor(c.getBackground());
        g2d.fillRect(0, 0, c.getWidth(), c.getHeight());

        g2d.dispose();

        // 2. 부모 클래스의 paint를 호출하여 모든 요소를 다시 그림
        super.paint(g, c);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle trackBounds = this.trackRect;
        // 트랙을 슬라이더의 정확한 세로 중앙에 그림
        int trackY = trackBounds.y + (trackBounds.height / 2) - 1;

        g2d.setColor(TRACK_COLOR);
        g2d.fillRect(trackBounds.x, trackY, trackBounds.width, 2); // 두께 2px 유지

        g2d.dispose();
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle thumbBounds = this.thumbRect;
        int diameter = 8; // 12 -> 8로 축소 (작은 동그라미)

        // 썸을 thumbRect의 정확한 중앙에 배치
        int centerX = thumbBounds.x + (thumbBounds.width - diameter) / 2;
        int centerY = thumbBounds.y + (thumbBounds.height - diameter) / 2;

        // 썸 배경 (흰색 원)
        g2d.setColor(THUMB_COLOR);
        g2d.fillOval(centerX, centerY, diameter, diameter);

        // 썸 테두리 (검은색 원)
        g2d.setColor(THUMB_BORDER_COLOR);
        g2d.setStroke(new BasicStroke(1.5f)); // 2f -> 1.5f로 축소 (얇은 테두리)
        g2d.drawOval(centerX, centerY, diameter - 1, diameter - 1);

        g2d.dispose();
    }

    @Override
    protected Dimension getThumbSize() {
        // 동그라미(8px) + 테두리(1.5px) + 최소 여유공간 = 12px
        return new Dimension(12, 12); // 16 -> 12로 축소
    }

    /**
     * 레이블(숫자)을 그릴 때 비활성화 상태에서도 검은색으로 표시
     */
    @Override
    public void paintLabels(Graphics g) {
        // 슬라이더가 비활성화되어 있어도 레이블을 검은색으로 표시
        if (slider.getLabelTable() != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.BLACK); // 레이블 색상을 검은색으로 강제 설정

            // 기존 foreground 색상 저장
            Color originalColor = slider.getForeground();

            // 일시적으로 foreground를 검은색으로 변경
            slider.setForeground(Color.BLACK);

            // 부모 클래스의 paintLabels 호출
            super.paintLabels(g2d);

            // 원래 색상 복원
            slider.setForeground(originalColor);

            g2d.dispose();
        }
    }

    /**
     * 눈금(틱)을 그릴 때 비활성화 상태에서도 검은색으로 표시
     */
    @Override
    public void paintTicks(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK); // 눈금 색상을 검은색으로 강제 설정
        super.paintTicks(g2d);
        g2d.dispose();
    }
}
