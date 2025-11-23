package share;

import javax.swing.*;
import write.WriteDiaryGUI;

public class SaveQuestion {
    public static Integer handleWindowClosing(JFrame frame, WriteDiaryGUI panel, int exitProgram) {
        
        // 1. 일기가 수정되지 않았을 경우, 즉시 종료 또는 창 닫기
        if (!panel.isModified) {
            if (exitProgram == 1) System.exit(0);
            else if (exitProgram == 2) frame.dispose();
            
            return 0;
        }

        // 2. 수정되었을 경우, 사용자에게 저장 여부 확인
        int result = JOptionPane.showConfirmDialog(
            frame, 
            "저장되지 않은 변경 사항이 있습니다. 저장하시겠습니까?", 
            "경고", 
            JOptionPane.YES_NO_CANCEL_OPTION
        );

        // 3. 사용자의 선택에 따른 분기 처리
        switch (result) {
            case JOptionPane.YES_OPTION:
                panel.saveOrFinish(); // 저장 후 다음 단계 실행
                break;
                
            case JOptionPane.NO_OPTION:
                break;
                
            case JOptionPane.CANCEL_OPTION:
                return 1;
        }

        // 4. 공통 종료/닫기 로직
        if (exitProgram == 1) System.exit(0);
        else if (exitProgram == 2) frame.dispose();
        
        return 0;
    }
}