package command;

import client.FilesListManager;
import common.ConsoleHelper;

/**
 * Refresh all files from list of files names which is on the user side
 */
public class CommandREFRESH implements Command {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Обновление списока файлов, для отправки на сервер.");

        FilesListManager filesListManager = getFilesList();

        filesListManager.refreshFilesList();

        ConsoleHelper.writeMessage("Список файлов, для отправки на сервер обновлен.");

        CommandCONTENT commandCONTENT = new CommandCONTENT();
        commandCONTENT.execute();
    }
}
