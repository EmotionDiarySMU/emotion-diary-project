import java.awt.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/*
 * JTextArea에 최대 글자 수를 제한하는 필터
 */
public class LengthFilter extends DocumentFilter {
    private int maxLength;

    public LengthFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    // 글자 삽입 시
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        // (현재 글자 수 + 새 글자 수)가 최대치 이하일 때만 허용
        if ((fb.getDocument().getLength() + string.length()) <= maxLength) {
            super.insertString(fb, offset, string, attr);
        } else {
            Toolkit.getDefaultToolkit().beep(); // 넘으면 삑- 소리
        }
    }

    // 글자 교체 시
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        // (현재 글자 수 - 지워질 글자 수 + 새 글자 수)
        int currentLength = fb.getDocument().getLength();
        int newLength = currentLength - length + text.length();

        if (newLength <= maxLength) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            Toolkit.getDefaultToolkit().beep(); // 넘으면 삑- 소리
        }
    }
}