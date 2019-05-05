package client;

import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import common.exception.InvalidInputFormatException;

import java.io.IOException;
import java.net.Socket;

public class Client {


    private volatile boolean clientConnected = false;
    private ConnectionManager connectionManager;


    public static void main(String[] args) throws Exception {
        //authorization block
        Client client = new Client();
        client.connect();
        while (!client.clientConnected){
            ConsoleHelper.writeMessage("\nЗарегистрируйтесь(1) или выполните вход(2)");
            try {
                int i = ConsoleHelper.readInt();
                if (i == 1) client.registration();
                else if (i == 2) client.authorization();
                else throw new InvalidInputFormatException();
            } catch (InvalidInputFormatException e){
                ConsoleHelper.writeMessage("Пожалуйста, выберите из предложенного списка");
            }
        }

        //main loop block
        ClientOperation operation = null;
        do {
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);
            } catch (InvalidInputFormatException | ArrayIndexOutOfBoundsException e){
                ConsoleHelper.writeMessage("Пожалуйста, выберите из предложенного списка");
            }
        } while (operation != ClientOperation.EXIT);
    }


    /**
     * User dialog method
     * @return - operation which user want to do
     * @throws IOException
     * @throws InvalidInputFormatException
     * @throws ArrayIndexOutOfBoundsException
     */
    public static ClientOperation askOperation() throws IOException, InvalidInputFormatException, ArrayIndexOutOfBoundsException {
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


    /**
     * User registration method
     * @throws IOException
     * @throws ClassNotFoundException
     */
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


    /**
     * User authorization method
     * @throws IOException
     * @throws ClassNotFoundException
     */
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


    /**
     * Connection method
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void connect() throws IOException, ClassNotFoundException {
        String serverAddress = getServerAddress();
        int serverPort = getServerPort();
        connectionManager = ConnectionManager.getConnectionManager(new Socket(serverAddress, serverPort));
    }


    protected String getServerAddress() throws IOException {
        ConsoleHelper.writeMessage("Введите адрес сервера");
//        String address = ConsoleHelper.readString();
        String address = "localhost";                               //TODO delete this
        ConsoleHelper.writeMessage(address);
        return address;
    }


    protected int getServerPort() throws IOException {
        ConsoleHelper.writeMessage("Введите адрес порта");
//        int port = ConsoleHelper.readInt();
        int port = 7777;                                            //TODO delete this
        ConsoleHelper.writeMessage(Integer.toString(port));
        return port;
    }


    protected String getUserName() throws IOException {
        ConsoleHelper.writeMessage("Введите имя пользователя");
        String userName = ConsoleHelper.readString();
//        String userName = "Sam1";                                 //TODO delete this
        ConsoleHelper.writeMessage(userName);
        return userName;
    }


    protected String getUserPassword() throws IOException {
        ConsoleHelper.writeMessage("Введите пароль");
        String userPassword = ConsoleHelper.readString();
//        String userPassword = "Password1";                        //TODO delete this
        ConsoleHelper.writeMessage(userPassword);
        return userPassword;
    }

}
