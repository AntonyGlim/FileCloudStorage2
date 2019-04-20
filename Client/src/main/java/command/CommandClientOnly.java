package command;

import client.FileManager;
import client.FilesList;
import command.Command;
import common.ConsoleHelper;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class CommandClientOnly implements Command {
    public FilesList getFilesList() throws Exception{
        return FilesList.getFilesList();
    }
}
