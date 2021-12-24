package shared.form;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;

public class UserInfoForm {
    private long userID;
    private String username;
    private String name;
    private String lastName;
    private String pass;
    private String pass2;
    private String email;
    private boolean visibleEmail;
    private String phone;
    private boolean visiblePhone;
    private String bio;
    private Date dateOfBirth;
    private boolean visibleDate;
    private String picSTR;
    private LinkedList<Long> groups = new LinkedList<>();
    private boolean isPrivate;
    private int lastSeenMode;

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVisibleEmail(boolean visibleEmail) {
        this.visibleEmail = visibleEmail;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setVisiblePhone(boolean visiblePhone) {
        this.visiblePhone = visiblePhone;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if(dateOfBirth != null)
            this.dateOfBirth = java.sql.Date.valueOf(dateOfBirth);
    }

    public void setVisibleDate(boolean visibleDate) {
        this.visibleDate = visibleDate;
    }

    public void setPicSTR(String picSTR) {
        this.picSTR = picSTR;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPass() {
        return pass;
    }

    public String getPass2() {
        return pass2;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getBio() {
        return bio;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public boolean isVisibleEmail() {
        return visibleEmail;
    }

    public boolean isVisiblePhone() {
        return visiblePhone;
    }

    public boolean isVisibleDate() {
        return visibleDate;
    }

    public String getPicSTR() {
        return picSTR;
    }

    public LinkedList<Long> getGroups() {
        return groups;
    }

    public void setGroups(LinkedList<Long> groups) {
        this.groups = groups;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public int getLastSeenMode() {
        return lastSeenMode;
    }

    public void setLastSeenMode(int lastSeenMode) {
        this.lastSeenMode = lastSeenMode;
    }
}
