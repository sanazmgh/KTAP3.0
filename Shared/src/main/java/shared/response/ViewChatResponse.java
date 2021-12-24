package shared.response;

import shared.model.Message;

import java.util.LinkedList;

public class ViewChatResponse extends Response {
    private final String gpName;
    private final long gpID;
    private final LinkedList<String> usernames;
    private final java.util.LinkedList<Message> messages;
    private final boolean blocked;

    public ViewChatResponse(String gpName, long gpID, LinkedList<String> usernames, LinkedList<Message> messages, boolean blocked) {
        this.gpName = gpName;
        this.gpID = gpID;
        this.usernames = usernames;
        this.messages = messages;
        this.blocked = blocked;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitChat(gpName, gpID, usernames, messages, blocked);
    }
}
