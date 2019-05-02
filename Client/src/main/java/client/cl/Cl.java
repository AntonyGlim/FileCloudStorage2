package client.cl;

import common.Message;
import common.MessageType;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;

public class Cl {
    public static void main(String[] args) {
        ObjectEncoderOutputStream oeos = null;
        ObjectDecoderInputStream odis = null;

        try (Socket socket = new Socket("localhost", 7777)) {
            oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            Message textMessage = new Message(MessageType.TEST);
            oeos.writeObject(textMessage);
            oeos.flush();
            odis = new ObjectDecoderInputStream(socket.getInputStream(), 100 * 1024 * 1024);
            Message msgFromServer = (Message)odis.readObject();
            System.out.println("Answer from server: " + msgFromServer.getText());
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
