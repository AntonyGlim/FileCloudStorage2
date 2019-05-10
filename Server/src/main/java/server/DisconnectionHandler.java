package server;

import common.ConsoleHelper;
import common.Message;
import common.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.Map;

public class DisconnectionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        try {
            if (msg == null) return;
            if (msg instanceof Message) {
                Message messageFromClient = (Message) msg;
                if (messageFromClient.getType().equals(MessageType.DISCONNECTION)) {
                    int name = Integer.parseInt(messageFromClient.getText()); //TODO replace messageFromClient.getText() to User.getName()
                    deleteUserFromMap(Server.connectionUsersMap, name);
                    ConsoleHelper.writeMessage(Server.connectionUsersMap.toString()); //TODO Delete this
                } else {
                    ctx.fireChannelRead(msg);
                }
            }
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private void deleteUserFromMap(Map<Integer, Long> map, Integer name){
        for (Map.Entry pair : map.entrySet()) {
            if (pair.getKey() == name) map.remove(name);
        }
    }
}
