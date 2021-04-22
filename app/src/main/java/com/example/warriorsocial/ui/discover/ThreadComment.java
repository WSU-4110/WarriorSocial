package com.example.warriorsocial.ui.discover;

public class ThreadComment {
    private String commentText;
    private int date;
    private String user;


    public ThreadComment() {}

    public ThreadComment(String commentText, int date, String user) {
        this.commentText = commentText;
        this.date = date;
        this.user = user;
    }

    public String getCommentText() {
        return commentText;
    }

    public int getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
