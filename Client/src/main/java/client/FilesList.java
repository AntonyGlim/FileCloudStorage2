package client;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The class keeps a list of the files
 * that the user wants to send to the server.
 */
public class FilesList {

    private static FilesList filesList;
    private List<Path> fileList;

    private FilesList() {
        this.fileList = new ArrayList<>();
    }

    public static synchronized FilesList getFilesList(){
        if (filesList == null) filesList = new FilesList();
        return filesList;
    }

    public void addFiles(List<Path> fileList){
        for (Path path : fileList) {
            addFile(path);
        }
    }

    public void addFile(Path file){
        this.fileList.add(file);
    }

    public void removeFiles(List<Path> fileList){
        for (Path path : fileList) {
            if (this.fileList.contains(path))
                this.fileList.remove(path);
        }
    }

    public List<Path> getFileList() {
        return fileList;
    }

    public void setFileList(List<Path> fileList) {
        this.fileList = fileList;
    }
}
