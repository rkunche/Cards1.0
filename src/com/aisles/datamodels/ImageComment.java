package com.aisles.datamodels;

public class ImageComment {
    Long mId;
    Long mOwnerImageId;
    Long mOwnerUserId;
    String mComment;
    String mCommenterFirstName;
    String mCommenterLastName;
    Long mLastModifiedTimestamp;
    Long mCreatedTimestamp;
    String mImageCommentOwnerImageURL;
    
    public ImageComment() {
    }
    
    public Long getId() {
        return mId;
    }
    
    public void setId(Long id) {
        this.mId = id;
    }
    
    public Long getOwnerUserId() {
        return mOwnerUserId;
    }
    
    public void setOwnerUserId(Long ownerUserId) {
        this.mOwnerUserId = ownerUserId;
    }
    
    public String getComment() {
        return mComment;
    }
    
    public void setComment(String comment) {
        this.mComment = comment;
    }
    
    public Long getLastModifiedTimestamp() {
        return mLastModifiedTimestamp;
    }
    
    public void setLastModifiedTimestamp(Long lastModifiedTimestamp) {
        this.mLastModifiedTimestamp = lastModifiedTimestamp;
    }
    
    public Long getCreatedTimestamp() {
        return mCreatedTimestamp;
    }
    
    public void setCreatedTimestamp(Long createdTimestamp) {
        this.mCreatedTimestamp = createdTimestamp;
    }
    
    public Long getOwnerImageId() {
        return mOwnerImageId;
    }
    
    public void setOwnerImageId(Long ownerImageId) {
        this.mOwnerImageId = ownerImageId;
    }
    
    public String getCommenterFirstName() {
        return mCommenterFirstName;
    }
    
    public void setCommenterFirstName(String commenterFirstName) {
        this.mCommenterFirstName = commenterFirstName;
    }
    
    public String getCommenterLastName() {
        return mCommenterLastName;
    }
    
    public void setCommenterLastName(String commenterLastName) {
        this.mCommenterLastName = commenterLastName;
    }
    
    public String getImageCommentOwnerImageURL() {
        return mImageCommentOwnerImageURL;
    }
    
    public void setImageCommentOwnerImageURL(String imageCommentOwnerImageURL) {
        this.mImageCommentOwnerImageURL = imageCommentOwnerImageURL;
    }
}
