package client;

import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import common.exception.InvalidInputFormatException;
import common.exception.PathIsNotFoundException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Main class with main loop
 */
public class Client {

    private static int clientName;
    private static volatile boolean clientConnected = false;
    private static ConnectionManager connectionManager;


    static {
        try {
            connect();
            while (!clientConnected){
                ConsoleHelper.writeMessage("\nЗарегистрируйтесь(1) или выполните вход(2).");
                try {
                    int i = ConsoleHelper.readInt();
//                    int i = 2; //TODO delete this
                    if (i == 1) registration();
                    else if (i == 2) authorization();
                    else throw new InvalidInputFormatException();
                } catch (InvalidInputFormatException e){
                    ConsoleHelper.writeError("Пожалуйста, выберите из предложенного списка.");
                } catch (PathIsNotFoundException e) {
                    ConsoleHelper.writeError("База данных повреждена, или отсутствует.");
                } catch (SQLException e) {
                    ConsoleHelper.writeError("Не удалось подключиться к базе данных.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            ConsoleHelper.writeError("Не удалось подключиться. Сервер не доступен.");
        } catch (InvalidInputFormatException e){
            ConsoleHelper.writeError("Проверьте адрес сервера и адрес порта.");
        }

    }


    public static void main(String[] args) throws Exception {
        //main loop block
        if (clientConnected) {
            ClientOperation operation = null;
            do {
                try {
                    operation = askOperation();
                    CommandExecutor.execute(operation);
                } catch (InvalidInputFormatException | ArrayIndexOutOfBoundsException e) {
                    ConsoleHelper.writeError("Пожалуйста, выберите из предложенного списка");
                }
            } while (operation != ClientOperation.EXIT);
        }
    }


    /**
     * User dialog method
     * @return - operation which user want to do
     * @throws IOException
     * @throws InvalidInputFormatException
     * @throws ArrayIndexOutOfBoundsException
     */
    public static ClientOperation askOperation() throws IOException, InvalidInputFormatException, ArrayIndexOutOfBoundsException {
        ConsoleHelper.writeMessage(              "");
        ConsoleHelper.writeMessage(              "+----------------------------------------------------+");
        ConsoleHelper.writeMessage(              "| Выберите операцию:                                 |");
        ConsoleHelper.writeMessage(String.format("|\t %d - добавить файл в список файлов для отправки  |", ClientOperation.ADD.ordinal()));
        ConsoleHelper.writeMessage(String.format("|\t %d - удалить файл из списка файлов для отправки  |", ClientOperation.REMOVE.ordinal()));
        ConsoleHelper.writeMessage(String.format("|\t %d - просмотреть список файлов для отправки      |", ClientOperation.CONTENT.ordinal()));
        ConsoleHelper.writeMessage(String.format("|\t %d - обновить список файлов для отправки         |", ClientOperation.REFRESH.ordinal()));
        ConsoleHelper.writeMessage(String.format("|\t %d - отправить файл на сервер                    |", ClientOperation.UPLOAD.ordinal()));
        ConsoleHelper.writeMessage(String.format("|\t %d - отправить все файлы из списка на сервер     |", ClientOperation.UPLOAD_ALL.ordinal()));
        ConsoleHelper.writeMessage(String.format("|\t %d - загрузить файл с сервера                    |", ClientOperation.DOWNLOAD.ordinal()));
        ConsoleHelper.writeMessage(String.format("|\t %d - загрузить все файлы с сервера               |", ClientOperation.DOWNLOAD_ALL.ordinal()));
        ConsoleHelper.writeMessage(String.format("|\t %d - список файлов на сервере                    |", ClientOperation.FILE_LIST.ordinal()));
        ConsoleHelper.writeMessage(String.format("|\t %d - удалить файл на сервере                     |", ClientOperation.DELETE_FILE_FROM_SERVER.ordinal()));
        ConsoleHelper.writeMessage(String.format("|\t %d - выход                                      |", ClientOperation.EXIT.ordinal()));
        ConsoleHelper.writeMessage(              "+----------------------------------------------------+");
        return ClientOperation.values()[ConsoleHelper.readInt()];
    }


    /**
     * User registration method
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void registration() throws IOException, ClassNotFoundException {
        int clientName = getUserName().hashCode();
        int clientPassword = getUserPassword().hashCode();
        connectionManager.send(new Message(MessageType.REGISTRATION, (clientName + " " + clientPassword)));
        Message message = connectionManager.receive();
        if (message.getType() == MessageType.REGISTRATION_OK) {
            clientConnected = true;
            Client.clientName = clientName;
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
    private static void authorization() throws IOException, ClassNotFoundException, PathIsNotFoundException, SQLException {
        int clientName = getUserName().hashCode();
        int clientPassword = getUserPassword().hashCode();
        connectionManager.send(new Message(MessageType.AUTHORIZATION, (clientName + " " + clientPassword)));
        Message message = connectionManager.receive();
        if (message.getType() == MessageType.AUTHORIZATION_OK) {
            clientConnected = true;
            DBManager.returnFilesListFromDB();
            Client.clientName = clientName;
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
    private static void connect() throws IOException, ConnectException, InvalidInputFormatException {
        String serverAddress = getServerAddress();
        int serverPort = getServerPort();
        connectionManager = ConnectionManager.getConnectionManager(new Socket(serverAddress, serverPort));
    }


    protected static String getServerAddress() throws IOException {
        ConsoleHelper.writeMessage("Введите адрес сервера");
        String address = ConsoleHelper.readString();
//        String address = "localhost"; //TODO delete this
        ConsoleHelper.writeMessage(address);
        return address;
    }


    protected static int getServerPort() throws IOException, InvalidInputFormatException {
        ConsoleHelper.writeMessage("Введите адрес порта");
        int port = ConsoleHelper.readInt();
//        int port = 7777; //TODO delete this
        ConsoleHelper.writeMessage(Integer.toString(port));
        return port;
    }


    protected static String getUserName() throws IOException {
        ConsoleHelper.writeMessage("Введите имя пользователя");
        String userName = ConsoleHelper.readString();
//        String userName = "2"; //TODO delete this
        ConsoleHelper.writeMessage(userName);
        return userName;
    }


    protected static String getUserPassword() throws IOException {
        ConsoleHelper.writeMessage("Введите пароль");
        String userPassword = ConsoleHelper.readString();
//        String userPassword = "2"; //TODO delete this
        ConsoleHelper.writeMessage(userPassword);
        return userPassword;
    }

}
