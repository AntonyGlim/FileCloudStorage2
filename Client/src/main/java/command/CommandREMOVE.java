package command;

import client.FilesListManager;
import common.ConsoleHelper;
import exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandREMOVE extends CommandClientOnly {
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Удаление файла из списока файлов, для отправки на сервер.");

            FilesListManager filesListManager = getFilesList();

            ConsoleHelper.writeMessage("Введите полное имя файла для удаления:");

            Path sourcePath = Paths.get(ConsoleHelper.readString());
            filesListManager.removeFile(sourcePath);

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Файл не был найден.");
        }
    }
}
