package com.activity.firebotmobile;

public class MessageModel {
    private String sender;
    private String message;
    private String time;

    public MessageModel(String sender, String message, String time) {
        this.sender = sender;
        this.message = message;
        this.time = time;
    }

    public String getSender() { return sender; }
    public String getMessage() { return message; }
    public String getTime() { return time; }
}
