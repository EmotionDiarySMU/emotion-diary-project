package com.diary.emotion.view;

import com.diary.emotion.model.DiaryDAO;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 일기 수정 화면 View 클래스
 * WriteDiaryView와 유사하지만 기존 일기 데이터를 로드하여 수정할 수 있습니다.
 */
public class EditDiaryView extends JPanel {

    // UI 컴포넌트
    private JTextField titleField;
    private JTextArea contentArea;
    private JSlider stressSlider;
    private JLabel stressValueLabel;
    private JButton updateButton;
    private JButton cancelButton;

    // 감정 선택 체크박스와 슬라이더
    private Map<String, JCheckBox> emotionCheckBoxes;
    private Map<String, JSlider> emotionSliders;
    private Map<String, JLabel> emotionValueLabels;

    // 현재 수정 중인 일기 ID
    private int currentEntryId = -1;

    // 파스텔 톤 색상
    private static final Color PASTEL_BLUE = new Color(230, 240, 255);
    private static final Color PASTEL_YELLOW = new Color(255, 255, 220);

    // 12가지 감정 정의 (이모지 + 이름)
    private static final String[][] EMOTIONS = {
        // 긍정적 감정
        {"😊", "행복"},
        {"😆", "신남"},
        {"😍", "설렘"},
        {"😌", "편안"},
        {"😂", "재미"},
        {"🤗", "고마움"},
        // 부정적 감정
        {"😢", "슬픔"},
        {"😠", "분노"},
        {"😰", "불안"},
        {"😅", "민망"},
        {"😧", "당황"},
        {"😔", "미안함"}
    };

