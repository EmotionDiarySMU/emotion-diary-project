package com.diary.emotion.view;

import com.diary.emotion.db.DiaryEntry;
import com.diary.emotion.db.Emotion;
import com.diary.emotion.ui.*;
import com.diary.emotion.write.EmotionSlotPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static com.diary.emotion.ui.UIColors.*;
import static com.diary.emotion.ui.UIFonts.*;

/**
 * 일기 수정 창 (수정 가능, WriteDiaryGUI와 완전 분리)
 */
public class ModifyPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    // UI Components
    public JTextField titleField;
    public JTextArea contentArea;
    public JSlider stressSlider;
    public JTextField stressValueField;
    public JScrollPane contentScrollPane;
    public JLabel[] iconLabels = new JLabel[4];
    public JTextField[] valueFields = new JTextField[4];
    public JPanel southPanel;

    public JButton cancelBtn;
    public JButton fineditBtn;

    private final EmotionSlotPanel[] emotionSlots = new EmotionSlotPanel[4]; // final 추가
    private DiaryEntry currentEntry;
    public boolean isModified = false;

    public JLabel dateLabel; // 날짜 라벨 추가

    private static final Font LABEL_FONT = new Font(BODY_REGULAR.getName(), Font.BOLD, 16);
    private static final Font INPUT_FONT = BODY_REGULAR;

    public ModifyPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_VIEW);

        initializeUI();
        setupButtonPanel();

        // ModifyPanel은 수정 가능해야 하므로 ViewStyleOverrider 적용 안 함
        // 감정 수치 필드가 편집 가능하도록 유지
    }

    // paintComponent에서 스타일 오버라이더 제거 - 생성자에서만 한 번 적용

    private void initializeUI() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(BG_VIEW);
        container.setBorder(new EmptyBorder(20, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 15, 0);

        // 날짜 라벨 초기화 및 추가 (ViewDiaryPanel과 동일한 스타일)
        dateLabel = new JLabel("", SwingConstants.LEFT);
        dateLabel.setFont(DATE_LABEL_FONT);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        container.add(dateLabel, gbc);

        // 제목 영역
        JLabel titleLabel = new JLabel("제목 :", SwingConstants.CENTER);
        titleLabel.setFont(LABEL_FONT);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 15, 15);
        container.add(titleLabel, gbc);

        titleField = new JTextField();
        titleField.setFont(INPUT_FONT);
        titleField.setPreferredSize(new Dimension(100, 30));
        titleField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addChangeListener(titleField);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        container.add(titleField, gbc);

        // 내용 영역
        contentArea = new JTextArea();
        contentArea.setFont(INPUT_FONT);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBackground(Color.WHITE);
        addChangeListener(contentArea);

        contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setPreferredSize(new Dimension(100, 200));
        contentScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        ScrollBarStyler.applyViewStyle(contentScrollPane);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 20, 0);
        container.add(contentScrollPane, gbc);

        // 감정 영역
        JLabel emotionLabel = new JLabel("<html><center>감정<br>수치</center></html>", SwingConstants.CENTER);
        emotionLabel.setFont(LABEL_FONT);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 15, 15);
        container.add(emotionLabel, gbc);

        JPanel emotionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        emotionPanel.setBackground(BG_VIEW);

        for (int i = 0; i < 4; i++) {
            emotionSlots[i] = new EmotionSlotPanel(i, emotionSlots, () -> isModified = true);
            iconLabels[i] = emotionSlots[i].getIconLabel();
            valueFields[i] = emotionSlots[i].getValueField();
            emotionPanel.add(emotionSlots[i]);
        }

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        container.add(emotionPanel, gbc);

        // 스트레스 영역
        JLabel stressLabel = new JLabel("<html><center>스트레스<br>수치</center></html>", SwingConstants.CENTER);
        stressLabel.setFont(LABEL_FONT);
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 15);
        container.add(stressLabel, gbc);

        JPanel stressPanel = new JPanel(new GridBagLayout());
        stressPanel.setBackground(BG_VIEW);
        GridBagConstraints sbc = new GridBagConstraints();

        // 스트레스 수치 입력 필드 초기화
        stressValueField = new JTextField();
        stressValueField.setName("stressValueField"); // 식별용 이름 설정
        stressValueField.setFont(BODY_SMALL); // 12px 폰트 사용
        stressValueField.setPreferredSize(new Dimension(45, 28)); // 크기 확대: 40→50, 24→28
        stressValueField.setHorizontalAlignment(SwingConstants.CENTER);
        stressValueField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(2, 4, 2, 4) // 상하 2px, 좌우 4px 패딩
        ));
        stressValueField.setBackground(Color.WHITE);
        stressValueField.setEditable(true); // 명시적으로 편집 가능 설정
        // 초기값은 fillEntry에서 설정

        // 스트레스 수치 입력 필드를 패널에 추가
        sbc.gridx = 0; sbc.weightx = 0; sbc.insets = new Insets(0, 0, 0, 10);
        stressPanel.add(stressValueField, sbc);

        stressSlider = new JSlider(0, 100, 50);
        stressSlider.setBackground(BG_VIEW);
        stressSlider.setOpaque(true);
        stressSlider.setDoubleBuffered(true);
        stressSlider.setPreferredSize(new Dimension(150, 35));
        stressSlider.setMajorTickSpacing(25);
        stressSlider.setMinorTickSpacing(5);
        stressSlider.setPaintTicks(true);
        stressSlider.setPaintLabels(true);
        stressSlider.setFont(new Font("SansSerif", Font.PLAIN, 8));
        stressSlider.setForeground(Color.BLACK); // 눈금 색상을 검은색으로 설정
        stressSlider.setUI(new CustomSliderUI(stressSlider));

        stressSlider.addChangeListener(e -> {
            int value = stressSlider.getValue();
            stressValueField.setText(String.valueOf(value));
            isModified = true;
            stressSlider.repaint();
        });

        stressValueField.addActionListener(e -> {
            try {
                int val = Integer.parseInt(stressValueField.getText());
                if (val >= 0 && val <= 100) {
                    stressSlider.setValue(val);
                }
            } catch(NumberFormatException ex) {
                // 예외 발생 시 무시
            }
        });

        stressValueField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
            void update() {
                SwingUtilities.invokeLater(() -> {
                    try {
                        String text = stressValueField.getText();
                        if(!text.isEmpty()) {
                            int val = Integer.parseInt(text);
                            if(val >= 0 && val <= 100 && val != stressSlider.getValue()) {
                                stressSlider.setValue(val);
                            }
                        }
                    } catch(Exception ex) {
                        // 예외 발생 시 무시
                    }
                });
                isModified = true;
            }
        });

        sbc.gridx = 1; sbc.weightx = 1.0; sbc.fill = GridBagConstraints.HORIZONTAL; sbc.insets = new Insets(0, 0, 0, 0);
        stressPanel.add(stressSlider, sbc);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);
        container.add(stressPanel, gbc);

        // 하단 버튼 영역 placeholder
        southPanel = new JPanel(new GridBagLayout());
        southPanel.setBackground(BG_VIEW);
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
        container.add(southPanel, gbc);

        add(container, BorderLayout.CENTER);
    }

    private void setupButtonPanel() {
        southPanel.removeAll();
        southPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);

        // 취소 버튼 (왼쪽)
        cancelBtn = ButtonFactory.createCustomButton("취소", Color.WHITE, UIColors.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        southPanel.add(cancelBtn, gbc);

        // 수정완료 버튼 (중앙)
        fineditBtn = ButtonFactory.createCustomButton("수정완료", Color.WHITE, UIColors.TEXT_PRIMARY);
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        southPanel.add(fineditBtn, gbc);

        // 오른쪽 빈 여백 (중앙 정렬을 위한 균형 맞추기)
        JPanel spacer = new JPanel();
        spacer.setBackground(BG_VIEW);
        spacer.setOpaque(false);
        gbc.gridx = 2;
        gbc.weightx = 1.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        southPanel.add(spacer, gbc);
    }

    private void addChangeListener(JTextComponent textComp) {
        textComp.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { isModified = true; }
            public void removeUpdate(DocumentEvent e) { isModified = true; }
            public void changedUpdate(DocumentEvent e) { isModified = true; }
        });
    }

    public void fillEntry(DiaryEntry entry) {
        this.currentEntry = entry;

        titleField.setText(entry.getTitle());
        contentArea.setText(entry.getContent());
        int stressLevel = entry.getStress_level();
        stressSlider.setValue(stressLevel);
        stressValueField.setText(String.valueOf(stressLevel));

        List<Emotion> emotions = entry.getEmotions();
        for (int i = 0; i < 4; i++) {
            if (i < emotions.size()) {
                Emotion e = emotions.get(i);
                emotionSlots[i].setEmotion(e.getEmoji_icon(), e.getEmotion_level());
            } else {
                emotionSlots[i].reset();
            }
        }

        isModified = false;

        // 1행: 날짜 라벨 설정
        if (entry.getEntry_date() != null) {
            String dateStr = entry.getEntry_date().toLocalDateTime().toLocalDate().toString();
            dateLabel.setText("[" + dateStr + "]");
        } else {
            dateLabel.setText("");
        }

        // 감정 영역 스타일 적용 (ModifyPanel용: WHITE)
        EmotionPanelStyler.applyModifyEmotionAreaStyle(this);
        this.revalidate();
        this.repaint();
    }

    public void saveOrFinish() {
        if (fineditBtn != null) {
            fineditBtn.doClick();
        }
    }

    public DiaryEntry getUpdatedEntry() {
        if (currentEntry == null) return null;

        currentEntry.setTitle(titleField.getText().trim());
        currentEntry.setContent(contentArea.getText().trim());
        currentEntry.setStress_level(stressSlider.getValue());

        List<Emotion> emotions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String icon = iconLabels[i].getText();
            if (!icon.equals("+")) {
                try {
                    int level = Integer.parseInt(valueFields[i].getText());
                    emotions.add(new Emotion(icon, level));
                } catch (NumberFormatException e) {
                    // 예외 발생 시 무시
                }
            }
        }
        currentEntry.setEmotions(emotions);

        return currentEntry;
    }

    // =========================================
    // 검증 메서드들 (backend-code에서 가져옴)
    // =========================================

    /**
     * 감정 수치 검증 (0-100 범위)
     * @param slotIndex 슬롯 인덱스
     * @return 검증 성공 여부
     */
    public boolean validateAndSaveEmotionValue(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= 4) {
            return false;
        }

        try {
            String text = valueFields[slotIndex].getText().trim();
            if (text.isEmpty()) {
                return true; // 빈 값은 허용
            }

            int value = Integer.parseInt(text);

            if (value < 0 || value > 100) {
                JOptionPane.showMessageDialog(this,
                    "감정 수치는 0~100 사이여야 합니다.",
                    "입력 오류",
                    JOptionPane.ERROR_MESSAGE);
                valueFields[slotIndex].setText("0");
                return false;
            }

            return true;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "숫자만 입력 가능합니다.",
                "입력 오류",
                JOptionPane.ERROR_MESSAGE);
            valueFields[slotIndex].setText("0");
            return false;
        }
    }

    /**
     * 스트레스 수치 검증 및 동기화 (0-100 범위)
     * @return 검증 성공 여부
     */
    public boolean validateAndSaveStressValue() {
        try {
            String text = stressValueField.getText().trim();
            if (text.isEmpty()) {
                stressValueField.setText("50");
                stressSlider.setValue(50);
                return true;
            }

            int value = Integer.parseInt(text);

            if (value < 0 || value > 100) {
                JOptionPane.showMessageDialog(this,
                    "스트레스 수치는 0~100 사이여야 합니다.",
                    "입력 오류",
                    JOptionPane.ERROR_MESSAGE);
                stressValueField.setText(String.valueOf(stressSlider.getValue()));
                return false;
            }

            // 슬라이더와 동기화
            if (stressSlider.getValue() != value) {
                stressSlider.setValue(value);
            }

            return true;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "숫자만 입력 가능합니다.",
                "입력 오류",
                JOptionPane.ERROR_MESSAGE);
            stressValueField.setText(String.valueOf(stressSlider.getValue()));
            return false;
        }
    }
}
