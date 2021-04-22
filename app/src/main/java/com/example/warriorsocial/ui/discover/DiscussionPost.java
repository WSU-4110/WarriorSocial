package com.example.warriorsocial.ui.discover;

public class DiscussionPost {
    private String categoryName;
    private String postText;
    private String postTitle;
    private String posterId;


    public DiscussionPost() {}

    public DiscussionPost(String categoryName, String postText, String postTitle, String posterId) {
        this.categoryName = categoryName;
        this.postText = postText;
        this.postTitle = postTitle;
        this.posterId = posterId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getPostText() {
        return postText;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPosterId() { return posterId; }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }
}
