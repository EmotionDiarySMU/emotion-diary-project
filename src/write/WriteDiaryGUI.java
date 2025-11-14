package write;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    JTextField titleField;
    JTextArea contentArea;
    JScrollPane contentScrollPane;
    
    JLabel[] iconLabels = new JLabel[4];
    JTextField[] valueFields = new JTextField[4]; 
    SingleIconChooserDialog iconDialog; // 아이콘 선택 팝업창

    JSlider stressSlider;
    JTextField stressValueField;

    public JButton saveButton;

    int[] emotionValues = new int[4];
    public boolean isModified = false;
    
    Color lightGreen = new Color(240, 255, 240);
    Color lightYellow = new Color(255, 255, 224);


    public WriteDiaryGUI() {
        setLayout(new BorderLayout());
        
        Arrays.fill(emotionValues, 0);;

        // --- 메인 컨텐츠 패널 (GridBagLayout 사용) ---
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(lightGreen); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- GBC row 0: 오늘의 질문 ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // 2열 병합
        JLabel questionLabel = new JLabel("오늘의 질문: ");
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
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
        titleField.getDocument().addDocumentListener(new SimpleModifyListener()); //♦️
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
//      gbc.weightx = 1.0; 왜 이걸 쓰면 오히려 문제가 생기는거지..? ♦️
        gbc.weighty = 1.0; // 세로로 꽉 차도록
        gbc.fill = GridBagConstraints.BOTH; 
        contentArea = new JTextArea(); 
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentScrollPane = new JScrollPane(contentArea);
        contentArea.getDocument().addDocumentListener(new SimpleModifyListener()); // ♦️
        ((AbstractDocument) contentArea.getDocument()).setDocumentFilter(new LengthFilter(30000)); // ♦️ 
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
        iconDisplayPanel.setBackground(lightGreen); 
        
        // 아이콘 선택 팝업창 초기화
        iconDialog = new SingleIconChooserDialog(this, iconLabels, lightYellow); // ♦️
        
        // 0-100 숫자만 입력받는 필터 생성
        NumericRangeFilter filter = new NumericRangeFilter(); // ♦️
        
        // 4개의 감정 슬롯(아이콘+텍스트필드) 생성
        for (int i = 0; i < 4; i++) {
            JPanel slotPanel = new JPanel(new BorderLayout());
            slotPanel.setBorder(BorderFactory.createEtchedBorder());
            slotPanel.setBackground(Color.WHITE);

            iconLabels[i] = new JLabel("[ ]", SwingConstants.CENTER);
            iconLabels[i].setFont(new Font("SansSerif", Font.PLAIN, 24));
            
            valueFields[i] = new JTextField(String.valueOf(emotionValues[i]), 3);
            valueFields[i].setHorizontalAlignment(JTextField.CENTER);
            ((AbstractDocument) valueFields[i].getDocument()).setDocumentFilter(filter); // ♦️
            valueFields[i].getDocument().addDocumentListener(new SimpleModifyListener()); // ♦️
            
            slotPanel.add(iconLabels[i], BorderLayout.CENTER);
            slotPanel.add(valueFields[i], BorderLayout.SOUTH);
            
            iconDisplayPanel.add(slotPanel);
            
            final int slotIndex = i;
            
            iconLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    iconDialog.setCurrentSlot(slotIndex, iconLabels[slotIndex].getText()); // ♦️
                    iconDialog.setVisible(true);
                    
                    String selectedIcon = iconDialog.getSelectedIcon();
                    if (selectedIcon != null) {
                        if (!iconLabels[slotIndex].getText().equals(selectedIcon)) {
                            iconLabels[slotIndex].setText(selectedIcon);
                            isModified = true; // ♦️
                        }
                    }
                }
            });
            
            valueFields[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    validateAndSaveEmotionValue(slotIndex); // ♦️
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
        stressPanel.setBackground(lightGreen);
        
        stressSlider = new JSlider(0, 100, 50);
        
        stressValueField = new JTextField("50", 3);
        ((AbstractDocument) stressValueField.getDocument()).setDocumentFilter(filter); // ♦️
        stressValueField.getDocument().addDocumentListener(new SimpleModifyListener()); // ♦️
        
        stressPanel.add(stressSlider, BorderLayout.CENTER);
        stressPanel.add(stressValueField, BorderLayout.EAST);

        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(stressPanel, gbc);

        // --- 하단 저장 버튼 ---
        saveButton = new JButton("저장하기");
        
        JPanel southPanel = new JPanel();
        southPanel.setBackground(lightGreen);
        southPanel.add(saveButton);
        
        // --- 프레임에 최종 조립 ---
        add(mainPanel, BorderLayout.CENTER); 
        add(southPanel, BorderLayout.SOUTH);

        // --- 이벤트 리스너 연결 ---

        stressSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (stressSlider.getValueIsAdjusting()) {
                    isModified = true;
                }
                stressValueField.setText(String.valueOf(stressSlider.getValue()));
            }
        });
        
        stressValueField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateAndSaveStressValue(); // ♦️
            }
        });

        // 저장 버튼 액션
        
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
                        validateAndSaveEmotionValue(i); // ♦️
                        emotionValuesList.add(emotionValues[i]);
                    }
                }
                
                validateAndSaveStressValue(); // ♦️
                int stressLevel = stressSlider.getValue();

                // ⭐️ --- 2. DB에 저장 ---
                try {
                    // DatabaseUtil의 새 메소드 호출!
                	boolean success = DatabaseManager.insertDiaryEntry( // ♦️
                            title, content, stressLevel, emotions, emotionValuesList
                        );

                    if (success) {
                        // 성공 시
                        JOptionPane.showMessageDialog(WriteDiaryGUI.this, "일기가 성공적으로 저장되었습니다.");
                        
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
        
    }
    
    
    // 감정 수치 텍스트필드 값 검증/저장 (0-100)
    void validateAndSaveEmotionValue(int slotIndex) {
        try {
            String text = valueFields[slotIndex].getText();
            int value = 0;
            if (text != null && !text.isEmpty()) {
                value = Integer.parseInt(text);
            }
            if (value < 0) value = 0;
            if (value > 100) value = 100;
            
            emotionValues[slotIndex] = value;
            valueFields[slotIndex].setText(String.valueOf(value));
        } catch (NumberFormatException nfe) {
            valueFields[slotIndex].setText(String.valueOf(emotionValues[slotIndex]));
        }
    }
    
    // 스트레스 수치 텍스트필드 값 검증/저장 및 슬라이더 동기화
    void validateAndSaveStressValue() {
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
}