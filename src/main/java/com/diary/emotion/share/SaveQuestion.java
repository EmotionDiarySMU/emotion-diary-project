package com.diary.emotion.share;

import javax.swing.*;

public class SaveQuestion {

    public static boolean handleWindowClosing(JFrame frame, Object panel, boolean exitProgram) {
        int result = JOptionPane.showConfirmDialog(
            frame,
            "저장되지 않은 변경 사항이 있습니다. 저장하시겠습니까?",
            "경고",
            JOptionPane.YES_NO_CANCEL_OPTION
        );

        switch (result) {
            case JOptionPane.YES_OPTION:
                break;
            case JOptionPane.NO_OPTION:
                break;
            case JOptionPane.CANCEL_OPTION:
                return false;
        }

        if (exitProgram) System.exit(0);
        else frame.dispose();
        return true;
    }
}