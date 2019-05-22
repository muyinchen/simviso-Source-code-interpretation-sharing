package com.simviso.nettydemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author: ZhiQiu
 * @email: fei6751803@163.com
 * @date: 2019/4/4 0:24.
 */
public class NettyChannelPoolHandler implements ChannelPoolHandler {
    @Override
    public void channelReleased(Channel ch) {
        System.out.println("channelReleased. Channel ID: " + ch.id());
    }

    @Override
    public void channelAcquired(Channel ch) {
        System.out.println("channelAcquired. Channel ID: " + ch.id());
    }

    @Override
    public void channelCreated(Channel ch) {
        ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
        System.out.println("channelCreated. Channel ID: " + ch.id());
        SocketChannel channel = (SocketChannel) ch;
        channel.config().setKeepAlive(true);
        channel.config().setTcpNoDelay(true);
        channel.pipeline()
               .addLast(new DelimiterBasedFrameDecoder(1024, delimiter))
               .addLast(new StringDecoder()).addLast(new StringEncoder()).addLast(new NettyClientHander());

    }
}
