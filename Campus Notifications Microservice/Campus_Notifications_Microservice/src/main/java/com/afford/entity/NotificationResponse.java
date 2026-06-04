package com.afford.entity;

import java.util.List;

public class NotificationResponse {

    private List<NotificationDTO> notifications;

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }
}