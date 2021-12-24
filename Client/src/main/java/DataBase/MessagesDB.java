package DataBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.config.Config;
import shared.model.Message;
import util.Constants;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class MessagesDB implements DBSet<Message>
{
    private static final Config config = new Config(Constants.CONFIG_ADDRESS);
    private static final File gsonFolder = new File(config.getProperty(String.class, "MessagesDirectory").orElse(""));
    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM dd, yyyy, HH:mm").setPrettyPrinting().create();

    @Override
    public Message get(long id) {
        try
        {
            for (File file : Objects.requireNonNull(gsonFolder.listFiles()))
            {
                if(file.getName().equals(Long.toString(id))) {
                    FileReader fileReader = new FileReader(file.getCanonicalPath());
                    Message message = gson.fromJson(fileReader, Message.class);
                    fileReader.close();
                    return message;
                }
            }
        }

        catch (Exception e)
        {
            System.err.println("failed to load Message by ID");
        }

        return null;
    }

    @Override
    public void add(Message message) {

        try {
            File file = new File(gsonFolder.getPath() + "/" + message.getMessageID());
            FileWriter fileWriter = new FileWriter(file.getCanonicalPath());
            gson.toJson(message , fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }

        catch (Exception e)
        {
            System.err.println("failed to add message");
        }
    }

    @Override
    public void update(Message message) {

        try {
            File file = new File(gsonFolder.getPath() + "/" + message.getMessageID());
            FileWriter fileWriter = new FileWriter(file.getCanonicalPath());
            gson.toJson(message , fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }

        catch (Exception e)
        {
            System.err.println("failed to update message");
        }
    }

    @Override
    public long lastID() {

        long lastID = 0;

        try
        {
            for (File file : Objects.requireNonNull(gsonFolder.listFiles()))
            {
                FileReader fileReader = new FileReader(file.getCanonicalPath());
                Message message = gson.fromJson(fileReader, Message.class);
                fileReader.close();

                lastID = Math.max(lastID , message.getMessageID());
            }
        }

        catch (Exception e)
        {
            System.err.println("failed to load Last ID");
        }

        return lastID;
    }
}
