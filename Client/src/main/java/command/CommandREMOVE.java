package command;

import client.FilesListManager;
import common.ConsoleHelper;
import common.exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Delete file name from list of files names which is on the user side
 */
public class CommandREMOVE implements Command {
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Удаление файла из списока файлов, для отправки на сервер.");

            FilesListManager filesListManager = getFilesList();

            ConsoleHelper.writeMessage("Введите полное имя файла для удаления:");

            Path sourcePath = Paths.get(ConsoleHelper.readString());
            filesListManager.removeFile(sourcePath);

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeError("Файл не был найден.");
        }
    }
}
