package controller.logic.tweeter;

import DataBase.DataBase;
import shared.model.Tweet;
import shared.model.User;
import shared.response.ViewTweetResponse;

import java.util.Collections;
import java.util.LinkedList;

public class TimelineController {
    private User user;
    private final LinkedList<Long> timeline = new LinkedList<>();
    private final TweetsListController tweetsListController;
    private final DataBase dataBase = DataBase.getDataBase();

    public TimelineController(){
        tweetsListController = new TweetsListController("Timeline");
    }

    public void setUser(User user) {
        this.user = user;
        tweetsListController.setViewer(user);
    }

    public ViewTweetResponse createTimeLine()
    {
        timeline.clear();
        user = dataBase.users.get(user.getId());

        for(int i=0 ; i<user.getFollowings().size() ; i++)
        {
            User currentUser = dataBase.users.get(user.getFollowings().get(i));

            if(!user.getMuted().contains(currentUser.getId()) &&
                    currentUser.isActive() &&
                    !currentUser.getBlocked().contains(user.getId()) &&
                    !user.getBlocked().contains(currentUser.getId()))
                timeline.addAll(currentUser.getTweets());

            for(long tweetID : currentUser.getLikedTweets())
            {
                Tweet currentTweet = dataBase.tweets.get(tweetID);
                User writerUser = dataBase.users.get(currentTweet.getUserId());

                if(writerUser != null)
                {
                    if(writerUser.isActive() &&
                            !writerUser.isPrivate() &&
                            !user.getFollowings().contains(writerUser.getId()) &&
                            !timeline.contains(currentTweet.getId()))
                        timeline.add(currentTweet.getId());
                }
            }
        }

        timeline.addAll(user.getTweets());
        Collections.sort(timeline);
        Collections.reverse(timeline);
        tweetsListController.setBaseList(timeline, false);

        return tweetsListController.getCurrentList();
    }

    public TweetsListController getTweetsListController() {
        return tweetsListController;
    }
}
