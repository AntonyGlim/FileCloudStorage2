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

import java.util.Map;

public class AuthorizationHandler extends ChannelInboundHandlerAdapter {

    private static DBManager dbManager = new DBManager();
    private static MainHandler mainHandler = MainHandler.getMainHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            if (msg == null) return;
            if (msg instanceof Message) {
                Message messageFromClient = (Message) msg;
                if (messageFromClient.getType().equals(MessageType.AUTHORIZATION)){

                    String[] tokens = messageFromClient.getText().split(" ");
                    int name = Integer.parseInt(tokens[0]);
                    int password = Integer.parseInt(tokens[1]);

                    if (!isContain(Server.connectionUsersMap, name)){
                        try{
                            mainHandler.setUser(dbManager.returnUserFromDBbyNameAndPass(name, password));
                            ConsoleHelper.writeMessage(mainHandler.getUser().toString());
                            Server.connectionUsersMap.put(name, System.currentTimeMillis());
                            ctx.writeAndFlush(new Message(MessageType.AUTHORIZATION_OK, "Вход выполнен успешно."));
                            ConsoleHelper.writeMessage(Server.connectionUsersMap.toString()); //TODO Delete this
                        } catch (NoSuchUserException e){
                            ctx.writeAndFlush(new Message(MessageType.AUTHORIZATION, "Не верное имя пользователя или пароль"));
                        }
                    } else {
                        ctx.writeAndFlush(new Message(MessageType.AUTHORIZATION, "Пользователь с таким именем уже подключон."));
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

    private boolean isContain(Map<Integer, Long> map, Integer name) {
        for (Map.Entry pair : map.entrySet()) {
            if (pair.getKey() == name) return true;
        }
        return false;
    }
}
