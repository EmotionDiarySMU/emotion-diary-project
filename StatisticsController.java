package share;

// Java 8+ 날짜/시간 라이브러리 임포트
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.DayOfWeek;
// '현실적인 주차' 계산을 위해 'WeekFields'와 'Locale' 임포트
import java.time.temporal.WeekFields;
import java.util.Locale;
// Java 데이터 구조 (Map) 임포트
import java.util.Map;
// JFreeChart 데이터셋 임포트
import org.jfree.data.category.DefaultCategoryDataset;
// [수정] PieDataset 임포트 (DAO에서 Map 대신 PieDataset을 직접 받도록 변경 - View와 호환)
import org.jfree.data.general.DefaultPieDataset; 
// Swing 이벤트 리스너 임포트
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * [수정] 통계 MVC 패턴의 '컨트롤러(Controller)'
 * (수정) 캡슐화를 위해 모든 멤버 변수가 'private'으로 변경되었습니다.
 * (수정) 'get...DateFromView' 메소드가 'WeekFields'를 사용한 '현실적인' 날짜 로직으로 업그레이드되었습니다.
 * (수정) DB 연동을 위해 DAO 호출 시 임시 'userId'를 전달합니다.
 */
public class StatisticsController {

    // Controller가 제어할 View 객체
    private StatisticsView view;
    // Controller가 데이터를 요청할 DAO 객체
    private StatisticsDAO dao;

    // --- 임시 사용자 ID ---
    // (중요) 로그인 기능이 병합되기 전까지 사용할 임시 ID입니다.
    // (TODO) 고객님의 'user' 테이블에 실제 데이터가 있는 user_id로 이 값을 변경해주세요. (예: "testuser")
    private static final String TEMP_USER_ID = "testuser";

    /**
     * StatisticsController 생성자 (이 생성자는 'public'이 맞습니다)
     * @param view (StatisticsView) 제어할 View 인스턴스
     * @param dao (StatisticsDAO) 데이터를 가져올 DAO 인스턴스
     */
    public StatisticsController(StatisticsView view, StatisticsDAO dao) {
        // (중요) View와 DAO 객체를 멤버 변수에 저장
        this.view = view;
        this.dao = dao;
        
        // (중요) View에 있는 모든 콤보박스에 '이벤트 리스너'를 연결(Attach)합니다.
        addListeners();
        
        // (초기화) 컨트롤러가 생성되는 즉시, '가짜 데이터'를 '진짜 데이터'로 갱신합니다.
        updateAllCharts();
    }

    /**
     * 'private': 이 클래스 '내부에서만' 사용되는 헬퍼 메소드입니다.
     * View의 모든 콤보박스에 "변경 시 updateAllCharts() 호출"이라는 동작을 연결합니다.
     */
    private void addListeners() {
        // "주간/월간/연간" 메인 콤보박스 리스너
        view.getViewModeSelector().addActionListener(e -> updateAllCharts());
        
        // --- "주간" 탭 콤보박스 리스너 ---
        view.getYearComboW().addActionListener(e -> updateAllCharts());
        view.getMonthComboW().addActionListener(e -> updateAllCharts());
        view.getWeekComboW().addActionListener(e -> updateAllCharts());
        
        // --- "월간" 탭 콤보박스 리스너 ---
        view.getYearComboM().addActionListener(e -> updateAllCharts());
        view.getMonthComboM().addActionListener(e -> updateAllCharts());
        
        // --- "연간" 탭 콤보박스 리스너 ---
        view.getYearComboY().addActionListener(e -> updateAllCharts());
    }

