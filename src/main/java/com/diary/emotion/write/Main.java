import javax.swing.SwingUtilities;

/*
 main-(실행 파일) 추후 수정해보기
 */
public class Main {
	public static void main(String[] args) {
		
		// ⭐️ 1. EDT 스레드- GUI 생성/실행
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 1. WriteDiaryGUI를 'w'라는 변수에 저장
                WriteDiaryGUI w = new WriteDiaryGUI();
                
                // 2. 그 'w'를 화면에 보여달라고 명령
                w.setVisible(true); 
            }
        });
		
		// ⭐️ 2. "DB 접속"은 따로 실행
        // (GUI가 1초도 멈추지 않도록 별도의 스레드에서 실행)
		new Thread(new Runnable() {
		    @Override
		    public void run() {
		        // ⭐️ 여기가 DatabaseUtil.createDatabase()인지 확인!
		        boolean success = DatabaseUtil.createDatabase(); 
		        if (success) {
		            System.out.println("데이터베이스 연결/초기화 성공.");
		        } else {
		            System.err.println("데이터베이스 연결/초기화 실패!");
		        }
		    }
		}).start();
        
	}
}