package command;

import client.ConnectionManager;
import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import common.exception.PathIsNotFoundException;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandDOWNLOAD implements Command {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Загрузка файла с сервера.");

        ConsoleHelper.writeMessage("Введите имя файла для загрузки:");
        ConnectionManager.getConnectionManager(null).send(new Message(MessageType.DOWNLOAD_FILE, ConsoleHelper.readString()));

        Message message = ConnectionManager.getConnectionManager(null).receive();
        if (message.getType() == MessageType.DOWNLOAD_FILE_OK){
            String absolutePathName = "Client/client_storage/";
            Path path = Paths.get(absolutePathName);
            if (!Files.exists(path)) Files.createDirectories(path);
            FileOutputStream fileOutputStream = new FileOutputStream(absolutePathName + message.getFile().getName());
            fileOutputStream.write(message.getBytes());
            fileOutputStream.close();
            ConsoleHelper.writeMessage("Файл успешно загружен.");
        } else {
            ConsoleHelper.writeMessage(message.getText());
        }
    }
}
