package command;

import common.FileProperties;
import client.FilesListManager;
import common.ConsoleHelper;
import common.exception.PathIsNotFoundException;

/**
 * Print list of files names which is on the user side
 */
public class CommandCONTENT implements Command {
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Просмотр списока файлов, для отправки на сервер.");

            FilesListManager filesListManager = getFilesList();

            for (FileProperties file : filesListManager.getFilesList()) {
                ConsoleHelper.writeMessage(file.toString());
            }

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeError("Файл не был найден.");
        }
    }
}
