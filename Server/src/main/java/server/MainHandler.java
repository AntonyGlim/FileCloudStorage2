package server;

import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import common.exception.PathIsNotFoundException;
import database.DBManager;
import exeption.NoSuchUserException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import user.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private volatile boolean clientConnected = false;
    private User user;
    private static DBManager dbManager = new DBManager();
    private String bigFileName = "";

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
            if (msg == null) return;

            if (msg instanceof Message) {
                Message messageFromClient = (Message) msg;

                if (messageFromClient.getType().equals(MessageType.REGISTRATION) && !clientConnected){
                    String[] tokens = messageFromClient.getText().split(" ");
                    String name = tokens[0];
                    String password = tokens[1];
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

                if (messageFromClient.getType().equals(MessageType.UPLOAD_FILE)){
                    String absolutePathName = "Server/server_storage/" + user.getName() + "/";
                    Path path = Paths.get(absolutePathName);
                    if (!Files.exists(path)) Files.createDirectories(path);
                    fileOutputStream = new FileOutputStream(absolutePathName + messageFromClient.getFile().getName());
                    fileOutputStream.write(messageFromClient.getBytes());
                    fileOutputStream.close();
                    ctx.writeAndFlush(new Message(MessageType.UPLOAD_FILE_OK, "Файл передан успешно."));
                }

                if (messageFromClient.getType().equals(MessageType.UPLOAD_BIG_FILE)){
                    String absolutePathName = "Server/server_storage/" + user.getName() + "/";
                    Path path = Paths.get(absolutePathName);
                    if (!Files.exists(path)) Files.createDirectories(path);
                    fileOutputStream = new FileOutputStream(absolutePathName + messageFromClient.getFile().getName(), true);
                    fileOutputStream.write(messageFromClient.getBytes());
                    fileOutputStream.close();
                }
                if (messageFromClient.getType().equals(MessageType.UPLOAD_BIG_FILE_END)){
                    ctx.writeAndFlush(new Message(MessageType.UPLOAD_FILE_OK, "Файл передан успешно."));
                }

                if (messageFromClient.getType().equals(MessageType.DOWNLOAD_FILE)){
                    String absolutePathName = "Server/server_storage/" + user.getName() + "/";
                    Path sourcePath = Paths.get(absolutePathName + messageFromClient.getText());
                    if (Files.notExists(sourcePath)) ctx.writeAndFlush(new Message(MessageType.DOWNLOAD_FILE, "Файл не найден"));
                    else if (sourcePath.toFile().length() <= 1024 * 1024 * 100){
                        ctx.writeAndFlush(new Message(MessageType.DOWNLOAD_FILE_OK, sourcePath.toFile()));
                    } else {
                        FileInputStream fileInputStream = new FileInputStream(sourcePath.toFile());
                        int bufferLength = 1024 * 512; //512 kb
                        byte[] buffer = new byte[bufferLength];
                        int i = 0;
                        while (fileInputStream.available() > 0) {
                            System.out.println(i); //TODO delete this
                            int count = fileInputStream.read(buffer);
                            if (count == bufferLength) {
                                ctx.writeAndFlush(new Message(
                                        MessageType.DOWNLOAD_BIG_FILE,
                                        sourcePath.toFile(),
                                        Integer.toString(i),
                                        buffer
                                ));
                                Thread.sleep(10); //TODO delete this
                            } else {
                                byte[] buffer2 = new byte[count];
                                fileInputStream.read(buffer2);
                                ctx.writeAndFlush(new Message(
                                        MessageType.DOWNLOAD_BIG_FILE,
                                        sourcePath.toFile(),
                                        Integer.toString(i),
                                        buffer2
                                ));
                            }
                            i++;
                        }
                        fileInputStream.close();
                        ctx.writeAndFlush(new Message(MessageType.DOWNLOAD_BIG_FILE_END, Integer.toString(i)));
                    }
                }

                if (messageFromClient.getType().equals(MessageType.DISCONNECTION)){
                    int name = Integer.parseInt(messageFromClient.getText());
                    deleteUserFromMap(Server.connectionUsersMap, name);
                    ConsoleHelper.writeMessage(Server.connectionUsersMap.toString()); //TODO Delete this
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

    private void deleteUserFromMap(Map<Integer, Long> map, Integer name){
        for (Map.Entry pair : map.entrySet()) {
            if (pair.getKey() == name) map.remove(name);
        }
    }
}
