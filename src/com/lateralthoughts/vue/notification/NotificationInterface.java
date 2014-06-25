package com.lateralthoughts.vue.notification;

import java.util.ArrayList;

public interface NotificationInterface {
    public ArrayList<NotificationAisle> getUserNotifications();
    
    public boolean removeNotification(long notificationId);
    
    public void aggrigateNotifications(
            ArrayList<NotificationAisle> notificationAisles);
}
