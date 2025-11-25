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

    public EmotionSlotPanel(int index, EmotionSlotPanel[] allSlots, Runnable onChangeCallback) {
        this.slotIndex = index;
        this.allSlots = allSlots;
        this.onChangeCallback = onChangeCallback;

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
        iconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openIconChooser();
            }
        });

        // 수치 입력 필드
        valueField = new JTextField("1", 3);
        valueField.setHorizontalAlignment(JTextField.CENTER);
        ((AbstractDocument) valueField.getDocument()).setDocumentFilter(new NumericRangeFilter(1, 100));
        valueField.setPreferredSize(new Dimension(40, 24));
        valueField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        valueField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { notifyChange(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { notifyChange(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { notifyChange(); }
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
            iconLabel.setText(selected);
            iconLabel.setFont(UIFonts.EMOJI);
            iconLabel.setBackground(new Color(255, 250, 240));
            notifyChange();
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
        valueField.setText("1"); // "0" -> "1"로 변경 (필터가 1~100 범위이므로)
    }

    public void setEmotion(String icon, int level) {
        iconLabel.setText(icon);
        iconLabel.setFont(UIFonts.EMOJI);
        iconLabel.setBackground(new Color(255, 250, 240));
        valueField.setText(String.valueOf(level));
    }
}

