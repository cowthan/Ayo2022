package com.example.netty.server1;

import com.example.netty.OutBoundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;



public class OutBoundHandler1 extends ChannelOutboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(OutBoundHandler.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg,
                      ChannelPromise promise) throws Exception {
        System.out.println("write handler");
        ctx.writeAndFlush(msg).addListener(new ChannelFutureListener(){
            @Override
            public void operationComplete(ChannelFuture future)
                    throws Exception {
                logger.info("下发成功！");
            }
        });
    }
}
