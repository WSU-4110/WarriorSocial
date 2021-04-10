package com.example.warriorsocial.data;

public class Reply {

    public String poster;
    public String body;
    public String post;

    public Reply(String poster, String body, String post) {
        this.poster = poster;
        this.body = body;
        this.post = post;
    }

    public Reply() {}

    public String getBody() {
        return body;
    }

    public String getPoster() {
        return poster;
    }

    public String getPost() {
        return post;
    }
}
