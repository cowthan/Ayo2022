package com.example.netty.server1;

import com.example.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import static com.example.netty.NettyServer.PORT_NUMBER;
import static com.example.netty.NettyServer.getLocalIp;


public class TCPServerNetty{

    private int port;
    private static Map<String, Channel> map = new ConcurrentHashMap<String, Channel>();

    public TCPServerNetty(int port){
        this.port = port;
    }

    public TCPServerNetty(){}

    public void start() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            // Decoders
                            //ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
                            ch.pipeline().addLast("bytesDecoder", new StringDecoder());
                            // Encoder
                            //ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                            ch.pipeline().addLast("bytesEncoder", new StringEncoder());
                            ch.pipeline().addLast(new OutBoundHandler1());
                            ch.pipeline().addLast(new IdleStateHandler(0,0,300), new InBoundHandler1());
                        }
                    });

            b.bind(port);
            // Start the server.
            //ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            //f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Shut down all event loops to terminate all threads.
            //bossGroup.shutdownGracefully();
            //workerGroup.shutdownGracefully();
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception{
        Log.info("正在开启服务器....");
        new TCPServerNetty(1001).start();
        Log.info("开启成功，监听端口：" + PORT_NUMBER + ", IP地址：" + getLocalIp());

        for(int i = 0; i < 1000; i++){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.info("尝试给客户端发消息...." + map.size());
            for(String ip: map.keySet()){
                Channel channel = map.get(ip);
                channel.writeAndFlush("[[[message from serve...]]]--" + i);
            }
        }

    }

    public static Map<String, Channel> getMap() {
        return map;
    }


}
