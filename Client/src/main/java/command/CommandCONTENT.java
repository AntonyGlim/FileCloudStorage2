package command;

import client.FileProperties;
import client.FilesListManager;
import common.ConsoleHelper;
import common.exception.PathIsNotFoundException;

public class CommandCONTENT implements Command {
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Просмотр списока файлов, для отправки на сервер.");

            FilesListManager filesListManager = getFilesList();

            for (FileProperties file : filesListManager.getFilesList()) {
                ConsoleHelper.writeMessage(file.toString());
            }

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Файл не был найден.");
        }
    }
}
