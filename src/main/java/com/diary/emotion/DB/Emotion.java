package com.diary.emotion.DB;

public class Emotion{
	
    private int emotion_level;
    private String emoji_icon;

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

