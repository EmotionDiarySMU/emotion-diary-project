package com.diary.emotion;

public class Main {
	public static void main(String[] args) {
		boolean success = DatabaseManager.createDatabase();
		if (success) QuestionDBManager.initializeQuestions();
		
		new AuthenticationFrame();
	}
}