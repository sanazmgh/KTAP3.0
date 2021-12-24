package shared.model;

import shared.form.UserInfoForm;

import java.util.Date;
import java.util.LinkedList;

public class User {

    private long id ;
    private boolean deleted = false ;

    private String name;
    private String lastName;
    private String username;
    private String password;
    private String bio;

    private String email;
    private boolean visibleEmail;

    private String phone;
    private boolean visiblePhone;

    private Long dateOfBirth;
    private boolean visibleDateOfBirth;

    private Date lastSeen ;
    private int lastSeenMode;
    /***
     * 0 : nobody
     * 1 : everybody
     * 2 : followers
     */

    private boolean isActive;
    private boolean Private;

    private String imageSTR;

    private long messengerID;

    private LinkedList<Long> tweets = new LinkedList<>();
    private LinkedList<Long> likedTweets = new LinkedList<>();
    private LinkedList<Long> requested = new LinkedList<>();
    private LinkedList<Long> followers = new LinkedList<>();
    private LinkedList<Long> followings = new LinkedList<>();
    private LinkedList<Long> blocked = new LinkedList<>();
    private LinkedList<Long> muted = new LinkedList<>();

    private LinkedList<Long> systemNotifications = new LinkedList<>();
    private LinkedList<Long> requestsNotifications = new LinkedList<>();

    public User() {}

    public User(String name, String lastName, String username, String password, String email )
    {
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActive = true;
        this.imageSTR = "";
        //this.profile = new Profile();
        this.lastSeen = new Date();
    }

    public User(UserInfoForm userInfoFormEvent) {
        this.name = userInfoFormEvent.getName();
        this.lastName = userInfoFormEvent.getLastName();
        this.username = userInfoFormEvent.getUsername();
        this.password = userInfoFormEvent.getPass();
        this.bio = userInfoFormEvent.getBio();
        this.email = userInfoFormEvent.getEmail();
        this.visibleEmail = userInfoFormEvent.isVisibleEmail();
        this.phone = userInfoFormEvent.getPhone();
        this.visiblePhone = userInfoFormEvent.isVisiblePhone();
        this.dateOfBirth = (userInfoFormEvent.getDateOfBirth() == null ? 100 : userInfoFormEvent.getDateOfBirth().getTime());
        this.visibleDateOfBirth = userInfoFormEvent.isVisibleDate();
        this.isActive = true;
        this.imageSTR = "";
        this.lastSeen = new Date();

    }

    public void updateInfo(UserInfoForm form)
    {
        this.name = form.getName();
        this.lastName = form.getLastName();
        this.username = form.getUsername();
        this.password = form.getPass();
        this.bio = form.getBio();
        this.email = form.getEmail();
        this.visibleEmail = form.isVisibleEmail();
        this.phone = form.getPhone();
        this.visiblePhone = form.isVisiblePhone();
        this.dateOfBirth = form.getDateOfBirth().getTime();
        this.visibleDateOfBirth = form.isVisibleDate();
        this.imageSTR = form.getPicSTR();
        this.lastSeen = new Date();

    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVisibleEmail() {
        return visibleEmail;
    }

    public void setVisibleEmail(boolean visibleEmail) {
        this.visibleEmail = visibleEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isVisiblePhone() {
        return visiblePhone;
    }

    public void setVisiblePhone(boolean visiblePhone) {
        this.visiblePhone = visiblePhone;
    }

    public Date getDateOfBirth() {
        return new Date(dateOfBirth);
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth.getTime();
    }

    public boolean isVisibleDateOfBirth() {
        return visibleDateOfBirth;
    }

    public void setVisibleDateOfBirth(boolean visibleDateOfBirth) {
        this.visibleDateOfBirth = visibleDateOfBirth;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public int getLastSeenMode() {
        return lastSeenMode;
    }

    public void setLastSeenMode(int lastSeenMode) {
        this.lastSeenMode = lastSeenMode;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isPrivate() {
        return Private;
    }

    public void setPrivate(boolean aPrivate) {
        Private = aPrivate;
    }

    public String getImageSTR() {
        return imageSTR;
    }

    public void setImageSTR(String imageSTR) {
        if(!imageSTR.equals(""))
            this.imageSTR = imageSTR;
    }

    public long getMessengerID() {
        return messengerID;
    }

    public void setMessengerID(long messengerID) {
        this.messengerID = messengerID;
    }

    public LinkedList<Long> getTweets() {
        return tweets;
    }

    public void setTweets(LinkedList<Long> tweets) {
        this.tweets = tweets;
    }

    public LinkedList<Long> getLikedTweets() {
        return likedTweets;
    }

    public void setLikedTweets(LinkedList<Long> likedTweets) {
        this.likedTweets = likedTweets;
    }

    public LinkedList<Long> getRequested() {
        return requested;
    }

    public void setRequested(LinkedList<Long> requested) {
        this.requested = requested;
    }

    public LinkedList<Long> getFollowers() {
        return followers;
    }

    public void setFollowers(LinkedList<Long> followers) {
        this.followers = followers;
    }

    public LinkedList<Long> getFollowings() {
        return followings;
    }

    public void setFollowings(LinkedList<Long> followings) {
        this.followings = followings;
    }

    public LinkedList<Long> getBlocked() {
        return blocked;
    }

    public void setBlocked(LinkedList<Long> blocked) {
        this.blocked = blocked;
    }

    public LinkedList<Long> getMuted() {
        return muted;
    }

    public void setMuted(LinkedList<Long> muted) {
        this.muted = muted;
    }

    public LinkedList<Long> getSystemNotifications() {
        return systemNotifications;
    }

    public void setSystemNotifications(LinkedList<Long> systemNotifications) {
        this.systemNotifications = systemNotifications;
    }

    public LinkedList<Long> getRequestsNotifications() {
        return requestsNotifications;
    }

    public void setRequestsNotifications(LinkedList<Long> requestsNotifications) {
        this.requestsNotifications = requestsNotifications;
    }
}
