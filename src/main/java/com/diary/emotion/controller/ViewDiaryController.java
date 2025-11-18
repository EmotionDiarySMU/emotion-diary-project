package com.diary.emotion.controller;

import com.diary.emotion.model.DiaryDAO;
import com.diary.emotion.view.ViewDiaryListView;

import javax.swing.*;

/**
 * 일기 목록 컨트롤러 (Controller 레이어)
 * ViewDiaryListView와 DiaryDAO를 연결하고 비즈니스 로직을 처리합니다.
 */
public class ViewDiaryController {

    private ViewDiaryListView view;
    private DiaryDAO dao;
    private String userId;
    private DiaryDAO.DiaryEntry[] currentDiaries;
    private boolean isDescending = true; // 기본값: 최신순

    // 콜백
    private EditDiaryCallback editCallback;
    private ViewDetailCallback viewDetailCallback;

    /**
     * 생성자
     */
    public ViewDiaryController(ViewDiaryListView view, String userId) {
        this.view = view;
        this.userId = userId;
        this.dao = new DiaryDAO();

        initCallbacks();
        loadAllDiaries();
    }

    /**
     * View의 콜백 초기화
     */
    private void initCallbacks() {
        // 검색 콜백
        view.setSearchCallback(keyword -> {
            if (keyword.isEmpty()) {
                loadAllDiaries();
            } else {
                searchByTitle(keyword);
            }
        });

        // 상세보기 콜백
        view.setViewCallback(entryId -> {
            if (viewDetailCallback != null) {
                viewDetailCallback.onViewDetail(entryId);
            } else {
                // 기본 동작: 간단한 다이얼로그로 표시
                showDiaryDetail(entryId);
            }
        });

        // 수정 콜백
        view.setEditCallback(entryId -> {
            if (editCallback != null) {
                editCallback.onEdit(entryId);
            }
        });

        // 삭제 콜백
        view.setDeleteCallback(entryId -> {
            deleteDiary(entryId);
        });

        // 정렬 콜백
        view.setSortCallback(descending -> {
            isDescending = descending;
            sortDiaries();
        });
    }

    /**
     * 모든 일기 로드
     */
    public void loadAllDiaries() {
        currentDiaries = dao.getDiariesByUserId(userId);
        sortDiaries();
        view.updateDiaryList(currentDiaries);
    }

    /**
     * 제목으로 검색
     */
    private void searchByTitle(String keyword) {
        currentDiaries = dao.searchByTitle(userId, keyword);
        sortDiaries();

        if (currentDiaries.length == 0) {
            view.showNoResults();
        } else {
            view.updateDiaryList(currentDiaries);
        }
    }

    /**
     * 일기 정렬
     */
    private void sortDiaries() {
        if (currentDiaries == null || currentDiaries.length == 0) {
            return;
        }

        // 날짜 기준 정렬
        java.util.Arrays.sort(currentDiaries, (d1, d2) -> {
            if (isDescending) {
                return d2.entryDate.compareTo(d1.entryDate); // 최신순
            } else {
                return d1.entryDate.compareTo(d2.entryDate); // 오래된순
            }
        });

        view.updateDiaryList(currentDiaries);
    }

    /**
     * 일기 삭제
     */
    private void deleteDiary(int entryId) {
        boolean success = dao.deleteDiary(entryId);

        if (success) {
            JOptionPane.showMessageDialog(view,
                "일기가 삭제되었습니다.",
                "삭제 완료",
                JOptionPane.INFORMATION_MESSAGE);
            loadAllDiaries(); // 목록 갱신
        } else {
            JOptionPane.showMessageDialog(view,
                "일기 삭제에 실패했습니다.",
                "오류",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 일기 상세보기 (기본 동작)
     */
    private void showDiaryDetail(int entryId) {
        DiaryDAO.DiaryEntry diary = dao.getDiaryById(entryId);

        if (diary == null) {
            JOptionPane.showMessageDialog(view,
                "일기를 찾을 수 없습니다.",
                "오류",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 감정 정보 문자열 생성
        StringBuilder emotionInfo = new StringBuilder("감정: ");
        if (diary.emotions != null && diary.emotions.length > 0) {
            for (DiaryDAO.EmotionData emotion : diary.emotions) {
                emotionInfo.append(emotion.emoji)
                          .append("(")
                          .append(emotion.level)
                          .append(") ");
            }
        } else {
            emotionInfo.append("없음");
        }

        // 상세 정보 표시
        String message = String.format(
            "제목: %s\n\n내용:\n%s\n\n스트레스: %d\n%s\n작성일: %s",
            diary.title,
            diary.content,
            diary.stressLevel,
            emotionInfo.toString(),
            diary.entryDate
        );

        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("맑은 고딕", java.awt.Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));

        JOptionPane.showMessageDialog(view,
            scrollPane,
            "일기 상세보기",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // 콜백 인터페이스
    public interface EditDiaryCallback {
        void onEdit(int entryId);
    }

    public interface ViewDetailCallback {
        void onViewDetail(int entryId);
    }

    // 콜백 setter
    public void setEditCallback(EditDiaryCallback callback) {
        this.editCallback = callback;
    }

    public void setViewDetailCallback(ViewDetailCallback callback) {
        this.viewDetailCallback = callback;
    }
}

