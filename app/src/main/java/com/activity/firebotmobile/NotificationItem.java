package com.activity.firebotmobile;

public class NotificationItem {

    private String title;
    private String description;
    private String timestamp;
    private int iconResId;

    public NotificationItem(String title, String description, String timestamp, int iconResId) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.iconResId = iconResId;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTimestamp() { return timestamp; }
    public int getIconResId() { return iconResId; }
}