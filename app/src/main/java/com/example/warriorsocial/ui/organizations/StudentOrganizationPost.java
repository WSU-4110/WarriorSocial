package com.example.warriorsocial.ui.organizations;

import com.google.firebase.Timestamp;

public class StudentOrganizationPost {
    private String postDescription;
    private String postEmail;
    private String postImage;
    //private Timestamp postTime;

    public StudentOrganizationPost() {}

    public StudentOrganizationPost(String postDescription, String postEmail, String postImage, Timestamp postTime) {
        this.postDescription = postDescription;
        this.postEmail = postEmail;
        this.postImage = postImage;
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
}
