package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

/**
 * 'question' DB 테이블 관련 로직(초기화, 랜덤 조회)을 전담하는 클래스
 */
public class QuestionDBManager {

    // 1. 50개 질문 리스트 (DB 초기화용 1회성 재료!)
    private static final List<String> QUESTIONS_LIST = Arrays.asList(
    		"오늘 하루, 기분 좋게 마무리했나요?",
            "오늘 가장 기억에 남는 순간은 언제였나요?",
            "오늘 '이건 좀 잘했다' 싶은 일이 있나요?",
            "오늘 먹었던 음식 중 가장 기분 좋게 했던 것은?",
            "오늘 예상치 못하게 웃음이 터졌던 순간이 있나요?",
            "오늘 머릿속을 가장 많이 차지한 생각은?",
            "어제보다 오늘 조금 더 나았던 점이 있다면?",
            "오늘 발견한 작은 '소확행(소소하지만 확실한 행복)'이 있나요?",
            "오늘 하루와 어울리는 노래 한 곡을 튼다면?",
            "오늘 가장 시간을 많이 쓴 일은 뭐예요?",
            "오늘 나를 행복하게 만든 사람은 누구인가요?",
            "요즘 소소하게 기대하고 있는 일이 있나요?",
            "오늘 나의 어떤 점이 마음에 들었나요?",
            "오늘 느낀 가장 기분 좋은 감정은?",
            "요즘 나에게 힘이 되는 말은?",
            "생각만 해도 기분 좋아지는 장소나 추억이 있나요?",
            "'이럴 땐 내가 참 좋다'고 느끼는 순간은?",
            "최근에 누군가에게 들었던 가장 기분 좋은 말은?",
            "내일 하루, '이것' 하나만은 꼭 했으면 좋겠다!",
            "오늘 마음을 조금 불편하게 한 일이 있나요?",
            "오늘 느낀 감정 중 가장 다루기 힘들었던 것은?",
            "요즘 머릿속을 떠나지 않는 걱정거리 한 가지?",
            "오늘 '그냥 참자' 하고 넘어간 일이 있나요?",
            "스트레스 받을 때 나도 모르게 하는 행동이 있나요?",
            "오늘 '아차' 싶었던 순간이 있나요?",
            "지금 당장 내 마음에서 비워내고 싶은 감정이 있나요?",
            "나도 모르게 한숨이 나왔던 순간은 언제인가요?",
            "다시 과거로 되돌아가서 바꾸고 싶은 순간이 있다면?",
            "나는 어떨 때 가장 '나답다'고 느끼나요?",
            "요즘 나의 가장 큰 관심사는 무엇인가요?",
            "읽고 싶은 책이나 보고 싶은 영화가 있나요?",
            "요즘 푹 빠져서 자주 듣는 노래가 있다면?",
            "가장 자주 하는 말버릇은 무엇인가요?",
            "내가 가장 편안함을 느끼는 장소는 어디인가요?",
            "나만의 스트레스 해소법이 있다면 무엇인가요?",
            "요즘 내 마음의 날씨를 표현한다면?",
            "새롭게 먹어보고 싶은 음식이 있나요?",
            "남들은 잘 모르는 나만의 소소한 즐거움은?",
            "이번 주말에 꼭 하고 싶은 일 한 가지는?",
            "이번 주가 가기 전에 꼭 하고 싶은 일이 있나요?",
            "주말에 날씨가 좋다면 가장 하고 싶은 일은?",
            "최근에 본 영상(유튜브, 드라마 등) 중에 가장 재미있었던 것은?",
            "오늘 하루 수고한 나에게 작은 선물을 준다면?",
            "오늘 대화한 사람 중 가장 기억에 남는 사람은?",
            "요즘 내가 고맙게 생각하는 사람은 누구인가요?",
            "오늘 누군가에게 조금 서운했던 순간이 있나요?",
            "오늘 내가 누군가에게 도움이 된 일이 있나요?",
            "만나면 나를 편안하게 해주는 친구가 있나요?",
            "최근 가장 감사하다고 느꼈던 순간이 있나요?",
            "요즘 가장 받고 싶은 선물은?"
    );

    /**
     * [기능 1] 
     * 프로그램 시작 시 question 테이블이 비어있으면 50개 질문을 삽입.
     */
    public static void initializeQuestions() {
        String checkSql = "SELECT COUNT(*) FROM question";
        String insertSql = "INSERT INTO question (question_text) VALUES (?)";

        // ⭐️ DB 연결은 'DatabaseManager'의 것을 빌려 씀
        try (Connection conn = DB.DatabaseManager.getConnection()) {
            
            // 1. 테이블이 비어있는지 확인
            try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql);
                 ResultSet rs = checkPstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
//                    System.out.println("질문 DB가 이미 초기화되어 있습니다. (Skipping)");
                    return; // 이미 데이터가 있으므로 종료
                }
            }

            // 2. 비어있으면 50개 질문 배치(Batch) 삽입
//            System.out.println("질문 DB 초기화를 시작합니다...");
            conn.setAutoCommit(false); // 트랜잭션 시작

            try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                for (String question : QUESTIONS_LIST) {
                    insertPstmt.setString(1, question.replace("'", "''")); // 작은따옴표 처리
                    insertPstmt.addBatch();
                }
                insertPstmt.executeBatch(); // 50개 쿼리 한 번에 실행
                conn.commit(); // DB에 최종 반영
//                System.out.println("질문 50개 DB 초기화 완료!");
            } catch (Exception e) {
                conn.rollback(); // 오류 시 롤백
                throw e; 
            } finally {
                conn.setAutoCommit(true); // 오토커밋 원상복구
            }
            
        } catch (Exception e) {
            System.err.println("질문 DB 초기화 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * [기능 2] (WriteDiaryGUI에서 호출)
     * "오늘의 질문"을 DB에서 랜덤하게 1개 가져옴.
     */
    public static String getTodaysQuestion() {
        String defaultQuestion = "오늘 하루는 어땠나요?"; // 기본값
        
        // ⭐️ SQL을 랜덤으로 1개 정렬 후 뽑기로 수정
        String sql = "SELECT question_text FROM question ORDER BY RAND() LIMIT 1";
        
        // ⭐️ DB 연결은 'DatabaseManager'의 것을 빌려 씀
        try (Connection conn = DB.DatabaseManager.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                // 쿼리 결과(랜덤 질문)가 있으면 즉시 반환
                return rs.getString("question_text");
            }
            
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 오류 출력
        }
        
        // (DB에 질문이 0개이거나, 쿼리 실패 시) 기본값 반환
        return defaultQuestion; 
    }
}