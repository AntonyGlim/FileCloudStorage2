package command;

import client.FilesListManager;

public abstract class CommandClientOnly implements Command {
    public FilesListManager getFilesList() throws Exception{
        return FilesListManager.getFilesListManager();
    }
}
