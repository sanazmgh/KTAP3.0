package shared.event;

import shared.response.Response;

public class SettingsEvent extends Event {
    private final String type;
    private final String action;

    public SettingsEvent(String type, String action)
    {
        this.type = type;
        this.action = action;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.getSettingsAction(authToken, type, action);
    }
}
