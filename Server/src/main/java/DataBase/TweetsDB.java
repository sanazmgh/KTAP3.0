package DataBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.Tweet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

public class TweetsDB {

    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM dd, yyyy, HH:mm").setPrettyPrinting().create();
    private Connection connection;
    static private final Logger logger = LogManager.getLogger(TweetsDB.class);

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean tweetExist(long tweetsID)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM `tweets` WHERE `id` = ?");
            statement.setLong(1, tweetsID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't check tweet by ID : " + tweetsID + "/ in TweetsDB");

            System.err.println("couldn't check tweet by ID");
        }

        return false;
    }

    public Tweet get(Long tweetID)
    {
        try
        {
            logger.warn("getting tweet by ID : " + tweetID + "/ in TweetsDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `tweets` WHERE `id` = ?");
            statement.setLong(1, tweetID);
            ResultSet resultSet = statement.executeQuery();

            Tweet tweet = null;

            while (resultSet.next()) {
                tweet = new Tweet();
                tweet.setId(resultSet.getLong("id"));
                tweet.setUserId(resultSet.getLong("userId"));
                tweet.setRetweetFromUserId(resultSet.getLong("retweetFromUserId"));
                tweet.setTweetText(resultSet.getString("tweetText"));
                tweet.setTweetImageSTR(resultSet.getString("tweetImageSTR"));
                tweet.setDateTweeted(new Date(resultSet.getLong("dateTweeted")));
                tweet.setCommented(resultSet.getBoolean("commented"));
                tweet.setNoOfReports(resultSet.getInt("noOfReports"));
                tweet.setComments(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("comments"), Long[].class))));
                tweet.setLikes(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("likes"), Long[].class))));
                tweet.setRetweets(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("retweets"), Long[].class))));
                tweet.setDeleted(resultSet.getBoolean("deleted"));
            }

            return tweet;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get tweet by ID : " + tweetID + "/ in TweetsDB");
            System.err.println("couldn't get tweet by ID");
        }

        return null;
    }

    public void save(Tweet tweet)
    {
        try
        {
            logger.warn("saving tweet : " + tweet.getId() + "/ in TweetsDB");

            PreparedStatement statement;

            if (tweetExist(tweet.getId())) {
                statement = connection.prepareStatement(
                        "UPDATE `tweets` SET `userId` = ?, `retweetFromUserId` = ?, `tweetText` = ?, `tweetImageSTR` = ?, `dateTweeted` = ?, `commented` = ?, " +
                                "`noOfReports` = ?, comments = ?,  `likes` = ?, `retweets` = ?, `deleted` = ? " +
                                "WHERE `id` = " + tweet.getId());
            }

            else {
                statement = connection.prepareStatement(
                        "INSERT INTO `tweets` (`userId`, `retweetFromUserId`, `tweetText`, `tweetImageSTR`, `dateTweeted`, `commented`, " +
                                "`noOfReports`, `comments`,  `likes`, `retweets`, `deleted`)" +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            }
            statement.setLong(1, tweet.getUserId());
            statement.setLong(2, tweet.getRetweetFromUserId());
            statement.setString(3, tweet.getTweetText());
            statement.setString(4, tweet.getTweetImageSTR());
            statement.setLong(5, tweet.getDateTweeted().getTime());
            statement.setBoolean(6, tweet.isCommented());
            statement.setInt(7, tweet.getNoOfReports());
            statement.setString(8, gson.toJson(tweet.getComments()));
            statement.setString(9, gson.toJson(tweet.getLikes()));
            statement.setString(10, gson.toJson(tweet.getRetweets()));
            statement.setBoolean(11, tweet.isDeleted());

            statement.executeUpdate();
            statement.close();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't save tweet : " + tweet.getId() + "/ in TweetsDB");
            exception.printStackTrace();
        }
    }

    public long lastID()
    {
        try
        {
            logger.warn("getting last ID in tweets" + "/ in TweetsDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `tweets` WHERE `id` = ( SELECT MAX(id) FROM `tweets` ) ");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getLong("id");

            else
                return 0;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't gat last ID in tweets" + "/ in TweetsDB");

            System.err.println("couldn't gat max ID in tweets");
        }

        return 0;
    }
}
