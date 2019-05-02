package client;

import common.ConsoleHelper;

import java.io.IOException;
import static client.DBManager.returnFilesListFromDB;
/*
TODO list:24.04.2019
TODO Разобраться с датой (она должна корректно выводиться)
TODO Реализовать команду removed
TODO добавить список команд по работе с сервером
1.
 */
/**
 * Стартовый класс для клиента
 */
public class Client {

    private String clientName;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public static void main(String[] args) throws Exception {

        ClientOperation operation = null;
        FilesListManager filesListManager = returnFilesListFromDB();

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
        ConsoleHelper.writeMessage(String.format("\t %d - обновить ссписок файлов для отправки", ClientOperation.REFRESH.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - вполнить подключение к серверу", ClientOperation.CONNECTION.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - выход", ClientOperation.EXIT.ordinal()));

        return ClientOperation.values()[ConsoleHelper.readInt()];
    }
}
