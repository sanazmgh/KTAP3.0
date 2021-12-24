package shared.response;

import shared.form.ProfileInfoForm;
import shared.form.UserInfoForm;
import shared.model.Tweet;

import java.util.LinkedList;

public class ViewTweetResponse extends Response {

    private final LinkedList<Tweet> tweets;
    private LinkedList<ProfileInfoForm> users = new LinkedList<>();
    private final String where;
    private final boolean isRootList;

    public ViewTweetResponse(LinkedList<Tweet> tweets, String where, boolean isRootList)
    {
        this.tweets = tweets;
        this.where = where;
        this.isRootList = isRootList;
    }

    public void setUsers(LinkedList<ProfileInfoForm> users)
    {
        this.users = users;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitTweets(tweets, users, where, isRootList);
    }
}
