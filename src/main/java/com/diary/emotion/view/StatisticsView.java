package com.diary.emotion.view;

// Java Swing GUI 컴포넌트 임포트
import javax.swing.JLabel; // 텍스트 라벨 컴포넌트
import javax.swing.JPanel; // 컴포넌트들을 담는 패널(컨테이너)
import javax.swing.JComboBox; // 드롭다운 목록 컴포넌트
import javax.swing.SwingConstants; // Swing에서 사용되는 상수들 (예: 정렬)
import javax.swing.BoxLayout; // 컴포넌트를 수직(Y_AXIS) 또는 수평(X_AXIS)으로 배치하는 레이아웃
import javax.swing.BorderFactory; // 패널에 여백(Border)을 생성하는 유틸리티
import javax.swing.Box; // 컴포넌트 사이에 고정된 간격(Strut)을 만들기 위해 임포트

import java.awt.BorderLayout; // 패널을 동/서/남/북/중앙으로 배치하는 레이아웃
import java.awt.FlowLayout; // 컴포넌트를 왼쪽에서 오른쪽으로, 줄바꿈하며 배치하는 레이아웃
import java.awt.Font; // 글꼴(Font)을 정의하기 위한 클래스
import java.awt.Dimension; // 컴포넌트의 크기(가로, 세로)를 정의
import java.awt.Component; // GUI 컴포넌트의 최상위 클래스 (예: 정렬 기준)
import java.awt.Color; // 색상(RGB)을 정의하기 위한 클래스
import java.awt.CardLayout; // 여러 패널을 카드처럼 겹쳐놓고 바꿔 보여주는 레이아웃
import java.awt.GridBagLayout; // 컴포넌트를 그리드(격자)에 복잡하게 배치하는 레이아웃
import java.awt.GridBagConstraints; // GridBagLayout의 제약조건(위치, 크기 등)을 설정
import java.awt.Insets; // 컴포넌트의 바깥 여백(Margin)을 설정
import java.awt.event.ActionEvent; // 버튼 클릭 등 '이벤트'가 발생했음을 알리는 객체
import java.awt.event.ActionListener; // '이벤트'가 발생했을 때 동작을 정의하는 인터페이스
import java.awt.geom.Ellipse2D; // 2D 타원(원)을 그리기 위한 클래스 (꺾은선 마커)

// JFreeChart 라이브러리 임포트
import org.jfree.chart.ChartFactory; // 막대/꺾은선 차트를 쉽게 만드는 팩토리 클래스
import org.jfree.chart.ChartPanel; // JFreeChart를 담을 수 있는 Swing 패널
import org.jfree.chart.JFreeChart; // 차트 객체의 본체
import org.jfree.chart.plot.PlotOrientation; // 차트의 방향 (수직/수평)
import org.jfree.chart.plot.CategoryPlot; // 차트의 Plot 영역(데이터가 그려지는 곳)
import org.jfree.chart.axis.ValueAxis; // 차트의 축 (Y축)
import org.jfree.chart.axis.NumberAxis; // 숫자로 이루어진 축 (Y축)
import org.jfree.chart.axis.NumberTickUnit; // 축의 눈금 단위 (예: 10단위)
import org.jfree.chart.renderer.category.BarRenderer; // 막대 차트를 그리는 렌더러
import org.jfree.chart.renderer.category.StandardBarPainter; // (디자인) 막대를 평평하게(매트하게) 칠하는 렌더러
import org.jfree.chart.renderer.category.LineAndShapeRenderer; // 꺾은선 차트를 그리는 렌더러

import org.jfree.data.category.DefaultCategoryDataset; // 차트에 사용될 데이터셋 (카테고리 기반)

// [신규] '진짜 데이터' 갱신을 위해 임포트
import java.util.Map; // 'updateEmotionChart'의 파라미터 타입

/**
 * [수정] 감정 통계 화면(View)을 담당하는 클래스 (JPanel)
 * (수정) 캡슐화를 위해 모든 멤버 변수가 'private'으로 변경되었습니다.
 * (수정) Controller가 '진짜 데이터'로 차트를 갱신할 수 있도록 'update...' 메소드가 추가되었습니다.
 */
public class StatisticsView extends JPanel { 

    // (디자인) 이 통계 탭의 전용 파스텔 톤 파란색 배경을 상수로 정의합니다.
    private static final Color PASTEL_BLUE = new Color(230, 240, 255);

