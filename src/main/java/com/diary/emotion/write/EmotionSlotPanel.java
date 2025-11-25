package com.diary.emotion.write;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.diary.emotion.ui.UIFonts;
import static com.diary.emotion.ui.UIColors.BG_WRITE;

/**
 * 감정 입력 슬롯 패널
 * 아이콘과 수치 입력 필드를 포함
 */
public class EmotionSlotPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel iconLabel;
    private JTextField valueField;
    private int slotIndex;
    private EmotionSlotPanel[] allSlots;
    private Runnable onChangeCallback;
    private boolean isEditable; // 편집 가능 여부

    public EmotionSlotPanel(int index, EmotionSlotPanel[] allSlots, Runnable onChangeCallback) {
        this(index, allSlots, onChangeCallback, true); // 기본값은 편집 가능
    }

    public EmotionSlotPanel(int index, EmotionSlotPanel[] allSlots, Runnable onChangeCallback, boolean isEditable) {
        this.slotIndex = index;
        this.allSlots = allSlots;
        this.onChangeCallback = onChangeCallback;
        this.isEditable = isEditable;

        setLayout(new BorderLayout());
        setBackground(BG_WRITE);
        initComponents();
    }

    private void initComponents() {
        // 아이콘 라벨
        iconLabel = new JLabel("+", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        iconLabel.setPreferredSize(new Dimension(60, 60));
        iconLabel.setOpaque(true);
        iconLabel.setBackground(Color.WHITE);
        iconLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // 편집 가능 모드일 때만 클릭 이벤트 및 커서 설정
        if (isEditable) {
            iconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openIconChooser();
                }
            });
        }

        // 수치 입력 필드
        valueField = new JTextField("0", 3);
        valueField.setHorizontalAlignment(JTextField.CENTER);
        // 요구사항 3: 감정 선택 전 0, 선택 후 1~100만 허용
        ((AbstractDocument) valueField.getDocument()).setDocumentFilter(new NumericRangeFilter(0, 100));
        valueField.setPreferredSize(new Dimension(40, 24));
        valueField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        valueField.setEditable(false); // 초기에는 비활성화 (감정 미선택 상태)
        valueField.setForeground(Color.GRAY); // 초기 상태는 회색 텍스트

        valueField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                validateEmotionValue();
                notifyChange();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                validateEmotionValue();
                notifyChange();
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                validateEmotionValue();
                notifyChange();
            }

            private void validateEmotionValue() {
                SwingUtilities.invokeLater(() -> {
                    String text = valueField.getText();
                    if (text.isEmpty()) return;

                    try {
                        int value = Integer.parseInt(text);
                        // 요구사항 3: 감정이 선택되지 않았으면 0만 허용, 선택되었으면 1~100
                        boolean hasEmotion = !iconLabel.getText().equals("+");
                        if (hasEmotion && value == 0) {
                            valueField.setText("1");
                        }
                    } catch (NumberFormatException ex) {
                        // 무시
                    }
                });
            }
        });

        add(iconLabel, BorderLayout.CENTER);

        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.setBackground(BG_WRITE);
        bottomWrapper.setBorder(new EmptyBorder(5, 12, 0, 12));
        bottomWrapper.add(valueField, BorderLayout.CENTER);

        add(bottomWrapper, BorderLayout.SOUTH);
    }

    private void openIconChooser() {
        String currentText = iconLabel.getText();
        String currentSelection = currentText.equals("+") ? null : currentText;
        List<String> usedIcons = getAllSelectedIcons();

        SingleIconChooserDialog dialog = new SingleIconChooserDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                currentSelection,
                usedIcons
        );
        dialog.setVisible(true);

        String selected = dialog.getSelectedIcon();
        if (selected != null) {
            // 요구사항 2: 같은 감정을 다시 클릭하면 취소 (토글)
            if (selected.equals(currentSelection)) {
                // 선택 취소
                reset();
                notifyChange();
            } else {
                // 새로운 감정 선택
                iconLabel.setText(selected);
                iconLabel.setFont(UIFonts.EMOJI);
                // 요구사항 3: 감정 선택 시 수치를 1로 설정
                valueField.setText("1");
                valueField.setForeground(Color.BLACK); // 텍스트 색상을 검정색으로 변경
                // 감정이 선택되면 수치 입력 필드 활성화
                if (isEditable) {
                    valueField.setEditable(true);
                }
                notifyChange();
            }
        }
    }

    private List<String> getAllSelectedIcons() {
        List<String> used = new ArrayList<>();
        if (allSlots != null) {
            for (int i = 0; i < allSlots.length; i++) {
                if (i == slotIndex || allSlots[i] == null) continue;
                String text = allSlots[i].getIconLabel().getText();
                if (!text.equals("+")) {
                    used.add(text);
                }
            }
        }
        return used;
    }

    private void notifyChange() {
        if (onChangeCallback != null) {
            onChangeCallback.run();
        }
    }

    public JLabel getIconLabel() {
        return iconLabel;
    }

    public JTextField getValueField() {
        return valueField;
    }

    public void reset() {
        iconLabel.setText("+");
        iconLabel.setFont(new Font("Arial", Font.BOLD, 20));
        iconLabel.setBackground(Color.WHITE);
        // 요구사항 3: 초기값은 0
        valueField.setText("0");
        valueField.setForeground(Color.GRAY); // 회색 텍스트
        // 감정이 선택되지 않은 상태이므로 수치 입력 필드 비활성화
        valueField.setEditable(false);
    }

    public void setEmotion(String icon, int level) {
        iconLabel.setText(icon);
        iconLabel.setFont(UIFonts.EMOJI);
        iconLabel.setBackground(new Color(255, 250, 240));
        valueField.setText(String.valueOf(level));
        valueField.setForeground(Color.BLACK); // 텍스트 색상을 검정색으로 설정
        // 감정이 설정되면 수치 입력 필드 활성화 (편집 가능한 경우에만)
        if (isEditable) {
            valueField.setEditable(true);
        }
    }
}

