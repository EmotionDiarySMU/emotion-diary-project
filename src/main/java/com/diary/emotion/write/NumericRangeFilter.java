package com.diary.emotion.write;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/*
 * 텍스트필드에 0-100 숫자만 입력받도록 강제하는 필터
 */
public class NumericRangeFilter extends DocumentFilter {
    
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (isValid(fb.getDocument(), offset, 0, string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (isValid(fb.getDocument(), offset, length, text)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
    
    // 입력된 후의 텍스트(futureText)가 유효한지 검사
    private boolean isValid(Document doc, int offset, int length, String text) throws BadLocationException {
        String currentText = doc.getText(0, doc.getLength());
        StringBuilder futureText = new StringBuilder(currentText);
        futureText.replace(offset, offset + length, text);
        
        // 1. 빈 문자열은 허용 (다 지울 수 있게)
        if (futureText.length() == 0) {
            return true;
        }
        
        // 2. 숫자로만 구성되었는지 확인
        if (!futureText.toString().matches("\\d+")) {
            return false; // 숫자가 아님
        }
        
        // 3. 0-100 범위 확인
        try {
            int value = Integer.parseInt(futureText.toString());
            return (value >= 0 && value <= 100); // 0-100 사이
        } catch (NumberFormatException e) {
            return false; // 너무 큰 숫자 
        }
    }
}