    // [수정] 'private'으로 변경 (캡슐화)
    // "평균 스트레스 지수" 텍스트를 표시하는 라벨입니다.
    private JLabel avgStressLabel;
    // [수정] 'private'으로 변경 (캡슐화)
    // "주간/월간/연간" 모드를 선택하는 메인 콤보박스입니다.
    private JComboBox<String> viewModeSelector; 
    // [수정] 'private'으로 변경 (캡슐화)
    // "주간/월간/연간" 선택에 따라 날짜 선택기를 교체해 보여줄 CardLayout 패널입니다.
    private JPanel datePickerCardPanel;
    // [수정] 'private'으로 변경 (캡슐화)
    // datePickerCardPanel을 제어하는 CardLayout 매니저입니다.
    private CardLayout datePickerCardLayout;
    
    // [수정] 'private'으로 변경 (캡슐화)
    // "주간" 탭에서 사용할 년/월/주차 선택 콤보박스 멤버 변수입니다.
    private JComboBox<String> yearComboW, monthComboW, weekComboW; 
    
    // [수정] 'private'으로 변경 (캡슐화)
    // "월간" 탭에서 사용할 년/월 선택 콤보박스 멤버 변수입니다.
    private JComboBox<String> yearComboM, monthComboM; 
    
    // [수정] 'private'으로 변경 (캡슐화)
    // "연간" 탭에서 사용할 년 선택 콤보박스 멤버 변수입니다.
    private JComboBox<String> yearComboY; 
    
    // [수정] 'private'으로 변경 (캡슐화)
    // 차트 영역 전체(감정, 스트레스, 라벨)를 담고 있는 메인 패널입니다.
    private JPanel mainChartPanel; 
    
    // 콤보박스에 사용할 날짜 모델(데이터)을 상수로 미리 정의합니다.
    private static final String[] YEARS = {"2020년", "2021년", "2022년", "2023년", "2024년", "2025년"};
    private static final String[] MONTHS = {"01월", "02월", "03월", "04월", "05월", "06월", "07월", "08월", "09월", "10월", "11월", "12월"};
    private static final String[] WEEKS = {"1주", "2주", "3주", "4주", "5주"};
    
    // (디자인) 차트의 X축 라벨("(감정)", "(요일)")에 사용할 작은 폰트를 상수로 정의합니다.
    private static final Font AXIS_LABEL_FONT = new Font("SansSerif", Font.PLAIN, 11);
    
    // (디자인) 차트 제목("<감정 지수>")에 사용할 굵은 폰트를 상수로 정의합니다.
    private static final Font CHART_TITLE_FONT = new Font("SansSerif", Font.BOLD, 16);


    /**
     * StatisticsView 생성자
     * 이 클래스의 객체가 생성될 때(new StatisticsView()) 자동으로 호출되어 GUI를 초기화합니다.
     */
    public StatisticsView() {
        // 이 패널(StatisticsView)의 레이아웃을 BorderLayout (동/서/남/북/중앙)으로 설정합니다.
        setLayout(new BorderLayout()); 
        // (디자인) 이 패널의 기본 배경색을 파스텔 블루로 설정합니다.
        setBackground(PASTEL_BLUE);
        // GUI 컴포넌트를 생성하고 배치하는 'initUI' 메소드를 호출합니다.
        initUI();
    }

    /**
     * GUI 컴포넌트를 생성하고 배치하는 메인 메소드
     */
    private void initUI() {
        
        // 1. 상단 컨트롤 패널 (BorderLayout.NORTH)
        
        // FlowLayout: 컴포넌트를 왼쪽->오른쪽으로, 수평 간격 5, 수직 간격 0으로 배치합니다.
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); 
        // (디자인) 컨트롤 패널의 배경색을 파스텔 블루로 설정합니다.
        controlPanel.setBackground(PASTEL_BLUE); 
        
