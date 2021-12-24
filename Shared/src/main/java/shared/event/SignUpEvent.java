package shared.event;

import shared.form.UserInfoForm;
import shared.response.Response;

public class SignUpEvent extends Event {
    private final UserInfoForm form;

    public SignUpEvent(UserInfoForm form)
    {
        this.form = form;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.signUp(form);
    }
}
