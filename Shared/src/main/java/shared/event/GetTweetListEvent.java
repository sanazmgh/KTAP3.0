package shared.event;

import shared.response.Response;

public class GetTweetListEvent extends Event {
    private final String where;
    private final long tweetID;
    private final String action;

    public GetTweetListEvent(String where, long tweetID, String action)
    {
        this.where = where;
        this.tweetID = tweetID;
        this.action = action;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.getTweetList(authToken, where, tweetID, action);
    }
}
