package com.diary.emotion;

import java.util.List;

public class DiaryEntry {

    private int entry_id;
    private String user_id;
    private String title;
    private String content;
    private int stress_level;
    private String entry_date;
    private List<Emotion> emotions;

    public DiaryEntry() {
    }

    public DiaryEntry(String user_id, String title, String content, int stress_level) {
        this.user_id = user_id;
        this.title = title;
        this.content = content;
        this.stress_level = stress_level;
    }

    public int getEntry_id() {
        return entry_id;
    }

    public void setEntry_id(int entry_id) {
        this.entry_id = entry_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStress_level() {
        return stress_level;
    }

    public void setStress_level(int stress_level) {
        this.stress_level = stress_level;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public List<Emotion> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<Emotion> emotions) {
        this.emotions = emotions;
    }
}