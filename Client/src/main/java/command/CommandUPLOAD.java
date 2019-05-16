package command;

import client.ConnectionManager;
import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import common.exception.PathIsNotFoundException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandUPLOAD implements Command {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Отправка файла на сервер.");
        ConsoleHelper.writeMessage("Введите полное имя файла для добавления:");
        Path sourcePath = Paths.get(ConsoleHelper.readString());
        sendFile(sourcePath);
    }

    public static void sendFile(Path sourcePath) throws ClassNotFoundException {
        try {
            if (Files.notExists(sourcePath)) throw new PathIsNotFoundException();
            if (sourcePath.toFile().length() <= 1024 * 1024 * 100){
                ConnectionManager.getConnectionManager(null).send(new Message(MessageType.UPLOAD_FILE, sourcePath.toFile()));
                ConsoleHelper.writeMessage(ConnectionManager.getConnectionManager(null).receive().getText());
            } else {
                FileInputStream fileInputStream = new FileInputStream(sourcePath.toFile());
                int bufferLength = 1024 * 1; //1 kb if size will be bigger file will came with defects
                byte[] buffer = new byte[bufferLength];
                int i = 0;
                while (fileInputStream.available() > 0) {
                    int count = fileInputStream.read(buffer);
                    if (count == bufferLength) {
                        ConnectionManager.getConnectionManager(null).send(new Message(
                                MessageType.UPLOAD_BIG_FILE,
                                sourcePath.toFile(),
                                Integer.toString(i),
                                buffer
                        ));
                    } else {
                        byte[] buffer2 = new byte[count];
                        fileInputStream.read(buffer2);
                        ConnectionManager.getConnectionManager(null).send(new Message(
                                MessageType.UPLOAD_BIG_FILE,
                                sourcePath.toFile(),
                                Integer.toString(i),
                                buffer2
                        ));
                    }
                    i++;
                }
                fileInputStream.close();
                ConnectionManager.getConnectionManager(null).send(new Message(MessageType.UPLOAD_BIG_FILE_END, sourcePath.toString()));
                ConsoleHelper.writeMessage(ConnectionManager.getConnectionManager(null).receive().getText());
            }
        } catch (PathIsNotFoundException | IOException e) {
            ConsoleHelper.writeMessage(String.format("Файл %s не был найден.", sourcePath));
        }
    }
}
