package controller.logic.tweeter;

import DataBase.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.Tweet;
import shared.model.User;

public class NewTweetController {

    static private final Logger logger = LogManager.getLogger(NewTweetController.class);
    private final DataBase dataBase = DataBase.getDataBase();
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void newTweet(String text, String picPath, long retweetedFromID, long commentedUnderID)
    {
        logger.info("New tweet by : " + user.getId() + "/ in newTweetController");

        user = dataBase.users.get(user.getId());

        Tweet tweet = new Tweet(user.getId(), retweetedFromID, text, picPath, commentedUnderID!=0);
        tweet.setId(dataBase.tweets.lastID()+1);

        if(commentedUnderID != 0)
        {
            Tweet commentedUnder = dataBase.tweets.get(commentedUnderID);
            commentedUnder.getComments().add(tweet.getId());

            dataBase.tweets.save(commentedUnder);
        }

        if(retweetedFromID != 0)
        {
            Tweet retweetedFrom = dataBase.tweets.get(retweetedFromID);
            retweetedFrom.getRetweets().add(retweetedFromID);

            dataBase.tweets.save(retweetedFrom);
        }

        dataBase.tweets.save(tweet);
        user.getTweets().add(tweet.getId());
        dataBase.users.save(user);
    }
}
