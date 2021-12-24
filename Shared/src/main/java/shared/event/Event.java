package shared.event;

import shared.response.Response;

public abstract class Event {
    protected long authToken;
    public abstract Response visit(EventVisitor eventVisitor);

    public long getAuthToken() {
        return authToken;
    }

    public void setAuthToken(long authToken) {
        this.authToken = authToken;
    }
}
