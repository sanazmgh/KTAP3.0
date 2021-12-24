package shared.event;

import shared.response.Response;

public class ChangeInMembersEvent extends Event{
    private final String action;
    private final String type;
    private final long gpID;
    private final String username;

    public ChangeInMembersEvent(String action, String type, long gpID, String username) {
        this.action = action;
        this.type = type;
        this.gpID = gpID;
        this.username = username;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.changeMembers(authToken, action, type, gpID, username);
    }
}
