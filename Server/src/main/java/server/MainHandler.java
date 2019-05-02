package server;

import common.ConsoleHelper;
import common.Message;
import common.common.FileMessage;
import common.common.FileRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainHandler extends ChannelInboundHandlerAdapter {

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
                fileOutputStream = new FileOutputStream("Server/" + messageFromClient.getFile().getName());
                fileOutputStream.write(messageFromClient.getBytes());
            }
        } finally {
            ReferenceCountUtil.release(msg);
            fileOutputStream.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
