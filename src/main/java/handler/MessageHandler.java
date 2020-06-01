package handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public class MessageHandler extends SimpleChannelInboundHandler<NettyMessage> {

    private int workerNum;
    private Byte taskType;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("worker["+workerNum+"]channel active 요청 ");
        String body = "worker["+workerNum+"/"+taskType+"] 등록 요청";
        NettyMessage registerMessage = new NettyMessage((byte)1, (byte)taskType, body.getBytes().length, body);
        ctx.writeAndFlush(registerMessage);
    }

    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
        System.out.println("worker["+workerNum+"("+taskType+")] read : "+msg.getBody());

        //임의 작업 처리 구간
        //Thread.sleep((workerNum%5)*1000);
        Random random = new Random();
        int s = random.nextInt(10);
        Thread.sleep(s*1000);

        //String body = "worker"+workerNum+" (task/id)"+ msg.getTaskType()+"/"+msg.getBody()+"  작업 완료";
        //NettyMessage nettyMessage = new NettyMessage((byte)0, (byte)taskType, body.getBytes().length, body);

        NettyMessage nettyMessage = new NettyMessage((byte)0, (byte)taskType, msg.toString().getBytes().length, msg.toString());
        ctx.writeAndFlush(nettyMessage);

        System.out.println("worker["+workerNum+"] read : complete :"+ msg.getBody());
    }

}
