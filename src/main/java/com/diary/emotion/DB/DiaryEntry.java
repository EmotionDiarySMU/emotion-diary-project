package com.diary.emotion.DB;

import java.sql.Timestamp;
import java.util.List;

// DB 테이블 구조에 맞게 필드 추가 및 수정
public class DiaryEntry {

    // 필드 정의 (DB 컬럼과 1:1 매칭)
    private int entry_id;       // int, Primary Key, auto_increment
    private String user_id;     // varchar(20)
    private String title;       // varchar(50)
    private String content;     // text
    private int stress_level;   // int
    private Timestamp entry_date;  // datetime (DB에서 가져온 날짜/시간 문자열)
    private Timestamp modify_date;  // datetime (DB에서 가져온 날짜/시간 문자열)
    private List<Emotion> emotions;

    public DiaryEntry() {
    }

    // 2. 데이터 삽입 시 사용할 생성자 (entry_id와 entry_date는 DB가 처리)
    // user_id, title, content, stress_level을 인자로 받음
    public DiaryEntry(String user_id, String title, String content, int stress_level) {
        this.user_id = user_id;
        this.title = title;
        this.content = content;
        this.stress_level = stress_level;
    }

    // --- Getter 및 Setter 메서드 ---

    // entry_id
    public int getEntry_id() {
        return entry_id;
    }

    public void setEntry_id(int entry_id) {
        this.entry_id = entry_id;
    }

    // user_id
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    // title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // stress_level
    public int getStress_level() {
        return stress_level;
    }

    public void setStress_level(int stress_level) {
        this.stress_level = stress_level;
    }

    // entry_date
    public Timestamp getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(Timestamp entry_date) {
        this.entry_date = entry_date;
    }

    // modify_date
    public Timestamp getModify_date() {
        return modify_date;
    }

    public void setModify_date(Timestamp modify_date) {
        this.modify_date = modify_date;
    }

    public List<Emotion> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<Emotion> emotions) {
        this.emotions = emotions;
    }
}