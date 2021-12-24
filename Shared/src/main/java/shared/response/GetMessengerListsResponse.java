package shared.response;

import shared.form.GroupDataForm;

import java.util.LinkedList;

public class GetMessengerListsResponse extends Response {
    private final LinkedList<GroupDataForm> forms;
    private final String type;

    public GetMessengerListsResponse(LinkedList<GroupDataForm> forms, String type) {
        this.forms = forms;
        this.type = type;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitMessengerLists(forms, type);
    }
}
