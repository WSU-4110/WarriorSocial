package com.example.warriorsocial.data;

public class Discussion {

    private String postID;
    private String textBody;
    private String userID;
    private String category;

    public Discussion(String postID, String textBody, String userID, String category) {
        this.postID = postID;
        this.textBody = textBody;
        this.userID = userID;
        this.category = category;
    }

    public Discussion() { }

    public String getPostID() {
        return postID;
    }

    public String getTextBody() {
        return textBody;
    }

    public String getUserID() {
        return userID;
    }

    public String getCategory() {
        return category;
    }
}
