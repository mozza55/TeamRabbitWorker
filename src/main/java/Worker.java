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
        final int MAX_FRAME_SIZE =1024;
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {//3.채널파이프 라인 설정에 일반 소캣채널 클래스 SocketChannel 설정
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            //디코더 추가
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(MAX_FRAME_SIZE,2,4));
                            //pipeline.addLast(new LineBasedFrameDecoder());
                            pipeline.addLast(new MessageDecoder());
                            //핸들러 추가
                            //pipeline.addLast(serviceHandler);
                            pipeline.addLast(new MessageHandler());
                            pipeline.addLast(new MessageEncoder());

                        }
                    });
            ChannelFuture f = b.connect("localhost", 8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }
}
