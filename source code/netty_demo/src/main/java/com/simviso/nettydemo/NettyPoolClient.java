package com.simviso.nettydemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.net.InetSocketAddress;

/**
 * @author: ZhiQiu
 * @email: fei6751803@163.com
 * @date: 2019/4/4 0:21.
 */
public class NettyPoolClient {
     final EventLoopGroup group = new NioEventLoopGroup();
    final Bootstrap strap = new Bootstrap();
    InetSocketAddress addr1 = new InetSocketAddress("127.0.0.1", 8080);
    InetSocketAddress addr2 = new InetSocketAddress("127.0.0.11", 8888);

    ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap;
    public void build() throws Exception {
        strap.group(group).channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)

             .option(ChannelOption.SO_KEEPALIVE, true);

        poolMap = new AbstractChannelPoolMap<>() {
            @Override
            protected SimpleChannelPool newPool(InetSocketAddress key) {
                return new FixedChannelPool(strap.remoteAddress(key), new NettyChannelPoolHandler(), 2);
            }
        };
    }

    public static void main(String[] args) throws Exception {
        NettyPoolClient client = new NettyPoolClient();
        client.build();
        final String ECHO_REQ = "Hello Netty.$_";
        for (int i = 0; i < 10; i++) {
            // depending on when you use addr1 or addr2 you will get different pools.
            final SimpleChannelPool pool = client.poolMap.get(client.addr1);
            Future<Channel> f = pool.acquire();
            f.addListener((FutureListener<Channel>) f1 -> {
                if (f1.isSuccess()) {
                    Channel ch = f1.getNow();
                    ch.writeAndFlush(ECHO_REQ);
                    // Release back to pool
                    pool.release(ch);
                }
            });
        }
    }
}
