package share;

import javax.swing.UIManager;

import DB.DatabaseManager;
import DB.QuestionDBManager;
import login.AuthenticationFrame;

public class Main {
	public static void main(String[] args) {
		
		// L&F 설정
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // L&F 설정 실패 시 오류 출력
            e.printStackTrace();
        }
		
		boolean success = DatabaseManager.createDatabase();
		if (success) QuestionDBManager.initializeQuestions();
		
		new AuthenticationFrame();
		
	}
}
