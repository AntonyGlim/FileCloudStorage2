package command;

import client.FilesListManager;
import common.ConsoleHelper;

public class CommandREFRESH extends CommandClientOnly  {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Обновление списока файлов, для отправки на сервер.");

        FilesListManager filesListManager = getFilesList();

//        filesListManager.refreshList();

        ConsoleHelper.writeMessage("Список файлов, для отправки на сервер обновлен.");

        CommandCONTENT commandCONTENT = new CommandCONTENT();
        commandCONTENT.execute();
    }
}
