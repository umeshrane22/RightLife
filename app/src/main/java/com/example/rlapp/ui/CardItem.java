package com.example.rlapp.ui;

public class CardItem {
    private String title;
    private int imageResId;
    private String imageUrl;
    private String content;
    private String buttonText;
    private String category;
    private boolean isAffirmation = false;
    private String viewCount;

    public String getCategory() {
        return category;
    }

    public CardItem(String title, int imageResId, String imageUrl, String content, String buttonText, String category, String viewCount) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.imageResId = imageResId;
        this.buttonText = buttonText;
        this.content = content;
        this.category = category;

        this.viewCount = viewCount;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getContent() {
        return content;
    }

    public boolean isAffirmation() {
        return isAffirmation;
    }

    public void setAffirmation(boolean affirmation) {
        isAffirmation = affirmation;
    }

    public String getViewCount() {
        return viewCount;
    }
}
