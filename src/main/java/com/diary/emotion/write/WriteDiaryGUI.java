package com.diary.emotion.write;

import static com.diary.emotion.ui.UIColors.*;
import static com.diary.emotion.ui.UIFonts.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

import com.diary.emotion.db.DatabaseManager;
import com.diary.emotion.db.QuestionDBManager;
import com.diary.emotion.db.DiaryEntry;
import com.diary.emotion.db.Emotion;
import com.diary.emotion.main.MainView;
import com.diary.emotion.ui.CustomSliderUI;
import com.diary.emotion.ui.ButtonFactory;
import com.diary.emotion.ui.ScrollBarStyler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 일기 작성 화면
 * 제목, 내용, 감정, 스트레스 수치를 입력받아 저장
 */

public class WriteDiaryGUI extends JPanel {
    private static final long serialVersionUID = 1L;

    // UI Components (public for external access)
    public JTextField titleField;
    public JTextArea contentArea;
    public JSlider stressSlider;
    public JTextField stressValueField;
    public JLabel questionLabel;
    public JButton saveButton;
    public JButton resetButton;
    public JPanel mainPanel;
    public JPanel southPanel;
    public JPanel resetButtonPanel;
    public JPanel saveButtonPanel;
    public JScrollPane contentScrollPane;
    public JLabel[] iconLabels = new JLabel[4];
    public JTextField[] valueFields = new JTextField[4];

    private EmotionSlotPanel[] emotionSlots = new EmotionSlotPanel[4];

    public boolean isModified = false;

    // 폰트 정의
    private static final Font LABEL_FONT = new Font(BODY_REGULAR.getName(), Font.BOLD, 16);
    private static final Font INPUT_FONT = BODY_REGULAR;

    public WriteDiaryGUI() {
        setLayout(new BorderLayout());
        setBackground(BG_WRITE);

        // 메인 컨테이너
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(BG_WRITE);
        container.setBorder(new EmptyBorder(20, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 15, 0);

        // =========================================
        // 1. 오늘의 질문 (제목 위, 중앙)
        // [요청사항 2] Q. 텍스트 부활 및 중앙 정렬
        // =========================================
        questionLabel = new JLabel("Q. " + QuestionDBManager.getTodayQuestion());
        questionLabel.setFont(new Font(BODY_REGULAR.getName(), Font.BOLD, 16));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2; // 전체 가로 차지
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0); // 제목과 간격
        container.add(questionLabel, gbc);

        // =========================================
        // 2. 제목 영역
        // =========================================
        // 라벨 [요청사항 6] 수직/수평 중앙 정렬
        JLabel titleLabel = new JLabel("제목 :", SwingConstants.CENTER);
        titleLabel.setFont(LABEL_FONT);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER; // 중앙 배치
        gbc.insets = new Insets(0, 0, 15, 15);
        container.add(titleLabel, gbc);

        // 입력 필드
        titleField = new JTextField();
        titleField.setFont(INPUT_FONT);
        titleField.setPreferredSize(new Dimension(100, 30));
        addChangeListener(titleField);
        titleField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        container.add(titleField, gbc);

        // =========================================
        // 3. 내용 영역
        // [요청사항 1] 감정 수치보다 위에 배치
        // [요청사항 3] 왼쪽으로 더 넓어져서 꽉 차게 (gridwidth=2)
        // [요청사항 8] 완전 하얀색 배경
        // =========================================
        contentArea = new JTextArea();
        contentArea.setFont(INPUT_FONT);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBackground(Color.WHITE);
        addChangeListener(contentArea);

        contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setPreferredSize(new Dimension(100, 200));
        contentScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // UI 패키지의 ScrollBarStyler 사용
        ScrollBarStyler.applyWriteStyle(contentScrollPane);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2; // 왼쪽 라벨 영역까지 침범하여 꽉 채움
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // 남은 세로 공간 차지
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 20, 0);
        container.add(contentScrollPane, gbc);

        // =========================================
        // 4. 감정 영역
        // [요청사항 1] 내용 칸 하단으로 이동
        // =========================================
        // 라벨 [요청사항 6] 수직/수평 중앙
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

        // 감정 선택 패널
        JPanel emotionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        emotionPanel.setBackground(BG_WRITE);

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

