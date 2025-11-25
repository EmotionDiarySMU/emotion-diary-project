package com.diary.emotion.db;

public class Emotion{
	
    private int emotion_level;
    private String emoji_icon;

    // 기본 생성자 추가
    public Emotion() {
        // 기본값 설정
        this.emoji_icon = "";
        this.emotion_level = 0;
    }

    // 생성자 추가
    public Emotion(String emoji_icon, int emotion_level) {
        this.emoji_icon = emoji_icon;
        this.emotion_level = emotion_level;
    }

    public int getEmotion_level() {
    	return emotion_level;
    }
    
    public void setEmotion_level(int emotion_level) {
    	this.emotion_level = emotion_level;
    }

    public String getEmoji_icon() {
    	return emoji_icon;
    }
    
    public void setEmoji_icon(String emoji_icon) {
    	this.emoji_icon = emoji_icon;
    }
    
}
