package client;

import common.ConsoleHelper;
import common.FileProperties;
import common.exception.FileAlreadyExistException;
import common.exception.PathIsNotFoundException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The class keeps a list of the files
 * that the user wants to send to the server.
 * Singleton pattern
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
     * Add only if file exist and list does not contain such file
     * Method for adding data with console
     * @param file
     * @throws PathIsNotFoundException
     */
    public void addFile(FileProperties file) throws PathIsNotFoundException, FileAlreadyExistException {
        if(!file.isFileExist()){
            throw new PathIsNotFoundException();
        }
        if (filesListContainsPath(Paths.get(file.getAbsolutePath()))){
            throw new FileAlreadyExistException();
        } else {
            this.filesList.add(file);
        }
    }

    /**
     * Add all files from DB,
     * it`s does not matter if file exist or not
     * @param file
     * @throws PathIsNotFoundException
     */
    public void addFileFromDB(FileProperties file) throws PathIsNotFoundException {
        this.filesList.add(file);
    }

    /**
     * Delete file from list
     * @param sourcePath
     */
    public void removeFile(Path sourcePath){
        for (int i = 0; i < filesList.size(); i++) {
            if (filesList.get(i).getAbsolutePath().equals(sourcePath.toString())){
                ConsoleHelper.writeMessage(String.format("Файл %s удален.", filesList.get(i).getAbsolutePath()));
                filesList.remove(i);
                return;
            }
        }
        ConsoleHelper.writeMessage(String.format("Файл %s не найден.", sourcePath));
    }

    /**
     * Refresh size and status isExist
     * @throws IOException
     */
    public void refreshFilesList() throws IOException {
        for (FileProperties file : filesList) {
            file.refresh();
        }
    }

    public int size(){
        return filesList.size();
    }

    public List<FileProperties> getFilesList() {
        return filesList;
    }

    /** If list contains such absolute path*/
    public boolean filesListContainsPath(Path sourcePath){
        for (FileProperties file : filesList) {
            if (file.getAbsolutePath().equals(sourcePath.toString())){
                return true;
            }
        }
        return false;
    }

}
