package com.banmo.sweethomeclient.client.handler;

import com.banmo.sweethomeclient.proto.ConnectorMsg;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


public class TransHandler extends ChannelHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ConnectorMsg.cMsgInfo cmsg = (ConnectorMsg.cMsgInfo) msg;

        if (cmsg.getCMsgType() == ConnectorMsg.cMsgInfo.CMsgType.TRANS) {
//            System.out.println("得到trans信息");
//            ConnectorMsg.Trans trans = cmsg.getTrans();
//            System.out.println(trans);
//            ctx.writeAndFlush(msg);
            System.out.println("收到其他用户的trans消息");
            System.out.println(cmsg);

        } else {
            ctx.fireChannelRead(msg);
        }
    }

}
