package shared.event;

import shared.response.Response;

public class NewGroupEvent extends Event {
    private final String gpName;
    private final String username;
    private final String type;

    public NewGroupEvent(String gpName, String username, String type) {
        this.gpName = gpName;
        this.username = username;
        this.type = type;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.newGroup(authToken, gpName, username, type);
    }
}
