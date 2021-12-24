package shared.event;

import shared.response.Response;

public class TweetActionsEvent extends Event {

    private final String action;
    private final long tweetID;

    public TweetActionsEvent(String action, long tweetID)
    {
        this.action = action;
        this.tweetID = tweetID;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.tweetActions(authToken, action, tweetID);
    }
}
