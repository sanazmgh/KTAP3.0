package shared.model;

import java.util.Date;
import java.util.LinkedList;

public class Tweet{
    private long id ;
    private boolean deleted = false ;

    private long userId;
    private String username = "";
    private long retweetFromUserId;
    private String retweetUsername = "";
    private String tweetText;
    private String tweetImageSTR;
    private Date dateTweeted;
    private boolean commented;
    private int noOfReports;
    private LinkedList<Long> comments = new LinkedList<>();
    private LinkedList<Long> likes = new LinkedList<>();
    private LinkedList<Long> retweets = new LinkedList<>();

    public Tweet() {};

    public Tweet(long userId , long retweetFromUserId, String tweetText, String tweetImageSTR, boolean commented) {
        this.userId = userId;
        this.retweetFromUserId = retweetFromUserId;
        this.tweetText = tweetText;
        this.tweetImageSTR = tweetImageSTR;
        dateTweeted = new Date();
        this.commented = commented;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRetweetFromUserId() {
        return retweetFromUserId;
    }

    public void setRetweetFromUserId(long retweetFromUserID) {
        this.retweetFromUserId = retweetFromUserID;
    }

    public String getRetweetUsername() {
        return retweetUsername;
    }

    public void setRetweetUsername(String retweetUsername) {
        this.retweetUsername = retweetUsername;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public String getTweetImageSTR() {
        return tweetImageSTR;
    }

    public void setTweetImageSTR(String tweetImageSTR) {
        this.tweetImageSTR = tweetImageSTR;
    }

    public Date getDateTweeted() {
        return dateTweeted;
    }

    public void setDateTweeted(Date dateTweeted) {
        this.dateTweeted = dateTweeted;
    }

    public boolean isCommented() {
        return commented;
    }

    public void setCommented(boolean commented) {
        this.commented = commented;
    }

    public LinkedList<Long> getComments() {
        return comments;
    }

    public void setComments(LinkedList<Long> comments) {
        this.comments = comments;
    }

    public LinkedList<Long> getLikes() {
        return likes;
    }

    public void setLikes(LinkedList<Long> likes) {
        this.likes = likes;
    }

    public LinkedList<Long> getRetweets() {
        return retweets;
    }

    public void setRetweets(LinkedList<Long> retweets) {
        this.retweets = retweets;
    }

    public void addOnReports()
    {
        this.noOfReports++;
    }

    public int getNoOfReports() {
        return noOfReports;
    }

    public void setNoOfReports(int noOfReports) {
        this.noOfReports = noOfReports;
    }
}
