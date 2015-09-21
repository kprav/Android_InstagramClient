package com.codepath.instagramclient;

public class InstagramPhoto {

    private class Comment {
        String commentUserName;
        String comment;
    }

    private String userName;
    private String caption;
    private String imageUrl;
    private String profilePicUrl;
    private String location;
    private String creationTimeStr;
    private long creationTime;
    private int imageWidth;
    private int imageHeight;
    private int likesCount;
    private Comment comment1;
    private Comment comment2;

    public String getComment1() {
        if (comment1 != null) {
            if (!comment1.commentUserName.equals("") && !comment1.comment.equals("")) {
                return "<b> <font color=\"#0D47A1\">" + comment1.commentUserName + "</font> </b> &nbsp;" + comment1.comment;
            }
        }
        return null;
    }

    public void setComment1(String commentUserName, String comment) {
        comment1 = new Comment();
        comment1.commentUserName = commentUserName;
        comment1.comment = comment;
    }

    public String getComment2() {
        if (comment2 != null) {
            if (!comment2.commentUserName.equals("") && !comment2.comment.equals("")) {
                return "<b> <font color=\"#0D47A1\">" + comment2.commentUserName + "</font> </b> &nbsp;" + comment2.comment;
            }
        }
        return null;
    }

    public void setComment2(String commentUserName, String comment) {
        comment2 = new Comment();
        comment2.commentUserName = commentUserName;
        comment2.comment = comment;
    }

    public String getCreationTimeStr() {
        return creationTimeStr;
    }

    public void setCreationTimeStr(String creationTimeStr) {
        this.creationTimeStr = creationTimeStr;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
}
