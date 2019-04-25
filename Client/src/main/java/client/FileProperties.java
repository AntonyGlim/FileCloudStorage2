package client;

import java.io.IOException;
import java.nio.file.Files;
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
    private boolean fileExist;

    /**
     * Конструктор для извлечения файлов из БД
     * @param name
     * @param size
     * @param absolutePath
     * @param timeWhenAdd
     */
    public FileProperties(String name, long size, Path absolutePath, Date timeWhenAdd) {
        this.name = name;
        this.size = size;
        this.absolutePath = absolutePath;
        this.timeWhenAdd = timeWhenAdd;
        this.fileExist = true;
    }

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

    public void setFileExist(boolean fileExist) {
        this.fileExist = fileExist;
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
        builder.append(" file exist: ");
        builder.append(isFileExist());
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
/*
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if ((o == null) || o.getClass() != this.getClass()) return false;
        FileProperties fileProperties = (FileProperties) o;
        if (!(o instanceof FileProperties)) return false;
        return (name == fileProperties.name || (name != null && name.equals(fileProperties.name)))
                && size == fileProperties.size
                && (absolutePath == fileProperties.absolutePath || (absolutePath != null && absolutePath.equals(fileProperties.absolutePath)));
    }
*/

    /**
     * Метод сравнивает только абсолютные пути к файлам
     * @param o
     * @return
     */
    public boolean equalsByAbsolutePath(Object o) {
        if (o == this) return true;
        if ((o == null) || o.getClass() != this.getClass()) return false;
        FileProperties fileProperties = (FileProperties) o;
        if (!(o instanceof FileProperties)) return false;
        return (absolutePath == fileProperties.absolutePath ||
                (absolutePath != null && absolutePath.equals(fileProperties.absolutePath)));
    }
}
