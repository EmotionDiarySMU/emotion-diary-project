package share;

import DB.DatabaseManager;
import login.AuthenticationFrame;

public class Main {
	public static void main(String[] args) {
		
		boolean success = DatabaseManager.createDatabase();
		
		new AuthenticationFrame();
		
		
		
	}
}
