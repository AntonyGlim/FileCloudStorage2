package command;

import client.FileProperties;
import client.FilesList;
import common.ConsoleHelper;
import exception.PathIsNotFoundException;

public class CommandCONTENT extends CommandClientOnly {
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Просмотр списока файлов, для отправки на сервер.");

            FilesList filesList = getFilesList();

            for (FileProperties file : filesList.getFileList()) {
                ConsoleHelper.writeMessage(file.toString());
            }

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Файл не был найден.");
        }
    }
}
