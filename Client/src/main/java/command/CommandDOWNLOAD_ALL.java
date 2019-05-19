package command;

import common.ConsoleHelper;
import common.FileProperties;

/**
 * Download all files (which lays on client folder) from server
 */
public class CommandDOWNLOAD_ALL implements Command {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Загрузка всех имеющихся файлов с сервера.");
        for (FileProperties file : CommandFILE_LIST.getFilesListFromServer()) {
            CommandDOWNLOAD.downloadFile(file.getName());
        }
    }
}
