package command;

import client.ConnectionManager;
import common.ConsoleHelper;
import common.Message;
import common.MessageType;

import java.io.IOException;
import java.net.Socket;

public class CommandCONNECTION extends CommandClientOnly {

    ConnectionManager connectionManager;

    @Override
    public void execute() throws Exception {

        ConsoleHelper.writeMessage("Подключение к серверу...");

        connect();


    }

    public void connect() throws IOException, ClassNotFoundException {
        String serverAddress = getServerAddress();
        int serverPort = getServerPort();
        String clientName = getUserName();
        connectionManager = new ConnectionManager(new Socket(serverAddress, serverPort));
        connectionManager.send(new Message(MessageType.TEST, clientName));
        ConsoleHelper.writeMessage(connectionManager.receive().getText());
    }

    /**
     * Должен запросить ввод адреса сервера у пользователя и вернуть введенное значение.
     * Адрес может быть строкой, содержащей ip, если клиент и сервер запущен на разных машинах
     * или 'localhost', если клиент и сервер работают на одной машине.
     * @return
     */
    protected String getServerAddress() throws IOException {
        ConsoleHelper.writeMessage("Введите адрес сервера");
//        String address = ConsoleHelper.readString();
        String address = "localhost";
        ConsoleHelper.writeMessage(address);
        return address;
    }

    /**
     * Должен запрашивать ввод порта сервера и возвращать его
     * @return
     */
    protected int getServerPort() throws IOException {
        ConsoleHelper.writeMessage("Введите адрес порта");
//        int port = ConsoleHelper.readInt();
        int port = 7777;
        ConsoleHelper.writeMessage(Integer.toString(port));
        return port;
    }

    /**
     * Должен запрашивать и возвращать имя пользователя
     * @return
     */
    protected String getUserName() throws IOException {
        ConsoleHelper.writeMessage("Введите имя пользователя");
        String userName = ConsoleHelper.readString();
        return userName;
    }

    /**
     * Должен запрашивать и возвращать пароль пользователя
     * @return
     */
    protected String getUserPassword() throws IOException {
        ConsoleHelper.writeMessage("Введите пароль");
        String userName = ConsoleHelper.readString();
        return userName;
    }
}
