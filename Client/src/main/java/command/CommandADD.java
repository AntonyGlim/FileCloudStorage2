package command;

import client.FileManager;
import client.FilesList;
import common.ConsoleHelper;
import exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandADD extends CommandClientOnly {
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Добавление нового файла в список файлов, для отправки на сервер.");

            FilesList filesList = getFilesList();

            ConsoleHelper.writeMessage("Введите полное имя файла для добавления:");
            Path sourcePath = Paths.get(ConsoleHelper.readString());

            filesList.addFile(sourcePath);

            ConsoleHelper.writeMessage("Добавление файла завершено успешно.");

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Файл не был найден.");
        }
    }
}
