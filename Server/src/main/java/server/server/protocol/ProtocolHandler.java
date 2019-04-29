package server.server.protocol;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class ProtocolHandler extends ChannelInboundHandlerAdapter {
    private int state = -1;
    private HashMap<String, Object> map = new HashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        // FileOutputStream fos = new FileOutputStream("1.txt", true);
        while (buf.readableBytes() > 0) {
            // fos.write(buf.readByte());
            System.out.print((char)buf.readByte());
        }
        // fos.close();
//        if (state == -1) {
//            if (buf.readableBytes() >= 1) {
//                map.put("type", buf.readByte());
//                state = 0;
//            }
//        }
//        if (state == 0) {
//            if (buf.readableBytes() >= 8) {
//                state = 1;
//                map.put("fileNameLength", buf.readLong());
//            }
//        }
//        if (state == 1) {
//            if (buf.readableBytes() >= (Long) map.get("fileNameLength")) {
//                byte[] fnb = new byte[((Long)map.get("fileNameLength")).intValue()];
//                buf.readBytes(fnb);
//                System.out.println(new String(fnb));
//                state = -1;
//                map.clear();
//            }
//        }
//        if(state == 2) {
//            FileOutputStream fos = new FileOutputStream("1.txt");
//            while (buf.readableBytes() > 0) {
//                fos.write(buf.readByte());
//            }
//        }


//        try {
//            if (msg == null) {
//                return;
//            }
//            if (msg instanceof FileRequest) {
//                FileRequest fr = (FileRequest) msg;
//                if (Files.exists(Paths.get("server_storage/" + fr.getFilename()))) {
//                    FileMessage fm = new FileMessage(Paths.get("server_storage/" + fr.getFilename()));
//                    ctx.writeAndFlush(fm);
//                }
//            }
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
