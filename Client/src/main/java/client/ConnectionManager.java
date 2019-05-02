package client;

import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * class is responsible
 * for the connection to the server
 */
public class ConnectionManager implements Closeable {

    private final Socket socket;
    private final ObjectEncoderOutputStream outOES;
    private final ObjectDecoderInputStream inODS;

    public ConnectionManager(Socket socket) throws IOException {
        this.socket = socket;
        this.outOES = new ObjectEncoderOutputStream(socket.getOutputStream());  //You need to create an ObjectOutputStream class object before creating an ObjectInputStream class object
        this.inODS = new ObjectDecoderInputStream(socket.getInputStream());     //otherwise, a mutual blocking of streams may occur.
    }

    public void connect() throws IOException {
        String serverAddress = getServerAddress();
        int serverPort = getServerPort();
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            ConnectionManager connectionManager = new ConnectionManager(socket);
        }
    }

    /**
     * Должен запросить ввод адреса сервера у пользователя и вернуть введенное значение.
     * Адрес может быть строкой, содержащей ip, если клиент и сервер запущен на разных машинах
     * или 'localhost', если клиент и сервер работают на одной машине.
     * @return
     */
    protected String getServerAddress() throws IOException {
        ConsoleHelper.writeMessage("Введите адрес сервера");
        String address = ConsoleHelper.readString();
        return address;
    }

    /**
     * Должен запрашивать ввод порта сервера и возвращать его
     * @return
     */
    protected int getServerPort() throws IOException {
        ConsoleHelper.writeMessage("Введите адрес порта");
        int port = ConsoleHelper.readInt();
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

    /**
     * Must write (serialize) a message to an ObjectEncoderOutputStream
     * This method will be called from multiple threads.
     * @param message
     * @throws IOException
     */
    public void send(Message message) throws IOException {
        synchronized (outOES){
            outOES.writeObject(message);
        }
    }

    /**
     * Must read (deserialize) data from ObjectInputStream.
     * A read operation cannot be simultaneously triggered by multiple threads.
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Message receive() throws IOException, ClassNotFoundException {
        Message msg = null;
        synchronized (inODS){
            msg = (Message) inODS.readObject();
        }
        return msg;
    }

    /**
     * Get remote socket address
     * @return
     */
    public SocketAddress getRemoteSocketAddress(){
        return socket.getRemoteSocketAddress();
    }

    /**
     * Each resource must be closed
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        inODS.close();
        outOES.close();
        socket.close();
    }


//    public boolean sendMessage() {
//        try (Socket socket = new Socket("localhost", 7777)) {
//            Message fileMessage = new Message(MessageType.FILE, new File("Client/1.jpg"));
//            System.out.println(fileMessage.getBytes().length);
//            oeos.writeObject(fileMessage);
//            oeos.flush();
//            odis = new ObjectDecoderInputStream(socket.getInputStream(), 100 * 1024 * 1024);
////            Message msgFromServer = (Message)odis.readObject();
////            System.out.println("Answer from server: " + msgFromServer.getText());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                oeos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                odis.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}