package shared.response;

public class WriteCommentResponse extends Response {

    private final long tweetID;

    public WriteCommentResponse(long tweetID)
    {
        this.tweetID = tweetID;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitCommenting(tweetID);
    }
}
