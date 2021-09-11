package com.android.meetup.Model;

public class Users {
    private String id;
    private String username;
    private String interest;
    private String imageURL;

    public Users(String id, String username, String interest, String imageURL) {
        this.id = id;
        this.username = username;
        this.interest = interest;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