    /**
     * 'private': 이 클래스 '내부에서만' 사용되는 헬퍼 메소드입니다.
     * (핵심 로직) View의 콤보박스에서 이벤트가 발생할 때마다 호출됩니다.
     */
    private void updateAllCharts() {
        try {
            // 0. [신규] 현재 사용자 ID 가져오기 (지금은 임시 ID 사용)
            String currentUserId = TEMP_USER_ID; 
            // (참고) 추후 로그인 기능이 병합되면 이 부분은 Session.getUserId() 등으로 대체됩니다.
            
            // 1. View에서 현재 모드(String) 읽기
            String mode = (String) view.getViewModeSelector().getSelectedItem();

            // (예외 처리) 콤보박스가 비어있으면(초기화 중이면) 즉시 중단
            if (mode == null) return;

            // 2. View에서 선택된 값(String)을 기반으로 '시작일'과 '종료일' (LocalDate) 계산
            LocalDate startDate = getStartDateFromView(mode);
            LocalDate endDate = getEndDateFromView(mode);
            
            // (디버깅) 계산된 날짜가 맞는지 콘솔에 출력
            // System.out.println("Mode: " + mode + ", Start: " + startDate + ", End: " + endDate);

            // 3. DAO에 '진짜 데이터' 요청
            // [수정] 모든 DAO 호출에 'currentUserId'를 첫 번째 인자로 전달합니다.
            
            // (구현 1순위) 평균 스트레스 지수
            double avgStress = dao.getAverageStress(currentUserId, startDate, endDate);
            
            // (구현 2순위) 감정 차트 데이터
            // [수정] DAO가 View가 바로 사용할 수 있는 DefaultPieDataset을 반환하도록 변경
            //DefaultPieDataset emotionDataset = dao.getEmotionData(currentUserId, startDate, endDate);

            // (구현 3순위) 스트레스 차트 데이터
            // [수정] DAO 호출 시그니처 변경 (userId, startDate, endDate, mode)
            DefaultCategoryDataset stressDataset = dao.getStressData(currentUserId, startDate, endDate, mode);
            
            // 4. View 갱신 (Update)
            
            // (구현 1순위) "평균 스트레스 지수" 라벨 텍스트 갱신
            view.getAvgStressLabel().setText(
                String.format("<html><center>평균 스트레스 지수<b>:</b> <b>%.1f</b></center></html>", avgStress)
            );
            
            // [수정] (구현 2/3순위) View의 갱신 메소드를 '진짜 데이터'로 호출
            // (참고) StatisticsView의 updateEmotionChart는 DefaultPieDataset을 받도록 설계되었습니다.
            //view.updateEmotionChart(emotionDataset); 
            view.updateStressChart(stressDataset);

        } catch (Exception e) {
            e.printStackTrace();
            // (오류 처리) 사용자에게 오류가 발생했음을 알리는 'View'의 메소드를 호출해야 합니다.
            // (예: view.showError("데이터를 불러오는 중 오류가 발생했습니다."))
        }
    }

    /**
     * 'private': 이 클래스 '내부에서만' 사용되는 헬퍼 메소드입니다.
     * 'WeekFields'를 사용하여 '현실적인' 주간/월간/연간 시작일을 계산합니다.
     * (이하 로직은 매우 잘 작성되어 있으므로 수정하지 않습니다.)
     * @param mode ("주간", "월간", "연간")
     * @return (LocalDate) 조회 시작일
     */
    private LocalDate getStartDateFromView(String mode) {
        
        if (mode.equals("주간")) {
            // "주간" 탭의 년/월/주차 콤보박스에서 값을 읽어옵니다.
            String yearStr = (String) view.getYearComboW().getSelectedItem();
            String monthStr = (String) view.getMonthComboW().getSelectedItem();
            String weekStr = (String) view.getWeekComboW().getSelectedItem();
            
            // (예외 처리) 콤보박스가 비어있으면(초기화 중이면) 임시 날짜 반환
            if (yearStr == null || monthStr == null || weekStr == null) return LocalDate.now();
            
            // "2025년" -> 2025, "11월" -> 11, "2주" -> 2
            int year = Integer.parseInt(yearStr.replace("년", ""));
            int month = Integer.parseInt(monthStr.replace("월", ""));
            int week = Integer.parseInt(weekStr.replace("주", ""));
            
            // (java.time) 해당 월의 1일 (예: 2025-11-01)
            YearMonth ym = YearMonth.of(year, month);
            
            // --- "현실적인 주차" 계산 로직 ---
            // (정책) '1주'는 '월요일'부터 시작하고, '그 달'에 1일이라도 포함되면 1주로 칩니다.
            WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
            
            // 1. 그 달의 1일 (예: 2025-11-01)
            LocalDate firstDayOfMonth = ym.atDay(1);
            // 2. 'week' (예: "2주")에 해당하는 '그 주의 아무 날짜' (1~7일 중 하나)를 찾습니다.
            //    (1주=1~7일, 2주=8~14일... 이라는 '단순 정책'을 임시로 사용)
            LocalDate dayInWeek = ym.atDay(Math.min((week - 1) * 7 + 1, ym.lengthOfMonth()));
            // 3. 'dayInWeek'가 속한 주의 '월요일'을 찾습니다. (예: 2025-11-08(토) -> 2025-11-03(월))
            LocalDate startDate = dayInWeek.with(weekFields.dayOfWeek(), 1L); // 1L = 월요일
            
            // (안전 장치) 만약 계산된 월요일(startDate)이 '그 달' (11월)이 아니면 (예: 10월 27일),
            // 그 달의 1일(11월 1일)을 시작일로 강제합니다.
            if (startDate.getMonthValue() != month) {
                return ym.atDay(1);
            }
            return startDate;
            
        } else if (mode.equals("월간")) {
            // "월간" 탭의 년/월 콤보박스에서 값을 읽어옵니다.
            String yearStr = (String) view.getYearComboM().getSelectedItem();
            String monthStr = (String) view.getMonthComboM().getSelectedItem();
            
            if (yearStr == null || monthStr == null) return LocalDate.now();
            
            int year = Integer.parseInt(yearStr.replace("년", ""));
            int month = Integer.parseInt(monthStr.replace("월", ""));
            
            // (java.time) 해당 월의 1일 (예: 2025-11-01)
            return YearMonth.of(year, month).atDay(1);
            
        } else if (mode.equals("연간")) {
            // "연간" 탭의 년 콤보박스에서 값을 읽어옵니다.
            String yearStr = (String) view.getYearComboY().getSelectedItem();
            
            if (yearStr == null) return LocalDate.now();
            
            int year = Integer.parseInt(yearStr.replace("년", ""));
            
            // (java.time) 해당 년의 1월 1일 (예: 2025-01-01)
            return LocalDate.of(year, 1, 1);
        }
        
        return LocalDate.now(); // (예외 처리) 기본값으로 현재 날짜 반환
    }

