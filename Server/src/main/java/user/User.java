package user;

import common.FileProperties;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class User {
    private int name;
    private int password;
    private long registration_date; //the same names that in DB
    private long time_last_visit;
    private String folderName;
    private ArrayList<FileProperties> fileList;

    public User(int name, int password, long registration_date, long time_last_visit) {
        this.name = name;
        this.password = password;
        this.registration_date = registration_date;
        this.time_last_visit = time_last_visit;
        this.folderName = "Server/server_storage/" + this.name + "/";
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public long getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(long registration_date) {
        this.registration_date = registration_date;
    }

    public long getTime_last_visit() {
        return time_last_visit;
    }

    public void setTime_last_visit(long time_last_visit) {
        this.time_last_visit = time_last_visit;
    }

    public String getFolderName() {
        return folderName;
    }

    public ArrayList<FileProperties> getFileList() {
        fileList = new ArrayList<>();
        File folder = new File(folderName);
        for (File file : folder.listFiles())
        {
            fileList.add(new FileProperties(file.getName(), file.length(), file.getAbsolutePath(), new Date(file.lastModified())));
            System.out.println(file.getName());
        }
        return fileList;
    }

    @Override
    public String toString() {
        return name + " " + registration_date + " " + time_last_visit;
    }
}
