package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * В классе будут храниться данные о объекте
 * имя файла
 * размер файла
 * абсолютный путь
 * время добавления
 * существует-ли файл на диске в данный момент
 */
public class FileProperties {
    private String name;
    private long size;
    private Path absolutePath;
    private Date timeWhenAdd;
    private boolean fileExist;

    /**
     * With the help of The constructor adds a file,
     * the properties of that are stored in the database
     */
    public FileProperties(String name, long size, Path absolutePath, Date timeWhenAdd) {
        this.name = name;
        this.size = size;
        this.absolutePath = absolutePath;
        this.timeWhenAdd = timeWhenAdd;
        if(Files.notExists(absolutePath)){
            fileExist = false;
        } else {
            fileExist = true;
        }
    }

    /**
     * With the help of The constructor adds a file,
     * which user write on console
     */
    public FileProperties(Path sourcePath) throws IOException {
        this.name = sourcePath.getFileName().toString();
        this.size = Files.size(sourcePath);
        this.absolutePath = sourcePath;
        this.timeWhenAdd = new Date();
        this.fileExist = true;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public Path getAbsolutePath() {
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
        // Строим красивую строку из свойств
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("\t");
        builder.append(size / 1024);
        builder.append(" Kb, ");
        builder.append("absolute path: ");
        builder.append(absolutePath);
        builder.append(", when added: ");
        String pattern = "yyyy.MM.dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(timeWhenAdd);
        builder.append(date);
        builder.append(" file exist: ");
        builder.append(isFileExist());
        return builder.toString();
    }
}
