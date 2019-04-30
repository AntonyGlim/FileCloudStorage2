package command;

import client.FilesList;
import common.ConsoleHelper;

/**
 * Контракт для всех команд, относящихся только к клиенту
 */
public abstract class CommandClientOnly implements Command {
    public FilesList getFilesList() throws Exception{
        return FilesList.getFilesList();
    }
}
