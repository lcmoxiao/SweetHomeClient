package com.banmo.sweethomeclient.client;


import com.banmo.sweethomeclient.client.handler.BeatClientHandler;
import com.banmo.sweethomeclient.client.handler.TransHandler;
import com.banmo.sweethomeclient.proto.ConnectorMsg;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class ConnectorClient {
    static int port = 8081;
    static String host = "192.168.0.106";
    static Channel channel;

    public static Channel getChannel() {
        return channel;
    }

//    public static void main(String[] args) {
//        if (args != null && args.length > 0) {
//            try {
//                port = Integer.parseInt(args[0]);
//            } catch (NumberFormatException ignored) {
//
//            }
//        }
//        new Thread() {
//            @Override
//            public void run() {
//                ConnectorClient.connect(port, host, handlers);
//            }
//        }.start();
//        new UserFunction().test();
//    }

    public static void connect() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) {
                            final ChannelHandler[] handlers = new ChannelHandler[]{
                                    new ProtobufVarint32FrameDecoder(),
                                    new ProtobufDecoder(ConnectorMsg.cMsgInfo.getDefaultInstance()),
                                    new ProtobufVarint32LengthFieldPrepender(),
                                    new ProtobufEncoder(),
                                    new IdleStateHandler(3, 3, 0, TimeUnit.SECONDS),
                                    new BeatClientHandler(),
                                    new TransHandler()
                            };
                            sc.pipeline().addLast(handlers);
                        }
                    });
            ChannelFuture f = null;
            try {
                f = b.connect(host, port).sync();
                channel = f.channel();
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void reconnect() {
        connect();
    }

}