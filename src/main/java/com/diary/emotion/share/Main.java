package share;

public class Main {
	public static void main(String[] args) {
		boolean success = DatabaseUtil.createDatabase();
		
		new MainView();
		
	}
}
