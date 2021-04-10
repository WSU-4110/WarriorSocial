package com.example.warriorsocial.data;

public class Post {

    private String category;
    private String title;
    private String body;
    private String poster;

    public Post(String category, String title, String body, String poster) {
        this.category = category;
        this.title = title;
        this.body = body;
        this.poster = poster;
    }

    public Post() { }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getPoster() {
        return poster;
    }

}
