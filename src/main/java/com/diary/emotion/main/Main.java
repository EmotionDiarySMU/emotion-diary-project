package com.diary.emotion.main;

import javax.swing.UIManager; // 추가됨
import com.diary.emotion.db.DatabaseManager;
import com.diary.emotion.db.QuestionDBManager;
import com.diary.emotion.login.AuthenticationFrame;

public class Main {
    public static void main(String[] args) {
        // 1. 데이터베이스 초기화
        boolean success = DatabaseManager.createDatabase();
        if (success) QuestionDBManager.initializeQuestions();

        // 2. [핵심 수정] UI 테마를 'CrossPlatform'으로 설정
        // 이 설정이 있어야 버튼의 setBackground(Color.WHITE)가 무시되지 않고 적용됩니다.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. 화면 실행
        new AuthenticationFrame();
    }
}