package command;

import client.*;
import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import common.exception.PathIsNotFoundException;

import static client.DBManager.deleteAllFromTable;
import static client.DBManager.insertIntoTable;


/**
 * The command is executed at the end of the program.
 */
public class CommandEXIT implements Command {

    public void execute() throws Exception {
        try {
            FilesListManager filesListManager = getFilesList();
            deleteAllFromTable(); //перед каждым сохранением удаляем все из БД
            if (filesListManager.size() > 0){
                for (FileProperties file : filesListManager.getFilesList()) {
                    insertIntoTable(
                            file.getName(),
                            file.getSize(),
                            file.getAbsolutePath().toString(),
                            file.getTimeWhenAdd().getTime()
                    );
                }
                ConsoleHelper.writeMessage("Список файлов сохранен.");
            }
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Файл не был найден.");
        }
        ConnectionManager.getConnectionManager(null).send(new Message(MessageType.DISCONNECTION, Client.getClientName() + ""));
        ConsoleHelper.writeMessage("До встречи!");
    }
}