package share;

import DB.DatabaseManager;
import DB.QuestionDBManager;
import login.AuthenticationFrame;

public class Main {
	public static void main(String[] args) {
		
		boolean success = DatabaseManager.createDatabase();
		if (success) QuestionDBManager.initializeQuestions();
		
		new AuthenticationFrame();
		
		
		
	}
}
