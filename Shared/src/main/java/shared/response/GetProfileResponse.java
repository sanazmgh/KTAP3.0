package shared.response;

import shared.form.ProfileInfoForm;

public class GetProfileResponse extends Response {

    private final ProfileInfoForm form;
    private final boolean prevStepAvailable;

    public GetProfileResponse(ProfileInfoForm form, boolean available)
    {
        this.form = form;
        this.prevStepAvailable = available;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitGetProfile(form, prevStepAvailable);
    }
}
