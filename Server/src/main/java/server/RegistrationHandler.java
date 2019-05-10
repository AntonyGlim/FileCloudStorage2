package server;

import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import database.DBManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import user.User;

import java.sql.SQLException;

public class RegistrationHandler extends ChannelInboundHandlerAdapter {

    private static DBManager dbManager = new DBManager();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null) return;
            if (msg instanceof Message) {
                Message messageFromClient = (Message) msg;
                if (messageFromClient.getType().equals(MessageType.REGISTRATION)){

                    String[] tokens = messageFromClient.getText().split(" ");
                    int name = Integer.parseInt(tokens[0]);
                    int password = Integer.parseInt(tokens[1]);

                    try {
                        long timeWhenAdd = System.currentTimeMillis();
                        long timeLastChange = timeWhenAdd;
                        dbManager.insertIntoTable(name, password, timeWhenAdd, timeLastChange);
                        MainHandler.user = dbManager.returnUserFromDBbyNameAndPass(name, password);
                        Server.connectionUsersMap.put(name, System.currentTimeMillis());
                        ctx.writeAndFlush(new Message(MessageType.REGISTRATION_OK, "Регистрация выполнена успешно."));
                        ConsoleHelper.writeMessage(Server.connectionUsersMap.toString()); //TODO Delete this
                    } catch (SQLException e){
                        ctx.writeAndFlush(new Message(MessageType.REGISTRATION, "Пользователь с таким именем уже существует."));
                    }
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
