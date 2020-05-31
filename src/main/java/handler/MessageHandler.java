package handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;


import java.nio.charset.Charset;

public class MessageHandler extends SimpleChannelInboundHandler<NettyMessage> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active 요청 ");
        String body = "worker 등록 요청";
        NettyMessage registerMessage = new NettyMessage((byte)1, (byte)1, body.getBytes().length, body );

        ctx.writeAndFlush(registerMessage);
    }

    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {

    }

}
