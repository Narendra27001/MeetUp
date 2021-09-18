package com.android.meetup.Model;

public class ChatList {
    private String interest;
    private String id;

    public ChatList(String interest, String id) {
        this.interest = interest;
        this.id = id;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChatList() {
    }
}

