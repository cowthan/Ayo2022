package com.example.netty;

import com.example.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * Created by user on 2016/10/27.
 */

public class NettyServer {
    private ServerBootstrap mServerBootstrap;
    private EventLoopGroup mWorkerGroup;
    private ChannelFuture channelFuture;
    private boolean isInit;

    private static NettyServer INSTANCE;

    public final static int PORT_NUMBER = 8888;

    private NettyServer() {
    }

    public static NettyServer getInstance() {
        if (INSTANCE == null) {
            synchronized (NettyServer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NettyServer();
                }
            }
        }
        return INSTANCE;
    }

    public void init() {
        if (isInit) {
            return;
        }
        //创建worker线程池，这里只创建了一个线程池，使用的是netty的多线程模型
        mWorkerGroup = new NioEventLoopGroup();
        //服务端启动引导类，负责配置服务端信息
        mServerBootstrap = new ServerBootstrap();
        mServerBootstrap.group(mWorkerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel nioServerSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioServerSocketChannel.pipeline();
                        pipeline.addLast("ServerSocketChannel out", new OutBoundHandler());
                        pipeline.addLast("ServerSocketChannel in", new InBoundHandler());
                    }
                })
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //为连接上来的客户端设置pipeline
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("decoder", new ProtobufDecoder(Test.ProtoTest.getDefaultInstance()));
                        pipeline.addLast("encoder", new ProtobufEncoder());
                        pipeline.addLast("out1", new OutBoundHandler());
                        pipeline.addLast("out2", new OutBoundHandler());
                        pipeline.addLast("in1", new InBoundHandler());
                        pipeline.addLast("in2", new InBoundHandler());
                        pipeline.addLast("handler", new ServerChannelHandler());
                    }
                });

        channelFuture = mServerBootstrap.bind(PORT_NUMBER);
        isInit = true;
    }

    public void shutDown() {
        if (channelFuture != null && channelFuture.isSuccess()) {
            isInit = false;
            channelFuture.channel().closeFuture();
            mWorkerGroup.shutdownGracefully();
        }
    }

    public static String getLocalIp(){
        try {
            return InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            //return "";
        }

        ///宣称可以在linux下跑的
        Enumeration allNetInterfaces = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
            return "";
        }
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements())
        {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            System.out.println(netInterface.getName());
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements())
            {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address)
                {
                    return ip.getHostAddress();
                }
            }
        }
        return "";
    }

    public static void main(String[] args){
        final String TAG = "netty";
        Log.d(TAG, "正在开启服务器....");
        NettyServer.getInstance().init();
        Log.d(TAG, "开启成功，监听端口：" + PORT_NUMBER + ", IP地址：" + getLocalIp());
        for(int i = 0; i < 100; i++){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            NettyServer.getInstance().se
        }

        // NettyServer.getInstance().shutDown();
    }
}
