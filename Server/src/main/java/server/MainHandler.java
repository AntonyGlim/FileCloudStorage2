package server;

import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import database.DBManager;
import exeption.NoSuchUserException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import user.User;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private volatile boolean clientConnected = false;
    private User user;
    private static DBManager dbManager = new DBManager();

    /**
     * The method is executed once,
     * when the client is connected.
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ConsoleHelper.writeMessage("Client connected... ");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FileOutputStream fileOutputStream = null;
        try {
            if (msg == null) {
                return;
            }

            if (msg instanceof Message) {
                Message messageFromClient = (Message) msg;
                if (messageFromClient.getType().equals(MessageType.REGISTRATION) && !clientConnected){

                    String[] tokens = messageFromClient.getText().split(" ");
                    String name = tokens[0];
                    String password = tokens[1];
//                    if (!isContain(Server.connectionUsersMap, Integer.parseInt(name))){
                        try {
                            long timeWhenAdd = System.currentTimeMillis();
                            long timeLastChange = timeWhenAdd;
                            dbManager.insertIntoTable(Integer.parseInt(name), Integer.parseInt(password), timeWhenAdd, timeLastChange);
                            clientConnected = true;
                            Server.connectionUsersMap.put(Integer.parseInt(name), System.currentTimeMillis());
                            ctx.writeAndFlush(new Message(MessageType.REGISTRATION_OK, "Регистрация выполнена успешно."));
                            ConsoleHelper.writeMessage(Server.connectionUsersMap.toString()); //TODO Delete this
                        } catch (SQLException e){
                            ctx.writeAndFlush(new Message(MessageType.REGISTRATION, "Пользователь с таким именем уже существует."));
                        }
//                    } else {
//                        ctx.writeAndFlush(new Message(MessageType.REGISTRATION, "Пользователь с таким именем уже подключон."));
//                    }
                }
                if (messageFromClient.getType().equals(MessageType.AUTHORIZATION) && !clientConnected){
                    String[] tokens = messageFromClient.getText().split(" ");
                    String name = tokens[0];
                    String password = tokens[1];
                    if (!isContain(Server.connectionUsersMap, Integer.parseInt(name))){
                        try{
                            user = dbManager.returnUserFromDBbyNameAndPass(Integer.parseInt(name), Integer.parseInt(password));
                            ConsoleHelper.writeMessage(user.toString());
                            clientConnected = true;
                            Server.connectionUsersMap.put(Integer.parseInt(name), System.currentTimeMillis());
                            ctx.writeAndFlush(new Message(MessageType.AUTHORIZATION_OK, "Вход выполнен успешно."));
                            ConsoleHelper.writeMessage(Server.connectionUsersMap.toString()); //TODO Delete this
                        } catch (NoSuchUserException e){
                            ctx.writeAndFlush(new Message(MessageType.AUTHORIZATION, "Не верное имя пользователя или пароль"));
                        }
                    } else {
                        ctx.writeAndFlush(new Message(MessageType.AUTHORIZATION, "Пользователь с таким именем уже подключон."));
                    }
                }
                if (messageFromClient.getType().equals(MessageType.FILE)){
                    fileOutputStream = new FileOutputStream(messageFromClient.getFile().getName());
                    fileOutputStream.write(messageFromClient.getBytes());
                    ctx.writeAndFlush(new Message(MessageType.FILE, "Файл передан успешно."));
                }
                if (messageFromClient.getType().equals(MessageType.DISCONNECTION)){

                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private boolean isContain(Map<Integer, Long> map, Integer name) {
        for (Map.Entry pair : map.entrySet()) {
            if (pair.getKey() == name) return true;
        }
        return false;
    }

    private void deleteUserFromMap(){

    }
}
