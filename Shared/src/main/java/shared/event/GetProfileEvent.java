package shared.event;

import shared.response.Response;

public class GetProfileEvent extends Event {
    private final long ownerUserID;

    public GetProfileEvent(long ownerUserID)
    {
        this.ownerUserID = ownerUserID;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.getProfile(authToken, ownerUserID);
    }
}
