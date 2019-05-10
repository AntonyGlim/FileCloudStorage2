package command;

import client.ConnectionManager;
import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import common.exception.PathIsNotFoundException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandUPLOAD implements Command {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Отправка файла на сервер.");

            ConsoleHelper.writeMessage("Введите полное имя файла для добавления:");

            Path sourcePath = Paths.get(ConsoleHelper.readString());
            if (Files.notExists(sourcePath)) throw new PathIsNotFoundException();
            if (sourcePath.toFile().length() <= 1024 * 1024 * 100){
                ConnectionManager.getConnectionManager(null).send(new Message(MessageType.UPLOAD_FILE, sourcePath.toFile()));
                ConsoleHelper.writeMessage(ConnectionManager.getConnectionManager(null).receive().getText());
            } else {
                FileInputStream fileInputStream = new FileInputStream(sourcePath.toFile());
                byte[] buffer = new byte[1024 * 1024 * 4];
                int i = 0;
                while (fileInputStream.available() > 0) {
                    int count = fileInputStream.read(buffer);
                    ConnectionManager.getConnectionManager(null).send(new Message(
                            MessageType.UPLOAD_BIG_FILE,
                            sourcePath.toFile(),
                            Integer.toString(i),
                            buffer
                    ));
                    i++;
                }
                fileInputStream.close();
                ConsoleHelper.writeMessage(ConnectionManager.getConnectionManager(null).receive().getText());
            }

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Файл не был найден.");
        }
    }
}
