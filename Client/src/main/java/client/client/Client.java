package client.client;

import common.Message;
import common.MessageType;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        ObjectEncoderOutputStream oeos = null;
        ObjectDecoderInputStream odis = null;

        try (Socket socket = new Socket("localhost", 7777)) {
            oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            Message fileMessage = new Message(MessageType.FILE, new File("Client/1.jpg"));
            System.out.println(fileMessage.getBytes().length);
            oeos.writeObject(fileMessage);
            oeos.flush();
            odis = new ObjectDecoderInputStream(socket.getInputStream(), 100 * 1024 * 1024);
//            Message msgFromServer = (Message)odis.readObject();
//            System.out.println("Answer from server: " + msgFromServer.getText());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oeos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                odis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
