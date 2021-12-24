package shared.event;

import shared.response.Response;

public class NewCommentEvent extends Event {
    private final long tweetID;

    public NewCommentEvent(long tweetID)
    {
        this.tweetID = tweetID;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.newComment(authToken, tweetID);
    }
}
