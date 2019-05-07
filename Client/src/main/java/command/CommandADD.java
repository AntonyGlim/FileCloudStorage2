package command;

import client.FileProperties;
import client.FilesListManager;
import common.ConsoleHelper;
import common.exception.FileAlreadyExistException;
import common.exception.PathIsNotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandADD implements Command {
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Добавление нового файла в список файлов, для отправки на сервер.");

            FilesListManager filesListManager = getFilesList();

            ConsoleHelper.writeMessage("Введите полное имя файла для добавления:");

            Path sourcePath = Paths.get(ConsoleHelper.readString());
            if (Files.notExists(sourcePath)) throw new PathIsNotFoundException();
            filesListManager.addFile(new FileProperties(sourcePath));

            ConsoleHelper.writeMessage("Добавление файла завершено успешно.");

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Файл не был найден.");
        } catch (FileAlreadyExistException e){
            ConsoleHelper.writeMessage("Файл с таким именем уже добавлен в список");
        }
    }
}
