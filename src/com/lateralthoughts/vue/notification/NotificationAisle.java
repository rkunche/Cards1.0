package com.lateralthoughts.vue.notification;

import java.util.ArrayList;

public class NotificationAisle {
    public NotificationAisle() {
        
    }
    
    public int getId() {
        return mId;
    }
    
    public void setId(int id) {
        this.mId = id;
    }
    
    private int mId;
    private String mAisleId;
    private String mUserName;
    
    public String getUserName() {
        return mUserName;
    }
    
    public void setUserName(String userName) {
        this.mUserName = userName;
    }
    
    public String getImageId() {
        return mImageId;
    }
    
    public void setImageId(String imageId) {
        this.mImageId = imageId;
    }
    
    private String mImageId;
    
    public String getNotificationText() {
        return mNotificationText;
    }
    
    public void setNotificationText(String notificationText) {
        this.mNotificationText = notificationText;
    }
    
    private String mNotificationText;
    
    public String getUserProfileImageUrl() {
        return mUserProfileImageUrl;
    }
    
    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.mUserProfileImageUrl = userProfileImageUrl;
    }
    
    public String getAisleTitle() {
        return mAisleTitle;
    }
    
    public void setAisleTitle(String aisleTitle) {
        this.mAisleTitle = aisleTitle;
    }
    
    public int getLikeCount() {
        return mLikeCount;
    }
    
    public void setLikeCount(int likeCount) {
        this.mLikeCount = likeCount;
    }
    
    public int getBookmarkCount() {
        return mBookmarkCount;
    }
    
    public void setBookmarkCount(int bookmarkCount) {
        this.mBookmarkCount = bookmarkCount;
    }
    
    public int getCommentsCount() {
        return mCommentsCount;
    }
    
    public void setCommentsCount(int commentsCount) {
        this.mCommentsCount = commentsCount;
    }
    
    public ArrayList<NotificationAisle> getAggregatedAisles() {
        return aggregatedAisles;
    }
    
    public void setAggregatedAisles(
            ArrayList<NotificationAisle> aggregatedAisles) {
        this.aggregatedAisles = aggregatedAisles;
    }
    
    private String mUserProfileImageUrl;
    private String mAisleTitle; // aisle title is combination of lokkingfor and
                               // occasion
    private int mLikeCount;
    private int mBookmarkCount;
    private int mCommentsCount;
    private ArrayList<NotificationAisle> aggregatedAisles;
    
    public String getAisleId() {
        return mAisleId;
    }
    
    public void setAisleId(String aisleId) {
        this.mAisleId = aisleId;
    }
    
    public boolean isReadStatus() {
        return mReadStatus;
    }
    
    public void setReadStatus(boolean readStatus) {
        this.mReadStatus = readStatus;
    }
    
    private boolean mReadStatus; // true for read, false for unread
    
    public NotificationAisle(int id, String aisleId, String imageId,
            String userName, String userProfileImageUrl, String aisleTitle,
            int likeCount, int bookmarkCount, int commentsCount,
            ArrayList<NotificationAisle> aggregatedAisles, boolean readStatus,
            String notificationText) {
        this.mAisleId = aisleId;
        this.mUserProfileImageUrl = userProfileImageUrl;
        this.mAisleTitle = aisleTitle;
        this.mLikeCount = likeCount;
        this.mBookmarkCount = bookmarkCount;
        this.mCommentsCount = commentsCount;
        this.aggregatedAisles = aggregatedAisles;
        this.mReadStatus = readStatus;
        this.mNotificationText = notificationText;
        this.mImageId = imageId;
        this.mId = id;
        this.mUserName = userName;
    }
    
    private boolean mEmptyNotification = false;
    
    public boolean ismEmptyNotification() {
        return mEmptyNotification;
    }
    
    public void setmEmptyNotification(boolean mEmptyNotification) {
        this.mEmptyNotification = mEmptyNotification;
    }
    
}
