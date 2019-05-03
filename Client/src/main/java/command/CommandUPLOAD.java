package command;

import client.Client;
import client.ConnectionManager;
import client.FileProperties;
import client.FilesListManager;
import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import exception.FileAlreadyExistException;
import exception.PathIsNotFoundException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandUPLOAD extends CommandClientOnly {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Отправка файла на сервер.");

            ConsoleHelper.writeMessage("Введите полное имя файла для добавления:");
//
            Path sourcePath = Paths.get(ConsoleHelper.readString());
            if (Files.notExists(sourcePath)) throw new PathIsNotFoundException();
            ConnectionManager.getConnectionManager(null).send(new Message(MessageType.FILE, sourcePath.toFile()));
//            ConnectionManager.getConnectionManager(null).send(new Message(MessageType.FILE, new File("Client/1.jpg")));
            ConsoleHelper.writeMessage("Файла успешно отправлен.");

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Файл не был найден.");
        }
    }
}