        // =========================================
        // 5. 스트레스 영역
        // =========================================
        // 라벨 [요청사항 6] 수직/수평 중앙
        JLabel stressLabel = new JLabel("<html><center>스트레스<br>수치</center></html>", SwingConstants.CENTER);
        stressLabel.setFont(LABEL_FONT);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 15);
        container.add(stressLabel, gbc);

        // 스트레스 컨트롤 패널 (수치 입력 + 슬라이더)
        JPanel stressPanel = new JPanel(new GridBagLayout());
        stressPanel.setBackground(BG_WRITE);
        GridBagConstraints sbc = new GridBagConstraints();

        // [요청사항 5] 숫자 칸이 왼쪽으로 이동
        stressValueField = new JTextField("50", 3);
        stressValueField.setHorizontalAlignment(JTextField.CENTER);
        stressValueField.setFont(new Font(BODY_REGULAR.getName(), Font.PLAIN, 12));
        stressValueField.setPreferredSize(new Dimension(40, 24));
        stressValueField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        ((AbstractDocument) stressValueField.getDocument()).setDocumentFilter(new NumericRangeFilter(0, 100));

        sbc.gridx = 0; sbc.weightx = 0; sbc.insets = new Insets(0, 0, 0, 10); // 오른쪽 여백
        stressPanel.add(stressValueField, sbc);

        // [요청사항 9] 슬라이더 크기 축소 (세로폭)
        // [요청사항 13(이전) + 4] 커스텀 슬라이더 (완벽한 원형 커서)
        stressSlider = new JSlider(0, 100, 50);
        stressSlider.setBackground(BG_WRITE);
        stressSlider.setOpaque(true); // ★ 배경을 직접 칠하기 위해 불투명하게 설정 (잔상 방지)
        stressSlider.setDoubleBuffered(true); // ★ 더블 버퍼링 활성화 (잔상 방지)
        stressSlider.setPreferredSize(new Dimension(150, 35)); // 25 -> 35로 증가 (위아래 여유 공간)
        stressSlider.setMajorTickSpacing(25);
        stressSlider.setMinorTickSpacing(5);
        stressSlider.setPaintTicks(true);
        stressSlider.setPaintLabels(true);
        stressSlider.setFont(new Font("SansSerif", Font.PLAIN, 8)); // 10 -> 8로 축소 (작은 숫자)
        stressSlider.setUI(new CustomSliderUI(stressSlider));

        // 이벤트 리스너 연결
        stressSlider.addChangeListener(e -> {
            stressValueField.setText(String.valueOf(stressSlider.getValue()));
            isModified = true;
            // ★ 슬라이더 영역 강제 갱신 (잔상 방지)
            stressSlider.repaint();
        });

        stressValueField.addActionListener(e -> {
            try {
                int val = Integer.parseInt(stressValueField.getText());
                stressSlider.setValue(val);
            } catch(NumberFormatException ex) {}
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
                            if(val != stressSlider.getValue()) {
                                stressSlider.setValue(val);
                            }
                        }
                    } catch(Exception ex) {}
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

        // =========================================
        // 6. 하단 버튼 영역 (UI 패키지 스타일 적용)
        // 구조: [다시쓰기(Left) --- 저장(Center) --- 빈공간(Right)]
        // 오른쪽 빈공간이 있어야 저장 버튼이 정확히 중앙에 옴
        // =========================================

        JPanel bottomButtonPanel = createButtonPanel();

        // 1. 왼쪽: 다시 쓰기 버튼 (ButtonFactory 사용)
        resetButton = createResetButton();

        // 2. 중앙: 저장 버튼 (ButtonFactory 사용)
        saveButton = createSaveButton();

        // 3. GridBagLayout으로 버튼 배치
        GridBagConstraints bbc = new GridBagConstraints();

        bbc.gridx = 0;
        bbc.weightx = 1.0; // 왼쪽 여백 차지
        bbc.anchor = GridBagConstraints.WEST; // 왼쪽 정렬
        bbc.insets = new Insets(0, 0, 0, 0);
        bottomButtonPanel.add(resetButton, bbc);

        bbc.gridx = 1;
        bbc.weightx = 0; // 고정 위치 (중앙)
        bbc.anchor = GridBagConstraints.CENTER;
        bottomButtonPanel.add(saveButton, bbc);

        // 오른쪽: 투명한 빈 공간 (균형 맞추기용)
        JLabel dummySpacer = new JLabel();
        dummySpacer.setPreferredSize(new Dimension(85, 30));

        bbc.gridx = 2;
        bbc.weightx = 1.0; // 오른쪽 여백 차지
        bottomButtonPanel.add(dummySpacer, bbc);

        // 완성된 하단 패널을 메인 컨테이너에 추가
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2; // 전체 가로 차지
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
        container.add(bottomButtonPanel, gbc);

        // southPanel은 ViewDiaryPanel, ModifyPanel 호환성을 위해 유지
        // bottomButtonPanel에서 버튼들을 직접 관리하므로 southPanel은 참조용으로만 유지
        southPanel = bottomButtonPanel;
        resetButtonPanel = new JPanel(); // 호환성 유지용 더미
        saveButtonPanel = new JPanel(); // 호환성 유지용 더미

        mainPanel = container;
        add(mainPanel, BorderLayout.CENTER);
    }

    // =========================================
    // Helper Methods
    // =========================================

    // Getter/Setter methods for encapsulation
    public JTextField getTitleField() {
        return titleField;
    }

    public JTextArea getContentArea() {
        return contentArea;
    }

    public JSlider getStressSlider() {
        return stressSlider;
    }

    public JTextField getStressValueField() {
        return stressValueField;
    }

    public JLabel getQuestionLabel() {
        return questionLabel;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JPanel getSouthPanel() {
        return southPanel;
    }

    public JScrollPane getContentScrollPane() {
        return contentScrollPane;
    }

    public JLabel[] getIconLabels() {
        return iconLabels;
    }

    public JTextField[] getValueFields() {
        return valueFields;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    private void addChangeListener(JTextComponent textComp) {
        textComp.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { isModified = true; }
            public void removeUpdate(DocumentEvent e) { isModified = true; }
            public void changedUpdate(DocumentEvent e) { isModified = true; }
        });
    }

    // =========================================
    // Logic Methods (Save, Clear, Fill)
    // =========================================

    /**
     * 버튼 패널 생성 (UI 패키지 스타일 적용)
     * GridBagLayout을 사용하여 버튼 배치를 위한 베이스 패널 생성
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_WRITE);
        return panel;
    }

    /**
     * 다시 쓰기 버튼 생성 (ButtonFactory 사용)
     * UI 패키지의 스타일을 적용한 버튼 생성
     */
    private JButton createResetButton() {
        JButton button = ButtonFactory.createCustomButton("다시 쓰기", Color.WHITE, Color.BLACK, 85, 30);
        button.addActionListener(e -> {
            // 작성 중인 내용이 있는지 확인
            if (isModified) {
                // 커스텀 옵션 다이얼로그 생성
                Object[] options = {"Yes", "No", "Cancel"};
                int result = JOptionPane.showOptionDialog(
                        this,
                        "작성 중인 일기가 있습니다.\n저장하고 새 일기를 쓰시겠습니까?",
                        "다시 쓰기",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[2]
                );

                if (result == JOptionPane.YES_OPTION) {
                    // Yes: 저장하고 초기화
                    saveEntry();
                } else if (result == JOptionPane.NO_OPTION) {
                    // No: 저장하지 않고 초기화
                    clearAllFields();
                }
                // Cancel: 아무것도 하지 않음
            } else {
                // 작성 중인 내용이 없으면 바로 초기화
                clearAllFields();
            }
        });
        return button;
    }

    /**
     * 저장 버튼 생성 (ButtonFactory 사용)
     * UI 패키지의 스타일을 적용한 버튼 생성
     */
    private JButton createSaveButton() {
        JButton button = ButtonFactory.createCustomButton("저장", Color.WHITE, Color.BLACK, 80, 30);
        button.addActionListener(e -> saveOrFinish());
        return button;
    }

    public void saveOrFinish() {
        // 팝업 없이 바로 저장
        saveEntry();
    }

    private void saveEntry() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();
        int stress = stressSlider.getValue();

        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "제목과 내용을 모두 입력해주세요.");
            return;
        }

        DiaryEntry entry = new DiaryEntry();
        entry.setTitle(title);
        entry.setContent(content);
        entry.setStress_level(stress);

        List<Emotion> emotions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String icon = iconLabels[i].getText();
            if (!icon.equals("+")) {
                try {
                    int level = Integer.parseInt(valueFields[i].getText());
                    emotions.add(new Emotion(icon, level));
                } catch (NumberFormatException e) {
                }
            }
        }
        entry.setEmotions(emotions);

        boolean success = DatabaseManager.insertDiaryEntry(
            entry.getTitle(),
            entry.getContent(),
            entry.getStress_level(),
            emotions.stream().map(Emotion::getEmoji_icon).collect(Collectors.toList()),
            emotions.stream().map(Emotion::getEmotion_level).collect(Collectors.toList())
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "일기가 저장되었습니다!");
            clearAllFields();
            MainView frame = (MainView) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                MainView.searchDiaryPanel.refreshList();
            }
        } else {
            JOptionPane.showMessageDialog(this, "저장 중 오류가 발생했습니다.");
        }
    }

    public void clearAllFields() {
        titleField.setText("");
        contentArea.setText("");
        stressSlider.setValue(50);
        stressValueField.setText("50");

        // 감정 수치 초기화
        for (int i = 0; i < 4; i++) {
            if (emotionSlots[i] != null) {
                emotionSlots[i].reset(); // 이 메서드가 모든 초기화를 담당
            }
        }
        isModified = false;
    }

    public void fillEntry(DiaryEntry entry) {
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
        isModified = false;
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
        if (slotIndex < 0 || slotIndex >= emotionSlots.length) {
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

    /**
     * 저장 여부 확인 후 초기화
     * backend-code의 checkAndClear 메서드 이식
     */
    public void checkAndClear() {
        if (isModified) {
            int result = JOptionPane.showConfirmDialog(
                this,
                "작성 중인 내용이 있습니다. 저장하지 않고 초기화하시겠습니까?",
                "확인",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                clearAllFields();
                isModified = false;
            }
            // NO를 선택하면 아무것도 하지 않음
        } else {
            // 수정된 내용이 없으면 바로 초기화
            clearAllFields();
        }
    }
}
