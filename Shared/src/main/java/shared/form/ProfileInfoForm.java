package shared.form;

import java.util.Date;

public class ProfileInfoForm {
    private boolean isMyProfile;
    private boolean visibleTweets;
    private boolean privacyStatus;
    private String isMuted;
    private String isBlocked;
    private String followingStatus;
    private String name;
    private String lastName;
    private String username;
    private long userID;
    private String info;
    private String bio;
    private String lastSeen;
    private String imageSTR;

    public boolean isMyProfile() {
        return isMyProfile;
    }

    public void setMyProfile(boolean myProfile) {
        isMyProfile = myProfile;
    }

    public String getFollowingStatus() {
        return followingStatus;
    }

    public void setFollowingStatus(String followingStatus) {
        this.followingStatus = followingStatus;
    }

    public String isMuted() {
        return isMuted;
    }

    public void setMuted(String muted) {
        isMuted = muted;
    }

    public String isBlocked() {
        return isBlocked;
    }

    public void setBlocked(String blocked) {
        isBlocked = blocked;
    }

    public boolean isVisibleTweets() {
        return visibleTweets;
    }

    public void setVisibleTweets(boolean visibleTweets) {
        this.visibleTweets = visibleTweets;
    }

    public boolean getPrivacyStatus() {
        return privacyStatus;
    }

    public void setPrivacyStatus(boolean privacyStatus) {
        this.privacyStatus = privacyStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getImageSTR() {
        return imageSTR;
    }

    public void setImageSTR(String imageSTR) {
        this.imageSTR = imageSTR;
    }
}
