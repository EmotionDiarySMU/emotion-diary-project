package com.diary.emotion.controller;

import com.diary.emotion.model.DiaryDAO;
import com.diary.emotion.view.EditDiaryView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 일기 수정 컨트롤러 (Controller 레이어)
 * EditDiaryView와 DiaryDAO를 연결하고 수정 로직을 처리합니다.
 */
public class EditDiaryController {

    private EditDiaryView view;
    private DiaryDAO dao;
    private UpdateCompleteCallback updateCompleteCallback;

    /**
     * 생성자
     */
    public EditDiaryController(EditDiaryView view) {
        this.view = view;
        this.dao = new DiaryDAO();

        initListeners();
    }

    /**
     * 버튼 리스너 초기화
     */
    private void initListeners() {
        // 수정 완료 버튼
        view.getUpdateButton().addActionListener(e -> updateDiary());

        // 취소 버튼
        view.getCancelButton().addActionListener(e -> {
            if (updateCompleteCallback != null) {
                updateCompleteCallback.onCancel();
            }
        });
    }

    /**
     * 일기 로드
     */
    public void loadDiary(int entryId) {
        DiaryDAO.DiaryEntry diary = dao.getDiaryById(entryId);

        if (diary == null) {
            JOptionPane.showMessageDialog(view,
                "일기를 찾을 수 없습니다.",
                "오류",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        view.loadDiary(diary);
    }

    /**
     * 일기 수정
     */
    private void updateDiary() {
        // 1. 입력값 검증
        String title = view.getTitle();
        String content = view.getContent();
        int stressLevel = view.getStressLevel();
        Map<String, Integer> emotions = view.getSelectedEmotions();
        int entryId = view.getCurrentEntryId();

        if (entryId == -1) {
            JOptionPane.showMessageDialog(view,
                "수정할 일기 정보가 없습니다.",
                "오류",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                "제목을 입력해주세요.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (emotions.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                "최소 1개 이상의 감정을 선택해주세요.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (emotions.size() > 4) {
            JOptionPane.showMessageDialog(view,
                "감정은 최대 4개까지만 선택할 수 있습니다.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. EmotionData 배열 생성
        List<DiaryDAO.EmotionData> emotionList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : emotions.entrySet()) {
            emotionList.add(new DiaryDAO.EmotionData(entry.getKey(), entry.getValue()));
        }
        DiaryDAO.EmotionData[] emotionArray = emotionList.toArray(new DiaryDAO.EmotionData[0]);

        // 3. 데이터베이스에 저장
        boolean success = dao.updateDiaryWithEmotions(entryId, title, content, stressLevel, emotionArray);

        // 4. 결과 처리
        if (success) {
            JOptionPane.showMessageDialog(view,
                "일기가 수정되었습니다.",
                "수정 완료",
                JOptionPane.INFORMATION_MESSAGE);

            // 콜백 호출
            if (updateCompleteCallback != null) {
                updateCompleteCallback.onUpdateComplete();
            }
        } else {
            JOptionPane.showMessageDialog(view,
                "일기 수정에 실패했습니다.",
                "저장 오류",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // 콜백 인터페이스
    public interface UpdateCompleteCallback {
        void onUpdateComplete();
        void onCancel();
    }

    // 콜백 setter
    public void setUpdateCompleteCallback(UpdateCompleteCallback callback) {
        this.updateCompleteCallback = callback;
    }
}

