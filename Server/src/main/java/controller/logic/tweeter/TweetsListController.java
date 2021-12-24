package controller.logic.tweeter;

import DataBase.DataBase;
import shared.form.ProfileInfoForm;
import shared.model.Tweet;
import shared.model.User;
import shared.response.ViewTweetResponse;

import java.util.LinkedList;

public class TweetsListController {
    private LinkedList<Long> rootTweets = new LinkedList<>();
    private final LinkedList<Long> commentsList = new LinkedList<>();
    private final TweetController tweetController;
    private final String where;
    private final DataBase dataBase = DataBase.getDataBase();

    public TweetsListController(String where)
    {
        this.tweetController = new TweetController();
        this.where = where;
    }

    public void setViewer(User viewer)
    {
        tweetController.setViewer(viewer);
    }

    public ViewTweetResponse getCurrentList()
    {
        LinkedList<Tweet> currentList = new LinkedList<>();
        LinkedList<ProfileInfoForm> users = new LinkedList<>();
        LinkedList<Long> tweetList;

        if(commentsList.isEmpty()) {
            tweetList = rootTweets;
        }

        else
            tweetList = dataBase.tweets.get(commentsList.getLast()).getComments();

        for(long i : tweetList)
        {
            Tweet currentTweet = dataBase.tweets.get(i);
            User currentUser = dataBase.users.get(currentTweet.getUserId());
            Tweet retweetedTweet = dataBase.tweets.get(currentTweet.getRetweetFromUserId());
            currentTweet.setUsername(currentUser.getUsername());

            if(retweetedTweet != null)
            {
                User retweetedFrom = dataBase.users.get(retweetedTweet.getUserId());
                currentTweet.setRetweetUsername(retweetedFrom.getUsername());
            }

            currentList.add(currentTweet);

            tweetController.setTweet(i);
            users.add(tweetController.getOwnersProfile());
        }

        ViewTweetResponse response =  new ViewTweetResponse(currentList, where, commentsList.isEmpty());
        response.setUsers(users);

        return response;
    }

    public ViewTweetResponse getPrevList()
    {
        if(!commentsList.isEmpty())
            commentsList.removeLast();

        return getCurrentList();
    }

    public ViewTweetResponse makeNextList(long tweetID)
    {
        commentsList.add(tweetID);
        return getCurrentList();
    }

    public void setBaseList(LinkedList<Long> tweets, boolean clearComments)
    {
        if(clearComments)
            commentsList.clear();

        rootTweets = tweets;
    }

    public TweetController getTweetController() {
        return tweetController;
    }
}
