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

    public void addFile(FileProperties file) throws PathIsNotFoundException {
        if(Files.notExists(file.getAbsolutePath())){
            throw new PathIsNotFoundException();
        } else {
            this.fileList.add(file);
        }
    }

    public void removeFiles(List<FileProperties> fileList){
        for (FileProperties fileProperty : fileList) {
            if (this.fileList.contains(fileProperty))
                this.fileList.remove(fileProperty);
        }
    }

    public void removeFile(FileProperties fileProperty){
        if (this.fileList.contains(fileProperty))
            this.fileList.remove(fileProperty);
        //TODO else - "thear is not such file"

    }

    public List<FileProperties> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileProperties> fileList) {
        this.fileList = fileList;
    }
}
