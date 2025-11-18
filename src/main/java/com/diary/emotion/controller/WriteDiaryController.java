package com.diary.emotion.controller;

import com.diary.emotion.view.WriteDiaryView;
import com.diary.emotion.model.DiaryDAO;
import com.diary.emotion.model.DiaryDAO.EmotionData;
import com.diary.emotion.util.Session;

import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 일기 쓰기 Controller 클래스
 * WriteDiaryView와 DiaryDAO를 연결하여 일기 작성 로직을 처리합니다.
 *
 * 주요 기능:
 * - 저장 버튼 클릭 처리
 * - 입력값 검증 (제목, 내용, 감정 등)
 * - 감정 4개 제한 확인
 * - DiaryDAO 호출하여 DB 저장
 */
public class WriteDiaryController {

    private WriteDiaryView view;
    private DiaryDAO dao;
    private Runnable onSaveSuccess;
    private Runnable onCancel;

    /**
     * WriteDiaryController 생성자
     * View와 DAO를 연결하고 이벤트 리스너를 설정합니다.
     *
     * @param view WriteDiaryView 객체
     * @param dao DiaryDAO 객체
     */
    public WriteDiaryController(WriteDiaryView view, DiaryDAO dao) {
        this.view = view;
        this.dao = dao;

        addListeners();
    }

    /**
     * View의 버튼들에 이벤트 리스너를 추가합니다.
     */
    private void addListeners() {
        // 저장 버튼 클릭 시
        view.getSaveButton().addActionListener(e -> handleSave());

        // 취소 버튼 클릭 시
        view.getCancelButton().addActionListener(e -> handleCancel());
    }

    /**
     * 저장 버튼 클릭 시 호출되는 메소드
     * 입력값을 검증하고 DB에 저장합니다.
     */
    private void handleSave() {
        // 1. 현재 로그인한 사용자 확인
        String userId = Session.getCurrentUserId();
        if (userId == null) {
            JOptionPane.showMessageDialog(view,
                "로그인이 필요합니다.",
                "오류",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. 입력값 가져오기
        String title = view.getTitle();
        String content = view.getContent();
        int stressLevel = view.getStressLevel();
        Map<String, Integer> selectedEmotions = view.getSelectedEmotions();

        // 3. 입력값 검증: 제목
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                "제목을 입력해주세요.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 4. 입력값 검증: 제목 길이 (최대 50자)
        if (title.length() > 50) {
            JOptionPane.showMessageDialog(view,
                "제목은 최대 50자까지 입력할 수 있습니다.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 5. 입력값 검증: 내용 (선택사항이지만 권장)
        if (content.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(view,
                "내용이 비어있습니다. 계속하시겠습니까?",
                "확인",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // 6. 입력값 검증: 감정 (최소 1개, 최대 4개)
        if (selectedEmotions.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                "최소 1개의 감정을 선택해주세요.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedEmotions.size() > 4) {
            JOptionPane.showMessageDialog(view,
                "감정은 최대 4개까지 선택할 수 있습니다.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 7. 감정 데이터 배열 생성
        EmotionData[] emotions = selectedEmotions.entrySet().stream()
            .map(entry -> new EmotionData(entry.getKey(), entry.getValue()))
            .toArray(EmotionData[]::new);

        // 8. 현재 시간으로 작성 날짜 설정
        LocalDateTime entryDate = LocalDateTime.now();

        // 9. DB에 저장 (트랜잭션)
        boolean success = dao.saveDiaryWithEmotions(
            userId, title, content, stressLevel, entryDate, emotions
        );

        if (success) {
            // 10. 저장 성공
            JOptionPane.showMessageDialog(view,
                "일기가 저장되었습니다! 😊",
                "저장 완료",
                JOptionPane.INFORMATION_MESSAGE);

            // 입력 필드 초기화
            view.clearAll();

            // 콜백 실행 (메인 화면으로 이동 등)
            if (onSaveSuccess != null) {
                onSaveSuccess.run();
            }
        } else {
            // 11. 저장 실패
            JOptionPane.showMessageDialog(view,
                "일기 저장 중 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.",
                "저장 실패",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 취소 버튼 클릭 시 호출되는 메소드
     */
    private void handleCancel() {
        // 입력된 내용이 있는지 확인
        String title = view.getTitle();
        String content = view.getContent();

        if (!title.isEmpty() || !content.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(view,
                "작성 중인 내용이 있습니다.\n정말 취소하시겠습니까?",
                "확인",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // 입력 필드 초기화
        view.clearAll();

        // 콜백 실행
        if (onCancel != null) {
            onCancel.run();
        }
    }

    /**
     * 저장 성공 시 실행할 콜백을 설정합니다.
     *
     * @param callback 실행할 콜백 (Runnable)
     */
    public void setOnSaveSuccess(Runnable callback) {
        this.onSaveSuccess = callback;
    }

    /**
     * 취소 버튼 클릭 시 실행할 콜백을 설정합니다.
     *
     * @param callback 실행할 콜백 (Runnable)
     */
    public void setOnCancel(Runnable callback) {
        this.onCancel = callback;
    }
}

