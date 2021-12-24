package controller.logic.tweeter;

import Constants.Constants;
import DataBase.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.form.ProfileInfoForm;
import shared.model.Tweet;
import shared.model.User;
import shared.response.StringResponse;

public class TweetController {
    private User viewer;
    private User owner;
    private Tweet tweet;
    private final static int REPORT = Constants.REPORT_LIMIT;
    private final DataBase dataBase = DataBase.getDataBase();
    static private final Logger logger = LogManager.getLogger(TweetController.class);


    public StringResponse getEvent(String action, long tweetID)
    {
        setTweet(tweetID);

        if(action.equals("Like")) {
            likeTweet();
            return new StringResponse("Liked");
        }

        else if(action.equals("Report")) {
            report();
            return new StringResponse("Report");
        }

        return new StringResponse("");
    }

    public void setViewer(User user)
    {
        this.viewer = user;
    }

    public void setTweet(long tweetID) {
        this.tweet = dataBase.tweets.get(tweetID);
        this.owner = dataBase.users.get(tweet.getUserId());
    }

    public String getPic(Tweet tweet)
    {
        return tweet.getTweetImageSTR();
    }

    public String likeCounts(Tweet tweet)
    {
        return Integer.toString(tweet.getLikes().size());
    }

    public String retweetCounts(Tweet tweet)
    {
        return Integer.toString(tweet.getRetweets().size());
    }

    public String commentCounts(Tweet tweet){
        return Integer.toString(tweet.getComments().size());
    }

    public void likeTweet()
    {
        logger.info("user : " + viewer.getId() + "/ liked tweet : " + tweet.getId() + "/ in tweetController");

        tweet = dataBase.tweets.get(tweet.getId());
        viewer = dataBase.users.get(viewer.getId());

        if(!tweet.getLikes().contains(viewer.getId()))
            tweet.getLikes().add(viewer.getId());

        viewer.getLikedTweets().add(tweet.getId());

        dataBase.tweets.save(tweet);
        dataBase.users.save(viewer);
    }

    public void report()
    {
        tweet = dataBase.tweets.get(tweet.getId());

        tweet.addOnReports();

        logger.info("user : " + viewer.getId() + "/ reported tweet : " + tweet.getId() + "/ in tweetController");

        if(tweet.getNoOfReports() >= REPORT)
        {
            logger.info("removed tweet " + tweet.getId() + "/ bc of too much reports" + "/ in tweetController");

            User user = dataBase.users.get(tweet.getUserId());
            user.getTweets().remove((Long) tweet.getId());
            dataBase.users.save(user);
        }

        dataBase.tweets.save(tweet);

    }

    public ProfileInfoForm getOwnersProfile()
    {
        ProfileInfoForm form = new ProfileInfoForm();

        owner = dataBase.users.get(owner.getId());
        viewer = dataBase.users.get(owner.getId());

        String followingStatus = "Follow";

        if(viewer.getFollowings().contains(owner.getId()))
            followingStatus = "Unfollow";

        if(viewer.getRequested().contains(owner.getId()))
            followingStatus = "Requested";

        form.setName(owner.getName());
        form.setLastName(owner.getLastName());
        form.setUsername(owner.getUsername());
        form.setFollowingStatus(followingStatus);
        form.setBlocked(viewer.getBlocked().contains(owner.getId()) ? "Unblock" : "Block");
        form.setMuted(viewer.getMuted().contains(owner.getId()) ? "Unmute" : "Mute");
        form.setUserID(owner.getId());
        form.setMyProfile(owner.getUsername().equals(viewer.getUsername()));
        form.setImageSTR(owner.getImageSTR());

        return form;
    }
}
