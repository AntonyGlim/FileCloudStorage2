package common;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The class will store data about the object.
 * file name
 * file size
 * absolute path
 * add time
 * Is there a file on the disk at the moment?
 * TODO Delete Path - Serialization
 */
public class FileProperties implements Serializable {
    private String name;
    private long size;
    private String absolutePath;
    private Date timeWhenAdd;
    private boolean fileExist;

    /**
     * With the help of The constructor adds a file,
     * the properties of that are stored in the database
     */
    public FileProperties(String name, long size, String absolutePath, Date timeWhenAdd) {
        this.name = name;
        this.size = size;
        this.absolutePath = absolutePath;
        this.timeWhenAdd = timeWhenAdd;
        if(Files.notExists(Paths.get(absolutePath))){
            fileExist = false;
        } else {
            fileExist = true;
        }
    }

    /**
     * With the help of The constructor adds a file,
     * which user write on console
     */
    public FileProperties(String sourcePath) throws IOException {
        this.name = Paths.get(sourcePath).getFileName().toString();
        this.size = Files.size(Paths.get(sourcePath));
        this.absolutePath = sourcePath;
        this.timeWhenAdd = new Date();
        this.fileExist = true;
    }

    /**
     * Refresh information about file
     * @throws IOException
     */
    public void refresh() throws IOException {
        if(Files.notExists(Paths.get(absolutePath))){
            fileExist = false;
        } else {
            fileExist = true;
            this.size = Files.size(Paths.get(absolutePath));
        }
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public Date getTimeWhenAdd() {
        return timeWhenAdd;
    }

    public boolean isFileExist() {
        return fileExist;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(changeStringLength(name, 16));
        builder.append(changeStringLength(((size / 1024) + " "), 8));
        builder.append(" Kb, ");
        builder.append("absolute path: ");
        builder.append(changeStringLength(absolutePath, 32));
        builder.append(", when added: ");
        String date = Constants.simpleDateFormat.format(timeWhenAdd);
        builder.append(changeStringLength(date, 20));
        builder.append(" file exist: ");
        builder.append(isFileExist());
        return builder.toString();
    }

    /**
     * Auxiliary method for more accurate output
     * @param text
     * @param lengthToChange
     * @return
     */
    private String changeStringLength(String text, int lengthToChange){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = text.length(); i < lengthToChange; i++) {
            stringBuilder.append(" ");
        }
        stringBuilder.append(text);
        return stringBuilder.toString();
    }
}
