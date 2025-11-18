package com.diary.emotion.view;

import com.diary.emotion.model.DiaryDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * 일기 목록 화면 (View 레이어)
 * 사용자의 일기 목록을 테이블로 표시하고 검색, 정렬, 수정, 삭제 기능을 제공합니다.
 */
public class ViewDiaryListView extends JPanel {

    // UI 컴포넌트
    private JTextField searchField;
    private JButton searchButton;
    private JButton resetButton;
    private JComboBox<String> sortComboBox;
    private JTable diaryTable;
    private DefaultTableModel tableModel;
    private JButton viewButton;
    private JButton editButton;
    private JButton deleteButton;

    // 콜백 인터페이스
    private SearchCallback searchCallback;
    private ViewCallback viewCallback;
    private EditCallback editCallback;
    private DeleteCallback deleteCallback;
    private SortCallback sortCallback;

    // 색상 상수
    private static final Color PASTEL_BLUE = new Color(174, 214, 241);
    private static final Color PASTEL_GREEN = new Color(169, 223, 191);
    private static final Color PASTEL_YELLOW = new Color(253, 254, 196);

    /**
     * 생성자
     */
    public ViewDiaryListView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(PASTEL_BLUE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
    }

    /**
     * UI 컴포넌트 초기화
     */
    private void initComponents() {
        // 상단 검색 패널
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);

        // 중앙 테이블 패널
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 검색 패널 생성
     */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(PASTEL_BLUE);

        // 검색 라벨
        JLabel searchLabel = new JLabel("제목 검색:");
        searchLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        // 검색 입력 필드
        searchField = new JTextField(20);
        searchField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        // 검색 버튼
        searchButton = new JButton("검색");
        searchButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        searchButton.setBackground(PASTEL_GREEN);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> {
            if (searchCallback != null) {
                String keyword = searchField.getText().trim();
                searchCallback.onSearch(keyword);
            }
        });

        // 초기화 버튼
        resetButton = new JButton("전체보기");
        resetButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        resetButton.setBackground(PASTEL_YELLOW);
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> {
            searchField.setText("");
            if (searchCallback != null) {
                searchCallback.onSearch("");
            }
        });

        // 정렬 라벨
        JLabel sortLabel = new JLabel("정렬:");
        sortLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        // 정렬 콤보박스
        sortComboBox = new JComboBox<>(new String[]{"최신순", "오래된순"});
        sortComboBox.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        sortComboBox.addActionListener(e -> {
            if (sortCallback != null) {
                String order = sortComboBox.getSelectedItem().toString();
                sortCallback.onSort(order.equals("최신순"));
            }
        });

        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(resetButton);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(sortLabel);
        panel.add(sortComboBox);

        return panel;
    }

    /**
     * 테이블 패널 생성
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PASTEL_BLUE);

        // 테이블 모델 생성
        String[] columnNames = {"번호", "날짜", "제목", "스트레스", "감정"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 셀 편집 불가
            }
        };

        // 테이블 생성
        diaryTable = new JTable(tableModel);
        diaryTable.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        diaryTable.setRowHeight(30);
        diaryTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14));
        diaryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 컬럼 너비 조정
        diaryTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // 번호
        diaryTable.getColumnModel().getColumn(1).setPreferredWidth(150); // 날짜
        diaryTable.getColumnModel().getColumn(2).setPreferredWidth(200); // 제목
        diaryTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // 스트레스
        diaryTable.getColumnModel().getColumn(4).setPreferredWidth(150); // 감정

        // 스크롤 패널
        JScrollPane scrollPane = new JScrollPane(diaryTable);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 버튼 패널 생성
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(PASTEL_BLUE);

        // 보기 버튼
        viewButton = new JButton("상세보기");
        viewButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        viewButton.setBackground(new Color(174, 214, 241));
        viewButton.setFocusPainted(false);
        viewButton.setPreferredSize(new Dimension(120, 40));
        viewButton.addActionListener(e -> {
            int selectedRow = diaryTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "일기를 선택해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (viewCallback != null) {
                int entryId = (int) tableModel.getValueAt(selectedRow, 0);
                viewCallback.onView(entryId);
            }
        });

        // 수정 버튼
        editButton = new JButton("수정");
        editButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        editButton.setBackground(PASTEL_GREEN);
        editButton.setFocusPainted(false);
        editButton.setPreferredSize(new Dimension(120, 40));
        editButton.addActionListener(e -> {
            int selectedRow = diaryTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "수정할 일기를 선택해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (editCallback != null) {
                int entryId = (int) tableModel.getValueAt(selectedRow, 0);
                editCallback.onEdit(entryId);
            }
        });

        // 삭제 버튼
        deleteButton = new JButton("삭제");
        deleteButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        deleteButton.setBackground(new Color(255, 179, 186));
        deleteButton.setFocusPainted(false);
        deleteButton.setPreferredSize(new Dimension(120, 40));
        deleteButton.addActionListener(e -> {
            int selectedRow = diaryTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "삭제할 일기를 선택해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int result = JOptionPane.showConfirmDialog(this,
                "정말 삭제하시겠습니까?",
                "삭제 확인",
                JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION && deleteCallback != null) {
                int entryId = (int) tableModel.getValueAt(selectedRow, 0);
                deleteCallback.onDelete(entryId);
            }
        });

        panel.add(viewButton);
        panel.add(editButton);
        panel.add(deleteButton);

        return panel;
    }

    /**
     * 일기 목록 업데이트
     */
    public void updateDiaryList(DiaryDAO.DiaryEntry[] diaries) {
        // 테이블 초기화
        tableModel.setRowCount(0);

        // 데이터 추가
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (DiaryDAO.DiaryEntry diary : diaries) {
            // 감정 이모지 문자열 생성
            StringBuilder emotionStr = new StringBuilder();
            if (diary.emotions != null) {
                for (DiaryDAO.EmotionData emotion : diary.emotions) {
                    emotionStr.append(emotion.emoji).append(" ");
                }
            }

            Object[] row = {
                diary.entryId,
                diary.entryDate.format(formatter),
                diary.title,
                diary.stressLevel,
                emotionStr.toString().trim()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * 검색 결과가 없을 때 메시지 표시
     */
    public void showNoResults() {
        tableModel.setRowCount(0);
        JOptionPane.showMessageDialog(this, "검색 결과가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
    }

    // 콜백 인터페이스 정의
    public interface SearchCallback {
        void onSearch(String keyword);
    }

    public interface ViewCallback {
        void onView(int entryId);
    }

    public interface EditCallback {
        void onEdit(int entryId);
    }

    public interface DeleteCallback {
        void onDelete(int entryId);
    }

    public interface SortCallback {
        void onSort(boolean descending);
    }

    // 콜백 setter 메소드
    public void setSearchCallback(SearchCallback callback) {
        this.searchCallback = callback;
    }

    public void setViewCallback(ViewCallback callback) {
        this.viewCallback = callback;
    }

    public void setEditCallback(EditCallback callback) {
        this.editCallback = callback;
    }

    public void setDeleteCallback(DeleteCallback callback) {
        this.deleteCallback = callback;
    }

    public void setSortCallback(SortCallback callback) {
        this.sortCallback = callback;
    }
}

