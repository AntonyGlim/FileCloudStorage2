package command;

import client.FilesList;
import common.ConsoleHelper;

public class CommandREFRESH extends CommandClientOnly  {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Обновление списока файлов, для отправки на сервер.");

        FilesList filesList = getFilesList();

        filesList.refreshList();

        ConsoleHelper.writeMessage("Список файлов, для отправки на сервер обновлен.");

        CommandCONTENT commandCONTENT = new CommandCONTENT();
        commandCONTENT.execute();
    }
}
