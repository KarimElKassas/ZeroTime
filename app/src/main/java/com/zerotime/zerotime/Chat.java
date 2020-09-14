package com.zerotime.zerotime;

public class Chat {
    private String Message,Sender,Receiver;

    public Chat(String message, String sender, String receiver) {
        Message = message;
        Sender = sender;
        Receiver = receiver;
    }

    public Chat() {
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
}
