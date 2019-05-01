package client;

import common.ConsoleHelper;
import exception.PathIsNotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The class keeps a list of the files
 * that the user wants to send to the server.
 */
public class FilesList {

    private static FilesList filesList;
    private List<FileProperties> fileList;

    private FilesList() {
        this.fileList = new ArrayList<>();
    }

    public static synchronized FilesList getFilesList(){
        if (filesList == null) filesList = new FilesList();
        return filesList;
    }

    public void addFiles(List<FileProperties> fileList) throws PathIsNotFoundException {
        for (FileProperties fileProperty : fileList) {
            addFile(fileProperty);
        }
    }

    /**
     * Метод добавляет только существующие файлы
     * @param file
     * @throws PathIsNotFoundException
     */
    public void addFile(FileProperties file) throws PathIsNotFoundException {
        if(Files.notExists(file.getAbsolutePath())){
            throw new PathIsNotFoundException();
        } else {
            this.fileList.add(file);
        }
    }

    /**
     * Метод отличается от public void addFile(FileProperties file) тем,
     * что добавляет все файлы из базы данных, не зависимо от того,
     * найден-ли он на ПК или нет.
     * @param file
     * @throws PathIsNotFoundException
     */
    public void addFileFromDB(FileProperties file) throws PathIsNotFoundException {
        setFileExistMethod(file);
        this.fileList.add(file);
    }

    public void removeFiles(List<FileProperties> fileList){
        for (FileProperties fileProperty : fileList) {
            if (this.fileList.contains(fileProperty))
                this.fileList.remove(fileProperty);
        }
    }

    public void removeFile(Path sourcePath){
        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).getAbsolutePath().equals(sourcePath)){
                ConsoleHelper.writeMessage(String.format("Файл %s удален.", fileList.get(i).getAbsolutePath()));
                fileList.remove(i);
                return;
            }
        }
        ConsoleHelper.writeMessage(String.format("Файл %s не найден.", sourcePath));
    }

    public void refreshList(){
        for (FileProperties file : fileList) {
            setFileExistMethod(file);
        }

    }

    public int size(){
        return fileList.size();
    }

    public List<FileProperties> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileProperties> fileList) {
        this.fileList = fileList;
    }

    private void setFileExistMethod(FileProperties file){
        if(Files.notExists(file.getAbsolutePath())){
            file.setFileExist(false);
        } else {
            file.setFileExist(true);
        }
    }
}
