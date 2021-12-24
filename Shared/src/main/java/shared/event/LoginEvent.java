package shared.event;

import shared.form.LoginForm;
import shared.response.Response;

public class LoginEvent extends Event {

    private final LoginForm form;

    public LoginEvent(LoginForm form)
    {
        this.form = form;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.login(form);
    }
}
