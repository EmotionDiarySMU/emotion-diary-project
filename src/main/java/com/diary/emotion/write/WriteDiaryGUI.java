package com.diary.emotion.write;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

import com.diary.emotion.DB.DatabaseManager;
import com.diary.emotion.DB.DiaryEntry;
import com.diary.emotion.DB.QuestionDBManager;
import com.diary.emotion.share.MainView;
import com.diary.emotion.view.SearchDiaryPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * 일기 작성 메인 GUI 클래스
 */
public class WriteDiaryGUI extends JPanel {

    private static final long serialVersionUID = 1L;

    public JPanel mainPanel;
    public JPanel southPanel;

    public GridBagConstraints gbc;

    public JLabel questionLabel;
    public JTextField titleField;
    public JTextArea contentArea;
    public JScrollPane contentScrollPane;

    public JPanel[] slotPanels = new JPanel[4];
    public JLabel[] iconLabels = new JLabel[4];
    public JTextField[] valueFields = new JTextField[4];
    SingleIconChooserDialog iconDialog; // 아이콘 선택 팝업창

    public JSlider stressSlider;
    public JTextField stressValueField;
    

    public JButton newPostButton;
    public JButton saveButton;

    public int[] emotionValues = new int[4];
    public boolean isModified = false;

    protected Color getBackgroundColor() {
        return new Color(240, 255, 240); // lightGreen
    }

    Color lightYellow = new Color(255, 255, 224);
    Color backgroundColor = getBackgroundColor();


    public WriteDiaryGUI() {
        setLayout(new BorderLayout());

        Arrays.fill(emotionValues, 0);;

        // --- 메인 컨텐츠 패널 (GridBagLayout 사용) ---
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(backgroundColor);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- GBC row 0: 오늘의 질문 ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        // 질문
        String randomQuestion;
        try {
            randomQuestion = QuestionDBManager.getTodaysQuestion();
        } catch (Exception e) {
            randomQuestion = "오늘의 질문을 불러오지 못했습니다.";
        }

        questionLabel = new JLabel("Q. " + randomQuestion);
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        questionLabel.setForeground(new Color(50, 50, 50));
        questionLabel.setHorizontalAlignment(JLabel.CENTER);

        mainPanel.add(questionLabel, gbc);

        // --- GBC row 1: 제목 ---
        gbc.gridwidth = 1; // 1열로 복구
//      gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel titleLabel = new JLabel("제목:");
        mainPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0; // 가로로 꽉 차도록
        titleField = new JTextField();
        titleField.getDocument().addDocumentListener(new SimpleModifyListener());
        mainPanel.add(titleField, gbc);

        // --- GBC row 2: 내용 ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0; // weightx 리셋
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel contentLabel = new JLabel("내용:");
        contentLabel.setOpaque(false);
        mainPanel.add(contentLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weighty = 1.0; // 세로로 꽉 차도록
        gbc.fill = GridBagConstraints.BOTH;
        contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentScrollPane = new JScrollPane(contentArea);
        contentArea.getDocument().addDocumentListener(new SimpleModifyListener());
        ((AbstractDocument) contentArea.getDocument()).setDocumentFilter(new LengthFilter(30000));
        mainPanel.add(contentScrollPane, gbc);

        // --- GBC row 3: 감정 (아이콘 + 수치 4칸) ---
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.0; // weighty 리셋
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel emotionLabel = new JLabel("감정:");
        mainPanel.add(emotionLabel, gbc);

        // 4개의 감정 슬롯을 담을 패널
        JPanel iconDisplayPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        iconDisplayPanel.setBackground(backgroundColor);

        // 아이콘 선택 팝업창 초기화
        iconDialog = new SingleIconChooserDialog(this, iconLabels, lightYellow);

        // 숫자만 입력받는 필터 생성
        NumericRangeFilter filter = new NumericRangeFilter();

        // 4개의 감정 슬롯(아이콘+텍스트필드) 생성
        for (int i = 0; i < 4; i++) {
        	slotPanels[i] = new JPanel(new BorderLayout());
        	slotPanels[i].setBorder(BorderFactory.createEtchedBorder());
        	slotPanels[i].setBackground(Color.WHITE);

            iconLabels[i] = new JLabel("[ ]", SwingConstants.CENTER);
            iconLabels[i].setFont(new Font("SansSerif", Font.PLAIN, 24));

            valueFields[i] = new JTextField(String.valueOf(emotionValues[i]), 3);
            valueFields[i].setHorizontalAlignment(JTextField.CENTER);
            ((AbstractDocument) valueFields[i].getDocument()).setDocumentFilter(filter);
            valueFields[i].getDocument().addDocumentListener(new SimpleModifyListener());

            slotPanels[i].add(iconLabels[i], BorderLayout.CENTER);
            slotPanels[i].add(valueFields[i], BorderLayout.SOUTH);

            iconDisplayPanel.add(slotPanels[i]);

            final int slotIndex = i;

            // 클릭 이벤트: 같은 아이콘 선택 시 삭제(토글), 다르면 변경
            iconLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 1. 현재 설정되어 있는 아이콘을 저장해둡니다.
                    String currentIcon = iconLabels[slotIndex].getText();

                    // 2. 팝업창을 띄웁니다.
                    iconDialog.setCurrentSlot(slotIndex, currentIcon);
                    iconDialog.setVisible(true);

                    // 3. 팝업창에서 선택해온 아이콘을 가져옵니다.
                    String selectedIcon = iconDialog.getSelectedIcon();

                    if (selectedIcon != null) {

                        // 만약 방금 선택한 아이콘이 원래 있던 아이콘과 "똑같다면" -> 삭제 (토글 OFF)
                        if (currentIcon.equals(selectedIcon)) {
                            iconLabels[slotIndex].setText("[ ]"); // 빈칸으로 되돌림
                            valueFields[slotIndex].setText("0");  // 수정 0으로 돌림
                            emotionValues[slotIndex] = 0;
                            isModified = true;
                        }
                        // 다른 아이콘을 선택했다면 -> 변경 (Update)
                        else {
                            iconLabels[slotIndex].setText(selectedIcon);


                            valueFields[slotIndex].setText("1"); // 1점으로 자동 설정
                            emotionValues[slotIndex] = 1;

                            isModified = true;
                        }

                    }
                }
            });

