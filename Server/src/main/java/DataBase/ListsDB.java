package DataBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

public class ListsDB {
    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM dd, yyyy, HH:mm").setPrettyPrinting().create();
    private Connection connection;
    static private final Logger logger = LogManager.getLogger(ListsDB.class);

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean listExists(long listID)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM `lists` WHERE `listID` = ?");
            statement.setLong(1, listID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't check list by ID : " + listID + "/ in ListsDB");
            System.err.println("couldn't check list by ID");
        }

        return false;
    }

    public List get(long listID)
    {
        try
        {
            logger.warn("getting list by ID : " + listID + "/ in ListsDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `lists` WHERE `listID` = ?");
            statement.setLong(1, listID);
            ResultSet resultSet = statement.executeQuery();

            List list = null;

            while (resultSet.next()) {
                list = new List();
                list.setListID(resultSet.getLong("listID"));
                list.setName(resultSet.getString("name"));
                list.setMembers(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("members"), Long[].class))));
            }

            return list;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get list by ID : " + listID + "/ in ListsDB");
            System.err.println("couldn't get list by ID");
        }

        return null;
    }

    public List getByName(String name)
    {
        try
        {
            logger.warn("getting list by name : " + name + "/ in ListsDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `lists` WHERE `name` = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            List list = null;

            while (resultSet.next()) {
                list = new List();
                list.setListID(resultSet.getLong("listID"));
                list.setName(resultSet.getString("name"));
                list.setMembers(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("members"), Long[].class))));
            }

            return list;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get list by name : " + name + "/ in ListsDB");

            System.err.println("couldn't get list by name");
        }

        return null;
    }

    public void save(List list)
    {
        try
        {
            logger.warn("saving list : " + list.getListID() + "/ in ListsDB");

            PreparedStatement statement;

            if (listExists(list.getListID()))
            {
                statement = connection.prepareStatement(
                        "UPDATE `lists` SET `name` = ?, `members` = ?" +
                                "WHERE `listID` = " + list.getListID());
            }

            else
            {
                statement = connection.prepareStatement(
                        "INSERT INTO `lists` (`name`, `members`)" +
                                "VALUES (?, ?)");
            }

            statement.setString(1, list.getName());
            statement.setString(2, gson.toJson(list.getMembers()));

            statement.executeUpdate();
            statement.close();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't save list : " + list.getListID() + "/ in ListsDB");
            System.err.println("couldn't save list");
        }
    }

    public long lastID()
    {
        try
        {
            logger.warn("getting last ID in lists " + "/ in ListsDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `lists` WHERE `listID` = ( SELECT MAX(listID) FROM `lists` ) ");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getLong("listID");

            else
                return 0;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get last ID in lists" + "/ in ListsDB");
            System.err.println("couldn't gat max ID in lists");
        }

        return 0;
    }
}
