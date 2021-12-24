package shared.response;

public class GetSettingsInfoResponse extends Response {

    private final boolean isPrivate;
    private final int lastSeen;

    public GetSettingsInfoResponse(boolean isPrivate, int lastSeen)
    {
        this.isPrivate = isPrivate;
        this.lastSeen = lastSeen;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitSettingsInfo(isPrivate, lastSeen);
    }
}
