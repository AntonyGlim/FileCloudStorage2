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
        FileOutputStream fileOutputStream = null;

        if (message.getType() == MessageType.DOWNLOAD_FILE_OK || message.getType() == MessageType.DOWNLOAD_BIG_FILE){
            String absolutePathName = "Client/client_storage/";
            Path path = Paths.get(absolutePathName);
            if (!Files.exists(path)) Files.createDirectories(path);
            if (message.getType() == MessageType.DOWNLOAD_FILE_OK){
                fileOutputStream = new FileOutputStream(absolutePathName + message.getFile().getName());
                fileOutputStream.write(message.getBytes());
                fileOutputStream.close();
                ConsoleHelper.writeMessage("Файл успешно загружен.");
            }
            if (message.getType() == MessageType.DOWNLOAD_BIG_FILE){
                while (!(message.getType() == MessageType.DOWNLOAD_BIG_FILE_END)){
                    if (fileOutputStream == null)
                        fileOutputStream = new FileOutputStream(absolutePathName + message.getFile().getName(), true);
                    fileOutputStream.write(message.getBytes());
                    message = ConnectionManager.getConnectionManager(null).receive();
                }
                fileOutputStream.close();
                ConsoleHelper.writeMessage("Файл успешно загружен.");
            }
        }

//        if (message.getType() == MessageType.DOWNLOAD_BIG_FILE_END) {
//            fileOutputStream.close();
//            ConsoleHelper.writeMessage("Файл успешно загружен.");
//        }

        if (message.getType() == MessageType.DOWNLOAD_FILE){
            ConsoleHelper.writeMessage(message.getText());
        }
    }
}

