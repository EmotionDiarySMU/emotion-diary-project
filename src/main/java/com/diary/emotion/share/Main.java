package com.diary.emotion.share;

import com.diary.emotion.stats.StatisticsController;
import com.diary.emotion.stats.StatisticsDAO;
import com.diary.emotion.stats.StatisticsView;
import com.diary.emotion.db.DatabaseManager;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {
		boolean success = DatabaseManager.createDatabase();

		if (!success) {
			System.err.println("데이터베이스 초기화에 실패했습니다.");
			System.err.println("");
			System.err.println("해결 방법:");
			System.err.println("   1. MySQL 서버가 실행 중인지 확인하세요");
			System.err.println("      터미널에서: mysql -u root -p");
			System.err.println("");
			System.err.println("   2. DatabaseManager.java의 DB_PW가 올바른지 확인하세요");
			System.err.println("      현재 설정: DB_ID=root, DB_PW=your_password_here");
			System.err.println("");
			System.err.println("   3. MySQL이 localhost:3306에서 실행 중인지 확인하세요");
			return;
		}

		System.out.println("데이터베이스 초기화 성공");

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Emotion Diary");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			StatisticsView view = new StatisticsView();
			StatisticsDAO dao = new StatisticsDAO();
			new StatisticsController(view, dao);

			frame.add(view);
			frame.setSize(495, 630);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);

			System.out.println("통계 UI 실행");
		});
	}
}
