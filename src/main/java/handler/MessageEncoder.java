package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class MessageEncoder extends MessageToByteEncoder<NettyMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf out) throws Exception {
        System.out.println("encoder 실행");
        if(msg == null){
            throw new Exception("message is null");
        }

        out.writeByte(msg.getMessageType());
        out.writeByte(msg.getTaskType());
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getBody().getBytes());

    }
}
