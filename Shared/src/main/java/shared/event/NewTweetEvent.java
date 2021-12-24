package shared.event;

import shared.response.Response;

public class NewTweetEvent extends Event {
    private final String picPath;
    private final String text;
    private final long retweetFromID;
    private final long commentedUnderID;

    public NewTweetEvent(String text, String picPath, long retweetFromID, long commentedUnderID)
    {
        this.text = text;
        this.picPath = picPath;
        this.retweetFromID = retweetFromID;
        this.commentedUnderID = commentedUnderID;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.newTweet(authToken, text, picPath, retweetFromID, commentedUnderID);
    }
}
