package com.zerotime.zerotime.Secretary.Pojos;

public class ChatList {
    private String userPrimaryPhone;

    public ChatList() {
    }

    public ChatList(String userPrimaryPhone) {
        this.userPrimaryPhone = userPrimaryPhone;
    }

    public String getUserPrimaryPhone() {
        return userPrimaryPhone;
    }

    public void setUserPrimaryPhone(String userPrimaryPhone) {
        this.userPrimaryPhone = userPrimaryPhone;
    }
}
