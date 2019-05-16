package command;

import client.FilesListManager;
import common.ConsoleHelper;
import common.FileProperties;

import java.nio.file.Paths;

public class CommandUPLOADALL implements Command  {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Отправка файлов на сервер.");

        FilesListManager filesListManager = getFilesList();
        for (FileProperties file : filesListManager.getFilesList()) {
            CommandUPLOAD.sendFile(Paths.get(file.getAbsolutePath()));
        }
    }
}
