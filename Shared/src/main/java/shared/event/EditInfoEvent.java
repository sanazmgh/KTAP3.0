package shared.event;

import shared.form.UserInfoForm;
import shared.response.Response;

public class EditInfoEvent extends Event {

    private final UserInfoForm form;

    public EditInfoEvent (UserInfoForm form)
    {
        this.form = form;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.changeInfo(authToken, form);
    }
}
