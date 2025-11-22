package com.diary.emotion.share;

import com.diary.emotion.db.DatabaseManager;
import com.diary.emotion.login.AuthenticationFrame;

public class Main {
	public static void main(String[] args) {
		
		boolean success = DatabaseManager.createDatabase();
		
		new AuthenticationFrame();
		
		
		
	}
}
