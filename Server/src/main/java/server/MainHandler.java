package server;

import common.ConsoleHelper;
import common.FileProperties;
import common.Message;
import common.MessageType;
import database.DBManager;
import exeption.NoSuchUserException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import user.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;

/**
 * MainHandler
 */
public class MainHandler extends ChannelInboundHandlerAdapter {

    private volatile boolean clientConnected = false;
    private User user;

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

            //REGISTRATION
            if (msg instanceof Message) {
                Message messageFromClient = (Message) msg;

                if (messageFromClient.getType().equals(MessageType.REGISTRATION) && !clientConnected){
                    String[] tokens = messageFromClient.getText().split(" ");
                    String name = tokens[0];
                    String password = tokens[1];
                    try {
                        long timeWhenAdd = System.currentTimeMillis();
                        long timeLastChange = timeWhenAdd;
                        DBManager.insertIntoTable(Integer.parseInt(name), Integer.parseInt(password), timeWhenAdd, timeLastChange);
                        clientConnected = true;
                        Server.connectionUsersMap.put(Integer.parseInt(name), System.currentTimeMillis());
                        ctx.writeAndFlush(new Message(MessageType.REGISTRATION_OK, "Регистрация выполнена успешно."));
                        ConsoleHelper.writeMessage(Server.connectionUsersMap.toString()); //TODO Delete this
                    } catch (SQLException e){
                        ctx.writeAndFlush(new Message(MessageType.REGISTRATION, "Пользователь с таким именем уже существует."));
                    }
                }

                //AUTHORIZATION
                if (messageFromClient.getType().equals(MessageType.AUTHORIZATION) && !clientConnected){
                    String[] tokens = messageFromClient.getText().split(" ");
                    String name = tokens[0];
                    String password = tokens[1];
                    if (!isContain(Server.connectionUsersMap, Integer.parseInt(name))){
                        try{
                            user = DBManager.returnUserFromDBbyNameAndPass(Integer.parseInt(name), Integer.parseInt(password));
                            DBManager.updateUserTimeLastVisit(user.getName());
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

                //UPLOAD_FILE (means for client)
                if (messageFromClient.getType().equals(MessageType.UPLOAD_FILE)){
                    String absolutePathName = user.getFolderName();
                    Path path = Paths.get(absolutePathName);
                    if (!Files.exists(path)) Files.createDirectories(path);
                    fileOutputStream = new FileOutputStream(absolutePathName + messageFromClient.getFile().getName());
                    fileOutputStream.write(messageFromClient.getBytes());
                    fileOutputStream.close();
                    ctx.writeAndFlush(new Message(MessageType.UPLOAD_FILE_OK, String.format("Файл %s передан успешно.", messageFromClient.getFile().getAbsolutePath())));
                }

                //UPLOAD_BIG_FILE (means for client)
                if (messageFromClient.getType().equals(MessageType.UPLOAD_BIG_FILE_START)){
                    Path path = Paths.get(user.getFolderName());
                    if (!Files.exists(path)) Files.createDirectories(path);
                    Path pathToFile = Paths.get(user.getFolderName() + messageFromClient.getText());
                    if (Files.exists(pathToFile)) Files.delete(pathToFile);
                }
                if (messageFromClient.getType().equals(MessageType.UPLOAD_BIG_FILE)){
                    fileOutputStream = new FileOutputStream(user.getFolderName() + messageFromClient.getFile().getName(), true);
                    fileOutputStream.write(messageFromClient.getBytes());
                    fileOutputStream.close();
                }
                if (messageFromClient.getType().equals(MessageType.UPLOAD_BIG_FILE_END)){
                    ctx.writeAndFlush(new Message(MessageType.UPLOAD_FILE_OK, String.format("Файл %s передан успешно.",messageFromClient.getText())));
                }

                //TODO DOWNLOAD_FILE (means for client) - not working correctly with big files
                if (messageFromClient.getType().equals(MessageType.DOWNLOAD_FILE)){
                    String absolutePathName = user.getFolderName();
                    Path sourcePath = Paths.get(absolutePathName + messageFromClient.getText());
                    if (Files.notExists(sourcePath)) ctx.writeAndFlush(new Message(MessageType.DOWNLOAD_FILE, "Файл не найден"));
                    else if (sourcePath.toFile().length() <= 1024 * 1024 * 100){
                        ctx.writeAndFlush(new Message(MessageType.DOWNLOAD_FILE_OK, sourcePath.toFile()));
                    } else {
                        ctx.writeAndFlush(new Message(
                                MessageType.DOWNLOAD_BIG_FILE_START,
                                sourcePath.toFile().getName()
                        ));
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
                            } else {
                                byte[] buffer2 = new byte[count];
                                for (int j = 0; j < count; j++) {
                                    buffer2[j] = buffer[j];
                                }
                                ctx.writeAndFlush(new Message(
                                        MessageType.DOWNLOAD_BIG_FILE,
                                        sourcePath.toFile(),
                                        Integer.toString(i),
                                        buffer2
                                ));
                                ctx.flush();
                            }
                            i++;
                        }
                        fileInputStream.close();
                        ctx.writeAndFlush(new Message(MessageType.DOWNLOAD_BIG_FILE_END, Integer.toString(i)));
                    }
                }

                //FILE_LIST - send to client
                if (messageFromClient.getType().equals(MessageType.FILE_LIST)){
                    ctx.writeAndFlush(new Message(MessageType.FILE_LIST_OK, user.getFileList()));
                }

                //DELETE_FILE_FROM_SERVER
                if (messageFromClient.getType().equals(MessageType.DELETE_FILE_FROM_SERVER)){
                    String fileToDeleteName = messageFromClient.getText();
                    if (user.getFileList() != null){
                        for (FileProperties file : user.getFileList()) {
                            if ((file.getName()).equals(fileToDeleteName)){
                                Files.delete(Paths.get(file.getAbsolutePath()));
                                ctx.writeAndFlush(new Message(MessageType.DELETE_FILE_FROM_SERVER_OK, "Файл успешно удален"));
                                break;
                            }
                            ctx.writeAndFlush(new Message(MessageType.DELETE_FILE_FROM_SERVER, "Файл не найден"));
                        }
                    } else {
                        ctx.writeAndFlush(new Message(MessageType.DELETE_FILE_FROM_SERVER, "Файл не найден"));
                    }
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

    /** act, when user disconnection not normal */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        deleteUserFromMap(Server.connectionUsersMap, user.getName());
        ConsoleHelper.writeMessage(String.format("Клиент %s отключился.", user.getName()));
        ConsoleHelper.writeMessage(Server.connectionUsersMap.toString()); //TODO Delete this
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
