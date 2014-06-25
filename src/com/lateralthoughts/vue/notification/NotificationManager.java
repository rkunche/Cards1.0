package com.lateralthoughts.vue.notification;

import java.util.ArrayList;

import com.googlenowcard.utils.Utils;

public class NotificationManager implements NotificationInterface{
    private ArrayList<NotificationAisle> mNotificationList = new ArrayList<NotificationAisle>();
    
    public NotificationManager() {
        // TODO Auto-generated constructor stub
    }
    
    public ArrayList<NotificationAisle> getUserNotifications() {
        //TODO: get the notifications from the db.
        for (int i = 0; i < 10; i++) {
            NotificationAisle notificationAisle = new NotificationAisle();
            notificationAisle.setNotificationText("Notification Text");
            notificationAisle.setReadStatus(false);
            notificationAisle.setAisleId("");
            notificationAisle.setmEmptyNotification(false);
            notificationAisle.setUserName("stefen hock");
            notificationAisle.setAisleTitle("Looking for pool party");
            notificationAisle.setCommentsCount(Utils.randInt(0, 20));
            notificationAisle.setLikeCount(Utils.randInt(0, 30));
            notificationAisle.setBookmarkCount(Utils.randInt(0, 10));
            mNotificationList.add(notificationAisle);
        }
        return mNotificationList;
    }

    @Override
    public boolean removeNotification(long notificationId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void aggrigateNotifications(
            ArrayList<NotificationAisle> notificationAisles) {
        // TODO Auto-generated method stub
        
    }
}
