package server;

import common.ConsoleHelper;
import common.Message;
import database.DBManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import user.User;

import java.io.FileOutputStream;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private volatile boolean clientConnected = false;
    private User user;
    private static MainHandler mainHandler;

    public static synchronized MainHandler getMainHandler(){
        if (mainHandler == null) mainHandler = new MainHandler();
        return mainHandler;
    }

    public boolean isClientConnected() {
        return clientConnected;
    }

    public void setClientConnected(boolean clientConnected) {
        this.clientConnected = clientConnected;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
