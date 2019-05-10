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
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private volatile boolean clientConnected = false;
    public static User user;
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

//
//                if (messageFromClient.getType().equals(MessageType.UPLOAD_FILE)){
//                    String absolutePathName = "Server/server_storage/" + user.getName() + "/";
//                    Path path = Paths.get(absolutePathName);
//                    if (!Files.exists(path)) Files.createDirectories(path);
//                    fileOutputStream = new FileOutputStream(absolutePathName + messageFromClient.getFile().getName());
//                    fileOutputStream.write(messageFromClient.getBytes());
//                    fileOutputStream.close();
//                    ctx.writeAndFlush(new Message(MessageType.UPLOAD_FILE_OK, "Файл передан успешно."));
//                }
                if (messageFromClient.getType().equals(MessageType.DOWNLOAD_FILE)){
                    String absolutePathName = "Server/server_storage/" + user.getName() + "/";
                    Path sourcePath = Paths.get(absolutePathName + messageFromClient.getText());
                    if (Files.notExists(sourcePath)) ctx.writeAndFlush(new Message(MessageType.DOWNLOAD_FILE, "Файл не найден"));
                    else ctx.writeAndFlush(new Message(MessageType.DOWNLOAD_FILE_OK, sourcePath.toFile()));
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


}
