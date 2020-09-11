package com.zerotime.zerotime.Moderator.Pojos;

public class NewOrders {
    String orderDescription, orderDate, orderPrice, orderNotes, receiverName, receiverPrimaryPhone,
           receiverSecondaryPhone, userPrimaryPhone;

    public NewOrders(String orderDescription, String orderDate, String orderPrice, String orderNotes, String receiverName, String receiverPrimaryPhone, String receiverSecondaryPhone, String userPrimaryPhone) {
        this.orderDescription = orderDescription;
        this.orderDate = orderDate;
        this.orderPrice = orderPrice;
        this.orderNotes = orderNotes;
        this.receiverName = receiverName;
        this.receiverPrimaryPhone = receiverPrimaryPhone;
        this.receiverSecondaryPhone = receiverSecondaryPhone;
        this.userPrimaryPhone = userPrimaryPhone;
    }
    public NewOrders(){

    }
    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPrimaryPhone() {
        return receiverPrimaryPhone;
    }

    public void setReceiverPrimaryPhone(String receiverPrimaryPhone) {
        this.receiverPrimaryPhone = receiverPrimaryPhone;
    }

    public String getReceiverSecondaryPhone() {
        return receiverSecondaryPhone;
    }

    public void setReceiverSecondaryPhone(String receiverSecondaryPhone) {
        this.receiverSecondaryPhone = receiverSecondaryPhone;
    }

    public String getUserPrimaryPhone() {
        return userPrimaryPhone;
    }

    public void setUserPrimaryPhone(String userPrimaryPhone) {
        this.userPrimaryPhone = userPrimaryPhone;
    }
}
