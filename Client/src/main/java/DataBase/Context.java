package DataBase;

public class Context {

    public UserDB users;
    public GroupsDB groups;
    public MessagesDB messages;
    public static Context context;

    private Context()
    {
        this.users = new UserDB();
        this.groups = new GroupsDB();
        this.messages = new MessagesDB();
    }

    public static Context getContext()
    {
        if(context == null)
            context = new Context();

        return context;
    }
}
