package command;

import client.FilesList;
import common.ConsoleHelper;
import exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandCONTENT extends CommandClientOnly {
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Просмотр списока файлов, для отправки на сервер.");

            FilesList filesList = getFilesList();

            for (Path path : filesList.getFileList()) {
                ConsoleHelper.writeMessage(path.toString());
            }

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Файл не был найден.");
        }
    }
}
