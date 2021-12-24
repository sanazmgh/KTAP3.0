package DataBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

public class UsersDB {

    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM dd, yyyy, HH:mm").setPrettyPrinting().create();
    private Connection connection;
    static private final Logger logger = LogManager.getLogger(UsersDB.class);


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean userExists(long userId)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM `users` WHERE `id` = ?");
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't check user by ID : " + userId + "/ in UsersDB");

            System.err.println("couldn't check user by ID");
        }

        return false;
    }

    public User getUser(ResultSet resultSet)
    {
        try
        {
            User user = null;

            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setBio(resultSet.getString("bio"));
                user.setEmail(resultSet.getString("email"));
                user.setVisibleEmail(resultSet.getBoolean("visibleEmail"));
                user.setPhone(resultSet.getString("phone"));
                user.setVisiblePhone(resultSet.getBoolean("visiblePhone"));
                user.setDateOfBirth(new Date(resultSet.getLong("dateOfBirth")));
                user.setVisibleDateOfBirth(resultSet.getBoolean("visibleDateOfBirth"));
                user.setLastSeen(new Date(resultSet.getLong("lastSeen")));
                user.setLastSeenMode(resultSet.getInt("lastSeenMode"));
                user.setActive(resultSet.getBoolean("isActive"));
                user.setPrivate(resultSet.getBoolean("Private"));
                user.setImageSTR(resultSet.getString("imageSTR"));
                user.setMessengerID(resultSet.getInt("messengerID"));
                user.setTweets(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("tweets"), Long[].class))));
                user.setLikedTweets(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("likedTweets"), Long[].class))));
                user.setRequested(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("requested"), Long[].class))));
                user.setFollowers(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("followers"), Long[].class))));
                user.setFollowings(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("followings"), Long[].class))));
                user.setBlocked(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("blocked"), Long[].class))));
                user.setMuted(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("muted"), Long[].class))));
                user.setSystemNotifications(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("systemNotifications"), Long[].class))));
                user.setRequestsNotifications(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("requestsNotifications"), Long[].class))));
                user.setDeleted(resultSet.getBoolean("deleted"));
            }

            return user;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get user " + "/ in UsersDB");

            System.err.println("couldn't get user");
        }

        return null;
    }

    public User get(long userId)
    {
        try
        {
            logger.warn("getting user by ID :" + userId  + "/ in UsersDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `users` WHERE `id` = ?");
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            User user = getUser(resultSet);

            statement.close();
            resultSet.close();
            return user;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get user by ID :" + userId  + "/ in UsersDB");

            System.err.println("couldn't get user by ID");
        }

        return null;
    }

    public User getByUsername(String username)
    {
        try
        {
            logger.warn("getting user by username :" + username  + "/ in UsersDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `users` WHERE `username` = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            User user = getUser(resultSet);

            statement.close();
            resultSet.close();
            return user;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get user by username :" + username  + "/ in UsersDB");

            System.err.println("couldn't get tweet by username");
        }

        return null;
    }

    public User getByEmail(String email)
    {
        try
        {
            logger.warn("getting user by email :" + email  + "/ in UsersDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `users` WHERE `email` = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            User user = getUser(resultSet);

            statement.close();
            resultSet.close();
            return user;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get user by email :" + email  + "/ in UsersDB");

            System.err.println("couldn't get user by email");
        }

        return null;
    }

    public LinkedList<User> all()
    {
        try
        {
            logger.warn("getting all users :"  + "/ in UsersDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `users`");

            ResultSet resultSet = statement.executeQuery();

            LinkedList<User> users = new LinkedList<>();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setBio(resultSet.getString("bio"));
                user.setEmail(resultSet.getString("email"));
                user.setVisibleEmail(resultSet.getBoolean("visibleEmail"));
                user.setPhone(resultSet.getString("phone"));
                user.setVisiblePhone(resultSet.getBoolean("visiblePhone"));
                user.setDateOfBirth(new Date(resultSet.getLong("dateOfBirth")));
                user.setVisibleDateOfBirth(resultSet.getBoolean("visibleDateOfBirth"));
                user.setLastSeen(new Date(resultSet.getLong("lastSeen")));
                user.setLastSeenMode(resultSet.getInt("lastSeenMode"));
                user.setActive(resultSet.getBoolean("isActive"));
                user.setPrivate(resultSet.getBoolean("Private"));
                user.setImageSTR(resultSet.getString("imageSTR"));
                user.setMessengerID(resultSet.getInt("messengerID"));
                user.setTweets(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("tweets"), Long[].class))));
                user.setLikedTweets(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("likedTweets"), Long[].class))));
                user.setRequested(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("requested"), Long[].class))));
                user.setFollowers(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("followers"), Long[].class))));
                user.setFollowings(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("followings"), Long[].class))));
                user.setBlocked(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("blocked"), Long[].class))));
                user.setMuted(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("muted"), Long[].class))));
                user.setSystemNotifications(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("systemNotifications"), Long[].class))));
                user.setRequestsNotifications(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("requestsNotifications"), Long[].class))));
                user.setDeleted(resultSet.getBoolean("deleted"));
                users.add(user);
            }

            statement.close();
            resultSet.close();
            return users;
        }

        catch (SQLException exception)
        {
            logger.warn("couldn't get all users "  + "/ in UsersDB");

            System.err.println("couldn't get all users");
        }

        return null;
    }

    public void save(User user)
    {
        try
        {
            logger.warn("saving user :" + user.getId()  + "/ in UsersDB");

            PreparedStatement statement;

            if (userExists(user.getId()))
            {
                statement = connection.prepareStatement(
                        "UPDATE `users` SET `name` = ?, `lastName` = ?, `username` = ?, `password` = ?, `bio` = ?, `email` = ?, `visibleEmail` = ?," +
                                "`phone` = ?, visiblePhone = ?,  `dateOfBirth` = ?, `visibleDateOfBirth` = ?, `lastSeen` = ?, `lastSeenMode` = ?, " +
                                "`isActive` = ?, `private` = ?, `imageSTR` = ?, `messengerID` = ?, `tweets` = ?, `likedTweets` = ?, `requested` = ?, `followers` = ?," +
                                "`followings` = ?, `blocked` = ?, `muted` = ?, `systemNotifications` = ?, `requestsNotifications` = ?, `deleted` = ? WHERE `id` = " + user.getId());
            }

            else
            {
                statement = connection.prepareStatement(
                        "INSERT INTO `users` (`name`, `lastName`, `username`, `password`, `bio`, `email`, `visibleEmail`, `phone`, `visiblePhone`, " +
                                "`dateOfBirth`, `visibleDateOfBirth`, `lastSeen`, `lastSeenMode`, `isActive`, `private`, `imageSTR`, `messengerID`, `tweets`, " +
                                "`likedTweets`, `requested`, `followers`, `followings`, `blocked`, `muted`, `systemNotifications`, `requestsNotifications`, `deleted`) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            }

            statement.setString(1, user.getName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getBio());
            statement.setString(6, user.getEmail());
            statement.setBoolean(7, user.isVisibleEmail());
            statement.setString(8, user.getPhone());
            statement.setBoolean(9, user.isVisiblePhone());
            statement.setLong(10, user.getDateOfBirth().getTime());
            statement.setBoolean(11, user.isVisibleDateOfBirth());
            statement.setLong(12, user.getLastSeen().getTime());
            statement.setInt(13, user.getLastSeenMode());
            statement.setBoolean(14, user.isActive());
            statement.setBoolean(15, user.isPrivate());
            statement.setString(16, user.getImageSTR());
            statement.setLong(17, user.getMessengerID());
            statement.setString(18, gson.toJson(user.getTweets()));
            statement.setString(19, gson.toJson(user.getLikedTweets()));
            statement.setString(20, gson.toJson(user.getRequested()));
            statement.setString(21, gson.toJson(user.getFollowers()));
            statement.setString(22, gson.toJson(user.getFollowings()));
            statement.setString(23, gson.toJson(user.getBlocked()));
            statement.setString(24, gson.toJson(user.getMuted()));
            statement.setString(25, gson.toJson(user.getSystemNotifications()));
            statement.setString(26, gson.toJson(user.getRequestsNotifications()));
            statement.setBoolean(27, user.isDeleted());

            statement.executeUpdate();
            statement.close();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't save user :" + user.getId()  + "/ in UsersDB");

            System.err.println("couldn't save user");
        }
    }

    public long lastID()
    {
        try
        {
            logger.warn("getting last ID in users "  + "/ in UsersDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `users` WHERE `id` = ( SELECT MAX(id) FROM `users` ) ");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getLong("id");

            else
                return 0;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get last ID in users "  + "/ in UsersDB");

            System.err.println("couldn't gat max ID in users");
        }

        return 0;
    }

}
