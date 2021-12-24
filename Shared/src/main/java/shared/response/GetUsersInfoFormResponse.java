package shared.response;

import shared.form.ProfileInfoForm;

import java.util.LinkedList;

public class GetUsersInfoFormResponse extends Response {

    private LinkedList<ProfileInfoForm> forms;

    public GetUsersInfoFormResponse(LinkedList<ProfileInfoForm> forms)
    {
        this.forms = forms;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitLists(forms);
    }
}
