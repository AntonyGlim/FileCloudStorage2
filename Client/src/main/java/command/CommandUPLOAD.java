package command;

import client.ConnectionManager;
import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import common.exception.PathIsNotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandUPLOAD extends CommandClientOnly {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Отправка файла на сервер.");

            ConsoleHelper.writeMessage("Введите полное имя файла для добавления:");

            Path sourcePath = Paths.get(ConsoleHelper.readString());
            if (Files.notExists(sourcePath)) throw new PathIsNotFoundException();
            ConnectionManager.getConnectionManager(null).send(new Message(MessageType.FILE, sourcePath.toFile()));
//            ConnectionManager.getConnectionManager(null).send(new Message(MessageType.FILE, new File("Client/1.jpg")));
            ConsoleHelper.writeMessage(ConnectionManager.getConnectionManager(null).receive().getText());

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Файл не был найден.");
        }
    }
}
