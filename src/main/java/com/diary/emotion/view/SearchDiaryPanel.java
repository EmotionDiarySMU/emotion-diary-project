package com.diary.emotion.view;

import com.diary.emotion.db.DatabaseManager;
import com.diary.emotion.db.DiaryEntry;
import com.diary.emotion.ui.ButtonFactory;
import com.diary.emotion.ui.ScrollBarStyler;
import com.diary.emotion.ui.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static com.diary.emotion.ui.UIColors.BG_LIGHT_CREAM;
import static com.diary.emotion.ui.UIColors.BG_VIEW;
import static com.diary.emotion.ui.UIFonts.BODY_REGULAR;

public class SearchDiaryPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public static Set<ExtraWindow> openWindows = new HashSet<>();
    public static DefaultTableModel diaryModel;
    public static List<DiaryEntry> diaryEntries = new ArrayList<>();

    // 컴포넌트
    static JTextField titleFd;
    static JButton serchBt;
    static JButton latestBtn;
    static JButton oldestBtn;

    static DateSelectorPanel firstDS;
    static DateSelectorPanel secondDS;

    private JTable diaries;

    public SearchDiaryPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_VIEW);

        // 🟢 1. 상단 검색 패널
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(BG_VIEW);
        // 여백 설정
        topPanel.setBorder(new EmptyBorder(15, 30, 10, 30));

        // ★ 폰트 설정 (기본 14px -> 16px Bold)
        // BODY_REGULAR가 14px이므로 +2하여 16px로 설정
        Font labelFont = new Font(BODY_REGULAR.getName(), Font.BOLD, 15);

        // [1-1] 제목 검색 행
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        titleRow.setBackground(BG_VIEW);

        JLabel titleLb = new JLabel("제목: ");
        titleLb.setFont(labelFont); // ★ 폰트 적용
        titleLb.setPreferredSize(new Dimension(50, 30));

        titleFd = StyleUtils.createStyledTextField();
        titleFd.setPreferredSize(new Dimension(380, 30));
        titleFd.setBackground(BG_LIGHT_CREAM);
        titleFd.setBorder(new LineBorder(Color.BLACK, 1));

        titleRow.add(titleLb);
        titleRow.add(titleFd);

        // [1-2] 날짜 검색 영역

        // 첫 번째 줄: 시작 날짜
        JPanel dateRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        dateRow1.setBackground(BG_VIEW);

        JLabel startLb = new JLabel("기간: ");
        startLb.setFont(labelFont); // ★ 폰트 적용
        startLb.setPreferredSize(new Dimension(50, 26));

        firstDS = new DateSelectorPanel();
        firstDS.applyFlatComboStyleWithoutColor(firstDS.getYearCombo());
        firstDS.applyFlatComboStyleWithoutColor(firstDS.getMonthCombo());
        firstDS.applyFlatComboStyleWithoutColor(firstDS.getDayCombo());

        dateRow1.add(startLb);
        dateRow1.add(firstDS);

        // 두 번째 줄: 종료 날짜 (오른쪽 정렬)
        JPanel dateRow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 2));
        dateRow2.setBackground(BG_VIEW);

        // 줄 맞춤용 투명 라벨
        JLabel spacerLb = new JLabel("");
        spacerLb.setPreferredSize(new Dimension(50, 26));

        JLabel waveLb = new JLabel(" ~ ");
        waveLb.setFont(BODY_REGULAR); // 물결은 너무 튀지 않게 기본 폰트 유지 (원하시면 labelFont로 변경 가능)

        secondDS = new DateSelectorPanel();
        secondDS.applyFlatComboStyleWithoutColor(secondDS.getYearCombo());
        secondDS.applyFlatComboStyleWithoutColor(secondDS.getMonthCombo());
        secondDS.applyFlatComboStyleWithoutColor(secondDS.getDayCombo());

        dateRow2.add(spacerLb); // 공간만 차지
        dateRow2.add(waveLb);
        dateRow2.add(secondDS);

        // [1-3] 검색 버튼
        JPanel searchBtnRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchBtnRow.setBackground(BG_VIEW);
        searchBtnRow.setBorder(new EmptyBorder(5, 0, 0, 0));

        serchBt = ButtonFactory.createCustomButton("검 색", Color.WHITE, Color.BLACK, 60, 28);
        searchBtnRow.add(serchBt);

        // [1-4] 정렬 버튼
        JPanel sortBtnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        sortBtnRow.setBackground(BG_VIEW);

        latestBtn = ButtonFactory.createCustomButton("최신순", Color.WHITE, Color.BLACK, 75, 28);
        oldestBtn = ButtonFactory.createCustomButton("오래된순", Color.WHITE, Color.BLACK, 75, 28);

        JPanel gap = new JPanel();
        gap.setPreferredSize(new Dimension(5, 0));
        gap.setBackground(BG_VIEW);

        sortBtnRow.add(latestBtn);
        sortBtnRow.add(gap);
        sortBtnRow.add(oldestBtn);

        // 상단 패널 추가
        topPanel.add(titleRow);
        topPanel.add(dateRow1);
        topPanel.add(dateRow2);
        topPanel.add(searchBtnRow);
        topPanel.add(sortBtnRow);

        add(topPanel, BorderLayout.NORTH);

        // 🟢 2. 중앙 일기 목록 (표)
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(BG_VIEW);
        tableWrapper.setBorder(new EmptyBorder(0, 30, 30, 30));

        String[] colNames = {"내용"};
        diaryModel = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        diaries = new JTable(diaryModel);
        diaries.setFont(BODY_REGULAR);
        diaries.setRowHeight(30);
        diaries.setTableHeader(null);

        diaries.setBackground(BG_LIGHT_CREAM);
        diaries.setGridColor(Color.BLACK);
        diaries.setShowHorizontalLines(true);
        diaries.setShowVerticalLines(false);
        diaries.setIntercellSpacing(new Dimension(0, 1));

        // 모든 행 동일한 배경색 적용 (줄무늬 제거)
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // 모든 행을 동일한 배경색으로 설정
                if (!isSelected) {
                    c.setBackground(BG_LIGHT_CREAM); // 모든 행 동일한 색상
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
                return c;
            }
        };
        renderer.setBorder(new EmptyBorder(0, 15, 0, 0));
        diaries.getColumnModel().getColumn(0).setCellRenderer(renderer);

        JScrollPane scrollPane = new JScrollPane(diaries);
        scrollPane.getViewport().setBackground(BG_LIGHT_CREAM);
        scrollPane.setBorder(new LineBorder(Color.BLACK, 1));
        // UI 패키지의 ScrollBarStyler 사용
        ScrollBarStyler.applyViewStyle(scrollPane);

        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        add(tableWrapper, BorderLayout.CENTER);

        // 🟢 3. 이벤트 리스너
        serchBt.addActionListener(e -> refreshDiaryModel(true));

        latestBtn.addActionListener(e -> {
            diaryEntries.sort(Comparator.comparing(DiaryEntry::getEntry_date).reversed());
            refreshDiaryModel(false);
        });

        oldestBtn.addActionListener(e -> {
            diaryEntries.sort(Comparator.comparing(DiaryEntry::getEntry_date));
            refreshDiaryModel(false);
        });

        diaries.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = diaries.getSelectedRow();
                    if (row != -1 && row < diaryEntries.size()) {
                        DiaryEntry entry = diaryEntries.get(row);
                        ExtraWindow ew = new ExtraWindow(entry);
                        ew.setVisible(true);
                    }
                }
            }
        });

        refreshDiaryModel(true);
    }


    public static Timestamp getStartTimestamp() {
        int year = firstDS.getYear();
        int month = firstDS.getMonth();
        int day = firstDS.getDay();
        // ⭐️ 날짜 미선택 시 과거 날짜로 설정 (1900-01-01)
        if (year == -1) return Timestamp.valueOf(LocalDate.of(1900, 1, 1).atStartOfDay());
        if (month == -1) month = 1;
        if (day == -1) day = 1;
        return Timestamp.valueOf(LocalDate.of(year, month, day).atStartOfDay());
    }

    public static Timestamp getEndTimestamp() {
        int year = secondDS.getYear();
        int month = secondDS.getMonth();
        int day = secondDS.getDay();
        // ⭐️ 날짜 미선택 시 미래 날짜로 설정 (2099-12-31)
        if (year == -1) return Timestamp.valueOf(LocalDate.of(2099, 12, 31).atTime(23, 59, 59));
        if (month == -1) month = 12;
        if (day == -1) day = YearMonth.of(year, month).lengthOfMonth();
        return Timestamp.valueOf(LocalDate.of(year, month, day).atTime(23, 59, 59));
    }

    public void refreshDiaryModel(boolean fromDB) {
        if (fromDB) {
            String keyword = titleFd.getText().trim();
            Timestamp start = getStartTimestamp();
            Timestamp end = getEndTimestamp();
            diaryEntries = DatabaseManager.searchEntries(keyword, start, end);
            diaryEntries.sort(Comparator.comparing(DiaryEntry::getEntry_date).reversed());
        }

        diaryModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // ⭐️ 최대 50개까지만 표시
        int maxDisplay = Math.min(diaryEntries.size(), 50);
        for (int i = 0; i < maxDisplay; i++) {
            DiaryEntry entry = diaryEntries.get(i);
            LocalDate date = entry.getEntry_date().toLocalDateTime().toLocalDate();
            String displayText = String.format("[%s]  %s", date.format(formatter), entry.getTitle());

            // 수정일이 있으면 표시
            if (entry.getModify_date() != null) {
                LocalDate modifyDate = entry.getModify_date().toLocalDateTime().toLocalDate();
                displayText += String.format(" (수정일: %s)", modifyDate.format(formatter));
            }

            diaryModel.addRow(new Object[]{displayText});
        }

        // 줄무늬가 화면 전체를 채우도록 충분한 빈 행 추가 (항상 50줄 유지)
        int minRows = 50;
        int currentRows = diaryModel.getRowCount();
        if (currentRows < minRows) {
            for (int i = 0; i < minRows - currentRows; i++) {
                diaryModel.addRow(new Object[]{""});
            }
        }
    }

    // refreshList 메서드 - 일기 저장 후 목록 새로고침
    public void refreshList() {
        refreshDiaryModel(true); // 데이터베이스에서 다시 조회
    }
}