            valueFields[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    validateAndSaveEmotionValue(slotIndex);
                }
            });
        }

        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(iconDisplayPanel, gbc);

        // --- GBC row 4: 스트레스 ---
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel stressLabel = new JLabel("스트레스 수치:");
        mainPanel.add(stressLabel, gbc);

        JPanel stressPanel = new JPanel(new BorderLayout(5, 0));
        stressPanel.setBackground(backgroundColor);

        stressSlider = new JSlider(0, 100, 50);
        stressSlider.setOpaque(false); //수정1125

        stressValueField = new JTextField("50", 3);
        ((AbstractDocument) stressValueField.getDocument()).setDocumentFilter(filter);
        stressValueField.getDocument().addDocumentListener(new SimpleModifyListener());

        stressPanel.add(stressSlider, BorderLayout.CENTER);
        stressPanel.add(stressValueField, BorderLayout.EAST);

        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(stressPanel, gbc);

        // --- 하단 다시쓰기, 저장 버튼 ---
        newPostButton = new JButton("다시 쓰기");
        saveButton = new JButton("저장하기");

        southPanel = new JPanel();
        southPanel.setBackground(backgroundColor);
        southPanel.add(newPostButton);
        southPanel.add(saveButton);

        // --- 프레임에 최종 조립 ---
        add(mainPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        // --- 이벤트 리스너 연결 ---

        // 스트레스 슬라이더
        stressSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (stressSlider.getValueIsAdjusting()) {
                    isModified = true;
                }
                stressValueField.setText(String.valueOf(stressSlider.getValue()));
            }
        });

        // 스트레스 필드
        stressValueField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateAndSaveStressValue();
            }
        });

        // 다시쓰기 버튼
        newPostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAndClear();
            }
        });

        // 저장 버튼
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. UI에서 모든 데이터 수집
                String title = titleField.getText();
                String content = contentArea.getText();

                List<String> emotions = new ArrayList<>();
                List<Integer> emotionValuesList = new ArrayList<>();

                for (int i = 0; i < 4; i++) {
                    String icon = iconLabels[i].getText();
                    if (!icon.equals("[ ]") && !icon.equals(" ")) {
                        emotions.add(icon);
                        validateAndSaveEmotionValue(i);
                        emotionValuesList.add(emotionValues[i]);
                    }
                }

                validateAndSaveStressValue();
                int stressLevel = stressSlider.getValue();

                // ⭐️ --- 2. DB에 저장 ---
                try {
                    // DatabaseUtil의 새 메소드 호출!
                    boolean success = DatabaseManager.insertDiaryEntry(
                            title, content, stressLevel, emotions, emotionValuesList
                    );

                    if (success) {
                        // 성공 시
                        JOptionPane.showMessageDialog(WriteDiaryGUI.this, "일기가 성공적으로 저장되었습니다.");

                        clearAllFields();

                        MainView.getInstance().viewPanel.refreshDiaryModel(true);

                        // 3. 저장 후 '수정됨' 플래그 리셋
                        isModified = false;

                    } else {
                        // DB 저장 실패 시 (e.g. 트랜잭션 롤백)
                        JOptionPane.showMessageDialog(WriteDiaryGUI.this,
                                "일기 저장에 실패했습니다. (DB 오류)",
                                "저장 실패",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    // DB 연결 자체에 실패하는 등 심각한 오류 발생 시
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(WriteDiaryGUI.this,
                            "DB 연결 중 심각한 오류가 발생했습니다.\n" + ex.getMessage(),
                            "DB 오류",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 제목 필드에 포커싱
        titleField.addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                // 창이 실제로 표시될 때 (HierarchyListener.SHOWING_CHANGED)
                if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                    // 이 리스너는 한 번만 실행되도록 제거
                    titleField.removeHierarchyListener(this);

                    // 포커스 요청
                    titleField.requestFocusInWindow();
                }
            }
        });
        
    }


    // 감정 수치 텍스트필드 값 검증/저장 (1-100)
    public void validateAndSaveEmotionValue(int slotIndex) {
        try {
            String text = valueFields[slotIndex].getText();
            int value = 0;
            if (text != null && !text.isEmpty()) {
                value = Integer.parseInt(text);
            }
            if (value < 1) value = 1;
            if (value > 100) value = 100;

            emotionValues[slotIndex] = value;
            valueFields[slotIndex].setText(String.valueOf(value));
        } catch (NumberFormatException nfe) {
            valueFields[slotIndex].setText(String.valueOf(emotionValues[slotIndex]));
        }
    }

    // 스트레스 수치 텍스트필드 값 검증/저장 및 슬라이더 동기화
    public void validateAndSaveStressValue() {
        try {
            String text = stressValueField.getText();
            int value = 0;
            if (text != null && !text.isEmpty()) {
                value = Integer.parseInt(text);
            }
            if (value < 0) value = 0;
            if (value > 100) value = 100;

            stressSlider.setValue(value);
            stressValueField.setText(String.valueOf(value));
        } catch (NumberFormatException nfe) {
            stressValueField.setText(String.valueOf(stressSlider.getValue()));
        }
    }

    // '수정됨' 플래그(isModified)를 true로 설정하는 간단한 리스너
    class SimpleModifyListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) { isModified = true; }
        @Override
        public void removeUpdate(DocumentEvent e) { isModified = true; }
        @Override
        public void changedUpdate(DocumentEvent e) { /* (무시) */ }
    }

    // [추가] 현 페이지에 작성한 내용을 다 지우고 다시 쓰는 기능
    public void checkAndClear() {
        if (isModified) {
            int result = JOptionPane.showConfirmDialog(this,
                    "작성 중인 일기가 있습니다.\n저장하고 새 일기를 쓰시겠습니까?",
                    "새 일기 작성",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                saveButton.doClick();
                if (!isModified) {
                    clearAllFields();
                }
            } else if (result == JOptionPane.NO_OPTION) {
                clearAllFields();
            }
        } else {
            clearAllFields();
        }
    }

    public void clearAllFields() {
        titleField.setText("");
        contentArea.setText("");

        stressSlider.setValue(50);
        stressValueField.setText("50");

        for (int i = 0; i < 4; i++) {
            iconLabels[i].setText("[ ]");
            valueFields[i].setText("0");
            emotionValues[i] = 0;
        }
        isModified = false;
    }

    // 자식 클래스에서 쓸 메서드
    public void fillEntry(DiaryEntry entry) {
        // 제목/내용/시간/스트레스
        titleField.setText(entry.getTitle());
        contentArea.setText(entry.getContent());
        stressSlider.setValue(entry.getStress_level());

        // 감정(최대 4개) 채우기: 안전하게 범위 검사
        List<com.diary.emotion.DB.Emotion> emotions = entry.getEmotions();
        for (int i = 0; i < valueFields.length; i++) {
            if (i < emotions.size()) {
                iconLabels[i].setText(emotions.get(i).getEmoji_icon());
                valueFields[i].setText(String.valueOf(emotions.get(i).getEmotion_level()));
            } else {
                iconLabels[i].setText("[ ]");
                valueFields[i].setText("0");
            }
        }
        isModified = false;
    }
    // SaveQuestion 클래스에서 쓸 것
    public void saveOrFinish() {
        saveButton.doClick();
    }

}