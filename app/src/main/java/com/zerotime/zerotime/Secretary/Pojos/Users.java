package com.zerotime.zerotime.Secretary.Pojos;

public class Users {
    private String userName;
    private String userPrimaryPhone;

    public Users() {
    }

    public Users(String userName, String userPrimaryPhone) {
        this.userName = userName;
        this.userPrimaryPhone = userPrimaryPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPrimaryPhone() {
        return userPrimaryPhone;
    }

    public void setUserPrimaryPhone(String userPrimaryPhone) {
        this.userPrimaryPhone = userPrimaryPhone;
    }
}
