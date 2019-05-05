package client;

import common.ConsoleHelper;
import common.Message;
import common.MessageType;

import java.io.IOException;
import java.net.Socket;

import static client.DBManager.returnFilesListFromDB;
/*
TODO
1.
 */
/**
 * Стартовый класс для клиента
 */
public class Client {

    private String clientName;
    private volatile boolean clientConnected = false; //оно будет устанавливаться в true, если клиент подсоединен к серверу или в false в противном случае
    private ConnectionManager connectionManager;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public static void main(String[] args) throws Exception {

        Client client = new Client();
        client.connect();
        while (!client.clientConnected){
            ConsoleHelper.writeMessage("Зарегистрируйтесь(1) или выполните вход(2)");
            int i = ConsoleHelper.readInt();
            if (i == 1){
                client.registration();
            }
            if (i == 2){
                client.authorization();
            }
        }

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
        ConsoleHelper.writeMessage(String.format("\t %d - отправить файл на сервер", ClientOperation.UPLOAD.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - выход", ClientOperation.EXIT.ordinal()));

        return ClientOperation.values()[ConsoleHelper.readInt()];
    }

    private void registration() throws IOException, ClassNotFoundException {
        String clientName = getUserName();
        String clientPassword = getUserPassword();
        connectionManager.send(new Message(MessageType.REGISTRATION, (clientName.hashCode() + " " + clientPassword.hashCode())));
        Message message = connectionManager.receive();
        if (message.getType() == MessageType.REGISTRATION_OK) {
            clientConnected = true;
            ConsoleHelper.writeMessage(message.getText());
        }
        if (message.getType() == MessageType.REGISTRATION) {
            ConsoleHelper.writeMessage(message.getText());
        }
    }

    private void authorization() throws IOException, ClassNotFoundException {
        String clientName = getUserName();
        String clientPassword = getUserPassword();
        connectionManager.send(new Message(MessageType.AUTHORIZATION, (clientName.hashCode() + " " + clientPassword.hashCode())));
        Message message = connectionManager.receive();
        if (message.getType() == MessageType.AUTHORIZATION_OK) {
            clientConnected = true;
            ConsoleHelper.writeMessage(message.getText());
        }
        if (message.getType() == MessageType.AUTHORIZATION) {
            ConsoleHelper.writeMessage(message.getText());
        }
    }

    private void connect() throws IOException, ClassNotFoundException {
        String serverAddress = getServerAddress();
        int serverPort = getServerPort();
        connectionManager = ConnectionManager.getConnectionManager(new Socket(serverAddress, serverPort));
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
//        String userName = "Sam1";
        ConsoleHelper.writeMessage(userName);
        return userName;
    }

    /**
     * Должен запрашивать и возвращать пароль пользователя
     * @return
     */
    protected String getUserPassword() throws IOException {
        ConsoleHelper.writeMessage("Введите пароль");
        String userPassword = ConsoleHelper.readString();
//        String userPassword = "Password1";
        ConsoleHelper.writeMessage(userPassword);
        return userPassword;
    }

}
