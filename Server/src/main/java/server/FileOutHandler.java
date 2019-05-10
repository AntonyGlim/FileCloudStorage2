package server;

import common.Message;
import common.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOutHandler extends ChannelInboundHandlerAdapter {

    private static MainHandler mainHandler = MainHandler.getMainHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null) return;
            if (msg instanceof Message) {
                Message messageFromClient = (Message) msg;
                if (messageFromClient.getType().equals(MessageType.DOWNLOAD_FILE)) {
                    String absolutePathName = "Server/server_storage/" + mainHandler.getUser().getName() + "/";
                    Path sourcePath = Paths.get(absolutePathName + messageFromClient.getText());
                    if (Files.notExists(sourcePath)) ctx.writeAndFlush(new Message(MessageType.DOWNLOAD_FILE, "Файл не найден"));
                    else ctx.writeAndFlush(new Message(MessageType.DOWNLOAD_FILE_OK, sourcePath.toFile()));
                } else {
                    ctx.fireChannelRead(msg);
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
