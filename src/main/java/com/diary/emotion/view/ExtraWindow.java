package com.diary.emotion.view;

import com.diary.emotion.db.DatabaseManager;
import com.diary.emotion.db.DiaryEntry;
import com.diary.emotion.main.MainView;
import com.diary.emotion.ui.UIConstants;
import static com.diary.emotion.ui.ViewStyleOverrider.*;

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

        setVisible(true);
    }

    private void setupEvents(DiaryEntry entry) {
        // [조회 창] 삭제 버튼
        viewPanel.deleteBtn.addActionListener(e -> deleteEntry(entry));

        // [수정 창] 취소 -> 조회 화면으로 복귀
        modifyPanel.cancelBtn.addActionListener(e -> {
            modifyPanel.fillEntry(entry); // 수정된 내용 버리고 초기화
            cardLayout.show(cardPanel, "VIEW");
            setTitle(UIConstants.APP_TITLE);

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
        // ModifyPanel에서 업데이트된 엔트리 가져오기
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

        // DB 업데이트
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