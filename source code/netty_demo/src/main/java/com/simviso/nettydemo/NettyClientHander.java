package com.simviso.nettydemo;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: ZhiQiu
 * @email: fei6751803@163.com
 * @date: 2019/4/4 0:25.
 */
public class NettyClientHander extends ChannelInboundHandlerAdapter {
    static AtomicInteger count = new AtomicInteger(1);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(count.getAndIncrement() + ":" + msg);
    }
}
