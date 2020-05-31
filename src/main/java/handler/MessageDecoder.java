package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;


import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {

    // LengthFieldBasedFrameDecoder 로 만들어진 frame 전체가 들어온다
    /*
     * header 2byte = 1byte(메세지 종류) + 1byte(task 종류)
     *  + length 4byte  = body 길이 정보
     *  + body (?) byte
     */

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("encoder 실행");
        byte messageType = in.readByte();
        byte taskType = in.readByte();
        int length = in.readInt();
        String body = null;
        if(length >0){
            ByteBuf buf = in.readBytes(length);
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            body = new String(req, "UTF-8");
        }
        NettyMessage nettyMessage = new NettyMessage(messageType,taskType,length,body);
        System.out.println("netty message :"+nettyMessage.toString());
        out.add(nettyMessage);
    }
}
