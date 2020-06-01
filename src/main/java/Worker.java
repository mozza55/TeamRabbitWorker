import handler.MessageDecoder;
import handler.MessageEncoder;
import handler.MessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Worker {

    private Bootstrap b;
    private NioEventLoopGroup loopGroup;

    public static void main(String[] args) throws Exception {
        for(int i=0; i<10; i++){
            Worker worker = new Worker();
            Byte task = (byte)(i%2);
            Runnable r = new WorkerThread(worker,i,task);
            new Thread(r).start();
        }
    }

    public Worker() {
        b= new Bootstrap();
        loopGroup = new NioEventLoopGroup();
        b.group(loopGroup).channel(NioSocketChannel.class);
    }

    public void connect(String host, int port,int workerNum,Byte taskType) throws Exception{
        //System.out.println("Worker:connect:enter");

        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                //디코더 추가
                pipeline.addLast(new LengthFieldBasedFrameDecoder(1024,2,4));
                pipeline.addLast(new MessageDecoder());
                pipeline.addLast(new MessageEncoder());
                pipeline.addLast(new MessageHandler(workerNum,taskType));

            }
        });
        ChannelFuture f = b.connect(host, port).sync();
        f.channel().closeFuture().sync();
        //System.out.println("Worker:connect:exit");
    }
}
