package DataBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.Group;
import shared.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GroupsDB {
    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM dd, yyyy, HH:mm").setPrettyPrinting().create();
    private Connection connection;
    static private final Logger logger = LogManager.getLogger(GroupsDB.class);

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean groupExists(long groupID)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM `groups` WHERE `groupID` = ?");
            statement.setLong(1, groupID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't check group by ID : " + groupID + "/ in GroupsDB");
            System.err.println("couldn't check group by ID");
        }

        return false;
    }

    public Group get(long groupID)
    {
        try
        {
            logger.warn("getting group by ID : " + groupID + "/ in GroupsDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `groups` WHERE `groupID` = ?");
            statement.setLong(1, groupID);
            ResultSet resultSet = statement.executeQuery();

            Group group = null;

            while (resultSet.next()) {
                group = new Group();
                group.setGroupID(resultSet.getLong("groupID"));
                group.setName(resultSet.getString("name"));
                group.setMembers(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("members"), Pair[].class))));
                group.setMessages(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("messages"), Long[].class))));
                group.setScheduledMessages(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("scheduledMessages"), Long[].class))));
            }

            return group;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get group by ID : " + groupID + "/ in GroupsDB");
            System.err.println("couldn't get group by ID");
        }

        return null;
    }

    public Group getByName(String name)
    {
        try
        {
            logger.warn("getting group by name : " + name + "/ in GroupsDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `groups` WHERE `name` = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            Group group = null;

            while (resultSet.next()) {
                group = new Group();
                group.setGroupID(resultSet.getLong("groupID"));
                group.setName(resultSet.getString("name"));
                group.setMembers(new LinkedList<>(List.of(gson.fromJson(resultSet.getString("members"), Pair[].class))));
                group.setMessages(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("messages"), Long[].class))));
                group.setScheduledMessages(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("scheduledMessages"), Long[].class))));
            }

            return group;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get group by name : " + name + "/ in GroupsDB");
            System.err.println("couldn't get group by name");
        }

        return null;
    }

    public void save(Group group)
    {
        try
        {
            logger.warn("saving group : " + group.getGroupID() + "/ in GroupsDB");

            PreparedStatement statement;

            if (groupExists(group.getGroupID())) {
                statement = connection.prepareStatement(
                        "UPDATE `groups` SET `name` = ?, `members` = ?, `messages` = ?, `scheduledMessages` = ?" +
                                "WHERE `groupID` = " + group.getGroupID());
            }

            else {
                statement = connection.prepareStatement(
                        "INSERT INTO `groups` (`name`, `members`, `messages`, `scheduledMessages`)" +
                                "VALUES (?, ?, ?, ?)");
            }

            statement.setString(1, group.getName());
            statement.setString(2, gson.toJson(group.getMembers()));
            statement.setString(3, gson.toJson(group.getMessages()));
            statement.setString(4, gson.toJson(group.getScheduledMessages()));

            statement.executeUpdate();
            statement.close();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't save group : " + group.getGroupID() + "/ in GroupsDB");
            exception.printStackTrace();
        }
    }

    public long lastID()
    {
        try
        {
            logger.warn("getting last ID in groups "  + "/ in GroupsDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `groups` WHERE `groupID` = ( SELECT MAX(groupID) FROM `groups` ) ");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getLong("groupID");

            else
                return 0;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get last ID in groups :" + "/ in GroupsDB");
            System.err.println("couldn't gat max ID in groups");
        }

        return 0;
    }
}
