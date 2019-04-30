package client;

import common.ConsoleHelper;

import java.io.IOException;
import static client.DBManager.returnFilesListFromDB;

/**
 * Стартовый класс для клиента
 */
public class Client {

    public static void main(String[] args) throws Exception {

        ClientOperation operation = null;
        FilesList filesList = returnFilesListFromDB();

        do {
            operation = askOperation();
            CommandExecutor.execute(operation);
        } while (operation != ClientOperation.EXIT);

    }

    /**
     * Метод выводит на экран варианты того, что может сделать пользователь,
     * и считывает от него цифру соответствующую номеру операции,
     * которую пользователь хочет выполнить.
     * @return
     * @throws IOException
     */
    public static ClientOperation askOperation() throws IOException {
        ConsoleHelper.writeMessage("");
        ConsoleHelper.writeMessage("Выберите операцию:");
        ConsoleHelper.writeMessage(String.format("\t %d - добавить файл в список файлов для отправки", ClientOperation.ADD.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - удалить файл из списка файлов для отправки", ClientOperation.REMOVE.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - просмотреть ссписок файлов для отправки", ClientOperation.CONTENT.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - выход", ClientOperation.EXIT.ordinal()));

        return ClientOperation.values()[ConsoleHelper.readInt()];
    }
}
