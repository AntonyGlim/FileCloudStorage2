package client;

import common.ConsoleHelper;
import exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The class keeps a list of the files
 * that the user wants to send to the server.
 */
public class FilesListManager {

    private static FilesListManager filesListManager;
    private List<FileProperties> filesList;

    private FilesListManager() {
        this.filesList = new ArrayList<>();
    }

    public static synchronized FilesListManager getFilesListManager(){
        if (filesListManager == null) filesListManager = new FilesListManager();
        return filesListManager;
    }

    /**
     * Метод добавляет только существующие файлы
     * @param file
     * @throws PathIsNotFoundException
     */
    public void addFile(FileProperties file) throws PathIsNotFoundException {
        if(!file.isFileExist()){
            throw new PathIsNotFoundException();
        } else {
            this.filesList.add(file);
        }
    }

    /**
     * Метод добавляет все файлы из базы данных,
     * не зависимо от того, найден-ли он на ПК или нет.
     * @param file
     * @throws PathIsNotFoundException
     */
    public void addFileFromDB(FileProperties file) throws PathIsNotFoundException {
        this.filesList.add(file);
    }

    public void removeFile(Path sourcePath){
        for (int i = 0; i < filesList.size(); i++) {
            if (filesList.get(i).getAbsolutePath().equals(sourcePath)){
                ConsoleHelper.writeMessage(String.format("Файл %s удален.", filesList.get(i).getAbsolutePath()));
                filesList.remove(i);
                return;
            }
        }
        ConsoleHelper.writeMessage(String.format("Файл %s не найден.", sourcePath));
    }

    public void refreshFilesList(){
        for (FileProperties file : filesList) {
//            setFileExistMethod(file);
        }

    }

    public int size(){
        return filesList.size();
    }

    public List<FileProperties> getFilesList() {
        return filesList;
    }

}
