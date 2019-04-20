package command;

import client.FileManager;
import command.Command;

public abstract class CommandClientOnly implements Command {
    public FileManager getFileManager() throws Exception{
        return new FileManager();
    }
}
