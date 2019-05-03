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

    private static ConnectionManager connectionManager;
    private final Socket socket;
    private final ObjectEncoderOutputStream outOES;
    private final ObjectDecoderInputStream inODS;

    private ConnectionManager(Socket socket) throws IOException {
        this.socket = socket;
        this.outOES = new ObjectEncoderOutputStream(socket.getOutputStream());  //You need to create an ObjectOutputStream class object before creating an ObjectInputStream class object
        this.inODS = new ObjectDecoderInputStream(socket.getInputStream());     //otherwise, a mutual blocking of streams may occur.
    }

    public static synchronized ConnectionManager getConnectionManager(Socket socket) throws IOException {
        if (connectionManager == null) connectionManager = new ConnectionManager(socket);
        return connectionManager;
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
            outOES.flush();
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
}