package command;

import client.FilesList;
import common.ConsoleHelper;

public abstract class CommandClientOnly implements Command {
    public FilesList getFilesList() throws Exception{
        return FilesList.getFilesList();
    }
}
