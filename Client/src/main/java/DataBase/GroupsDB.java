package DataBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.config.Config;
import shared.form.GroupDataForm;
import util.Constants;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Objects;

public class GroupsDB implements DBSet<GroupDataForm> {

    private static final Config config = new Config(Constants.CONFIG_ADDRESS);
    private static final File gsonFolder = new File(config.getProperty(String.class, "GroupsDirectory").orElse(""));
    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM dd, yyyy, HH:mm").setPrettyPrinting().create();

    @Override
    public GroupDataForm get(long id) {
        try
        {
            for (File file : Objects.requireNonNull(gsonFolder.listFiles()))
            {
                if(file.getName().equals(Long.toString(id))) {
                    FileReader fileReader = new FileReader(file.getCanonicalPath());
                    GroupDataForm form = gson.fromJson(fileReader, GroupDataForm.class);
                    fileReader.close();

                    return form;
                }
            }
        }

        catch (Exception e)
        {
            System.err.println("failed to load group by ID");
        }

        return null;
    }

    @Override
    public void add(GroupDataForm group) {

        try {
            File file = new File(gsonFolder.getPath() + "/" + group.getGpID());
            FileWriter fileWriter = new FileWriter(file.getCanonicalPath());
            gson.toJson(group , fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }

        catch (Exception e)
        {
            System.err.println("failed to add group");
        }
    }

    @Override
    public void update(GroupDataForm group) {
        try {
            //System.out.println(group.getMessages());
            File file = new File(gsonFolder.getPath() + "/" + group.getGpID());
            FileWriter fileWriter = new FileWriter(file.getCanonicalPath());
            gson.toJson(group , fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }

        catch (Exception e)
        {
            System.err.println("failed to update group");
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
                GroupDataForm group = gson.fromJson(fileReader, GroupDataForm.class);
                fileReader.close();

                lastID = Math.max(lastID , group.getGpID());
            }
        }

        catch (Exception e)
        {
            System.err.println("failed to load lastID");
        }

        return lastID;
    }
}
