package share;

import javax.swing.*;

import write.WriteDiaryGUI;

public class SaveQuestion {
	public static void handleWindowClosing(JFrame frame, WriteDiaryGUI panel, boolean exitProgram) {
	    if (panel.isModified) {
	        int result = JOptionPane.showConfirmDialog(frame, 
	            "저장되지 않은 변경 사항이 있습니다. 저장하시겠습니까?", 
	            "경고", 
	            JOptionPane.YES_NO_CANCEL_OPTION);

	        if (result == JOptionPane.YES_OPTION) {
	            panel.saveButton.doClick();
	            if (panel.isModified) return;
	        }

	        if (result == JOptionPane.NO_OPTION) {
	            if (exitProgram) System.exit(0);
	            else frame.dispose();
	        }
	    } else {
	        if (exitProgram) System.exit(0);
	        else frame.dispose();
	    }
	}
}
