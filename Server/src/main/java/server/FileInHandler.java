package server;

import common.Message;
import common.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileInHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null) return;
            if (msg instanceof Message) {
                Message messageFromClient = (Message) msg;
                if (messageFromClient.getType().equals(MessageType.UPLOAD_FILE)) {
                    String absolutePathName = "Server/server_storage/" + MainHandler.user.getName() + "/";
                    Path path = Paths.get(absolutePathName);
                    if (!Files.exists(path)) Files.createDirectories(path);
                    try(FileOutputStream fileOutputStream = new FileOutputStream(absolutePathName + messageFromClient.getFile().getName());){
                        fileOutputStream.write(messageFromClient.getBytes());
                    }
                    ctx.writeAndFlush(new Message(MessageType.UPLOAD_FILE_OK, "Файл передан успешно."));
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