    /**
     * 'private': 이 클래스 '내부에서만' 사용되는 헬퍼 메소드입니다.
     * 'WeekFields'를 사용하여 '현실적인' 주간/월간/연간 종료일을 계산합니다.
     * (이하 로직은 매우 잘 작성되어 있으므로 수정하지 않습니다.)
     * @param mode ("주간", "월간", "연간")
     * @return (LocalDate) 조회 종료일
     */
    private LocalDate getEndDateFromView(String mode) {
        
        if (mode.equals("주간")) {
            // "주간" 탭의 년/월/주차 콤보박스에서 값을 읽어옵니다.
            String yearStr = (String) view.getYearComboW().getSelectedItem();
            String monthStr = (String) view.getMonthComboW().getSelectedItem();
            String weekStr = (String) view.getWeekComboW().getSelectedItem();
            
            if (yearStr == null || monthStr == null || weekStr == null) return LocalDate.now();

            int year = Integer.parseInt(yearStr.replace("년", ""));
            int month = Integer.parseInt(monthStr.replace("월", ""));
            int week = Integer.parseInt(weekStr.replace("주", ""));
            
            YearMonth ym = YearMonth.of(year, month);
            
            // --- "현실적인 주차" 계산 로직 ---
            WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
            
            // 1. 위 'getStartDateFromView'와 동일한 로직으로 '시작일'(월요일)을 찾습니다.
            LocalDate dayInWeek = ym.atDay(Math.min((week - 1) * 7 + 1, ym.lengthOfMonth()));
            LocalDate startDate = dayInWeek.with(weekFields.dayOfWeek(), 1L);
            
            if (startDate.getMonthValue() != month) {
                startDate = ym.atDay(1);
            }

            // 2. '시작일'(월요일)에 6일을 더해 '종료일'(일요일)을 계산합니다.
            LocalDate endDate = startDate.plusDays(6);

            // (안전 장치) 만약 계산된 일요일(endDate)이 '그 달' (11월)이 아니면 (예: 12월 1일),
            // 그 달의 마지막 날(11월 30일)을 종료일로 강제합니다.
            if (endDate.getMonthValue() != month) {
                return ym.atEndOfMonth(); 
            }
            return endDate;
            
        } else if (mode.equals("월간")) {
            String yearStr = (String) view.getYearComboM().getSelectedItem();
            String monthStr = (String) view.getMonthComboM().getSelectedItem();
            
            if (yearStr == null || monthStr == null) return LocalDate.now();

            int year = Integer.parseInt(yearStr.replace("년", ""));
            int month = Integer.parseInt(monthStr.replace("월", ""));
            
            // (java.time) 해당 월의 마지막 날 (예: 2025-11-30)
            return YearMonth.of(year, month).atEndOfMonth();
            
        } else if (mode.equals("연간")) {
            String yearStr = (String) view.getYearComboY().getSelectedItem();
            
            if (yearStr == null) return LocalDate.now();

            int year = Integer.parseInt(yearStr.replace("년", ""));
            
            // (java.time) 해당 년의 12월 31일 (예: 2025-12-31)
            return LocalDate.of(year, 12, 31);
        }
        
        return LocalDate.now(); // (예외 처리) 기본값으로 현재 날짜 반환
    }

}