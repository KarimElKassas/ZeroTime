package com.zerotime.zerotime.Secretary.Pojos;

public class SecretaryChatPojo {
    private String Message,Sender,Receiver;
    public boolean isSeen;


    public SecretaryChatPojo() {
    }

    public SecretaryChatPojo(String message, String sender, String receiver, boolean isSeen) {
        Message = message;
        Sender = sender;
        Receiver = receiver;
        this.isSeen = isSeen;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