    /**
     * EditDiaryView 생성자
     */
    public EditDiaryView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(PASTEL_BLUE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 초기화
        emotionCheckBoxes = new HashMap<>();
        emotionSliders = new HashMap<>();
        emotionValueLabels = new HashMap<>();

        // 상단 패널 (제목)
        add(createTitlePanel(), BorderLayout.NORTH);

        // 중앙 패널 (스크롤 가능한 내용 + 감정 + 스트레스)
        add(createCenterPanel(), BorderLayout.CENTER);

        // 하단 패널 (버튼)
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    /**
     * 제목 입력 패널 생성
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(PASTEL_BLUE);

        JLabel titleLabel = new JLabel("제목:");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        titleField = new JTextField();
        titleField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(titleField, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 중앙 패널 생성 (스크롤 가능)
     */
    private JPanel createCenterPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(PASTEL_BLUE);

        // 1. 내용 입력 영역
        mainPanel.add(createContentPanel());
        mainPanel.add(Box.createVerticalStrut(10));

        // 2. 감정 선택 영역
        mainPanel.add(createEmotionPanel());
        mainPanel.add(Box.createVerticalStrut(10));

        // 3. 스트레스 영역
        mainPanel.add(createStressPanel());

        // 스크롤 가능하게
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(PASTEL_BLUE);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    /**
     * 내용 입력 패널 생성
     */
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(PASTEL_BLUE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel label = new JLabel("내용:");
        label.setFont(new Font("SansSerif", Font.BOLD, 14));

        contentArea = new JTextArea(6, 40);
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(contentArea);

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 감정 선택 패널 생성 (12가지 감정)
     */
    private JPanel createEmotionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PASTEL_YELLOW);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            "감정 선택 (최대 4개)",
            0, 0,
            new Font("SansSerif", Font.BOLD, 13)
        ));

        // 긍정적 감정 (6개)
        JPanel positivePanel = new JPanel();
        positivePanel.setLayout(new BoxLayout(positivePanel, BoxLayout.Y_AXIS));
        positivePanel.setBackground(PASTEL_YELLOW);
        positivePanel.setBorder(BorderFactory.createTitledBorder("긍정적 감정"));

        for (int i = 0; i < 6; i++) {
            positivePanel.add(createEmotionRow(EMOTIONS[i][0], EMOTIONS[i][1]));
        }

        // 부정적 감정 (6개)
        JPanel negativePanel = new JPanel();
        negativePanel.setLayout(new BoxLayout(negativePanel, BoxLayout.Y_AXIS));
        negativePanel.setBackground(PASTEL_YELLOW);
        negativePanel.setBorder(BorderFactory.createTitledBorder("부정적 감정"));

        for (int i = 6; i < 12; i++) {
            negativePanel.add(createEmotionRow(EMOTIONS[i][0], EMOTIONS[i][1]));
        }

        panel.add(positivePanel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(negativePanel);

        return panel;
    }

    /**
     * 개별 감정 행 생성 (체크박스 + 슬라이더)
     */
    private JPanel createEmotionRow(String emoji, String name) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(PASTEL_YELLOW);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // 체크박스
        JCheckBox checkBox = new JCheckBox(emoji + " " + name);
        checkBox.setBackground(PASTEL_YELLOW);
        checkBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        emotionCheckBoxes.put(emoji, checkBox);

        // 슬라이더 패널
        JPanel sliderPanel = new JPanel(new BorderLayout(5, 0));
        sliderPanel.setBackground(PASTEL_YELLOW);

        JSlider slider = new JSlider(0, 100, 50);
        slider.setBackground(PASTEL_YELLOW);
        slider.setEnabled(false); // 기본적으로 비활성화
        emotionSliders.put(emoji, slider);

        JLabel valueLabel = new JLabel("50");
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        valueLabel.setPreferredSize(new Dimension(30, 20));
        emotionValueLabels.put(emoji, valueLabel);

        sliderPanel.add(slider, BorderLayout.CENTER);
        sliderPanel.add(valueLabel, BorderLayout.EAST);

        // 체크박스 선택 시 슬라이더 활성화
        checkBox.addActionListener(e -> {
            boolean selected = checkBox.isSelected();
            slider.setEnabled(selected);

            if (selected) {
                // 선택된 감정이 4개를 초과하는지 확인
                long selectedCount = emotionCheckBoxes.values().stream()
                    .filter(JCheckBox::isSelected)
                    .count();

                if (selectedCount > 4) {
                    checkBox.setSelected(false);
                    slider.setEnabled(false);
                    JOptionPane.showMessageDialog(this,
                        "감정은 최대 4개까지 선택할 수 있습니다.",
                        "선택 제한",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // 슬라이더 변경 시 값 표시
        slider.addChangeListener(e -> {
            valueLabel.setText(String.valueOf(slider.getValue()));
        });

        panel.add(checkBox, BorderLayout.WEST);
        panel.add(sliderPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 스트레스 수치 패널 생성
     */
    private JPanel createStressPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(new Color(255, 230, 230)); // 연한 핑크
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            "스트레스 수치",
            0, 0,
            new Font("SansSerif", Font.BOLD, 13)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel label = new JLabel("스트레스:");
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));

        stressSlider = new JSlider(0, 100, 50);
        stressSlider.setMajorTickSpacing(25);
        stressSlider.setMinorTickSpacing(5);
        stressSlider.setPaintTicks(true);
        stressSlider.setPaintLabels(true);
        stressSlider.setBackground(new Color(255, 230, 230));

        stressValueLabel = new JLabel("50", SwingConstants.CENTER);
        stressValueLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        stressValueLabel.setPreferredSize(new Dimension(40, 20));

        stressSlider.addChangeListener(e -> {
            stressValueLabel.setText(String.valueOf(stressSlider.getValue()));
        });

        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(new Color(255, 230, 230));
        labelPanel.add(label, BorderLayout.WEST);
        labelPanel.add(stressValueLabel, BorderLayout.EAST);

        panel.add(labelPanel, BorderLayout.NORTH);
        panel.add(stressSlider, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 버튼 패널 생성
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(PASTEL_BLUE);

        cancelButton = new JButton("취소");
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelButton.setBackground(new Color(211, 211, 211));
        cancelButton.setPreferredSize(new Dimension(100, 35));

        updateButton = new JButton("수정 완료");
        updateButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        updateButton.setBackground(new Color(135, 206, 250)); // 라이트 블루
        updateButton.setPreferredSize(new Dimension(120, 35));

        panel.add(cancelButton);
        panel.add(updateButton);

        return panel;
    }

    /**
     * 기존 일기 데이터 로드
     */
    public void loadDiary(DiaryDAO.DiaryEntry diary) {
        if (diary == null) {
            return;
        }

        currentEntryId = diary.entryId;

        // 제목, 내용, 스트레스 설정
        titleField.setText(diary.title);
        contentArea.setText(diary.content);
        stressSlider.setValue(diary.stressLevel);

        // 모든 감정 초기화
        clearEmotions();

        // 기존 감정 데이터 로드
        if (diary.emotions != null) {
            for (DiaryDAO.EmotionData emotion : diary.emotions) {
                JCheckBox checkBox = emotionCheckBoxes.get(emotion.emoji);
                JSlider slider = emotionSliders.get(emotion.emoji);

                if (checkBox != null && slider != null) {
                    checkBox.setSelected(true);
                    slider.setEnabled(true);
                    slider.setValue(emotion.level);
                }
            }
        }
    }

    /**
     * 감정 선택 초기화
     */
    private void clearEmotions() {
        emotionCheckBoxes.values().forEach(cb -> cb.setSelected(false));
        emotionSliders.values().forEach(slider -> {
            slider.setValue(50);
            slider.setEnabled(false);
        });
    }

    // Getter 메소드들

    public int getCurrentEntryId() {
        return currentEntryId;
    }

    public String getTitle() {
        return titleField.getText().trim();
    }

    public String getContent() {
        return contentArea.getText().trim();
    }

    public int getStressLevel() {
        return stressSlider.getValue();
    }

    /**
     * 선택된 감정들을 Map 형태로 반환
     * @return Map<이모지, 수치>
     */
    public Map<String, Integer> getSelectedEmotions() {
        Map<String, Integer> selected = new HashMap<>();

        emotionCheckBoxes.forEach((emoji, checkBox) -> {
            if (checkBox.isSelected()) {
                int level = emotionSliders.get(emoji).getValue();
                selected.put(emoji, level);
            }
        });

        return selected;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    /**
     * 모든 입력 필드 초기화
     */
    public void clearAll() {
        currentEntryId = -1;
        titleField.setText("");
        contentArea.setText("");
        stressSlider.setValue(50);
        clearEmotions();
    }
}

