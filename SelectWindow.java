import javax.swing.*;

public class SelectWindow extends JMenuBar { // ⭐️ JMenuBar 상속
    private static final long serialVersionUID = 1L;
    private JMenu menuFile, menuView, menuStats;
    private JMenuItem itemWrite, itemViewAll;

    public SelectWindow() {
        menuFile = new JMenu("일기 (File)");
        menuView = new JMenu("열람 (View)");
        menuStats = new JMenu("통계 (Stats)");
        
        itemWrite = new JMenuItem("새 일기 쓰기");
        itemViewAll = new JMenuItem("모든 일기 보기");
        menuFile.add(itemWrite);
        menuFile.add(itemViewAll);
        
        add(menuFile);
        add(menuView);
        add(menuStats);
    }

    // ⭐️ WriteDiaryGUI가 리스너를 붙일 수 있도록 getter 제공
    public JMenuItem getWriteItem() {
        return itemWrite;
    }

    public JMenuItem getViewItem() {
        return itemViewAll;
    }
}