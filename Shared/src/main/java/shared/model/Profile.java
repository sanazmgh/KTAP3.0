package shared.model;

import java.util.LinkedList;

public class Profile{

    private final LinkedList<Long> tweets = new LinkedList<>(); //List of tweInteger
    // TODO consider : private final LinkedList<Integer> likedTweets = new LinkedList<>();
    private final LinkedList<Long> requested = new LinkedList<>(); //List of Users ID
    private final LinkedList<Long> followers = new LinkedList<>(); //List of Users ID
    private final LinkedList<Long> followings = new LinkedList<>(); //List of Users ID
    private final LinkedList<Long> blocked = new LinkedList<>(); //List of Users ID
    private final LinkedList<Long> muted = new LinkedList<>(); //List of Users ID

    private final LinkedList<Long> systemNotifications = new LinkedList<>();
    private final LinkedList<Long> requestsNotifications = new LinkedList<>();

    public Profile() {}

    public LinkedList<Long> getTweets() {
        return tweets;
    }

    public void setTweets(Long tweet) {
        tweets.add(tweet);
    }

    /*public LinkedList<Integer> getLikedTweets() {
        return likedTweets;
    }

    public void setLikedTweets(int tweet)
    {
        likedTweets.add(tweet);
    }*/

    public LinkedList<Long> getRequested() {
        return requested;
    }

    public LinkedList<Long> getFollowers() {
        return followers;
    }

    public LinkedList<Long> getFollowings() {
        return followings;
    }

    public LinkedList<Long> getBlocked() {
        return blocked;
    }

    public LinkedList<Long> getMuted() {
        return muted;
    }

    public void setMuted(Long muted) {
        this.muted.add(muted);
    }

    public LinkedList<Long> getSystemNotifications() {
        return systemNotifications;
    }

    public void setSystemNotifications(Long notification) {
        this.systemNotifications.add(notification);
    }

    public LinkedList<Long> getRequestsNotifications() {
        return requestsNotifications;
    }

    public void setRequestsNotifications(Long notification) {
        this.requestsNotifications.add(notification);
    }

}
