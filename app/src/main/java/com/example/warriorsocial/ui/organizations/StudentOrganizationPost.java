package com.example.warriorsocial.ui.organizations;

import com.google.firebase.Timestamp;
import com.google.firebase.database.Exclude;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class StudentOrganizationPost {
    public String postDescription;
    public String postEmail;
    public String postImage;
    public String postTitle;
    public String uid;
    public int likeCount = 0;
    public Map<String, Boolean> likes = new HashMap<>();
    //private Timestamp postTime;

    public StudentOrganizationPost() {}

    public StudentOrganizationPost(String postDescription, String postEmail, String postImage, String postTitle, String uid) {
        this.postDescription = postDescription;
        this.postEmail = postEmail;
        this.postImage = postImage;
        this.postTitle = postTitle;
        this.uid = uid;
        //this.postTime = postTime;
    }

    public String getPostDescription() {
        return postDescription;
    }
    public String getPostEmail() {
        return postEmail;
    }
    public String getPostImage() {
        return postImage;
    }
    public String getPostTitle() { return postTitle; }
    public String getuid() { return uid; }
    /*
    public Timestamp getPostTime() {
        return getPostTime();
    }*/

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public void setPostEmail(String postEmail) {
        this.postEmail = postEmail;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
    /*
    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }*/

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("postEmail", postEmail);
        result.put("postImage", postImage);
        result.put("postTitle", postTitle);
        result.put("postDescription", postDescription);
        result.put("likeCount", likeCount);
        result.put("likes", likes);

        return result;
    }
}
