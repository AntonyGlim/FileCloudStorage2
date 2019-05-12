package command;

import client.ConnectionManager;
import common.ConsoleHelper;
import common.FileProperties;
import common.Message;
import common.MessageType;

import java.util.ArrayList;

public class CommandFILELIST implements Command  {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Список файлов расположеных на сервере:");
        ConnectionManager.getConnectionManager(null).send(new Message(MessageType.FILE_LIST));
        Message message = ConnectionManager.getConnectionManager(null).receive();
        if (message.getType() == MessageType.FILE_LIST_OK){
            if (message.getFileList() == null){
                ConsoleHelper.writeMessage("Список пуст.");
            } else {
                printList(message.getFileList());
            }
        }
    }

    private void printList(ArrayList<FileProperties> fileList){
        for (FileProperties file : fileList) {
            ConsoleHelper.writeMessage(file.toString());
        }

    }
}
