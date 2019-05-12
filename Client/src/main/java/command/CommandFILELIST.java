package command;

import client.ConnectionManager;
import common.ConsoleHelper;
import common.Message;
import common.MessageType;

public class CommandFILELIST implements Command  {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Список файлов расположеных на сервере:");
        ConnectionManager.getConnectionManager(null).send(new Message(MessageType.FILE_LIST));
        Message message = ConnectionManager.getConnectionManager(null).receive();
        if (message.getType() == MessageType.FILE_LIST_OK){
            ConsoleHelper.writeMessage(message.getFileList().toString());
        }
    }
}
