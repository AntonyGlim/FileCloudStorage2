package command;

import client.ConnectionManager;
import common.ConsoleHelper;
import common.FileProperties;
import common.Message;
import common.MessageType;

import java.util.ArrayList;

public class CommandFILE_LIST implements Command  {

    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Список файлов расположеных на сервере:");
        printList(getFilesListFromServer());
    }

    private void printList(ArrayList<FileProperties> fileList){
        for (FileProperties file : fileList) {
            ConsoleHelper.writeMessage(file.toString());
        }
    }

    public static ArrayList<FileProperties> getFilesListFromServer() throws Exception{
        ConnectionManager.getConnectionManager(null).send(new Message(MessageType.FILE_LIST));
        Message message = ConnectionManager.getConnectionManager(null).receive();
        ArrayList<FileProperties> list = null;
        if (message.getType() == MessageType.FILE_LIST_OK){
            if (message.getFileList() != null){
                list = (message.getFileList());
            } else {
                ConsoleHelper.writeMessage("Список пуст.");
            }
        }
        return list;
    }
}
