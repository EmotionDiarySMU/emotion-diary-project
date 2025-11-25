package com.diary.emotion.view;

import com.diary.emotion.db.DiaryEntry;
import com.diary.emotion.db.Emotion;
import com.diary.emotion.ui.ButtonFactory;
import com.diary.emotion.ui.CustomSliderUI;
import com.diary.emotion.ui.ScrollBarStyler;
import com.diary.emotion.ui.UIColors;
import com.diary.emotion.write.EmotionSlotPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import static com.diary.emotion.ui.UIColors.*;
import static com.diary.emotion.ui.UIFonts.*;

/**
 * 일기 조회 창 (수정 불가, WriteDiaryGUI와 완전 분리)
 */
public class ViewDiaryPanel extends JPanel {


    // UI Components
    public JTextField titleField;
    public JTextArea contentArea;
    public JSlider stressSlider;
    public JTextField stressValueField;
    public JScrollPane contentScrollPane;
    public JLabel[] iconLabels = new JLabel[4];
    public JTextField[] valueFields = new JTextField[4];
    public JPanel southPanel;

    public JButton deleteBtn;
    public JButton editBtn;

    private JLabel dateLabel; // 날짜 라벨 추가
    private final EmotionSlotPanel[] emotionSlots = new EmotionSlotPanel[4];

    private static final Font LABEL_FONT = new Font(BODY_REGULAR.getName(), Font.BOLD, 16);
    private static final Font INPUT_FONT = BODY_REGULAR;

    private DiaryEntry currentEntry; // 현재 조회 중인 일기 항목 저장

    public ViewDiaryPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_VIEW);

        dateLabel = new JLabel(); // 초기화 추가
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14)); // 스타일 설정
        dateLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(dateLabel, BorderLayout.NORTH); // 레이아웃에 추가

        initializeUI();
        setupButtonPanel();
    }

    private void initializeUI() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(BG_VIEW);
        container.setBorder(new EmptyBorder(20, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 15, 0);

        // 날짜 라벨 초기화 및 추가
        dateLabel = new JLabel("", SwingConstants.LEFT);
        dateLabel.setFont(new Font(BODY_REGULAR.getName(), Font.BOLD, 16));
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
        titleField.setEditable(false); // 조회 전용으로 편집 불가
        titleField.setBackground(BG_LIGHT_CREAM); // 배경색 설정
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
        contentArea.setBackground(BG_LIGHT_CREAM); // 배경색 설정
        contentArea.setEditable(false); // 조회 전용으로 편집 불가

        contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setPreferredSize(new Dimension(100, 200));
        contentScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        ScrollBarStyler.applyWriteStyle(contentScrollPane);

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
            emotionSlots[i] = new EmotionSlotPanel(i, emotionSlots, () -> {});
            iconLabels[i] = emotionSlots[i].getIconLabel();
            valueFields[i] = emotionSlots[i].getValueField();
            valueFields[i].setEditable(false); // 조회 전용으로 편집 불가
            valueFields[i].setBackground(BG_LIGHT_CREAM); // 배경색 설정
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

        stressValueField = new JTextField("50", 3);
        stressValueField.setHorizontalAlignment(JTextField.CENTER);
        stressValueField.setFont(new Font(BODY_REGULAR.getName(), Font.PLAIN, 12));
        stressValueField.setPreferredSize(new Dimension(40, 24));
        stressValueField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        stressValueField.setEditable(false); // 조회 전용으로 편집 불가
        stressValueField.setBackground(BG_LIGHT_CREAM); // 배경색 설정
        sbc.gridx = 0; sbc.weightx = 0; sbc.insets = new Insets(0, 0, 0, 10);
        stressPanel.add(stressValueField, sbc);

        stressSlider = new JSlider(0, 100, 50);
        stressSlider.setBackground(BG_VIEW);
        stressSlider.setPreferredSize(new Dimension(150, 35));
        stressSlider.setMajorTickSpacing(25);
        stressSlider.setMinorTickSpacing(5);
        stressSlider.setPaintTicks(true);
        stressSlider.setPaintLabels(true);
        stressSlider.setFont(new Font("SansSerif", Font.PLAIN, 8));
        stressSlider.setUI(new CustomSliderUI(stressSlider));
        stressSlider.setEnabled(false); // 조회 전용으로 비활성화
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

        deleteBtn = ButtonFactory.createCustomButton("삭제", Color.WHITE, UIColors.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        southPanel.add(deleteBtn, gbc);

        editBtn = ButtonFactory.createCustomButton("수정하기", Color.WHITE, UIColors.TEXT_PRIMARY);
        editBtn.addActionListener(e -> {
            if (currentEntry != null) {
                ModifyPanel modifyPanel = new ModifyPanel();
                modifyPanel.fillEntry(currentEntry);
                JFrame frame = new JFrame("일기 수정");
                frame.add(modifyPanel);
                frame.setSize(495, 630);
                frame.setVisible(true);
            }
        });
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        southPanel.add(editBtn, gbc);

        JPanel dummy = new JPanel();
        dummy.setOpaque(false);
        dummy.setPreferredSize(new Dimension(80, 30));
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        southPanel.add(dummy, gbc);
    }

    public void fillEntry(DiaryEntry entry) {
        this.currentEntry = entry; // 현재 일기 항목 저장

        titleField.setText(entry.getTitle());
        contentArea.setText(entry.getContent());
        stressSlider.setValue(entry.getStress_level());
        stressValueField.setText(String.valueOf(entry.getStress_level()));

        List<Emotion> emotions = entry.getEmotions();
        for (int i = 0; i < 4; i++) {
            if (i < emotions.size()) {
                Emotion e = emotions.get(i);
                emotionSlots[i].setEmotion(e.getEmoji_icon(), e.getEmotion_level());
            } else {
                emotionSlots[i].reset();
            }
        }

        // 날짜 라벨 설정
        if (entry.getEntry_date() != null) {
            String dateStr = entry.getEntry_date().toLocalDateTime().toLocalDate().toString();
            dateLabel.setText("[" + dateStr + "]");
        } else {
            dateLabel.setText("");
        }
    }
}
