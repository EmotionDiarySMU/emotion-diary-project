package com.diary.emotion.view;

import com.diary.emotion.db.DatabaseManager;
import com.diary.emotion.db.DiaryEntry;
import com.diary.emotion.main.MainView;
import com.diary.emotion.ui.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * 일기 조회 및 수정을 위한 팝업 윈도우
 * CardLayout을 사용하여 ViewPanel과 ModifyPanel을 전환
 */
public class ExtraWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    public ViewDiaryPanel viewPanel;
    public ModifyPanel modifyPanel;
    public int entryId;

    CardLayout cardLayout;
    JPanel cardPanel;

    public ExtraWindow(DiaryEntry entry) {
        this.entryId = entry.getEntry_id();
        UIConstants.setupFrame(this); // 기본 크기(495x630) 설정

        // 창 타이틀 설정 (작성일과 수정일 포함)
        String title = UIConstants.APP_TITLE;
        if (entry.getEntry_date() != null) {
            String dateStr = entry.getEntry_date().toLocalDateTime().toLocalDate().toString();
            title += " - " + dateStr;

            // 수정일이 있으면 추가
            if (entry.getModify_date() != null) {
                String modifyDateStr = entry.getModify_date().toLocalDateTime().toLocalDate().toString();
                title += " (수정일: " + modifyDateStr + ")";
            }
        }
        setTitle(title);

        // 요구사항 4: ViewDiaryPanel 창 닫을 때 프로그램 종료 방지
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // CardLayout 설정
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 패널 초기화 (새로 정의한 클래스 사용)
        viewPanel = new ViewDiaryPanel();
        modifyPanel = new ModifyPanel();

        // 데이터 채우기
        viewPanel.fillEntry(entry);
        modifyPanel.fillEntry(entry);

        // 카드 추가
        cardPanel.add(viewPanel, "VIEW");
        cardPanel.add(modifyPanel, "EDIT");

        add(cardPanel);

        // 이벤트 설정
        setupEvents(entry);

        // 메인 창과 겹치지 않게 위치 설정
        positionWindowNextToMain();

        setVisible(true);
    }

    /**
     * 메인 창 옆에 ExtraWindow를 배치
     */
    private void positionWindowNextToMain() {
        // 모든 Frame 중에서 MainView 찾기
        for (Frame frame : Frame.getFrames()) {
            if (frame instanceof MainView && frame.isVisible()) {
                Point mainLocation = frame.getLocation();
                Dimension mainSize = frame.getSize();

                // 메인 창의 오른쪽에 배치
                int x = mainLocation.x + mainSize.width + 10; // 10px 간격
                int y = mainLocation.y;

                // 화면 경계를 벗어나는지 확인
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                if (x + getWidth() > screenSize.width) {
                    // 오른쪽에 공간이 없으면 왼쪽에 배치
                    x = mainLocation.x - getWidth() - 10;
                    if (x < 0) {
                        // 왼쪽에도 공간이 없으면 메인 창 아래에 배치
                        x = mainLocation.x;
                        y = mainLocation.y + mainSize.height + 10;
                    }
                }

                setLocation(x, y);
                return;
            }
        }

        // MainView를 찾지 못한 경우 화면 중앙에 배치
        setLocationRelativeTo(null);
    }

    private void setupEvents(DiaryEntry entry) {
        // [조회 창] 삭제 버튼
        viewPanel.deleteBtn.addActionListener(e -> deleteEntry(entry));

        // [수정 창] 취소 -> 조회 화면으로 복귀
        modifyPanel.cancelBtn.addActionListener(e -> {
            modifyPanel.fillEntry(entry); // 수정된 내용 버리고 초기화
            cardLayout.show(cardPanel, "VIEW");

            // 타이틀 복원 (작성일과 수정일 포함)
            String title = UIConstants.APP_TITLE;
            if (entry.getEntry_date() != null) {
                String dateStr = entry.getEntry_date().toLocalDateTime().toLocalDate().toString();
                title += " - " + dateStr;

                if (entry.getModify_date() != null) {
                    String modifyDateStr = entry.getModify_date().toLocalDateTime().toLocalDate().toString();
                    title += " (수정일: " + modifyDateStr + ")";
                }
            }
            setTitle(title);

            viewPanel.revalidate();
            viewPanel.repaint();
        });

        // [수정 창] 완료 -> DB 업데이트
        modifyPanel.fineditBtn.addActionListener(e -> updateEntry(entry));

        // 창 닫힘 시 리소스 정리
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // 필요한 경우 MainView 리스트 갱신 등 처리
            }
        });
    }

    private void deleteEntry(DiaryEntry entry) {
        int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (DatabaseManager.deleteEntry(entry.getEntry_id())) {
                JOptionPane.showMessageDialog(this, "삭제되었습니다.");
                refreshMainView();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "삭제 실패");
            }
        }
    }

    private void updateEntry(DiaryEntry entry) {
        // 1. 스트레스 수치 검증
        if (!modifyPanel.validateAndSaveStressValue()) {
            return; // 검증 실패 시 저장 중단
        }

        // 2. 감정 수치 검증 (모든 슬롯)
        for (int i = 0; i < 4; i++) {
            String icon = modifyPanel.iconLabels[i].getText();
            if (!icon.equals("+")) { // 감정이 선택된 경우만 검증
                if (!modifyPanel.validateAndSaveEmotionValue(i)) {
                    return; // 검증 실패 시 저�� 중단
                }
            }
        }

        // 3. ModifyPanel에서 업데이트된 엔트리 가져오기
        DiaryEntry updatedEntry = modifyPanel.getUpdatedEntry();

        if (updatedEntry == null) {
            JOptionPane.showMessageDialog(this, "수정할 데이터가 없습니다.");
            return;
        }

        String title = updatedEntry.getTitle();
        String content = updatedEntry.getContent();

        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "제목과 내용을 모두 입력해주세요.");
            return;
        }

        // 4. DB 업데이트
        boolean success = DatabaseManager.updateEntry(updatedEntry);

        if (success) {
            JOptionPane.showMessageDialog(this, "수정되었습니다.");
            refreshMainView();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "수정 실패");
        }
    }

    private void refreshMainView() {
        if (MainView.searchDiaryPanel != null) {
            MainView.searchDiaryPanel.refreshList();
        }
    }
}