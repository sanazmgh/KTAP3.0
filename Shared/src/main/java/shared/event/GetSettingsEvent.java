package shared.event;

import shared.response.Response;

public class GetSettingsEvent extends Event{

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.getSettingsInfo(authToken);
    }
}
