package client;

import java.nio.file.Path;
import java.util.Date;

/**
 * В классе будут храниться данные о объекте
 * имя файла
 * размер файла
 * абсолютный путь
 * время добавления
 */
public class FileProperties {
    private String name;
    private long size;
    private Path absolutePath;
    private Date timeWhenAdd;

    public FileProperties(String name, long size, Path absolutePath, Date timeWhenAdd) {
        this.name = name;
        this.size = size;
        this.absolutePath = absolutePath;
        this.timeWhenAdd = timeWhenAdd;
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
        builder.append(timeWhenAdd.getTime());
        return builder.toString();
    }
}
