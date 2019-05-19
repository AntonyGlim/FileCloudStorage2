package command;

import client.FilesListManager;

/**
 * Each command must have a method void execute()
 */
public interface Command {
    void execute() throws Exception;

    default FilesListManager getFilesList() throws Exception{
        return FilesListManager.getFilesListManager();
    }
}
