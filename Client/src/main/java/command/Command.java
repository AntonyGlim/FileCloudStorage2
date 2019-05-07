package command;

import client.FilesListManager;

/**
 * Каждая команда должна иметь метод void execute()
 * TODO возможно перенести его в общий модуль
 */
public interface Command {
    void execute() throws Exception;

    default FilesListManager getFilesList() throws Exception{
        return FilesListManager.getFilesListManager();
    }
}
