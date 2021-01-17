package com.banmo.sweethomeclient.client;

import com.banmo.sweethomeclient.proto.ConnectorMsg;
import com.google.protobuf.ByteString;

import java.util.Scanner;

import io.netty.channel.ChannelFuture;

public class UserFunction {


    void test() {
        Scanner s = new Scanner(System.in);
        while (true) {
            String str = s.next();

            if (str.equals("t1")) {
            } else if (str.equals("t2")) {
                ConnectorClient.getChannel()
                        .writeAndFlush(generateTransMessage(2, 2, 1, ConnectorMsg.Trans.MsgType.WORD, "2to1".getBytes()));
            } else if (str.equals("c1")) {
                ConnectorClient.getChannel()
                        .writeAndFlush(generateConnectMessage(1));
            } else if (str.equals("c2")) {
                ConnectorClient.getChannel()
                        .writeAndFlush(generateConnectMessage(2));
            } else {
                System.out.println("退出");
                break;
            }
        }
    }

    public static ChannelFuture sendTestTransMessage(int srcUserid, int dstUserid) {
        return ConnectorClient.getChannel()
                .writeAndFlush(generateTransMessage(1, srcUserid, dstUserid, ConnectorMsg.Trans.MsgType.WORD, "1to2".getBytes()));
    }

    public static ChannelFuture sendTestConnectMessage(int userid) {
        return ConnectorClient.getChannel()
                .writeAndFlush(generateConnectMessage(userid));
    }

    /**
     * @param msgID      信息序列ID
     * @param srcUserid  源用户id
     * @param dstUserid  目标用户id
     * @param msgType    信息种类（文字，图片，语音）
     * @param msgContent 信息的具体内容
     * @return
     */
    public static ConnectorMsg.cMsgInfo generateTransMessage(int msgID, int srcUserid, int dstUserid, ConnectorMsg.Trans.MsgType msgType, byte[] msgContent) {
        ConnectorMsg.cMsgInfo.Builder builder = ConnectorMsg.cMsgInfo.newBuilder();
        builder.setCMsgType(ConnectorMsg.cMsgInfo.CMsgType.TRANS);
        ConnectorMsg.Trans.Builder transMsg = ConnectorMsg.Trans.newBuilder();
        transMsg.setMsgID(msgID)
                .setMsgMark(1)
                .setSrcUserid(srcUserid)
                .setDstUserid(dstUserid)
                .setMsgType(msgType)
                .setMsgContent(ByteString.copyFrom(msgContent));
        builder.setTrans(transMsg);
        System.out.println(builder.build());
        return builder.build();
    }

    public static ConnectorMsg.cMsgInfo generateConnectMessage(int userid) {
        ConnectorMsg.cMsgInfo.Builder builder = ConnectorMsg.cMsgInfo.newBuilder();
        builder.setCMsgType(ConnectorMsg.cMsgInfo.CMsgType.CONNECT);
        ConnectorMsg.Connect.Builder connectMsg = ConnectorMsg.Connect.newBuilder();
        connectMsg.setUserid(userid)
                .setResponsetime(System.currentTimeMillis())
                .setMsgType(ConnectorMsg.Connect.MsgType.ONLINE);
        builder.setConnect(connectMsg);
        System.out.println(builder.build());
        return builder.build();
    }

}
