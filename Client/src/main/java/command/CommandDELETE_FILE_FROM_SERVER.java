package command;

import client.ConnectionManager;
import common.ConsoleHelper;
import common.Message;
import common.MessageType;

public class CommandDELETE_FILE_FROM_SERVER implements Command {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Удаление файла с сервера.");

        ConsoleHelper.writeMessage("Введите имя файла для удаления:");
        ConnectionManager.getConnectionManager(null).send(new Message(MessageType.DELETE_FILE_FROM_SERVER, ConsoleHelper.readString()));

        Message message = ConnectionManager.getConnectionManager(null).receive();
        if (message.getType() == MessageType.DELETE_FILE_FROM_SERVER_OK || message.getType() == MessageType.DELETE_FILE_FROM_SERVER){
            ConsoleHelper.writeMessage(message.getText());
        } else {
            ConsoleHelper.writeMessage("Произошла ошибка при удалении.");
        }
    }
}
