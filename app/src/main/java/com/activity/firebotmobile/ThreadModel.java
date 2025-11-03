package com.activity.firebotmobile;

public class ThreadModel {
    private String id;
    private String name;
    private String lastMessage;
    private String updatedAt;

    public ThreadModel(String id, String name, String lastMessage, String updatedAt) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLastMessage() { return lastMessage; }
    public String getUpdatedAt() { return updatedAt; }
}