        // (디자인) 컨트롤 패널에 바깥 여백(Margin)을 설정합니다. (상: 20, 좌: 10, 하: 0, 우: 0)
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 0)); 
        
        // "주간", "월간", "연간" 문자열 배열을 데이터로 JComboBox를 생성합니다.
        viewModeSelector = new JComboBox<>(new String[]{"주간", "월간", "연간"}); 
        // 컨트롤 패널에 'viewModeSelector'를 추가합니다.
        controlPanel.add(viewModeSelector);

        // CardLayout 매니저 객체를 생성합니다.
        datePickerCardLayout = new CardLayout();
        // CardLayout을 사용하는 'datePickerCardPanel'을 생성합니다.
        datePickerCardPanel = new JPanel(datePickerCardLayout);
        // (디자인) CardLayout 패널을 투명하게 설정하여, 부모(controlPanel)의 파스텔 블루 배경이 보이도록 합니다.
        datePickerCardPanel.setOpaque(false); 
        
        // --- 1-1. CardLayout에 들어갈 "주간" 탭용 날짜 선택기 패널 ---
        JPanel weeklyCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        weeklyCard.setBackground(PASTEL_BLUE); // (디자인) 배경색 설정
        yearComboW = new JComboBox<>(YEARS); // 'YEARS' 배열로 년 콤보박스 생성
        monthComboW = new JComboBox<>(MONTHS); // 'MONTHS' 배열로 월 콤보박스 생성
        weekComboW = new JComboBox<>(WEEKS); // 'WEEKS' 배열로 주 콤보박스 생성
        
        weeklyCard.add(yearComboW); // "주간" 패널에 년 콤보박스 추가
        weeklyCard.add(monthComboW); // "주간" 패널에 월 콤보박스 추가
        weeklyCard.add(weekComboW); // "주간" 패널에 주 콤보박스 추가

        // --- 1-2. CardLayout에 들어갈 "월간" 탭용 날짜 선택기 패널 ---
        JPanel monthlyCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        monthlyCard.setBackground(PASTEL_BLUE); // (디자인) 배경색 설정
        yearComboM = new JComboBox<>(YEARS); // 'YEARS' 배열로 년 콤보박스 생성
        monthComboM = new JComboBox<>(MONTHS); // 'MONTHS' 배열로 월 콤보박스 생성
        
        monthlyCard.add(yearComboM); // "월간" 패널에 년 콤보박스 추가
        monthlyCard.add(monthComboM); // "월간" 패널에 월 콤보박스 추가

        // --- 1-3. CardLayout에 들어갈 "연간" 탭용 날짜 선택기 패널 ---
        JPanel yearlyCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        yearlyCard.setBackground(PASTEL_BLUE); // (디자인) 배경색 설정
        yearComboY = new JComboBox<>(YEARS); // 'YEARS' 배열로 년 콤보박스 생성
        yearlyCard.add(yearComboY); // "연간" 패널에 년 콤보박스 추가
        
        // --- 1-4. CardLayout 패널에 3개의 (주간/월간/연간) 패널을 "이름표"와 함께 추가 ---
        datePickerCardPanel.add(weeklyCard, "주간");
        datePickerCardPanel.add(monthlyCard, "월간");
        datePickerCardPanel.add(yearlyCard, "연간");

        
        // --- 1-5. 날짜 콤보박스의 기본 선택값 설정 (예: 2025년 11월 2주) ---
        yearComboW.setSelectedItem("2025년");
        monthComboW.setSelectedItem("11월");
        weekComboW.setSelectedItem("2주"); 
        
        yearComboM.setSelectedItem("2025년");
        monthComboM.setSelectedItem("11월");

        yearComboY.setSelectedItem("2025년");

        // --- 1-6. 메인 "주간/월간/연간" 콤보박스에 이벤트 리스너(동작) 추가 ---
        // (주의: 이 리스너는 이제 Controller가 아닌 View가 직접 소유합니다.
        // Controller는 이 콤보박스에 '별도의' 리스너를 추가하여 동작을 감지합니다.)
        viewModeSelector.addActionListener(new ActionListener() {
            @Override // actionPerformed 메소드를 구현합니다.
            public void actionPerformed(ActionEvent e) {
                // 1. 콤보박스에서 현재 선택된 항목(예: "월간")의 텍스트를 가져옵니다.
                String selectedMode = (String) viewModeSelector.getSelectedItem();
                
                // 2. CardLayout이 'selectedMode' 이름표("월간")에 맞는 카드를 보여주도록 합니다.
                datePickerCardLayout.show(datePickerCardPanel, selectedMode);
                
                // 3. (중요) 선택된 모드("월간")에 맞는 '가짜 데이터'와 X축을 가진 새 차트 패널을 생성합니다.
                // (참고: Controller가 연결되면, 이 '가짜' 패널은 즉시 '진짜' 데이터로 덮어쓰기됩니다.)
                JPanel newChartPanel = createChartPanel(selectedMode);
                
                // 4. 기존 차트 패널을 'newChartPanel'로 교체하고 화면을 갱신합니다.
                setMainChartPanel(newChartPanel); 
            }
        });

        // 1-7. 상단 컨트롤 패널(controlPanel)에 CardLayout 패널(datePickerCardPanel)을 추가합니다.
        controlPanel.add(datePickerCardPanel);
        // CardLayout 패널의 기본 표시 카드를 "주간"으로 설정합니다.
        datePickerCardLayout.show(datePickerCardPanel, "주간"); 

        // 1-8. 완성된 상단 컨트롤 패널을 메인 창(StatisticsView)의 "NORTH"(북쪽)에 추가합니다.
        add(controlPanel, BorderLayout.NORTH); 

        
        // 2. 중앙 차트 패널 (BorderLayout.CENTER)
        
        // 프로그램 시작 시 기본값("주간")으로 차트 패널을 생성합니다.
        mainChartPanel = createChartPanel("주간"); 
        // 생성된 차트 패널을 메인 창(StatisticsView)의 "CENTER"(중앙)에 추가합니다.
        add(mainChartPanel, BorderLayout.CENTER);
    }
    

    /**
     * "메인 차트 패널" (감정, 스트레스, 라벨)을 생성하는 메소드
     * @param mode 현재 선택된 모드 ("주간", "월간", "연간")
     * @return 차트와 라벨이 조립된 'centerPanel' (JPanel)
     */
    private JPanel createChartPanel(String mode) {
        // 1. 전체를 감싸는 'centerPanel' 생성 (수직(Y_AXIS)으로 컴포넌트를 쌓는 BoxLayout)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); 
        centerPanel.setBackground(PASTEL_BLUE); // (디자인) 배경색 설정
        
        // 2. "감정 지수" 헤더 패널 생성 (GridBagLayout)
        
        // 2-1. "<감정 지수>" 텍스트 라벨 (외부 JLabel)
        JLabel emotionTitleLabel = new JLabel("<감정 지수>");
        emotionTitleLabel.setFont(CHART_TITLE_FONT); // (디자인) 굵은 폰트 적용

        // 2-2. "감정 지수" 막대 차트 생성 (JFreeChart의 Legend 기능은 끔)
        JPanel emotionChartPanel = createDemoEmotionBarChart(mode); 
        
        // 2-3. "횟수/수치"를 표시할 "가짜 범례(Fake Legend)" JPanel을 수동으로 생성
        JPanel fakeLegendPanel = createCustomLegendPanel();
        
        // 2-4. 'emotionHeaderPanel'을 GridBagLayout으로 생성 (복잡한 배치를 위함)
        JPanel emotionHeaderPanel = new JPanel(new GridBagLayout()); 
        emotionHeaderPanel.setBackground(PASTEL_BLUE); // (디자인) 배경색 설정
        
        // GridBagLayout의 제약조건(GBC) 객체 생성 (제목용)
        GridBagConstraints gbcTitle = new GridBagConstraints();
        
        gbcTitle.gridx = 0; // GBC(제목): 0행 0열에 위치
        gbcTitle.gridy = 0;
        gbcTitle.gridwidth = 2; // GBC(제목): 2개의 열(column)을 모두 차지 (가로로 길게)
        gbcTitle.anchor = GridBagConstraints.CENTER; // GBC(제목): 차지한 공간(2열)의 "중앙(CENTER)"에 배치
        gbcTitle.weightx = 1.0; // GBC(제목): 가로(weightx) 방향으로 남는 공간을 모두 차지
        
        // "emotionHeaderPanel"에 'emotionTitleLabel'을 'gbcTitle' 제약조건으로 추가
        emotionHeaderPanel.add(emotionTitleLabel, gbcTitle);
        
        // GBC 객체 생성 (범례용)
        GridBagConstraints gbcLegend = new GridBagConstraints();
        
        gbcLegend.gridx = 1; // GBC(범례): 0행 1열에 위치 (제목과 같은 행, 두 번째 열)
        gbcLegend.gridy = 0;
        gbcLegend.gridwidth = 1; // GBC(범례): 1개의 열만 차지
        gbcLegend.anchor = GridBagConstraints.EAST; // GBC(범례): 차지한 공간의 "오른쪽 끝(EAST)"에 배치
        gbcLegend.weightx = 0.0; // GBC(범례): 가로 방향으로 남는 공간을 차지하지 않음
        
        // GBC(범례): (디자인) 오른쪽 바깥 여백(Margin)을 22px로 설정
        gbcLegend.insets = new Insets(0, 0, 0, 22); 
        
        // "emotionHeaderPanel"에 'fakeLegendPanel'을 'gbcLegend' 제약조건으로 추가
        emotionHeaderPanel.add(fakeLegendPanel, gbcLegend);
        
        // (디자인) 완성된 'emotionHeaderPanel'의 상단 여백을 25px로 설정
        emotionHeaderPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        
        // 3. "스트레스 지수" 헤더 (JLabel)
        
        JLabel stressTitleLabel = new JLabel("<스트레스 지수>");
        stressTitleLabel.setFont(CHART_TITLE_FONT); // (디자인) 굵은 폰트 적용
        stressTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // (디자인) 중앙 정렬
        stressTitleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 5, 10)); // (디자인) 여백 설정

        // 3-1. "스트레스 지수" 꺾은선 차트 생성
        JPanel stressChartPanel = createDemoStressLineChart(mode); 

        
        // 4. 'centerPanel' (수직 BoxLayout)에 모든 컴포넌트 최종 조립
        
        centerPanel.add(emotionHeaderPanel); // (1) "감정 지수" 헤더 (제목+범례)
        
        // (2) "감정 지수" 차트 (Plot)
        emotionChartPanel.setPreferredSize(new Dimension(400, 260)); // (디자인) 크기 설정
        emotionChartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260)); // (디자인) 최대 크기 설정
        emotionChartPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10)); // (디자인) 여백 설정
        centerPanel.add(emotionChartPanel); // 패널에 추가

        centerPanel.add(stressTitleLabel); // (3) "스트레스 지수" 헤더 (제목)

        // (4) "스트레스 지수" 차트 (Plot)
        stressChartPanel.setPreferredSize(new Dimension(400, 260)); // (디자인) 크기 설정
        stressChartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260)); // (디자인) 최대 크기 설정
        stressChartPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10)); // (디자인) 여백 설정
        centerPanel.add(stressChartPanel); // 패널에 추가
        
        // (5) "평균 스트레스 지수" 라벨
        // 'getAvgStressText' 메소드가 반환하는 (HTML 포맷의) 텍스트로 라벨 생성
        avgStressLabel = new JLabel(getAvgStressText(mode), SwingConstants.CENTER); 
        avgStressLabel.setFont(new Font("SansSerif", Font.PLAIN, 16)); // (디자인) 폰트 설정
        avgStressLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // (디자인) 중앙 정렬
        avgStressLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // (디자인) 여백 설정
        centerPanel.add(avgStressLabel); // 패널에 추가

        // 완성된 'centerPanel'을 반환합니다.
        return centerPanel;
    }

    /**
     * 'mode'에 따라 "평균 스트레스 지수" 라벨의 '가짜 데이터' 텍스트를 생성합니다.
     * (이 부분은 나중에 Controller가 '진짜 데이터'로 갱신할 예정입니다.)
     * @param mode 현재 선택된 모드 ("주간", "월간", "연간")
     * @return HTML 포맷의 라벨 텍스트 (예: "평균 스트레스 지수: 25.0")
     */
    private String getAvgStressText(String mode) {
        // (임시 데이터)
        double avgStress = 25.0; // "주간" 모드의 가짜 평균값
        
        if (mode.equals("월간")) {
            avgStress = 31.0; // "월간" 모드의 가짜 평균값
        } else if (mode.equals("연간")) {
            avgStress = 28.0; // "연간" 모드의 가짜 평균값
        }
        
        // HTML을 사용하여 텍스트를 포맷팅합니다. (굵은 글씨<b>, 중앙 정렬<center>)
        return String.format("<html><center>평균 스트레스 지수<b>:</b> <b>%.1f</b></center></html>", avgStress);
    }

    /**
     * '가짜 데이터'로 "스트레스 수치" 꺾은선 차트를 생성합니다.
     * @param mode 현재 모드 ("주간", "월간", "연간")에 따라 X축 라벨과 데이터가 변경됩니다.
     * @return 꺾은선 차트가 담긴 'ChartPanel' (JPanel)
     */
    private JPanel createDemoStressLineChart(String mode) {
        // 1. 차트 데이터를 담을 'dataset' 객체 생성
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // X축 라벨의 기본값을 "(요일)"로 설정
        String xAxisLabel = "(요일)"; 
        
        // 2. 'mode'에 따라 '가짜 데이터'와 X축 라벨을 다르게 설정
        if(mode.equals("주간")) {
            xAxisLabel = "(요일)"; // X축 라벨 변경
            dataset.setValue(30, "Stress", "월");
            dataset.setValue(20, "Stress", "화");
            dataset.setValue(40, "Stress", "수");
            dataset.setValue(10, "Stress", "목");
            dataset.setValue(30, "Stress", "금");
            dataset.setValue(20, "Stress", "토");
            dataset.setValue(50, "Stress", "일");
        } else if(mode.equals("월간")) {
            xAxisLabel = "(주)"; // X축 라벨 변경
            dataset.setValue(31, "Stress", "1주");
            dataset.setValue(25, "Stress", "2주");
            dataset.setValue(40, "Stress", "3주");
            dataset.setValue(18, "Stress", "4주");
        } else if(mode.equals("연간")) {
            xAxisLabel = "(월)"; // X축 라벨 변경
            dataset.setValue(28, "Stress", "1월");
            dataset.setValue(35, "Stress", "2월");
            dataset.setValue(30, "Stress", "3월");
            dataset.setValue(21, "Stress", "4월");
            dataset.setValue(19, "Stress", "5월");
        }

        // 3. JFreeChart 팩토리를 사용해 '꺾은선 차트(lineChart)' 생성
        JFreeChart lineChart = ChartFactory.createLineChart(
            null, // 차트 제목 
            xAxisLabel, // X축 라벨 
            "(%)", // Y축 라벨
            dataset, // 차트에 표시할 데이터셋
            PlotOrientation.VERTICAL, // 차트 방향 (수직)
            false, // 범례(Legend) 표시 안 함
            true, // 툴팁 표시 함
            false // URL 링크 생성 안 함
        );
        
        // 4. (디자인) 차트 디자인 세부 설정
        lineChart.setBackgroundPaint(PASTEL_BLUE); // 차트 전체 배경색

        // 4-1. Plot 영역(데이터가 그려지는 곳) 설정
        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.getDomainAxis().setLabelFont(AXIS_LABEL_FONT); // X축 라벨 폰트
        plot.setRangeGridlinesVisible(true); // Y축 줄눈 보이기
        plot.setRangeGridlinePaint(new Color(220, 220, 220)); // 줄눈 색상
        
        // 4-2. Y축(NumberAxis) 설정
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setRange(0.0, 100.0); // Y축 범위 0~100 고정
        yAxis.setTickUnit(new NumberTickUnit(10.0)); // Y축 눈금 10단위
        yAxis.setLabelAngle(Math.PI / 2.0); // Y축 라벨 "(%)" 수직 표시
        yAxis.setLabelFont(AXIS_LABEL_FONT); // Y축 라벨 "(%)" 폰트 작게
        
        // 4-3. 꺾은선(Line) 렌더러 설정
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesStroke(0, new java.awt.BasicStroke(2.5f)); // 선 굵기
        renderer.setSeriesShapesVisible(0, true); // 데이터 포인트에 모양 표시
        renderer.setSeriesShape(0, new Ellipse2D.Double(-3.5, -3.5, 7.0, 7.0)); // '빈 동그라미'
        renderer.setSeriesFillPaint(0, Color.WHITE); // 동그라미 내부 흰색
        renderer.setSeriesOutlinePaint(0, renderer.getSeriesPaint(0)); // 동그라미 테두리
        renderer.setUseFillPaint(true);
        renderer.setUseOutlinePaint(true);
        
        // 4-4. Plot 배경 설정
        plot.setBackgroundPaint(Color.white); // Plot 영역(그래프) 배경을 흰색으로
        plot.setOutlineVisible(false); // Plot 영역 테두리 제거

        // 5. 완성된 차트(lineChart)를 'ChartPanel'에 담아서 반환
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setBackground(PASTEL_BLUE); // ChartPanel 배경색 통일
        return chartPanel;
    }

    /**
     * '가짜 데이터'로 "감정 통계" 막대 차트를 생성합니다. 
     * @param mode 현재 모드 (현재는 사용되지 않으나, 향후 확장성을 위해 유지)
     * @return 막대 차트가 담긴 'ChartPanel' (JPanel)
     */
    private JPanel createDemoEmotionBarChart(String mode) {
        // 1. 차트 데이터를 담을 'dataset' 객체 생성
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // 2. '가짜 데이터' 설정 (2개의 시리즈: "횟수", "수치")
        dataset.setValue(10, "횟수", "😢");
        dataset.setValue(20, "횟수", "🥰");
        dataset.setValue(30, "횟수", "😴");
        dataset.setValue(10, "횟수", "😍");
        dataset.setValue(10, "횟수", "😱");
        dataset.setValue(20, "횟수", "기타");
        dataset.setValue(20, "수치", "😢");
        dataset.setValue(10, "수치", "🥰");
        dataset.setValue(10, "수치", "😴");
        dataset.setValue(10, "수치", "😍");
        dataset.setValue(30, "수치", "😱");
        dataset.setValue(20, "수치", "기타");
        
        // 3. JFreeChart 팩토리를 사용해 '막대 차트(barChart)' 생성
        JFreeChart barChart = ChartFactory.createBarChart(
            null, // 차트 제목
            "(감정)", // X축 라벨
            "(%)", // Y축 라벨
            dataset, // 데이터셋
            PlotOrientation.VERTICAL, // 방향
            false, // 범례(Legend) 표시 안 함 (V24: "가짜 범례" 수동 생성)
            true, // 툴팁
            false // URL
        );
        
        // 4. (디자인) 차트 디자인 세부 설정
        barChart.setBackgroundPaint(PASTEL_BLUE); // 차트 전체 배경색
        
        // 4-1. Plot 영역 설정
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.getDomainAxis().setLabelFont(AXIS_LABEL_FONT); // X축 라벨 폰트
        plot.setRangeGridlinesVisible(true); // Y축 줄눈 보이기
        plot.setRangeGridlinePaint(new Color(220, 220, 220)); // 줄눈 색상
        
        // 4-2. Y축(NumberAxis) 설정
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setRange(0.0, 100.0); // Y축 범위 0~100
        yAxis.setTickUnit(new NumberTickUnit(10.0)); // Y축 눈금 10단위
        yAxis.setLabelAngle(Math.PI / 2.0); // Y축 라벨 "(%)" 수직
        yAxis.setLabelFont(AXIS_LABEL_FONT); // Y축 라벨 "(%)" 폰트 작게
        
        // 4-3. 막대(Bar) 렌더러 설정
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter()); // (디자인) 매트한 막대
        renderer.setShadowVisible(false); // (디자인) 그림자 제거
        renderer.setSeriesPaint(0, new Color(100, 150, 255)); // "횟수" 색상
        renderer.setSeriesPaint(1, new Color(190, 220, 255)); // "수치" 색상
        renderer.setItemMargin(0.1); // 그룹 내 막대 간격
        plot.getDomainAxis().setCategoryMargin(0.3); // X축 항목 간격 (막대 얇아짐)
        
        // 4-4. Plot 배경 설정
        plot.setBackgroundPaint(Color.white); // Plot 영역 배경 흰색
        plot.setOutlineVisible(false); // Plot 영역 테두리 제거

        // 5. 완성된 차트(barChart)를 'ChartPanel'에 담아서 반환
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setBackground(PASTEL_BLUE); // ChartPanel 배경색 통일
        
        return chartPanel; // 완성된 차트 패널 반환
    }

    /**
     * (V24) "횟수", "수치"를 표시할 "가짜 범례(Fake Legend)" JPanel을 수동으로 생성합니다.
     * @return 범례 항목("횟수", "수치")이 담긴 JPanel
     */
    private JPanel createCustomLegendPanel() {
        // FlowLayout: 컴포넌트를 오른쪽(RIGHT) 정렬
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        legendPanel.setBackground(Color.WHITE); // (디자인) 범례 패널 배경 흰색
        legendPanel.setOpaque(true); // (디자인) 불투명

        // "횟수" 범례 항목(색상+텍스트)을 생성하여 'legendPanel'에 추가
        legendPanel.add(createLegendItem(new Color(100, 150, 255), "횟수"));
        legendPanel.add(Box.createHorizontalStrut(5)); // 가로 간격
        // "수치" 범례 항목(색상+텍스트)을 생성하여 'legendPanel'에 추가
        legendPanel.add(createLegendItem(new Color(190, 220, 255), "수치"));
        
        // (디자인) 범례 패널의 안쪽 여백(Padding)을 5px로 설정
        legendPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 완성된 범례 패널을 반환
        return legendPanel;
    }

    /**
     * (V24) "가짜 범례"의 개별 항목(색상 상자 + 텍스트)을 생성하는 헬퍼 메소드
     * @param color 범례 항목의 색상
     * @param text  범례 항목의 텍스트 (예: "횟수")
     * @return 색상 상자와 텍스트가 조립된 JPanel
     */
    private JPanel createLegendItem(Color color, String text) {
        // FlowLayout: 컴포넌트를 왼쪽(LEFT) 정렬
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        itemPanel.setBackground(Color.WHITE); // (디자인) 항목 패널 배경 흰색
        itemPanel.setOpaque(true); // (디자인) 불투명

        // 1. 색상 상자(JPanel) 생성
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(10, 10)); // (디자인) 크기
        colorBox.setBackground(color); // (디자인) 배경색
        // (V30) (디자인) 색상 상자의 테두리(Border) 제거

        // 2. 텍스트 라벨(JLabel) 생성
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 12)); // (디자인) 폰트

        // 'itemPanel'에 'colorBox'와 'textLabel'을 순서대로 추가
        itemPanel.add(colorBox);
        itemPanel.add(textLabel);

        // 완성된 항목 패널을 반환
        return itemPanel;
    }


    // --- [신규] Controller가 View를 '진짜 데이터'로 갱신하기 위한 Public 메소드들 ---
    
    /**
     * [신규] (Controller용) '진짜 데이터'(dataset)로 스트레스 차트를 갱신합니다.
     * @param stressDataset DAO로부터 받은 '진짜' 스트레스 데이터셋
     */
    public void updateStressChart(DefaultCategoryDataset stressDataset) {
        // 1. 'mainChartPanel'에서 'stressChartPanel' (세 번째 컴포넌트[2])을 찾습니다.
        // (주의: 이 방식은 'centerPanel'의 컴포넌트 순서(Header, Chart, Header, Chart, Label)에 의존합니다.)
        try {
            // centerPanel(mainChartPanel) -> stressChartPanel (네 번째 컴포넌트[3])
            Component chartComponent = mainChartPanel.getComponent(3);
            if (chartComponent instanceof ChartPanel) {
                // 2. ChartPanel에서 JFreeChart 객체를 가져옵니다.
                ChartPanel chartPanel = (ChartPanel) chartComponent;
                JFreeChart chart = chartPanel.getChart();
                // 3. JFreeChart의 Plot에서 Dataset을 '진짜 데이터'로 교체합니다.
                CategoryPlot plot = chart.getCategoryPlot();
                plot.setDataset(stressDataset); 
                // (TODO: X축 라벨(요일/주/월)도 'mode'에 따라 Controller가 갱신해줘야 함)
                // String xAxisLabel = "(요일)"; // mode에 따라 변경
                // plot.getDomainAxis().setLabel(xAxisLabel);
            }
        } catch (Exception e) {
            e.printStackTrace(); // (오류 처리)
        }
    }

    /**
     * [신규] (Controller용) '진짜 데이터'(emotionData)로 감정 차트를 갱신합니다.
     * @param emotionData DAO로부터 받은 '진짜' 감정 데이터 Map
     */
    public void updateEmotionChart(Map<String, Map<String, Double>> emotionData) {
        // 1. 'mainChartPanel'에서 'emotionChartPanel' (첫 번째 컴포넌트[1])을 찾습니다.
        try {
            // centerPanel(mainChartPanel) -> emotionChartPanel (두 번째 컴포넌트[1])
            Component chartComponent = mainChartPanel.getComponent(1);
            if (chartComponent instanceof ChartPanel) {
                // 2. ChartPanel에서 JFreeChart 객체를 가져옵니다.
                ChartPanel chartPanel = (ChartPanel) chartComponent;
                JFreeChart chart = chartPanel.getChart();
                // 3. '진짜 데이터' Map을 JFreeChart의 Dataset으로 변환합니다.
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                Map<String, Double> counts = emotionData.get("횟수");
                Map<String, Double> values = emotionData.get("수치");
                
                // (TODO) 이모지("😢", "🥰"...) 목록을 DAO 또는 고정된 리스트에서 가져와야 합니다.
                String[] emotions = {"😢", "🥰", "😴", "😍", "😱", "기타"};
                
                for (String emotion : emotions) {
                    // 4. Dataset을 '진짜 데이터'로 채웁니다. (데이터가 없으면 0.0)
                    dataset.setValue(counts.getOrDefault(emotion, 0.0), "횟수", emotion);
                    dataset.setValue(values.getOrDefault(emotion, 0.0), "수치", emotion);
                }
                
                // 5. JFreeChart의 Plot에서 Dataset을 '진짜 데이터'로 교체합니다.
                CategoryPlot plot = chart.getCategoryPlot();
                plot.setDataset(dataset);
            }
        } catch (Exception e) {
            e.printStackTrace(); // (오류 처리)
        }
    }


    // --- Controller가 View의 컴포넌트에 접근(Get)하기 위한 Public 메소드들 ---
    
    /**
     * "주간/월간/연간" 콤보박스를 반환합니다.
     */
    public JComboBox<String> getViewModeSelector() {
        return viewModeSelector;
    }
    
    // --- "주간" 탭의 콤보박스 반환 ---
    public JComboBox<String> getYearComboW() { return yearComboW; }
    public JComboBox<String> getMonthComboW() { return monthComboW; }
    public JComboBox<String> getWeekComboW() { return weekComboW; }
    
    // --- "월간" 탭의 콤보박스 반환 ---
    public JComboBox<String> getYearComboM() { return yearComboM; }
    public JComboBox<String> getMonthComboM() { return monthComboM; }
    
    // --- "연간" 탭의 콤보박스 반환 ---
    public JComboBox<String> getYearComboY() { return yearComboY; }

    /**
     * "평균 스트레스 지수" 라벨을 반환합니다.
     * (Controller가 이 라벨의 텍스트를 '진짜 데이터'로 갱신하기 위해 필요합니다.)
     */
    public JLabel getAvgStressLabel() {
        return avgStressLabel;
    }

    /**
     * (Controller용) 메인 차트 패널을 'newPanel'로 교체하고 화면을 갱신(revalidate/repaint)합니다.
     * @param newPanel Controller가 '진짜 데이터'로 새로 생성한 차트 패널
     */
    public void setMainChartPanel(JPanel newPanel) {
        // 기존 'mainChartPanel'이 존재하면,
        if (mainChartPanel != null) {
            // 메인 창(StatisticsView)에서 기존 패널을 제거합니다.
            remove(mainChartPanel);
        }
        // 멤버 변수 'mainChartPanel'을 'newPanel'로 교체합니다.
        mainChartPanel = newPanel;
        // 메인 창(StatisticsView)의 "CENTER"(중앙)에 'newPanel'을 추가합니다.
        add(mainChartPanel, BorderLayout.CENTER);
        // 레이아웃을 새로 계산하도록 Swing에 알립니다.
        revalidate();
        // 화면을 다시 그리도록 Swing에 알립니다.
        repaint();
    }
}