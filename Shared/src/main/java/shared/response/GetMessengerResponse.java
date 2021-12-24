package shared.response;

import shared.form.GroupDataForm;

import java.util.LinkedList;

public class GetMessengerResponse extends Response {
    private final LinkedList<GroupDataForm> forms;

    public GetMessengerResponse(LinkedList<GroupDataForm> forms)
    {
        this.forms = forms;
    }


    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitGetMessenger(forms);
    }
}
