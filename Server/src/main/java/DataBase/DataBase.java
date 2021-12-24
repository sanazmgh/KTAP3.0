package DataBase;

import controller.network.SocketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
    public UsersDB users;
    public TweetsDB tweets;
    public NotificationsDB notifications;
    public MessengersDB messenger;
    public GroupsDB groups;
    public ListsDB lists;
    public MessagesDB messages;
    public static DataBase dataBase;
    public Connection connection;
    static private final Logger logger = LogManager.getLogger(DataBase.class);

    public void connectToDb()
    {
        try
        {
            String url = "jdbc:mysql://localhost:3306/serverdatabase";
            String username = "root";
            String password = "0312436629";

            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(50);
            statement.close();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get connected to database " + "/ in dataBase");

            System.err.println("couldn't get connected");
        }
    }

    public void createDataBase()
    {
        this.users = new UsersDB();
        this.tweets = new TweetsDB();
        this.notifications = new NotificationsDB();
        this.messenger = new MessengersDB();
        this.groups = new GroupsDB();
        this.lists = new ListsDB();
        this.messages = new MessagesDB();

        this.users.setConnection(connection);
        this.tweets.setConnection(connection);
        this.notifications.setConnection(connection);
        this.messenger.setConnection(connection);
        this.groups.setConnection(connection);
        this.lists.setConnection(connection);
        this.messages.setConnection(connection);

    }

    public static DataBase getDataBase()
    {
        if(dataBase == null)
            dataBase = new DataBase();

        return dataBase;
    }
}
