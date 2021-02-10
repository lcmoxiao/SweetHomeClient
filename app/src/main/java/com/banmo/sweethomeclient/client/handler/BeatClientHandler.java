package com.banmo.sweethomeclient.client.handler;


import com.banmo.sweethomeclient.client.ConnectorClient;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.proto.ConnectorMsg;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

import static com.banmo.sweethomeclient.client.tool.MsgGenerateTools.generateBeatMessage;
import static com.banmo.sweethomeclient.client.tool.MsgGenerateTools.generateConnectMessage;


@ChannelHandler.Sharable
public class BeatClientHandler extends ChannelHandlerAdapter {

    private int readIdleTimes = 0;
    private int writeIdleTimes = 0;
    private int reconnectTimes = 0;


    private void resetIdleTimes() {
        readIdleTimes = 0;
        writeIdleTimes = 0;
        reconnectTimes = 0;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;

        String eventType = null;
        switch (event.state()) {
            case READER_IDLE:
                eventType = "读空闲第" + ++readIdleTimes + "次";
                break;
            case WRITER_IDLE:
                eventType = "写空闲第" + ++writeIdleTimes + "次";
                break;
            case ALL_IDLE:
                eventType = "读写空闲";
                // 不处理
                break;
        }
//        System.out.println(ctx.channel().remoteAddress() + "超时事件：" + eventType);
        if (Math.max(readIdleTimes, writeIdleTimes) > 1) {
//            System.out.println("超过3s未有读写，尝试确认连接,第" + ++reconnectTimes + "次");
            ctx.channel().writeAndFlush(generateBeatMessage());
        }
        if (Math.max(readIdleTimes, writeIdleTimes) > 2) {
//            System.out.println("主动确认连接失败，断开连接,尝试重连");
            ctx.writeAndFlush(generateConnectMessage(UserInfos.user.getUserid()));
            ConnectorClient.reconnect();
            ctx.channel().close();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ConnectorMsg.cMsgInfo cmsg = (ConnectorMsg.cMsgInfo) msg;

        if (cmsg.getCMsgType() == ConnectorMsg.cMsgInfo.CMsgType.BEAT) {
            resetIdleTimes();
//            System.out.println("得到服务器的beat信息，重置超时计时器");
        } else {
            //System.out.println("非beat信息向下传递");
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
    }
}
