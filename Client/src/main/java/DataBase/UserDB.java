package DataBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.config.Config;
import shared.form.UserInfoForm;
import shared.model.User;
import util.Constants;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class UserDB implements DBSet<UserInfoForm> {

    private static final Config config = new Config(Constants.CONFIG_ADDRESS);
    private static final File gsonFolder = new File(config.getProperty(String.class, "UsersDirectory").orElse(""));
    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM dd, yyyy, HH:mm").setPrettyPrinting().create();

    @Override
    public UserInfoForm get(long id) {
        try
        {
            for (File file : Objects.requireNonNull(gsonFolder.listFiles()))
            {
                if(file.getName().equals(Long.toString(id))) {
                    FileReader fileReader = new FileReader(file.getCanonicalPath());
                    UserInfoForm form = gson.fromJson(fileReader, UserInfoForm.class);
                    fileReader.close();

                    return form;
                }
            }
        }

        catch (Exception e)
        {
            System.err.println("failed to load user by ID");
        }

        return null;
    }

    public UserInfoForm getByUsername(String username) {

        try {
            for (File file : Objects.requireNonNull(gsonFolder.listFiles())) {
                FileReader fileReader = new FileReader(file.getCanonicalPath());
                UserInfoForm form = gson.fromJson(fileReader, UserInfoForm.class);
                fileReader.close();

                if (form.getUsername().equals(username))
                    return form;
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void add(UserInfoForm form) {

        try {
            File file = new File(gsonFolder.getPath() + "/" + form.getUserID());
            FileWriter fileWriter = new FileWriter(file.getCanonicalPath());
            gson.toJson(form , fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }

        catch (Exception e)
        {
            System.err.println("failed to add group");
        }
    }

    public void add(User user)
    {
        UserInfoForm form = get(user.getId());

        if(form == null)
            form = new UserInfoForm();

        form.setUserID(user.getId());
        form.setUsername(user.getUsername());
        form.setName(user.getName());
        form.setLastName(user.getLastName());
        form.setPass(user.getPassword());
        form.setPicSTR(user.getImageSTR());
        form.setPrivate(user.isPrivate());
        form.setLastSeenMode(user.getLastSeenMode());

        try
        {
            File file = new File(gsonFolder.getPath() + "/" + form.getUserID());
            FileWriter fileWriter = new FileWriter(file.getCanonicalPath());
            gson.toJson(form , fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }

        catch (Exception e)
        {
            System.err.println("failed to add group");
        }
    }

    @Override
    public void update(UserInfoForm form) {

        try {
            File file = new File(gsonFolder.getPath() + "/" + form.getUserID());
            FileWriter fileWriter = new FileWriter(file.getCanonicalPath());
            gson.toJson(form , fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }

        catch (Exception e)
        {
            System.err.println("failed to update user");
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
                UserInfoForm user = gson.fromJson(fileReader, UserInfoForm.class);
                fileReader.close();

                lastID = Math.max(lastID , user.getUserID());
            }
        }

        catch (Exception e)
        {
            System.err.println("failed to load Last ID");
        }

        return lastID;
    }

}
